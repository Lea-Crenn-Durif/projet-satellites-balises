package satellitesbalises.modele.monde;

import java.io.Serializable;
import java.util.Objects;


public final class GeometrieMonde implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final double NIVEAU_SURFACE = 0;

    private final double largeur;
    private final double hauteurCiel;
    private final double profondeurMer;

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

    public Zone zoneDe(Position position) {
        return position.getY() > NIVEAU_SURFACE ? Zone.CIEL : Zone.MER;
    }

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
