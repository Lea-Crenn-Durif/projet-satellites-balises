package satellitesbalises.modele.commande;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eventHandler.EventHandler;
import satellitesbalises.modele.communication.StatutSatellite;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.creation.Parametres;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.evenement.EcouteurCommandes;
import satellitesbalises.modele.evenement.EcouteurSatellites;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.evenement.EvenementStatutModifie;
import satellitesbalises.modele.mesure.EnvironnementSimule;
import satellitesbalises.modele.mesure.Grandeur;
import satellitesbalises.modele.mesure.InstrumentSimple;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.Simulation;


class ConfigurationTest {

    private final GeometrieMonde geometrie = new GeometrieMonde(200, 100, 80);
    private final FabriqueEntites fabrique = new FabriqueEntites(geometrie);
    private final EventHandler eventHandler = new EventHandler();
    private final List<String> journal = new ArrayList<>();
    private Simulation simulation;
    private Satellite s1;
    private Balise b1;

    @BeforeEach
    void creerSimulation() {
        simulation = new Simulation(new Monde(geometrie), EnvironnementSimule.parDefaut(), eventHandler);
        s1 = fabrique.nouveauSatellite("s1").en(new Position(50, 50)).construire();
        b1 = fabrique.nouvelleBalise("b1").en(new Position(50, -30)).avecMemoire(10)
                .equipeeDe(new InstrumentSimple("temperature", Grandeur.TEMPERATURE)).construire();
        simulation.ajouter(s1);
        simulation.ajouter(b1);
        eventHandler.registerListener(EvenementCommandeIgnoree.class, (EcouteurCommandes) e -> journal.add("ignorée : " + e.getRaison()));
        eventHandler.registerListener(EvenementStatutModifie.class, (EcouteurSatellites) e -> journal.add("statut " + e.getSatellite().getStatut()));
    }

    private static Parametres parametres(String nom, double valeur) {
        return new Parametres(Map.of(nom, valeur));
    }

    @Test
    void changerStatut_arreterPuisDemarrer() {
        simulation.planifier(new CommandeChangerStatut("s1", false), 1);
        simulation.planifier(new CommandeChangerStatut("s1", true), 2);

        simulation.avancerDUnPas();
        assertEquals(StatutSatellite.HORS_SERVICE, s1.getStatut());

        simulation.avancerDUnPas();
        assertEquals(StatutSatellite.DISPONIBLE, s1.getStatut());
        assertEquals(List.of("statut HORS_SERVICE", "statut DISPONIBLE"), journal);
    }

    @Test
    void configurer_porteeDuSatellite() {
        simulation.planifier(new CommandeConfigurer("s1", parametres("portee", 60)), 1);

        simulation.avancerDUnPas();

        assertEquals(60, s1.getPortee(), 1e-9);
    }

    @Test
    void configurer_capaciteSuperieure_acceptee() {
        simulation.planifier(new CommandeConfigurer("b1", parametres("memoire", 40)), 1);

        simulation.avancerDUnPas();

        assertEquals(40, b1.getMemoire().getCapacite());
    }

    @Test
    void configurer_capaciteInferieureAuContenu_refuseEtGardeAncienne() {
        for (int i = 0; i < 5; i++) {
            simulation.avancerDUnPas(); // 5 mesures en mémoire
        }
        simulation.planifier(new CommandeConfigurer("b1", parametres("memoire", 3)), 6);

        simulation.avancerDUnPas();

        assertEquals(10, b1.getMemoire().getCapacite());
        assertEquals(1, journal.size());
        assertTrue(journal.get(0).startsWith("ignorée"), journal.toString());
    }

    @Test
    void configurer_proprieteInconnue_ignoreeEtSignalee() {
        simulation.planifier(new CommandeConfigurer("s1", parametres("couleurDuCiel", 3)), 1);

        simulation.avancerDUnPas();

        assertEquals(1, journal.size());
        assertTrue(journal.get(0).contains("couleurDuCiel"), journal.toString());
    }
}
