package satellitesbalises.persistance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import eventHandler.EventHandler;
import satellitesbalises.modele.commande.CommandeChangerStatut;
import satellitesbalises.modele.commande.CommandeSimulation;
import satellitesbalises.modele.communication.Transfert;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.deplacement.DeplacementHorizontal;
import satellitesbalises.modele.deplacement.DeplacementOrbital;
import satellitesbalises.modele.deplacement.DeplacementSinusoidal;
import satellitesbalises.modele.entite.Apparence;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.evenement.EcouteurTemps;
import satellitesbalises.modele.evenement.EvenementPasTermine;
import satellitesbalises.modele.mesure.EnvironnementSimule;
import satellitesbalises.modele.mesure.Grandeur;
import satellitesbalises.modele.mesure.InstrumentCourantometre;
import satellitesbalises.modele.mesure.InstrumentSimple;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.Simulation;


class DepotSimulationTest {

    @TempDir
    Path dossier;

    private final GeometrieMonde geometrie = new GeometrieMonde(200, 100, 80);
    private final DepotSimulation depot = new DepotSimulationSerialisation();
    private Simulation simulation;

    @BeforeEach
    void creerCampagne() {
        simulation = campagne();
    }

    /** Une petite campagne complète : un satellite orbital, deux balises aux comportements différents. */
    private Simulation campagne() {
        FabriqueEntites fabrique = new FabriqueEntites(geometrie);
        Simulation s = new Simulation(new Monde(geometrie), EnvironnementSimule.parDefaut(), new EventHandler());
        s.ajouter(fabrique.nouveauSatellite("s1").en(new Position(0, 50)).avecDeplacement(new DeplacementOrbital(2))
                .avecPortee(30).avecDebit(2).avecApparence(new Apparence("bleu", 12)).construire());
        s.ajouter(fabrique.nouvelleBalise("b1").en(new Position(40, -5)).avecMemoire(6).avecVitesseVerticale(3)
                .avecDeplacement(new DeplacementHorizontal(1))
                .equipeeDe(new InstrumentSimple("temperature", Grandeur.TEMPERATURE)).construire());
        s.ajouter(fabrique.nouvelleBalise("b2").en(new Position(150, -20)).avecMemoire(4).avecVitesseVerticale(2)
                .avecDeplacement(new DeplacementSinusoidal(1, 5, 40, 20))
                .equipeeDe(new InstrumentCourantometre()).construire());
        return s;
    }

    /** Résumé textuel et complet de l'état visible d'une simulation. */
    private static String empreinte(Simulation s) {
        StringBuilder texte = new StringBuilder("pas=" + s.getPasCourant() + "\n");
        for (Satellite sat : s.getMonde().getSatellites()) {
            texte.append(sat.getNom()).append(' ').append(sat.getPosition()).append(' ').append(sat.getStatut())
                    .append(" portee=").append(sat.getPortee()).append(" archive=").append(sat.getArchive().getNombreMesures())
                    .append(' ').append(sat.getApparence()).append('\n');
        }
        for (Balise b : s.getMonde().getBalises()) {
            texte.append(b.getNom()).append(' ').append(b.getPosition()).append(' ').append(b.getActivite().getNom())
                    .append(" memoire=").append(b.getMemoire().getTaille()).append('/').append(b.getMemoire().getCapacite())
                    .append(" instruments=").append(b.getInstruments().stream().map(i -> i.getNom()).collect(Collectors.joining(",")))
                    .append('\n');
        }
        for (Transfert t : s.getCommunications().getTransferts()) {
            texte.append("transfert ").append(t.getBalise().getNom()).append("->").append(t.getSatellite().getNom())
                    .append(" transmis=").append(t.getTransmis()).append('\n');
        }
        return texte.toString();
    }

    private static void avancer(Simulation s, int nombreDePas) {
        for (int i = 0; i < nombreDePas; i++) {
            s.avancerDUnPas();
        }
    }

    private static void avancerJusqua(Simulation s, BooleanSupplier condition) {
        for (int i = 0; i < 5000 && !condition.getAsBoolean(); i++) {
            s.avancerDUnPas();
        }
        assertTrue(condition.getAsBoolean(), "la situation attendue n'est jamais arrivée");
    }

    private Path fichier() {
        return dossier.resolve("campagne.sim");
    }

