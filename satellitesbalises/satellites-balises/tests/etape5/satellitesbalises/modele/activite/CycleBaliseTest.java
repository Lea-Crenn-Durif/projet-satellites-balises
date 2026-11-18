package satellitesbalises.modele.activite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eventHandler.EventHandler;
import satellitesbalises.modele.commande.CommandeForcerRemontee;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.deplacement.DeplacementHorizontal;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.evenement.EcouteurBalises;
import satellitesbalises.modele.evenement.EvenementActiviteModifiee;
import satellitesbalises.modele.evenement.EvenementEquipementModifie;
import satellitesbalises.modele.evenement.EvenementMemoireModifiee;
import satellitesbalises.modele.mesure.EnvironnementSimule;
import satellitesbalises.modele.mesure.Grandeur;
import satellitesbalises.modele.mesure.InstrumentSimple;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;
import satellitesbalises.modele.simulation.Simulation;


class CycleBaliseTest {

    private final GeometrieMonde geometrie = new GeometrieMonde(200, 100, 80);
    private final FabriqueEntites fabrique = new FabriqueEntites(geometrie);
    private final EventHandler eventHandler = new EventHandler();
    private Simulation simulation;
    private Balise b1;

    @BeforeEach
    void creerBaliseDeReference() {
        simulation = new Simulation(new Monde(geometrie), EnvironnementSimule.parDefaut(), eventHandler);
        b1 = fabrique.nouvelleBalise("b1").en(new Position(50, -10)).avecMemoire(2).avecVitesseVerticale(4)
                .equipeeDe(new InstrumentSimple("temperature", Grandeur.TEMPERATURE)).construire();
        simulation.ajouter(b1);
    }

    private void avancer(int nombreDePas) {
        for (int i = 0; i < nombreDePas; i++) {
            simulation.avancerDUnPas();
        }
    }

    private ContexteSimulation contexte() {
        return new ContexteSimulation(simulation.getPasCourant(), geometrie, simulation.getEnvironnement(), eventHandler);
    }

    /** Amène la balise de référence en Attente à la surface (5 pas). */
    private void amenerEnAttente() {
        avancer(5);
        assertInstanceOf(ActiviteAttente.class, b1.getActivite(), "préalable : la balise doit être en attente");
    }

    // ---------- EX-5.1, EX-5.2 : Collecte → Remontée ----------

    @Test
    void collecte_memoirePleine_passeEnRemontee() {
        avancer(1);
        assertInstanceOf(ActiviteCollecte.class, b1.getActivite());

        avancer(1);
        assertInstanceOf(ActiviteRemontee.class, b1.getActivite());
    }

    @Test
    void conditionDuree_dureeAtteinte_passeEnRemontee() {
        Balise b2 = fabrique.nouvelleBalise("b2").en(new Position(100, -20)).avecMemoire(50)
                .remontantQuand(new ConditionDuree(3)).construire();
        simulation.ajouter(b2);

        avancer(2);
        assertInstanceOf(ActiviteCollecte.class, b2.getActivite(), "2 pas de collecte sur 3");

        avancer(1);
        assertInstanceOf(ActiviteRemontee.class, b2.getActivite());
    }

    // ---------- EX-5.3 ----------

    @Test
    void demanderRemontee_enCollecte_passeEnRemontee() {
        Balise b2 = fabrique.nouvelleBalise("b2").en(new Position(100, -20)).avecMemoire(50).construire();
        simulation.ajouter(b2);
        simulation.planifier(new CommandeForcerRemontee("b2"), 1);

        avancer(1);

        assertInstanceOf(ActiviteRemontee.class, b2.getActivite());
    }

    // ---------- EX-5.4 ----------

    @Test
    void remontee_monteALaVerticaleSansMesurer() {
        avancer(3);

        assertEquals(new Position(50, -6), b1.getPosition());
        assertEquals(2, b1.getMemoire().getTaille(), "pas de mesure pendant la remontée");
    }

    @Test
    void remontee_atteintSurface_passeEnAttenteSansDepasser() {
        avancer(5);

        assertEquals(0, b1.getPosition().getY(), 1e-9, "jamais au-dessus de la surface");
        assertInstanceOf(ActiviteAttente.class, b1.getActivite());
        assertTrue(b1.estEnAttente());
    }

