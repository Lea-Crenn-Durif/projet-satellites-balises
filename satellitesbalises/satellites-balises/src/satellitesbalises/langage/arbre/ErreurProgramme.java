package satellitesbalises.langage.arbre;

import java.util.Objects;

/**
 * Une erreur dans un programme, avec sa position pour que l'utilisateur la retrouve (EX-7.5, EX-7.6).
 *
 * La colonne vaut 0 quand seule la ligne est connue (erreurs de sens).
 */
public final class ErreurProgramme {

    private final int ligne;
    private final int colonne;
    private final String message;

    public ErreurProgramme(int ligne, int colonne, String message) {
        this.ligne = ligne;
        this.colonne = colonne;
        this.message = Objects.requireNonNull(message, "message");
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof ErreurProgramme)) {
            return false;
        }
        ErreurProgramme autre = (ErreurProgramme) objet;
        return ligne == autre.ligne
                && colonne == autre.colonne
                && message.equals(autre.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ligne, colonne, message);
    }

    /** Par exemple « ligne 3, colonne 22 : … » ; la colonne n'est donnée que si elle est connue. */
    @Override
    public String toString() {
        return "ligne " + ligne + (colonne > 0 ? ", colonne " + colonne : "") + " : " + message;
    }
}
