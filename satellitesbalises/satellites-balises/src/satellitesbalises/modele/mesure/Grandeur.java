package satellitesbalises.modele.mesure;

import java.io.Serializable;
import java.util.Objects;


public final class Grandeur implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nom;
    private final String unite;

    public Grandeur(String nom, String unite) {
        this.nom = Objects.requireNonNull(nom, "nom");
        this.unite = Objects.requireNonNull(unite, "unite");
    }

    public String getNom() {
        return nom;
    }

    public String getUnite() {
        return unite;
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof Grandeur)) {
            return false;
        }
        Grandeur autre = (Grandeur) objet;
        return nom.equals(autre.nom) && unite.equals(autre.unite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, unite);
    }

    @Override
    public String toString() {
        return nom + " (" + unite + ")";
    }
}
