package satellitesbalises.langage.arbre;

/**
 * Un traitement des instructions, une méthode par type d'instruction (patron Visiteur).
 * Exemples : l'interpréteur, qui produit des commandes ; un compteur, dans les tests.
 *
 * @param <R> ce que renvoie chaque visite ({@code Void} si rien)
 */
public interface VisiteurInstruction<R> {

    R visiter(InstructionCreer instruction);

    R visiter(InstructionSupprimer instruction);

    R visiter(InstructionConfigurer instruction);

    R visiter(InstructionComportement instruction);

    R visiter(InstructionEquiper instruction);

    R visiter(InstructionDesequiper instruction);

    R visiter(InstructionAgir instruction);

    R visiter(InstructionApparence instruction);

    R visiter(InstructionAttendre instruction);
}
