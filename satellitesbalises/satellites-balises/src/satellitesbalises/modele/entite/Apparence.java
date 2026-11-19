package satellitesbalises.modele.entite;

import java.io.Serializable;
import java.util.Objects;

/**
 * Comment dessiner une entité (EX-2.9) : une couleur (en toutes lettres) et une taille en pixels.
 *
 * <p>Le modèle ne fait que la ranger : c'est la vue qui l'interprète. Elle est sauvegardée
 * avec le reste du modèle.</p>
 */
public final class Apparence implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Apparence PAR_DEFAUT = new Apparence("noir", 32);

    private final String couleur;
    private final int taille;

    public Apparence(String couleur, int taille) {
        this.couleur = Objects.requireNonNull(couleur, "couleur");
        this.taille = taille;
    }

    /** Couleur « noir », taille 32 pixels. */
    public static Apparence parDefaut() {
        return PAR_DEFAUT;
    }

    public String getCouleur() {
        return couleur;
    }

    public int getTaille() {
        return taille;
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof Apparence)) {
            return false;
        }
        Apparence autre = (Apparence) objet;
        return taille == autre.taille && couleur.equals(autre.couleur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(couleur, taille);
    }

    @Override
    public String toString() {
        return couleur + ", taille " + taille;
    }
}
