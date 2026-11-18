package satellitesbalises.modele.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eventHandler.EventHandler;
import satellitesbalises.modele.activite.ActiviteAttente;
import satellitesbalises.modele.activite.ActiviteDescente;
import satellitesbalises.modele.activite.ActiviteTransmission;
import satellitesbalises.modele.commande.CommandeChangerStatut;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.evenement.EcouteurTransferts;
import satellitesbalises.modele.evenement.EvenementTransfertDemarre;
import satellitesbalises.modele.evenement.EvenementTransfertProgresse;
import satellitesbalises.modele.evenement.EvenementTransfertTermine;
import satellitesbalises.modele.mesure.EnvironnementSimule;
import satellitesbalises.modele.mesure.Grandeur;
import satellitesbalises.modele.mesure.InstrumentSimple;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;
import satellitesbalises.modele.simulation.Simulation;

/**
 * Étape 6 — EX-6.2 à EX-6.8 : la rencontre balise ↔ satellite et le transfert.
 *
 * <p>Calendrier d'une balise à capacité c, en y = −2, vitesse verticale 5 :</p>
 * <ul>
 *   <li>pas 1 à c : collecte (une mesure par pas) ; au pas c la mémoire est pleine → Remontée ;</li>
 *   <li>pas c+1 : surface atteinte → Attente, et dans la phase « communications » du même pas,
 *       le transfert est créé (rien n'est encore transmis) → Transmission ;</li>
 *   <li>pas suivants : « débit » mesures par pas ; le transfert se termine au pas où part la dernière.</li>
 * </ul>
 */
class CommunicationTest {

    private final GeometrieMonde geometrie = new GeometrieMonde(200, 100, 80);
    private final FabriqueEntites fabrique = new FabriqueEntites(geometrie);
    private final EventHandler eventHandler = new EventHandler();
    private Simulation simulation;

    @BeforeEach
    void creerSimulation() {
        simulation = new Simulation(new Monde(geometrie), EnvironnementSimule.parDefaut(), eventHandler);
    }

    private Balise balise(String nom, double x, int capacite) {
        Balise b = fabrique.nouvelleBalise(nom).en(new Position(x, -2)).avecMemoire(capacite).avecVitesseVerticale(5)
                .equipeeDe(new InstrumentSimple("temperature", Grandeur.TEMPERATURE)).construire();
        simulation.ajouter(b);
        return b;
    }

    private Satellite satellite(String nom, double x) {
        Satellite s = fabrique.nouveauSatellite(nom).en(new Position(x, 50)).avecPortee(20).avecDebit(3).construire();
        simulation.ajouter(s);
        return s;
    }

    private void avancer(int nombreDePas) {
        for (int i = 0; i < nombreDePas; i++) {
            simulation.avancerDUnPas();
        }
    }

    private List<Transfert> transferts() {
        return simulation.getCommunications().getTransferts();
    }

    // ---------- EX-6.2 : les trois conditions ----------

    @Test
    void baliseEnAttente_satelliteDisponibleEtProche_transfertCree() {
        Balise b1 = balise("b1", 50, 1);
        Satellite s1 = satellite("s1", 60);

        avancer(2);

        assertEquals(1, transferts().size());
        assertSame(b1, transferts().get(0).getBalise());
        assertSame(s1, transferts().get(0).getSatellite());
        assertInstanceOf(ActiviteTransmission.class, b1.getActivite());
        assertEquals(StatutSatellite.OCCUPE, s1.getStatut());
    }

    @Test
    void baliseEnCollecte_pasDeTransfert() {
        balise("b1", 50, 100);
        satellite("s1", 50);

        avancer(5);

        assertTrue(transferts().isEmpty());
    }

    @Test
    void satelliteHorsService_pasDeTransfert() {
        Balise b1 = balise("b1", 50, 1);
        satellite("s1", 50).arreter();

        avancer(2);

        assertTrue(transferts().isEmpty());
        assertInstanceOf(ActiviteAttente.class, b1.getActivite());
    }

    @Test
    void satelliteTropLoin_pasDeTransfert() {
        Balise b1 = balise("b1", 50, 1);
        satellite("s1", 100);

        avancer(2);

        assertTrue(transferts().isEmpty());
        assertInstanceOf(ActiviteAttente.class, b1.getActivite());
    }

    // ---------- EX-6.3 ----------

    @Test
    void choisirSatellite_deuxCandidats_lePlusProche() {
        balise("b1", 50, 1);
        satellite("loin", 65);
        Satellite proche = satellite("proche", 55);

        avancer(2);

        assertSame(proche, transferts().get(0).getSatellite());
    }

    // ---------- EX-6.4 ----------

    @Test
    void deuxBalisesEnAttente_unSatellite_uneSeuleTransmet() {
        Balise b1 = balise("b1", 50, 1);
        Balise b2 = balise("b2", 55, 1);
        satellite("s1", 52);

        avancer(2);

        assertEquals(1, transferts().size());
        assertSame(b1, transferts().get(0).getBalise(), "la première balise ajoutée est servie en premier");
        assertInstanceOf(ActiviteAttente.class, b2.getActivite(), "l'autre attend son tour");
    }

