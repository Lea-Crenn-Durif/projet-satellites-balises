package satellitesbalises.langage.arbre;

import java.util.Objects;

/**
 * Décale les instructions suivantes de n pas : {@code attendre 200}.
 */
public final class InstructionAttendre extends Instruction {

    private final long nombreDePas;

    public InstructionAttendre(int ligne, long nombreDePas) {
        super(ligne);
        this.nombreDePas = nombreDePas;
    }

    public long getNombreDePas() {
        return nombreDePas;
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
        if (!(objet instanceof InstructionAttendre)) {
            return false;
        }
        InstructionAttendre autre = (InstructionAttendre) objet;
        return getLigne() == autre.getLigne()
                && nombreDePas == autre.nombreDePas;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLigne(), nombreDePas);
    }

    @Override
    public String toString() {
        return "InstructionAttendre[ligne " + getLigne() + ", nombreDePas = " + nombreDePas + "]";
    }
}
