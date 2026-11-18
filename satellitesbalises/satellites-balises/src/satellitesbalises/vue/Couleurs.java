package satellitesbalises.vue;

import java.awt.Color;
import java.util.Map;

final class Couleurs {

    private static final Map<String, Color> PAR_NOM = Map.ofEntries(
            Map.entry("noir", Color.BLACK),
            Map.entry("blanc", Color.WHITE),
            Map.entry("gris", Color.GRAY),
            Map.entry("rouge", new Color(200, 30, 30)),
            Map.entry("vert", new Color(20, 140, 40)),
            Map.entry("bleu", new Color(30, 70, 200)),
            Map.entry("jaune", new Color(230, 200, 0)),
            Map.entry("orange", new Color(240, 130, 0)),
            Map.entry("violet", new Color(130, 50, 170)),
            Map.entry("rose", new Color(230, 90, 160)));

    private Couleurs() {
        // Classe utilitaire : pas d'instance.
    }

    static Color depuisNom(String nom) {
        return PAR_NOM.getOrDefault(nom, Color.BLACK);
    }
}
