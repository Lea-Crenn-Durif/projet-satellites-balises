package satellitesbalises.modele.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eventHandler.EventHandler;
import satellitesbalises.modele.commande.CommandeAjouterBalise;
import satellitesbalises.modele.commande.CommandeAjouterSatellite;
import satellitesbalises.modele.commande.CommandeChangerApparence;
import satellitesbalises.modele.commande.CommandeSupprimer;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.entite.Apparence;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Entite;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.evenement.EcouteurEntites;
import satellitesbalises.modele.evenement.EvenementApparenceModifiee;
import satellitesbalises.modele.evenement.EvenementEntiteAjoutee;
import satellitesbalises.modele.evenement.EvenementEntiteSupprimee;
import satellitesbalises.modele.evenement.EvenementPositionModifiee;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.monde.Position;

/** Étape 2 — EX-2.5, EX-2.9 : ajout, suppression, apparence, via la simulation et ses commandes. */
class SimulationEntitesTest {

    private final GeometrieMonde geometrie = new GeometrieMonde(200, 100, 80);
    private final FabriqueEntites fabrique = new FabriqueEntites(geometrie);
    private final EventHandler eventHandler = new EventHandler();
    private final EcouteurEspion espion = new EcouteurEspion();
    private Simulation simulation;

    @BeforeEach
    void creerSimulation() {
        simulation = new Simulation(new Monde(geometrie), (g, p, pas) -> 0.0, eventHandler);
        eventHandler.registerListener(EvenementEntiteAjoutee.class, espion);
        eventHandler.registerListener(EvenementEntiteSupprimee.class, espion);
        eventHandler.registerListener(EvenementApparenceModifiee.class, espion);
    }

    private Balise balise(String nom) {
        return fabrique.nouvelleBalise(nom).en(new Position(50, -30)).construire();
    }

    @Test
    void ajouter_balise_presenteDansLeMondeEtAnnoncee() {
        Balise b1 = balise("b1");

        simulation.ajouter(b1);

        assertSame(b1, simulation.getMonde().trouverBalise("b1").orElseThrow());
        assertEquals(List.of("ajoutée b1"), espion.journal);
    }

    @Test
    void supprimer_baliseExistante_introuvableEtAnnoncee() {
        simulation.ajouter(balise("b1"));

        simulation.supprimer("b1");

        assertTrue(simulation.getMonde().trouver("b1").isEmpty());
        assertEquals(List.of("ajoutée b1", "supprimée b1"), espion.journal);
    }

    @Test
    void commandeAjouterBalise_executeeAuPasPrevu() {
        simulation.planifier(new CommandeAjouterBalise(balise("b1")), 2);

        simulation.avancerDUnPas();
        assertTrue(simulation.getMonde().trouver("b1").isEmpty(), "pas encore au pas 1");

        simulation.avancerDUnPas();
        assertTrue(simulation.getMonde().trouver("b1").isPresent(), "présente au pas 2");
    }

    @Test
    void commandeAjouterSatellite_satellitePresent() {
        Satellite s1 = fabrique.nouveauSatellite("s1").en(new Position(50, 50)).construire();
        simulation.planifier(new CommandeAjouterSatellite(s1), 1);

        simulation.avancerDUnPas();

        assertSame(s1, simulation.getMonde().trouverSatellite("s1").orElseThrow());
    }

    @Test
    void commandeSupprimer_entiteRetireeAuPasPrevu() {
        simulation.ajouter(balise("b1"));
        simulation.planifier(new CommandeSupprimer("b1"), 1);

        simulation.avancerDUnPas();

        assertTrue(simulation.getMonde().trouver("b1").isEmpty());
    }

    @Test
    void changerApparence_publieApparenceModifiee() {
        Balise b1 = balise("b1");
        simulation.ajouter(b1);
        Apparence verte = new Apparence("vert", 20);
        simulation.planifier(new CommandeChangerApparence("b1", verte), 1);

        simulation.avancerDUnPas();

        assertEquals(verte, b1.getApparence());
        assertEquals("apparence b1", espion.journal.get(espion.journal.size() - 1));
    }

    /** Note ce qu'il reçoit, sous forme de texte simple à comparer. */
    private static final class EcouteurEspion implements EcouteurEntites {
        private final List<String> journal = new ArrayList<>();

        private static String nom(Entite e) {
            return e.getNom();
        }

        @Override
        public void entiteAjoutee(EvenementEntiteAjoutee e) {
            journal.add("ajoutée " + nom(e.getEntite()));
        }

        @Override
        public void entiteSupprimee(EvenementEntiteSupprimee e) {
            journal.add("supprimée " + nom(e.getEntite()));
        }

        @Override
        public void positionModifiee(EvenementPositionModifiee e) {
            // non écouté dans ce test
        }

        @Override
        public void apparenceModifiee(EvenementApparenceModifiee e) {
            journal.add("apparence " + nom(e.getEntite()));
        }
    }
}
