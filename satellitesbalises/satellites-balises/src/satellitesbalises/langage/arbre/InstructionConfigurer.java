package satellitesbalises.langage.arbre;

import java.util.List;
import java.util.Objects;

/**
 * Change des caractéristiques : {@code configurer s2 { portee = 60 }}.
 */
public final class InstructionConfigurer extends Instruction {

    private final String nom;
    private final List<Propriete> proprietes;

    public InstructionConfigurer(int ligne, String nom, List<Propriete> proprietes) {
        super(ligne);
        this.nom = Objects.requireNonNull(nom, "nom");
        this.proprietes = List.copyOf(proprietes);
    }

    public String getNom() {
        return nom;
    }

    public List<Propriete> getProprietes() {
        return proprietes;
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
        if (!(objet instanceof InstructionConfigurer)) {
            return false;
        }
        InstructionConfigurer autre = (InstructionConfigurer) objet;
        return getLigne() == autre.getLigne()
                && nom.equals(autre.nom)
                && proprietes.equals(autre.proprietes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLigne(), nom, proprietes);
    }

    @Override
    public String toString() {
        return "InstructionConfigurer[ligne " + getLigne() + ", nom = " + nom
                + ", proprietes = " + proprietes + "]";
    }
}