    /** Recharge comme après un redémarrage : nouvelle simulation, nouveau bus d'événements. */
    private Simulation recharger() {
        Simulation relue = depot.charger(fichier());
        relue.rebrancher(new EventHandler());
        return relue;
    }

    // ---------- prérequis : la simulation est déterministe ----------

    @Test
    void deuxCampagnesIdentiques_memeEmpreinteApres300Pas() {
        Simulation autre = campagne();

        avancer(simulation, 300);
        avancer(autre, 300);

        assertEquals(empreinte(simulation), empreinte(autre));
    }

    // ---------- EX-8.1 ----------

    @Test
    void sauverPuisCharger_etatIdentique() {
        avancer(simulation, 137);

        depot.sauvegarder(simulation, fichier());

        assertEquals(empreinte(simulation), empreinte(recharger()));
    }

    // ---------- EX-8.3 ----------

    @Test
    void charger_transfertEnCours_memeInstanceQueMonde() {
        avancerJusqua(simulation, () -> !simulation.getCommunications().getTransferts().isEmpty());

        depot.sauvegarder(simulation, fichier());
        Simulation relue = recharger();

        Transfert transfert = relue.getCommunications().getTransferts().get(0);
        assertSame(relue.getMonde().trouverBalise(transfert.getBalise().getNom()).orElseThrow(), transfert.getBalise());
        assertSame(relue.getMonde().trouverSatellite(transfert.getSatellite().getNom()).orElseThrow(), transfert.getSatellite());
    }

    // ---------- EX-8.4 (avec une commande prévue pour plus tard) ----------

    @Test
    void reprise_apresKPas_identiqueSansInterruption() {
        avancerJusqua(simulation, () -> !simulation.getCommunications().getTransferts().isEmpty());
        simulation.planifier(new CommandeChangerStatut("s1", false), simulation.getPasCourant() + 100);
        depot.sauvegarder(simulation, fichier());

        Simulation relue = recharger();
        avancer(simulation, 300);
        avancer(relue, 300);

        assertEquals(empreinte(simulation), empreinte(relue));
    }

    // ---------- EX-8.5 ----------

    @Test
    void charger_puisRebrancher_vueRecoitLesEvenements() {
        depot.sauvegarder(simulation, fichier());
        Simulation relue = depot.charger(fichier());
        EventHandler nouveau = new EventHandler();
        List<Long> pasRecus = new ArrayList<>();
        nouveau.registerListener(EvenementPasTermine.class, (EcouteurTemps) e -> pasRecus.add(e.getPas()));

        relue.rebrancher(nouveau);
        relue.avancerDUnPas();

        assertEquals(List.of(1L), pasRecus);
    }

    // ---------- EX-8.6 ----------

    @Test
    void sauvegarder_neLaissePasDeFichierTemporaire() throws IOException {
        depot.sauvegarder(simulation, fichier());

        try (Stream<Path> contenu = Files.list(dossier)) {
            assertEquals(List.of(fichier()), contenu.toList());
        }
    }

    @Test
    void sauvegarder_echecEcriture_ancienFichierIntact() {
        avancer(simulation, 10);
        depot.sauvegarder(simulation, fichier());
        avancer(simulation, 5);
        simulation.planifier(new CommandeNonSauvegardable(), 1000);

        assertThrows(ErreurPersistance.class, () -> depot.sauvegarder(simulation, fichier()));

        assertEquals(10, depot.charger(fichier()).getPasCourant(), "l'ancienne sauvegarde est toujours lisible");
    }

    // ---------- EX-8.7 ----------

    @Test
    void charger_fichierCorrompu_leveErreurPersistance() throws IOException {
        Files.writeString(fichier(), "ceci n'est pas une simulation");

        assertThrows(ErreurPersistance.class, () -> depot.charger(fichier()));
    }

    @Test
    void charger_fichierAbsent_leveErreurPersistance() {
        assertFalse(Files.exists(fichier()));

        assertThrows(ErreurPersistance.class, () -> depot.charger(fichier()));
    }

    /** Commande volontairement impossible à sérialiser (elle contient un Object ordinaire). */
    private static final class CommandeNonSauvegardable implements CommandeSimulation {
        private static final long serialVersionUID = 1L;
        @SuppressWarnings({"unused", "serial"})
        private final Object pasSerialisable = new Object();

        @Override
        public void executer(Simulation s) {
        }
    }
}
