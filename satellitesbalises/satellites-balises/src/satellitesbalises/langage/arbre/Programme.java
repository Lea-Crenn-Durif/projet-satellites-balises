package satellitesbalises.langage.arbre;

import java.util.List;
import java.util.Objects;

/**
 * Un programme : la suite de ses instructions, dans l'ordre du texte.
 */
public final class Programme {

    private final List<Instruction> instructions;

    public Programme(List<Instruction> instructions) {
        this.instructions = List.copyOf(instructions);
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof Programme)) {
            return false;
        }
        Programme autre = (Programme) objet;
        return instructions.equals(autre.instructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instructions);
    }

    @Override
    public String toString() {
        return "Programme[instructions = " + instructions + "]";
    }
}
