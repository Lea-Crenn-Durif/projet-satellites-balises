package satellitesbalises.langage.arbre;

import java.util.Objects;

/**
 * Un nom et sa valeur : {@code portee = 40}, {@code couleur = "orange"}.
 */
public final class Propriete {

    private final String nom;
    private final Valeur valeur;

    public Propriete(String nom, Valeur valeur) {
        this.nom = Objects.requireNonNull(nom, "nom");
        this.valeur = Objects.requireNonNull(valeur, "valeur");
    }

    public String getNom() {
        return nom;
    }

    public Valeur getValeur() {
        return valeur;
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof Propriete)) {
            return false;
        }
        Propriete autre = (Propriete) objet;
        return nom.equals(autre.nom)
                && valeur.equals(autre.valeur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, valeur);
    }

    @Override
    public String toString() {
        return "Propriete[nom = " + nom + ", valeur = " + valeur + "]";
    }
}
