package satellitesbalises.langage.arbre;

import java.util.List;
import java.util.Objects;

/**
 * Un nom suivi de paramètres nommés : {@code sinusoidal(amplitude = 15, periode = 60)}, {@code temperature()}.
 *
 * Le nom est résolu par un catalogue du modèle : un nouvel instrument ou un nouveau déplacement
 * ne change pas la grammaire (EX-7.9).
 */
public final class AppelComportement {

    private final String nom;
    private final List<Propriete> parametres;

    public AppelComportement(String nom, List<Propriete> parametres) {
        this.nom = Objects.requireNonNull(nom, "nom");
        this.parametres = List.copyOf(parametres);
    }

    public String getNom() {
        return nom;
    }

    public List<Propriete> getParametres() {
        return parametres;
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof AppelComportement)) {
            return false;
        }
        AppelComportement autre = (AppelComportement) objet;
        return nom.equals(autre.nom)
                && parametres.equals(autre.parametres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, parametres);
    }

    @Override
    public String toString() {
        return "AppelComportement[nom = " + nom + ", parametres = " + parametres + "]";
    }
}
