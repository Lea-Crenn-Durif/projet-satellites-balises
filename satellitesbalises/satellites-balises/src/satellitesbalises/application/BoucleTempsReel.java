package satellitesbalises.application;

import java.util.Objects;

import javax.swing.Timer;

import satellitesbalises.modele.simulation.Simulation;

/**
 * Fait avancer la simulation d'un pas à intervalle régulier (décision D2).
 *
 * <p>C'est une minuterie Swing : chaque pas s'exécute sur le fil d'affichage, donc une action
 * de l'utilisateur (pause, programme, sauvegarde) tombe toujours entre deux pas, jamais au milieu.</p>
 */
class BoucleTempsReel {

    private final Timer minuterie;

    BoucleTempsReel(Simulation simulation, int millisecondesParPas) {
        Objects.requireNonNull(simulation, "simulation");
        this.minuterie = new Timer(millisecondesParPas, tic -> simulation.avancerDUnPas());
    }

    void demarrer() {
        minuterie.start();
    }

    void mettreEnPause() {
        minuterie.stop();
    }

    boolean estEnPause() {
        return !minuterie.isRunning();
    }

    void changerVitesse(int millisecondesParPas) {
        minuterie.setDelay(millisecondesParPas);
    }
}
