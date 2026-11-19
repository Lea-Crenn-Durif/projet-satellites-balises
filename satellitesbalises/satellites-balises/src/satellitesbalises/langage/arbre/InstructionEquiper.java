package satellitesbalises.langage.arbre;

import java.util.List;
import java.util.Objects;

/**
 * Ajoute des instruments à une balise : {@code equiper b1 temperature(), salinite()}.
 */
public final class InstructionEquiper extends Instruction {

    private final String nom;
    private final List<AppelComportement> instruments;

    public InstructionEquiper(int ligne, String nom, List<AppelComportement> instruments) {
        super(ligne);
        this.nom = Objects.requireNonNull(nom, "nom");
        this.instruments = List.copyOf(instruments);
    }

    public String getNom() {
        return nom;
    }

    public List<AppelComportement> getInstruments() {
        return instruments;
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
        if (!(objet instanceof InstructionEquiper)) {
            return false;
        }
        InstructionEquiper autre = (InstructionEquiper) objet;
        return getLigne() == autre.getLigne()
                && nom.equals(autre.nom)
                && instruments.equals(autre.instruments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLigne(), nom, instruments);
    }

    @Override
    public String toString() {
        return "InstructionEquiper[ligne " + getLigne() + ", nom = " + nom
                + ", instruments = " + instruments + "]";
    }
}
