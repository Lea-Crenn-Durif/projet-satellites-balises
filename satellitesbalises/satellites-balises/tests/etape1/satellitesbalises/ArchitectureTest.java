package satellitesbalises;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

/**
 * Filet de sécurité de l'architecture (exigence EX-1.9).
 *
 * <p>Le modèle (paquetage satellitesbalises.modele) ne doit dépendre ni de
 * l'affichage, ni du langage, ni de la persistance, ni de l'application.
 * Il n'a le droit d'utiliser que Java et l'EventHandler du cours.</p>
 *
 * <p>Ce test lit les fichiers sources du modèle et échoue en nommant le
 * fichier et la ligne fautive dès qu'un import interdit apparaît.</p>
 */
class ArchitectureTest {

    /** Le test peut être lancé depuis la racine du projet, le module ou le dossier des tests. */
    private static final List<Path> EMPLACEMENTS_POSSIBLES = List.of(
            Path.of("src", "satellitesbalises", "modele"),
            Path.of("..", "src", "satellitesbalises", "modele"),
            Path.of("satellites-balises", "src", "satellitesbalises", "modele"));

    private static final List<String> IMPORTS_INTERDITS_DANS_LE_MODELE = List.of(
            "import nicellipse.",
            "import javax.swing.",
            "import java.awt.",
            "import org.antlr.",
            "import satellitesbalises.vue.",
            "import satellitesbalises.langage.",
            "import satellitesbalises.persistance.",
            "import satellitesbalises.application.");

    @Test
    void modele_aucunImportInterdit_independantDeLAffichageEtDuReste() throws IOException {
        List<String> infractions = new ArrayList<>();

        Path sourcesDuModele = EMPLACEMENTS_POSSIBLES.stream()
                .filter(Files::isDirectory)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Dossier src/satellitesbalises/modele introuvable depuis " + Path.of("").toAbsolutePath()));

        try (Stream<Path> fichiers = Files.walk(sourcesDuModele)) {
            for (Path fichier : fichiers.filter(f -> f.toString().endsWith(".java")).toList()) {
                infractions.addAll(chercherImportsInterdits(fichier));
            }
        }

        assertTrue(infractions.isEmpty(),
                "Le modèle dépend de code qu'il ne doit pas connaître :\n" + String.join("\n", infractions));
    }

    private static List<String> chercherImportsInterdits(Path fichier) throws IOException {
        List<String> infractions = new ArrayList<>();
        List<String> lignes = Files.readAllLines(fichier, StandardCharsets.UTF_8);
        for (int numero = 0; numero < lignes.size(); numero++) {
            String ligne = lignes.get(numero).trim();
            for (String interdit : IMPORTS_INTERDITS_DANS_LE_MODELE) {
                if (ligne.startsWith(interdit)) {
                    infractions.add(fichier + " ligne " + (numero + 1) + " : " + ligne);
                }
            }
        }
        return infractions;
    }
}
