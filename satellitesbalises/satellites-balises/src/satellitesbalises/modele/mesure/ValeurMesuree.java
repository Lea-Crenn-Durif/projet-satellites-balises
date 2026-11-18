package satellitesbalises.modele.mesure;

import java.io.Serializable;
import java.util.Objects;

/** La valeur d'une grandeur dans une mesure, par exemple : température = 12,5. */
public final class ValeurMesuree implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Grandeur grandeur;
    private final double valeur;

    public ValeurMesuree(Grandeur grandeur, double valeur) {
        this.grandeur = Objects.requireNonNull(grandeur, "grandeur");
        this.valeur = valeur;
    }

    public Grandeur getGrandeur() {
        return grandeur;
    }

    public double getValeur() {
        return valeur;
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof ValeurMesuree)) {
            return false;
        }
        ValeurMesuree autre = (ValeurMesuree) objet;
        return grandeur.equals(autre.grandeur) && Double.compare(valeur, autre.valeur) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(grandeur, valeur);
    }

    @Override
    public String toString() {
        return grandeur.getNom() + " = " + valeur + " " + grandeur.getUnite();
    }
}
