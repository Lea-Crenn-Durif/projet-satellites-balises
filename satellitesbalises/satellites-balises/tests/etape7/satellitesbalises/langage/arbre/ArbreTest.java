package satellitesbalises.langage.arbre;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Étape 7a — EX-7.1 : l'arbre abstrait existe seul, sans ANTLR ni modèle.
 * Les classes de l'arbre redéfinissent equals : deux arbres identiques sont égaux.
 */
class ArbreTest {

    @Test
    void deuxArbresIdentiques_sontEgaux() {
        assertEquals(ProgrammesExemples.construction(), ProgrammesExemples.construction());
    }

    @Test
    void accepter_appelleLaBonneMethodeDuVisiteur() {
        CompteurDInstructions compteur = new CompteurDInstructions();

        for (Instruction instruction : ProgrammesExemples.communication().getInstructions()) {
            instruction.accepter(compteur);
        }

        assertEquals(2, compteur.creations);
        assertEquals(3, compteur.ordres);
        assertEquals(2, compteur.attentes);
    }

    @Test
    void ligne_conserveePourLesMessagesDErreur() {
        assertEquals(13, ProgrammesExemples.communication().getInstructions().get(8).getLigne());
    }

    /** Un visiteur minimal : il compte, il n'exécute rien. */
    private static final class CompteurDInstructions implements VisiteurInstruction<Void> {
        private int creations;
        private int ordres;
        private int attentes;

        @Override
        public Void visiter(InstructionCreer i) {
            creations++;
            return null;
        }

        @Override
        public Void visiter(InstructionSupprimer i) {
            return null;
        }

        @Override
        public Void visiter(InstructionConfigurer i) {
            return null;
        }

        @Override
        public Void visiter(InstructionComportement i) {
            return null;
        }

        @Override
        public Void visiter(InstructionEquiper i) {
            return null;
        }

        @Override
        public Void visiter(InstructionDesequiper i) {
            return null;
        }

        @Override
        public Void visiter(InstructionAgir i) {
            ordres++;
            return null;
        }

        @Override
        public Void visiter(InstructionApparence i) {
            return null;
        }

        @Override
        public Void visiter(InstructionAttendre i) {
            attentes++;
            return null;
        }
    }
}
