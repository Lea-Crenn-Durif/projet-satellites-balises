package satellitesbalises.langage.arbre;

import java.util.Objects;

/**
 * Supprime une entité : {@code supprimer b2}.
 */
public final class InstructionSupprimer extends Instruction {

    private final String nom;

    public InstructionSupprimer(int ligne, String nom) {
        super(ligne);
        this.nom = Objects.requireNonNull(nom, "nom");
    }

    public String getNom() {
        return nom;
    }

    @Override
    public <R> R accepter(VisiteurInstruction<R> visiteur) {
        return visiteur.visiter(this);
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof InstructionSupprimer)) {
            return false;
        }
        InstructionSupprimer autre = (InstructionSupprimer) objet;
        return getLigne() == autre.getLigne()
                && nom.equals(autre.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLigne(), nom);
    }

    @Override
    public String toString() {
        return "InstructionSupprimer[ligne " + getLigne() + ", nom = " + nom + "]";
    }
}
