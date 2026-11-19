package satellitesbalises.langage.arbre;

import java.util.Objects;

/**
 * Une valeur numérique : {@code 40}, {@code -80}, {@code 2.5}.
 */
public final class ValeurNombre implements Valeur {

    private final double nombre;

    public ValeurNombre(double nombre) {
        this.nombre = nombre;
    }

    public double getNombre() {
        return nombre;
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof ValeurNombre)) {
            return false;
        }
        ValeurNombre autre = (ValeurNombre) objet;
        return Double.compare(nombre, autre.nombre) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return "ValeurNombre[nombre = " + nombre + "]";
    }
}
