package satellitesbalises.modele.simulation;

import java.util.Objects;

import eventHandler.EventHandler;
import satellitesbalises.modele.mesure.Environnement;
import satellitesbalises.modele.monde.GeometrieMonde;

public final class ContexteSimulation {

    private final long pas;
    private final GeometrieMonde geometrie;
    private final Environnement environnement;
    private final EventHandler eventHandler;

    public ContexteSimulation(long pas, GeometrieMonde geometrie, Environnement environnement,
                              EventHandler eventHandler) {
        this.pas = pas;
        this.geometrie = Objects.requireNonNull(geometrie, "geometrie");
        this.environnement = Objects.requireNonNull(environnement, "environnement");
        this.eventHandler = Objects.requireNonNull(eventHandler, "eventHandler");
    }

    public long getPas() {
        return pas;
    }

    public GeometrieMonde getGeometrie() {
        return geometrie;
    }

    public Environnement getEnvironnement() {
        return environnement;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }
}
