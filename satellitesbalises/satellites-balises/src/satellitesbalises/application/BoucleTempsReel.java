package satellitesbalises.application;

import java.util.Objects;

import javax.swing.Timer;

import satellitesbalises.modele.simulation.Simulation;


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
