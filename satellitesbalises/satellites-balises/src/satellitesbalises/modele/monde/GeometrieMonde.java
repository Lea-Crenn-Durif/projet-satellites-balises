package satellitesbalises.modele.monde;

import java.io.Serializable;
import java.util.Objects;

/**
 * Les dimensions du monde (EX-1.1 à EX-1.3) : un rectangle de largeur donnée,
 * avec le ciel au-dessus de la surface et la mer en dessous.
 *
 * <p>x va de 0 à largeur ; y va de −profondeurMer (le fond) à +hauteurCiel (le haut du ciel).
 * Les dimensions ne changent jamais une fois le monde créé.</p>
 */
public final class GeometrieMonde implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Ordonnée de la surface de la mer. */
    public static final double NIVEAU_SURFACE = 0;

    private final double largeur;
    private final double hauteurCiel;
    private final double profondeurMer;

    /**
     * Crée un monde aux dimensions données (EX-1.1).
     *
     * @throws IllegalArgumentException si une dimension est nulle ou négative
     */
    public GeometrieMonde(double largeur, double hauteurCiel, double profondeurMer) {
        verifierStrictementPositive("largeur", largeur);
        verifierStrictementPositive("hauteurCiel", hauteurCiel);
        verifierStrictementPositive("profondeurMer", profondeurMer);
        this.largeur = largeur;
        this.hauteurCiel = hauteurCiel;
        this.profondeurMer = profondeurMer;
    }

    public double getLargeur() {
        return largeur;
    }

    public double getHauteurCiel() {
        return hauteurCiel;
    }

    public double getProfondeurMer() {
        return profondeurMer;
    }

    /** CIEL au-dessus de la surface, MER à la surface et en dessous (EX-1.2). */
    public Zone zoneDe(Position position) {
        return position.getY() > NIVEAU_SURFACE ? Zone.CIEL : Zone.MER;
    }

    /** Vrai si la position est dans le rectangle du monde, bords compris (EX-1.3). */
    public boolean contient(Position position) {
        return entre(position.getX(), 0, largeur) && entre(position.getY(), -profondeurMer, hauteurCiel);
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof GeometrieMonde)) {
            return false;
        }
        GeometrieMonde autre = (GeometrieMonde) objet;
        return Double.compare(largeur, autre.largeur) == 0
                && Double.compare(hauteurCiel, autre.hauteurCiel) == 0
                && Double.compare(profondeurMer, autre.profondeurMer) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(largeur, hauteurCiel, profondeurMer);
    }

    @Override
    public String toString() {
        return "GeometrieMonde[largeur=" + largeur + ", hauteurCiel=" + hauteurCiel
                + ", profondeurMer=" + profondeurMer + "]";
    }

    private static boolean entre(double valeur, double minimum, double maximum) {
        return minimum <= valeur && valeur <= maximum;
    }

    private static void verifierStrictementPositive(String nom, double valeur) {
        if (valeur <= 0) {
            throw new IllegalArgumentException(nom + " doit être strictement positive : " + valeur);
        }
    }
}
