package satellitesbalises.modele.monde;

import java.io.Serializable;
import java.util.Objects;

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

    public Position translater(double dx, double dy) {
        return new Position(x + dx, y + dy);
    }

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
