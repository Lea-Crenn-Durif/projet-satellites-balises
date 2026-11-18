package satellitesbalises.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import eventHandler.EventHandler;
import satellitesbalises.langage.analyse.AnalyseurProgramme;
import satellitesbalises.langage.interpretation.InterpreteurProgramme;
import satellitesbalises.modele.activite.ConditionDuree;
import satellitesbalises.modele.activite.ConditionMemoirePleine;
import satellitesbalises.modele.activite.ConditionOu;
import satellitesbalises.modele.creation.CataloguesParDefaut;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.deplacement.DeplacementHorizontal;
import satellitesbalises.modele.deplacement.DeplacementImmobile;
import satellitesbalises.modele.deplacement.DeplacementOrbital;
import satellitesbalises.modele.deplacement.DeplacementSinusoidal;
import satellitesbalises.modele.deplacement.DeplacementVertical;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.mesure.EnvironnementSimule;
import satellitesbalises.modele.mesure.Grandeur;
import satellitesbalises.modele.mesure.InstrumentCourantometre;
import satellitesbalises.modele.mesure.InstrumentSimple;
import satellitesbalises.modele.mesure.Mesure;
import satellitesbalises.modele.mesure.ValeurMesuree;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.monde.Zone;
import satellitesbalises.modele.simulation.Simulation;
import satellitesbalises.persistance.DepotSimulationSerialisation;

/**
 * Étape 9 — EX-9.1 : tests d'intégration couvrant la liste du §6 du sujet :
 * plusieurs entités, déplacements variés, plusieurs instruments, mesures de natures différentes,
 * conditions de communication, transferts, langage, sauvegarde et reprise.
 */
class ScenarioCompletTest {

    private final GeometrieMonde geometrie = new GeometrieMonde(800, 300, 300);

    private Simulation grandeCampagne() {
        FabriqueEntites f = new FabriqueEntites(geometrie);
        Simulation s = new Simulation(new Monde(geometrie), EnvironnementSimule.parDefaut(), new EventHandler());
        s.ajouter(f.nouveauSatellite("s1").en(new Position(0, 200)).avecDeplacement(new DeplacementOrbital(3)).avecPortee(50).construire());
        s.ajouter(f.nouveauSatellite("s2").en(new Position(400, 250)).avecDeplacement(new DeplacementOrbital(5)).avecPortee(40).construire());
        s.ajouter(f.nouveauSatellite("s3").en(new Position(700, 150)).avecPortee(60).avecDebit(4).construire()); // géostationnaire
        s.ajouter(f.nouvelleBalise("b1").en(new Position(100, -50)).avecDeplacement(new DeplacementHorizontal(2)).avecMemoire(20)
                .equipeeDe(new InstrumentSimple("temperature", Grandeur.TEMPERATURE)).construire());
        s.ajouter(f.nouvelleBalise("b2").en(new Position(250, -120)).avecDeplacement(new DeplacementVertical(2, 60, 200))
                .avecVitesseVerticale(4).equipeeDe(new InstrumentSimple("salinite", Grandeur.SALINITE), new InstrumentCourantometre()).construire());
        s.ajouter(f.nouvelleBalise("b3").en(new Position(500, -80)).avecDeplacement(new DeplacementSinusoidal(1, 20, 80, 80))
                .remontantQuand(new ConditionOu(new ConditionMemoirePleine(), new ConditionDuree(60)))
                .equipeeDe(new InstrumentSimple("turbidite", Grandeur.TURBIDITE)).construire());
        s.ajouter(f.nouvelleBalise("b4").en(new Position(690, -30)).avecDeplacement(new DeplacementImmobile()).avecMemoire(10)
                .equipeeDe(new InstrumentSimple("temperature", Grandeur.TEMPERATURE)).construire());
        s.ajouter(f.nouvelleBalise("b5").en(new Position(720, -200)).avecDeplacement(new DeplacementHorizontal(1)).avecMemoire(15)
                .avecVitesseVerticale(5).equipeeDe(new InstrumentCourantometre()).construire());
        return s;
    }

