package satellitesbalises.langage.analyse;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import eventHandler.EventHandler;
import satellitesbalises.langage.DossierExemples;
import satellitesbalises.langage.interpretation.InterpreteurProgramme;
import satellitesbalises.langage.interpretation.RapportExecution;
import satellitesbalises.modele.creation.CataloguesParDefaut;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.mesure.EnvironnementSimule;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.simulation.Simulation;

/**
 * Étape 7c — EX-7.10 : tous les programmes du dossier exemples/ s'analysent et s'exécutent
 * sans erreur dans le monde de référence (800 × 300 × 300), puis tournent 500 pas.
 * Ajouter un exemple = déposer un fichier .sb : il est testé automatiquement.
 */
class ExemplesTest {

    @Test
    void tousLesExemples_analysesInterpretesEtExecutes() throws IOException {
        List<Path> fichiers;
        try (Stream<Path> contenu = Files.list(DossierExemples.chemin())) {
            fichiers = contenu.filter(f -> f.toString().endsWith(".sb")).sorted().toList();
        }
        assertTrue(fichiers.size() >= 4, "au moins quatre exemples attendus");

        for (Path fichier : fichiers) {
            GeometrieMonde geometrie = new GeometrieMonde(800, 300, 300);
            Simulation simulation = new Simulation(new Monde(geometrie), EnvironnementSimule.parDefaut(), new EventHandler());
            InterpreteurProgramme interpreteur = new InterpreteurProgramme(new FabriqueEntites(geometrie), CataloguesParDefaut.creer());

            RapportExecution rapport = interpreteur.interpreter(
                    new AnalyseurProgramme().analyser(Files.readString(fichier, StandardCharsets.UTF_8)), simulation);

            assertTrue(rapport.estReussi(), fichier + " : " + rapport.getErreurs());
            for (int pas = 0; pas < 500; pas++) {
                simulation.avancerDUnPas();
            }
        }
    }
}
