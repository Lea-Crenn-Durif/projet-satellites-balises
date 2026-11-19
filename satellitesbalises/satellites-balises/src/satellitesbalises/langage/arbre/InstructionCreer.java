package satellitesbalises.langage.arbre;

import java.util.List;
import java.util.Objects;

/**
 * Crée une entité : {@code satellite s1 en (0, 200) { portee = 40 ; debit = 3 }}.
 */
public final class InstructionCreer extends Instruction {

    private final TypeEntite type;
    private final String nom;
    private final double x;
    private final double y;
    private final List<Propriete> proprietes;

    public InstructionCreer(int ligne, TypeEntite type, String nom, double x, double y, List<Propriete> proprietes) {
        super(ligne);
        this.type = Objects.requireNonNull(type, "type");
        this.nom = Objects.requireNonNull(nom, "nom");
        this.x = x;
        this.y = y;
        this.proprietes = List.copyOf(proprietes);
    }

    public TypeEntite getType() {
        return type;
    }

    public String getNom() {
        return nom;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /** Les propriétés facultatives, dans l'ordre écrit. */
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
        if (!(objet instanceof InstructionCreer)) {
            return false;
        }
        InstructionCreer autre = (InstructionCreer) objet;
        return getLigne() == autre.getLigne()
                && type == autre.type
                && nom.equals(autre.nom)
                && Double.compare(x, autre.x) == 0
                && Double.compare(y, autre.y) == 0
                && proprietes.equals(autre.proprietes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLigne(), type, nom, x, y, proprietes);
    }

    @Override
    public String toString() {
        return "InstructionCreer[ligne " + getLigne() + ", type = " + type + ", nom = " + nom
                + ", x = " + x + ", y = " + y + ", proprietes = " + proprietes + "]";
    }
}
