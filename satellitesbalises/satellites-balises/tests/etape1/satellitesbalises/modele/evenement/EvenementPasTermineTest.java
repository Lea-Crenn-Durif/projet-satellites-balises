package satellitesbalises.modele.evenement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import eventHandler.EventHandler;
import satellitesbalises.modele.mesure.Environnement;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.simulation.Simulation;


class EvenementPasTermineTest {

    private final Simulation simulation = new Simulation(
            new Monde(new GeometrieMonde(200, 100, 80)), (g, p, pas) -> 0.0, new EventHandler());

    @Test
    void sendTo_evenementPasTermine_appellePasTermine() {
        EcouteurEspion espion = new EcouteurEspion();
        EvenementPasTermine evenement = new EvenementPasTermine(simulation, 7);

        evenement.sendTo(espion);

        assertEquals(1, espion.recus.size());
        assertSame(evenement, espion.recus.get(0));
        assertEquals(7, espion.recus.get(0).getPas());
    }

    @Test
    void getSource_renvoieLaSimulation() {
        assertSame(simulation, new EvenementPasTermine(simulation, 1).getSource());
    }

    @Test
    void send_ecouteurAbonne_recoitLEvenement() {
        EventHandler eventHandler = new EventHandler();
        EcouteurEspion espion = new EcouteurEspion();
        eventHandler.registerListener(EvenementPasTermine.class, espion);

        eventHandler.send(new EvenementPasTermine(simulation, 1));

        assertEquals(1, espion.recus.size());
    }

    @Test
    void send_typeNonAbonne_ecouteurNonAppele() {
        EventHandler eventHandler = new EventHandler();
        EcouteurEspion espion = new EcouteurEspion();
        // abonné à un AUTRE type d'événement
        eventHandler.registerListener(EvenementCommandeIgnoree.class, espion);

        eventHandler.send(new EvenementPasTermine(simulation, 1));

        assertEquals(0, espion.recus.size());
    }

    /** Écoute le temps ; implémente aussi EcouteurCommandes pour le test d'abonnement croisé. */
    private static final class EcouteurEspion implements EcouteurTemps, EcouteurCommandes {
        private final List<EvenementPasTermine> recus = new ArrayList<>();

        @Override
        public void pasTermine(EvenementPasTermine evenement) {
            recus.add(evenement);
        }

        @Override
        public void commandeIgnoree(EvenementCommandeIgnoree evenement) {
            // non utilisé ici
        }
    }
}
