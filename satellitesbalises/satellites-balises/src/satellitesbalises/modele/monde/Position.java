package satellitesbalises.modele.monde;

import java.io.Serializable;
import java.util.Objects;

/**
 * Un point du monde, dans le repère du modèle : y &gt; 0 dans le ciel, y = 0 à la surface, y &lt; 0 dans la mer.
 *
 * <p>Une position ne change jamais (ses champs sont {@code final}) : se déplacer, c'est obtenir
 * une nouvelle position. Deux positions aux mêmes coordonnées sont égales.</p>
 */
public final class Position implements Serializable {

    private static final long serialVersionUID = 1L;

    private final double x;
    private final double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /** Renvoie la position décalée de (dx, dy), sans modifier celle-ci. */
    public Position translater(double dx, double dy) {
        return new Position(x + dx, y + dy);
    }

    /** Distance horizontale entre deux positions, sans tenir compte de la hauteur ni du sens. */
    public double ecartHorizontal(Position autre) {
        return Math.abs(x - autre.x);
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof Position)) {
            return false;
        }
        Position autre = (Position) objet;
        return Double.compare(x, autre.x) == 0 && Double.compare(y, autre.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
