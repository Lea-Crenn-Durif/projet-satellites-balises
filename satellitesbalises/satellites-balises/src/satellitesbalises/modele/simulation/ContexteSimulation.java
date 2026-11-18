package satellitesbalises.modele.simulation;

import java.util.Objects;

import eventHandler.EventHandler;
import satellitesbalises.modele.mesure.Environnement;
import satellitesbalises.modele.monde.GeometrieMonde;

/**
 * Ce dont une entité a besoin pour agir pendant un pas : le numéro du pas, la géométrie du monde,
 * l'environnement à mesurer et l'EventHandler pour annoncer ce qui change.
 *
 * <p>La simulation en crée un nouveau à chaque pas. Grâce à lui, les entités n'ont pas besoin
 * de connaître la simulation.</p>
 */
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
