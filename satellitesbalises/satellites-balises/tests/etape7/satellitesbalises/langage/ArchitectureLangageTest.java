package satellitesbalises.langage;

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
 * Étape 7 — EX-7.1 : l'arbre abstrait ne dépend ni d'ANTLR ni du modèle, et l'interpréteur ne voit
 * jamais ANTLR. Seul langage.analyse connaît ANTLR.
 */
class ArchitectureLangageTest {

    private static final List<Path> RACINES_POSSIBLES = List.of(
            Path.of("src", "satellitesbalises", "langage"),
            Path.of("..", "src", "satellitesbalises", "langage"),
            Path.of("satellites-balises", "src", "satellitesbalises", "langage"));

    @Test
    void arbre_niAntlrNiModele() throws IOException {
        verifierAucunImport("arbre", List.of("import org.antlr.", "import satellitesbalises.modele.",
                "import satellitesbalises.vue.", "import satellitesbalises.langage.analyse.",
                "import satellitesbalises.langage.interpretation."));
    }

    @Test
    void interpretation_sansAntlr() throws IOException {
        verifierAucunImport("interpretation", List.of("import org.antlr.", "import satellitesbalises.langage.analyse.",
                "import satellitesbalises.vue."));
    }

    private static void verifierAucunImport(String paquetage, List<String> interdits) throws IOException {
        Path racine = RACINES_POSSIBLES.stream()
                .filter(Files::isDirectory)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Dossier src/satellitesbalises/langage introuvable depuis " + Path.of("").toAbsolutePath()));
        List<String> infractions = new ArrayList<>();
        try (Stream<Path> fichiers = Files.walk(racine.resolve(paquetage))) {
            for (Path fichier : fichiers.filter(f -> f.toString().endsWith(".java")).toList()) {
                List<String> lignes = Files.readAllLines(fichier, StandardCharsets.UTF_8);
                for (int numero = 0; numero < lignes.size(); numero++) {
                    String ligne = lignes.get(numero).trim();
                    if (interdits.stream().anyMatch(ligne::startsWith)) {
                        infractions.add(fichier + " ligne " + (numero + 1) + " : " + ligne);
                    }
                }
            }
        }
        assertTrue(infractions.isEmpty(), "langage." + paquetage + " dépend de ce qu'il ne doit pas connaître :\n"
                + String.join("\n", infractions));
    }
}
