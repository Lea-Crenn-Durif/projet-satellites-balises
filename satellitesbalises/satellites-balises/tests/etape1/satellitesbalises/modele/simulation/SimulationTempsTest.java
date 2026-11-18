package satellitesbalises.modele.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eventHandler.EventHandler;
import satellitesbalises.modele.commande.CommandeSimulation;
import satellitesbalises.modele.evenement.EcouteurTemps;
import satellitesbalises.modele.evenement.EvenementPasTermine;
import satellitesbalises.modele.mesure.Environnement;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;

class SimulationTempsTest {

    /** Environnement neutre : l'étape 1 ne mesure rien. */
    private static final Environnement ENVIRONNEMENT_NEUTRE = (grandeur, position, pas) -> 0.0;

    private Simulation simulation;
    private EventHandler eventHandler;

    @BeforeEach
    void creerSimulation() {
        eventHandler = new EventHandler();
        simulation = new Simulation(new Monde(new GeometrieMonde(200, 100, 80)), ENVIRONNEMENT_NEUTRE, eventHandler);
    }

    // ---------- EX-1.4 ----------

    @Test
    void nouvelleSimulation_pasCourantVautZero() {
        assertEquals(0, simulation.getPasCourant());
    }

    @Test
    void avancerDUnPas_troisFois_pasCourantVaut3() {
        simulation.avancerDUnPas();
        simulation.avancerDUnPas();
        simulation.avancerDUnPas();

        assertEquals(3, simulation.getPasCourant());
    }

    // ---------- EX-1.5 ----------

    @Test
    void planifier_pasFutur_nonExecuteeAvant() {
        List<Long> executions = new ArrayList<>();
        simulation.planifier(s -> executions.add(s.getPasCourant()), 3);

        simulation.avancerDUnPas();
        simulation.avancerDUnPas();
        assertTrue(executions.isEmpty(), "rien avant le pas 3");

        simulation.avancerDUnPas();
        assertEquals(List.of(3L), executions);
    }

    @Test
    void planifier_executeeUneSeuleFois() {
        List<Long> executions = new ArrayList<>();
        simulation.planifier(s -> executions.add(s.getPasCourant()), 1);

        for (int i = 0; i < 5; i++) {
            simulation.avancerDUnPas();
        }

        assertEquals(1, executions.size());
    }

    @Test
    void planifier_deuxCommandesMemePas_ordreRespecte() {
        List<String> ordre = new ArrayList<>();
        simulation.planifier(s -> ordre.add("premiere"), 2);
        simulation.planifier(s -> ordre.add("seconde"), 2);

        simulation.avancerDUnPas();
        simulation.avancerDUnPas();

        assertEquals(List.of("premiere", "seconde"), ordre);
    }

    @Test
    void planifier_pasDejaPasse_executeeAuPasSuivant() {
        simulation.avancerDUnPas();
        simulation.avancerDUnPas();
        List<Long> executions = new ArrayList<>();

        simulation.planifier(s -> executions.add(s.getPasCourant()), 1);
        simulation.avancerDUnPas();

        assertEquals(List.of(3L), executions);
    }

    @Test
    void planifier_commandesDansLeDesordre_executeesParPasCroissant() {
        List<Long> executions = new ArrayList<>();
        CommandeSimulation noter = s -> executions.add(s.getPasCourant());
        simulation.planifier(noter, 4);
        simulation.planifier(noter, 2);

        for (int i = 0; i < 4; i++) {
            simulation.avancerDUnPas();
        }

        assertEquals(List.of(2L, 4L), executions);
    }

    // ---------- EX-1.7 ----------

    @Test
    void avancerDUnPas_envoiePasTermineAvecLeNumeroDuPas() {
        EcouteurDePas ecouteur = new EcouteurDePas();
        eventHandler.registerListener(EvenementPasTermine.class, ecouteur);

        simulation.avancerDUnPas();
        simulation.avancerDUnPas();

        assertEquals(List.of(1L, 2L), ecouteur.pasRecus);
    }

    @Test
    void pasTermine_commandeDuPasDejaExecutee() {
        List<String> journal = new ArrayList<>();
        simulation.planifier(s -> journal.add("commande"), 1);
        eventHandler.registerListener(EvenementPasTermine.class, (EcouteurTemps) e -> journal.add("pas terminé"));

        simulation.avancerDUnPas();

        assertEquals(List.of("commande", "pas terminé"), journal);
    }

    private static final class EcouteurDePas implements EcouteurTemps {
        private final List<Long> pasRecus = new ArrayList<>();

        @Override
        public void pasTermine(EvenementPasTermine evenement) {
            pasRecus.add(evenement.getPas());
        }
    }
}