    // ---------- EX-5.5 ----------

    @Test
    void attente_dixPas_memoireEtPositionInchangees() {
        amenerEnAttente();

        avancer(10);

        assertEquals(2, b1.getMemoire().getTaille());
        assertEquals(new Position(50, 0), b1.getPosition());
        assertInstanceOf(ActiviteAttente.class, b1.getActivite());
    }

    // ---------- EX-5.1 : Attente → Transmission → Descente / Attente ----------

    @Test
    void commencerTransmission_enAttente_passeEnTransmission() {
        amenerEnAttente();

        b1.commencerTransmission(contexte());

        assertInstanceOf(ActiviteTransmission.class, b1.getActivite());
        assertFalse(b1.estEnAttente());
    }

    @Test
    void commencerTransmission_enCollecte_ignore() {
        b1.commencerTransmission(contexte());

        assertInstanceOf(ActiviteCollecte.class, b1.getActivite());
    }

    @Test
    void terminerTransmission_complete_passeEnDescente() {
        amenerEnAttente();
        b1.commencerTransmission(contexte());
        b1.ceder(2);

        b1.terminerTransmission(true, contexte());

        assertInstanceOf(ActiviteDescente.class, b1.getActivite());
    }

    @Test
    void terminerTransmission_interrompue_revientEnAttente() {
        amenerEnAttente();
        b1.commencerTransmission(contexte());

        b1.terminerTransmission(false, contexte());

        assertInstanceOf(ActiviteAttente.class, b1.getActivite());
    }

    // ---------- EX-5.6 ----------

    @Test
    void descente_profondeurAtteinte_reprendCollecte() {
        amenerEnAttente();
        b1.commencerTransmission(contexte());
        b1.ceder(2);
        b1.terminerTransmission(true, contexte());

        // y : 0 → −4 → −8 → −10 (s'arrête exactement à sa profondeur de travail)
        avancer(3);

        assertEquals(-10, b1.getPosition().getY(), 1e-9);
        assertInstanceOf(ActiviteCollecte.class, b1.getActivite());
    }

    // ---------- EX-5.7 ----------

    @Test
    void cycleComplet_deplacementInchange() {
        Deplacement horizontal = new DeplacementHorizontal(2);
        Balise b2 = fabrique.nouvelleBalise("b2").en(new Position(100, -10)).avecMemoire(2).avecVitesseVerticale(5)
                .avecDeplacement(horizontal).equipeeDe(new InstrumentSimple("temperature", Grandeur.TEMPERATURE)).construire();
        simulation.ajouter(b2);

        avancer(2); // collecte : x = 102 puis 104, mémoire pleine → Remontée
        avancer(2); // remontée verticale
        assertEquals(104, b2.getPosition().getX(), 1e-9, "la remontée est verticale");
        assertSame(horizontal, b2.getDeplacement(), "le déplacement configuré n'est pas perdu");
    }

    // ---------- EX-5.8 ----------

    @Test
    void changerActivite_publieActiviteModifiee() {
        EcouteurDActivite ecouteur = new EcouteurDActivite();
        eventHandler.registerListener(EvenementActiviteModifiee.class, ecouteur);

        avancer(5); // Collecte → Remontée (pas 2), Remontée → Attente (pas 5)

        assertEquals(List.of("Remontée", "Attente"), ecouteur.activites);
    }

    @Test
    void nomsDesActivites_pourLAffichage() {
        assertEquals("Collecte", new ActiviteCollecte().getNom());
        assertEquals("Remontée", new ActiviteRemontee(-10).getNom());
        assertEquals("Attente", new ActiviteAttente(-10).getNom());
        assertEquals("Transmission", new ActiviteTransmission(-10).getNom());
        assertEquals("Descente", new ActiviteDescente(-10).getNom());
    }

    private static final class EcouteurDActivite implements EcouteurBalises {
        private final List<String> activites = new ArrayList<>();

        @Override
        public void activiteModifiee(EvenementActiviteModifiee e) {
            activites.add(e.getBalise().getActivite().getNom());
        }

        @Override
        public void memoireModifiee(EvenementMemoireModifiee e) {
        }

        @Override
        public void equipementModifie(EvenementEquipementModifie e) {
        }
    }
}
