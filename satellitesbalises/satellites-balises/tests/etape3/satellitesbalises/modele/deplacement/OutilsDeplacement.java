package satellitesbalises.modele.deplacement;

import eventHandler.EventHandler;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.simulation.ContexteSimulation;

final class OutilsDeplacement {

    /** Monde de 200 de large, 100 de ciel, 80 de mer. */
    static final GeometrieMonde GEOMETRIE = new GeometrieMonde(200, 100, 80);

    private OutilsDeplacement() {
    }

    /** Contexte du pas demandé, sans mesure ni événement utiles aux déplacements. */
    static ContexteSimulation contexte(long pas) {
        return new ContexteSimulation(pas, GEOMETRIE, (g, p, n) -> 0.0, new EventHandler());
    }
}