    // ---------- EX-6.5 ----------

    @Test
    void transfert_dixMesuresDebit3_termineEn4Pas() {
        Balise b1 = balise("b1", 50, 10);
        satellite("s1", 50);

        avancer(11); // transfert créé au pas 11
        assertInstanceOf(ActiviteTransmission.class, b1.getActivite());

        avancer(3); // 3 + 3 + 3
        assertEquals(9, transferts().get(0).getTransmis());
        assertInstanceOf(ActiviteTransmission.class, b1.getActivite());

        avancer(1); // la dernière mesure part
        assertInstanceOf(ActiviteDescente.class, b1.getActivite());
    }

    // ---------- EX-6.6 ----------

    @Test
    void avancer_totalBalisePlusArchiveConstant() {
        Balise b1 = balise("b1", 50, 10);
        Satellite s1 = satellite("s1", 50);
        avancer(11);

        for (int pas = 0; pas < 4; pas++) {
            avancer(1);
            assertEquals(10, b1.getMemoire().getTaille() + s1.getArchive().getNombreMesures(),
                    "aucune mesure perdue ni dupliquée");
        }
        assertEquals(10, s1.getArchive().getMesuresDe("b1").size());
        assertTrue(b1.getMemoire().estVide());
    }

    // ---------- EX-6.7 ----------

    @Test
    void avancer_satelliteHorsPortee_baliseEnAttenteAvecReste() {
        Balise b1 = balise("b1", 50, 10);
        Satellite s1 = satellite("s1", 50);
        avancer(12); // transfert créé au pas 11, 3 mesures parties au pas 12

        s1.placer(new Position(180, 50), new ContexteSimulation(12, geometrie, simulation.getEnvironnement(), eventHandler));
        avancer(1);

        assertTrue(transferts().isEmpty(), "transfert rompu");
        assertInstanceOf(ActiviteAttente.class, b1.getActivite());
        assertEquals(7, b1.getMemoire().getTaille(), "la balise garde ce qui n'est pas parti");
        assertEquals(3, s1.getArchive().getNombreMesures());
        assertEquals(StatutSatellite.DISPONIBLE, s1.getStatut());
    }

    // ---------- EX-6.8 ----------

    @Test
    void transfertComplet_satelliteDisponibleBaliseEnDescente() {
        Balise b1 = balise("b1", 50, 1);
        Satellite s1 = satellite("s1", 50);

        avancer(3); // créé au pas 2, la seule mesure part au pas 3

        assertTrue(transferts().isEmpty());
        assertEquals(StatutSatellite.DISPONIBLE, s1.getStatut());
        assertInstanceOf(ActiviteDescente.class, b1.getActivite());
    }

    // ---------- EX-6.9 ----------

    @Test
    void arreterPendantLeTransfert_satelliteResteHorsService() {
        Balise b1 = balise("b1", 50, 10);
        Satellite s1 = satellite("s1", 50);
        avancer(12);
        simulation.planifier(new CommandeChangerStatut("s1", false), 13);

        avancer(1);

        assertEquals(StatutSatellite.HORS_SERVICE, s1.getStatut());
        assertInstanceOf(ActiviteAttente.class, b1.getActivite());
        assertTrue(transferts().isEmpty());
    }

    // ---------- EX-2.5 : supprimer une entité rompt ses transferts ----------

    @Test
    void supprimerBalisePendantTransfert_satelliteLibere() {
        balise("b1", 50, 10);
        Satellite s1 = satellite("s1", 50);
        avancer(12);

        simulation.supprimer("b1");

        assertTrue(transferts().isEmpty());
        assertEquals(StatutSatellite.DISPONIBLE, s1.getStatut());
    }

    // ---------- EX-6.10 (partie testable) : les événements de transfert ----------

    @Test
    void transfert_publieDemarreProgresseTermine() {
        balise("b1", 50, 4);
        satellite("s1", 50);
        EcouteurDeTransferts ecouteur = new EcouteurDeTransferts();
        eventHandler.registerListener(EvenementTransfertDemarre.class, ecouteur);
        eventHandler.registerListener(EvenementTransfertProgresse.class, ecouteur);
        eventHandler.registerListener(EvenementTransfertTermine.class, ecouteur);

        avancer(7); // 4 pas de collecte, créé au pas 5, 3 puis 1 mesure aux pas 6 et 7

        assertEquals(List.of("démarré", "progressé", "progressé", "terminé complet"), ecouteur.journal);
    }

    private static final class EcouteurDeTransferts implements EcouteurTransferts {
        private final List<String> journal = new ArrayList<>();

        @Override
        public void transfertDemarre(EvenementTransfertDemarre e) {
            journal.add("démarré");
        }

        @Override
        public void transfertProgresse(EvenementTransfertProgresse e) {
            journal.add("progressé");
        }

        @Override
        public void transfertTermine(EvenementTransfertTermine e) {
            journal.add(e.estComplet() ? "terminé complet" : "terminé incomplet");
        }
    }
}
