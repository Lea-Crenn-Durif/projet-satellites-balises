package satellitesbalises.modele.commande;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import eventHandler.EventHandler;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.evenement.EcouteurCommandes;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.mesure.EnvironnementSimule;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.Simulation;

class CommandeIgnoreeTest {

    private final GeometrieMonde geometrie = new GeometrieMonde(200, 100, 80);
    private final EventHandler eventHandler = new EventHandler();
    private final Simulation simulation = new Simulation(new Monde(geometrie), EnvironnementSimule.parDefaut(), eventHandler);
    private final List<String> raisons = new ArrayList<>();

    private void ecouter() {
        eventHandler.registerListener(EvenementCommandeIgnoree.class, (EcouteurCommandes) e -> raisons.add(e.getRaison()));
    }

    @Test
    void executer_entiteSupprimeeEntreTemps_commandeIgnoreeEtSignalee() {
        ecouter();
        simulation.ajouter(new FabriqueEntites(geometrie).nouveauSatellite("s1").en(new Position(50, 50)).construire());
        simulation.planifier(new CommandeChangerStatut("s1", false), 5);
        simulation.supprimer("s1");

        for (int pas = 0; pas < 6; pas++) {
            simulation.avancerDUnPas(); // aucune exception
        }

        assertEquals(1, raisons.size());
        assertTrue(raisons.get(0).contains("s1"), raisons.get(0));
        assertEquals(6, simulation.getPasCourant());
    }

    @Test
    void chaqueCommandeSurEntiteInconnue_ignoree() {
        ecouter();
        simulation.planifier(new CommandeSupprimer("x"), 1);
        simulation.planifier(new CommandeForcerRemontee("x"), 1);
        simulation.planifier(new CommandeEquiper("x", null), 1);
        simulation.planifier(new CommandeChangerStatut("x", true), 1);

        simulation.avancerDUnPas();

        assertEquals(4, raisons.size());
    }
}
