package satellitesbalises.langage.arbre;

import java.util.Objects;

/**
 * Donne un ordre : {@code arreter s1}, {@code demarrer s1}, {@code remonter b1}.
 */
public final class InstructionAgir extends Instruction {

    private final String nom;
    private final Ordre ordre;

    public InstructionAgir(int ligne, String nom, Ordre ordre) {
        super(ligne);
        this.nom = Objects.requireNonNull(nom, "nom");
        this.ordre = Objects.requireNonNull(ordre, "ordre");
    }

    public String getNom() {
        return nom;
    }

    public Ordre getOrdre() {
        return ordre;
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
        if (!(objet instanceof InstructionAgir)) {
            return false;
        }
        InstructionAgir autre = (InstructionAgir) objet;
        return getLigne() == autre.getLigne()
                && nom.equals(autre.nom)
                && ordre == autre.ordre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLigne(), nom, ordre);
    }

    @Override
    public String toString() {
        return "InstructionAgir[ligne " + getLigne() + ", nom = " + nom + ", ordre = " + ordre + "]";
    }
}
