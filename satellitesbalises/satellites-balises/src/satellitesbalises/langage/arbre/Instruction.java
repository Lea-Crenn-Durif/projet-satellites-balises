package satellitesbalises.langage.arbre;

/**
 * Une instruction d'un programme. Elle garde son numéro de ligne pour que les messages
 * d'erreur disent où regarder.
 *
 * <p>Chaque instruction sait appeler la bonne méthode d'un visiteur (patron Visiteur) :
 * l'interpréteur n'a besoin d'aucun {@code instanceof}.</p>
 */
public abstract class Instruction {

    private final int ligne;

    protected Instruction(int ligne) {
        this.ligne = ligne;
    }

    /** Numéro de la ligne du texte, à partir de 1. */
    public int getLigne() {
        return ligne;
    }

    /** Appelle la méthode du visiteur qui correspond à ce type d'instruction. */
    public abstract <R> R accepter(VisiteurInstruction<R> visiteur);
}