    @Test
    void grandeCampagne_3000Pas_toutLeMondeResteDansSaZone() {
        Simulation s = grandeCampagne();

        for (int pas = 1; pas <= 3000; pas++) {
            s.avancerDUnPas();
            for (Satellite sat : s.getMonde().getSatellites()) {
                assertEquals(Zone.CIEL, geometrie.zoneDe(sat.getPosition()), sat.getNom() + " au pas " + pas);
            }
            for (Balise b : s.getMonde().getBalises()) {
                assertTrue(geometrie.contient(b.getPosition()), b.getNom() + " hors du monde au pas " + pas);
                assertEquals(Zone.MER, geometrie.zoneDe(b.getPosition()), b.getNom() + " au pas " + pas);
            }
        }
    }

    @Test
    void grandeCampagne_3000Pas_donneesDeToutesLesBalisesEtDeToutesNatures() {
        Simulation s = grandeCampagne();
        for (int pas = 0; pas < 3000; pas++) {
            s.avancerDUnPas();
        }

        Set<String> balisesEntendues = new HashSet<>();
        Set<Grandeur> grandeursRecues = new HashSet<>();
        for (Satellite sat : s.getMonde().getSatellites()) {
            for (Balise b : s.getMonde().getBalises()) {
                for (Mesure m : sat.getArchive().getMesuresDe(b.getNom())) {
                    balisesEntendues.add(b.getNom());
                    m.getValeurs().stream().map(ValeurMesuree::getGrandeur).forEach(grandeursRecues::add);
                }
            }
        }

        assertEquals(Set.of("b1", "b2", "b3", "b4", "b5"), balisesEntendues, "chaque balise a pu transmettre");
        assertEquals(5, grandeursRecues.size(), "les cinq grandeurs sont arrivées aux satellites");
    }

    @Test
    void langage_exemple1_3000Pas_lesSatellitesRecoiventDesDonnees() throws IOException {
        Simulation s = new Simulation(new Monde(geometrie), EnvironnementSimule.parDefaut(), new EventHandler());
        String texte = Files.readString(Path.of("exemples", "01-construction.sb"), StandardCharsets.UTF_8);
        new InterpreteurProgramme(new FabriqueEntites(geometrie), CataloguesParDefaut.creer())
                .interpreter(new AnalyseurProgramme().analyser(texte), s);

        for (int pas = 0; pas < 3000; pas++) {
            s.avancerDUnPas();
        }

        int recues = s.getMonde().getSatellites().stream().mapToInt(sat -> sat.getArchive().getNombreMesures()).sum();
        assertTrue(recues > 0, "aucune donnée transmise en 3000 pas");
    }

    @Test
    void sauvegardeEnPleineCampagne_repriseSansPerte(@TempDir Path dossier) {
        Simulation s = grandeCampagne();
        for (int pas = 0; pas < 1000; pas++) {
            s.avancerDUnPas();
        }
        DepotSimulationSerialisation depot = new DepotSimulationSerialisation();
        depot.sauvegarder(s, dossier.resolve("campagne.sim"));

        Simulation relue = depot.charger(dossier.resolve("campagne.sim"));
        relue.rebrancher(new EventHandler());
        for (int pas = 0; pas < 1000; pas++) {
            s.avancerDUnPas();
            relue.avancerDUnPas();
        }

        for (Satellite sat : s.getMonde().getSatellites()) {
            Satellite jumeau = relue.getMonde().trouverSatellite(sat.getNom()).orElseThrow();
            assertEquals(sat.getArchive().getNombreMesures(), jumeau.getArchive().getNombreMesures(), sat.getNom());
            assertEquals(sat.getPosition(), jumeau.getPosition(), sat.getNom());
        }
    }
}
