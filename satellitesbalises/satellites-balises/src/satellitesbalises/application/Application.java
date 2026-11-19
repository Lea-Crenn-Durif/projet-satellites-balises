package satellitesbalises.application;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import javax.swing.SwingUtilities;

import eventHandler.EventHandler;
import satellitesbalises.langage.analyse.AnalyseurProgramme;
import satellitesbalises.langage.interpretation.InterpreteurProgramme;
import satellitesbalises.modele.creation.CataloguesParDefaut;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.mesure.EnvironnementSimule;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.simulation.Simulation;
import satellitesbalises.vue.VueSimulation;

/**
 * Point d'entrée : assemble la simulation, la vue, le langage et la boucle temps réel.
 *
 * <p>C'est le seul endroit qui connaît toutes les classes concrètes (racine de composition).
 * Au démarrage, il charge et exécute le programme de démonstration {@code exemples/00-demonstration.sb}.</p>
 */
public final class Application {

    private static final GeometrieMonde GEOMETRIE = new GeometrieMonde(800, 300, 300);
    private static final int MILLISECONDES_PAR_PAS = 100;
    private static final String DEMONSTRATION = "00-demonstration.sb";

    /** L'application peut être lancée depuis le module ou depuis la racine du projet. */
    private static final List<Path> DOSSIERS_EXEMPLES_POSSIBLES = List.of(
            Path.of("exemples"), Path.of("satellites-balises", "exemples"));

    private Application() {
        // Classe utilitaire : pas d'instance.
    }

    public static void main(String[] arguments) {
        SwingUtilities.invokeLater(Application::demarrer);
    }

    private static void demarrer() {
        EventHandler eventHandler = new EventHandler();
        Simulation simulation = new Simulation(new Monde(GEOMETRIE), EnvironnementSimule.parDefaut(), eventHandler);

        BoucleTempsReel boucle = new BoucleTempsReel(simulation, MILLISECONDES_PAR_PAS);
        InterpreteurProgramme interpreteur = new InterpreteurProgramme(
                new FabriqueEntites(GEOMETRIE), CataloguesParDefaut.creer());
        ControleurApplication controleur = new ControleurApplication(
                boucle, simulation, new AnalyseurProgramme(), interpreteur);

        VueSimulation vue = new VueSimulation(GEOMETRIE, controleur);
        vue.abonner(eventHandler);
        lancerLaDemonstration(vue, controleur);

        vue.ouvrir();
        boucle.demarrer();
    }

    private static void lancerLaDemonstration(VueSimulation vue, ControleurApplication controleur) {
        Optional<String> programme = lireDemonstration();
        if (programme.isEmpty()) {
            vue.afficherMessage("Programme de démonstration introuvable (exemples/" + DEMONSTRATION + ").");
            return;
        }
        vue.afficherProgramme(programme.get());
        vue.afficherMessage("Démonstration : " + controleur.executerProgramme(programme.get()));
    }

    private static Optional<String> lireDemonstration() {
        for (Path dossier : DOSSIERS_EXEMPLES_POSSIBLES) {
            Path fichier = dossier.resolve(DEMONSTRATION);
            if (Files.isRegularFile(fichier)) {
                try {
                    return Optional.of(Files.readString(fichier, StandardCharsets.UTF_8));
                } catch (IOException erreur) {
                    return Optional.empty();
                }
            }
        }
        return Optional.empty();
    }
}
