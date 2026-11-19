package satellitesbalises.langage.arbre;

import java.util.Objects;

/**
 * Une valeur texte, sans les guillemets : {@code "orange"} donne {@code orange}.
 */
public final class ValeurTexte implements Valeur {

    private final String texte;

    public ValeurTexte(String texte) {
        this.texte = Objects.requireNonNull(texte, "texte");
    }

    public String getTexte() {
        return texte;
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof ValeurTexte)) {
            return false;
        }
        ValeurTexte autre = (ValeurTexte) objet;
        return texte.equals(autre.texte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texte);
    }

    @Override
    public String toString() {
        return "ValeurTexte[texte = " + texte + "]";
    }
}
