package satellitesbalises.langage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Trouve le dossier exemples/, que les tests soient lancés depuis le module, le dossier des tests
 * ou la racine du projet (comme ArchitectureTest pour src/).
 */
public final class DossierExemples {

    private static final List<Path> EMPLACEMENTS_POSSIBLES = List.of(
            Path.of("exemples"),
            Path.of("..", "exemples"),
            Path.of("satellites-balises", "exemples"));

    private DossierExemples() {
    }

    public static Path chemin() {
        return EMPLACEMENTS_POSSIBLES.stream()
                .filter(Files::isDirectory)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Dossier exemples/ introuvable depuis " + Path.of("").toAbsolutePath()));
    }

    public static Path fichier(String nom) {
        return chemin().resolve(nom);
    }
}
