package satellitesbalises.vue;

import java.awt.Point;
import java.util.Objects;

import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;

public class ConvertisseurCoordonnees {

    private final GeometrieMonde geometrie;

    public ConvertisseurCoordonnees(GeometrieMonde geometrie) {
        this.geometrie = Objects.requireNonNull(geometrie, "geometrie");
    }

    public Point versEcran(Position position) {
        return new Point(
                (int) Math.round(position.getX()),
                (int) Math.round(geometrie.getHauteurCiel() - position.getY()));
    }
}
