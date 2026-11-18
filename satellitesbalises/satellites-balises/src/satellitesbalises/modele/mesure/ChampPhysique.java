package satellitesbalises.modele.mesure;

import java.io.Serializable;
import java.util.Objects;

import satellitesbalises.modele.monde.Position;

public final class ChampPhysique implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final double LONGUEUR_ONDE = 60;
    private static final double DUREE_ONDE = 40;

    private final double valeurSurface;
    private final double gradientProfondeur;
    private final double amplitude;

    public ChampPhysique(double valeurSurface, double gradientProfondeur, double amplitude) {
        this.valeurSurface = valeurSurface;
        this.gradientProfondeur = gradientProfondeur;
        this.amplitude = amplitude;
    }

    public double getValeurSurface() {
        return valeurSurface;
    }

    public double getGradientProfondeur() {
        return gradientProfondeur;
    }

    public double getAmplitude() {
        return amplitude;
    }

    public double valeur(Position position, long pas) {
        double profondeur = -position.getY();
        double ondulation = Math.sin(position.getX() / LONGUEUR_ONDE + pas / DUREE_ONDE);
        return valeurSurface + gradientProfondeur * profondeur + amplitude * ondulation;
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof ChampPhysique)) {
            return false;
        }
        ChampPhysique autre = (ChampPhysique) objet;
        return Double.compare(valeurSurface, autre.valeurSurface) == 0
                && Double.compare(gradientProfondeur, autre.gradientProfondeur) == 0
                && Double.compare(amplitude, autre.amplitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valeurSurface, gradientProfondeur, amplitude);
    }

    @Override
    public String toString() {
        return valeurSurface + " + " + gradientProfondeur + " × profondeur ± " + amplitude;
    }
}
