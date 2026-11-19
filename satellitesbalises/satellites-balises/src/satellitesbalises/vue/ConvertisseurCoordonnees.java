package satellitesbalises.vue;

import java.awt.Point;
import java.util.Objects;

import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;

/**
 * Le seul endroit qui convertit une position du modèle en pixels de l'écran (EX-1.8).
 *
 * <p>Dans le modèle, y monte et vaut 0 à la surface ; à l'écran, y descend et vaut 0 en haut.
 * D'où : xÉcran = x et yÉcran = hauteurCiel − y.</p>
 */
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
