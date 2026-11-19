package satellitesbalises.langage.arbre;

import java.util.Objects;

/**
 * Retire un instrument à une balise : {@code desequiper b1 turbidite}.
 */
public final class InstructionDesequiper extends Instruction {

    private final String nom;
    private final String nomInstrument;

    public InstructionDesequiper(int ligne, String nom, String nomInstrument) {
        super(ligne);
        this.nom = Objects.requireNonNull(nom, "nom");
        this.nomInstrument = Objects.requireNonNull(nomInstrument, "nomInstrument");
    }

    public String getNom() {
        return nom;
    }

    public String getNomInstrument() {
        return nomInstrument;
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
        if (!(objet instanceof InstructionDesequiper)) {
            return false;
        }
        InstructionDesequiper autre = (InstructionDesequiper) objet;
        return getLigne() == autre.getLigne()
                && nom.equals(autre.nom)
                && nomInstrument.equals(autre.nomInstrument);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLigne(), nom, nomInstrument);
    }

    @Override
    public String toString() {
        return "InstructionDesequiper[ligne " + getLigne() + ", nom = " + nom
                + ", nomInstrument = " + nomInstrument + "]";
    }
}
