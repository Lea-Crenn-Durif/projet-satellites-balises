package satellitesbalises.modele.mesure;

import java.io.Serializable;
import java.util.Objects;

/**
 * Une grandeur physique que l'on peut mesurer, par exemple la température en °C.
 * Deux grandeurs de même nom et de même unité sont égales.
 *
 * <p>Les cinq grandeurs du projet sont prédéfinies ci-dessous. On peut en créer d'autres
 * sans rien modifier ici (EX-4.8).</p>
 */
public final class Grandeur implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Grandeur TEMPERATURE = new Grandeur("temperature", "°C");
    public static final Grandeur SALINITE = new Grandeur("salinite", "PSU");
    public static final Grandeur TURBIDITE = new Grandeur("turbidite", "NTU");
    public static final Grandeur VITESSE_COURANT = new Grandeur("vitesseCourant", "m/s");
    public static final Grandeur DIRECTION_COURANT = new Grandeur("directionCourant", "°");

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
