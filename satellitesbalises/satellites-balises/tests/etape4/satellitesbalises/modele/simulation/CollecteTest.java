package satellitesbalises.modele.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eventHandler.EventHandler;
import satellitesbalises.modele.commande.CommandeDesequiper;
import satellitesbalises.modele.commande.CommandeEquiper;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.evenement.EcouteurBalises;
import satellitesbalises.modele.evenement.EvenementActiviteModifiee;
import satellitesbalises.modele.evenement.EvenementEquipementModifie;
import satellitesbalises.modele.evenement.EvenementMemoireModifiee;
import satellitesbalises.modele.mesure.EnvironnementSimule;
import satellitesbalises.modele.mesure.Grandeur;
import satellitesbalises.modele.mesure.Instrument;
import satellitesbalises.modele.mesure.InstrumentCourantometre;
import satellitesbalises.modele.mesure.InstrumentSimple;
import satellitesbalises.modele.mesure.Mesure;
import satellitesbalises.modele.mesure.ValeurMesuree;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.monde.Position;

/** Étape 4 — EX-4.1, EX-4.4, EX-4.8, EX-4.9 : une balise en collecte remplit sa mémoire. */
class CollecteTest {

    private final GeometrieMonde geometrie = new GeometrieMonde(200, 100, 80);
    private final FabriqueEntites fabrique = new FabriqueEntites(geometrie);
    private final EventHandler eventHandler = new EventHandler();
    private Simulation simulation;

    @BeforeEach
    void creerSimulation() {
        simulation = new Simulation(new Monde(geometrie), EnvironnementSimule.parDefaut(), eventHandler);
    }

    private Balise baliseEquipee(Instrument... instruments) {
        Balise b1 = fabrique.nouvelleBalise("b1").en(new Position(50, -30)).avecMemoire(50).equipeeDe(instruments).construire();
        simulation.ajouter(b1);
        return b1;
    }

    @Test
    void agir_collecte_memoireAugmenteDuNombreDInstruments() {
        Balise b1 = baliseEquipee(new InstrumentSimple("temperature", Grandeur.TEMPERATURE), new InstrumentCourantometre());

        simulation.avancerDUnPas();
        assertEquals(2, b1.getMemoire().getTaille());

        simulation.avancerDUnPas();
        simulation.avancerDUnPas();
        assertEquals(6, b1.getMemoire().getTaille());
    }

    @Test
    void mesuresStockees_dateesEtPlacees() {
        Balise b1 = baliseEquipee(new InstrumentSimple("temperature", Grandeur.TEMPERATURE));

        simulation.avancerDUnPas();
        simulation.avancerDUnPas();

        List<Mesure> mesures = b1.getMemoire().getMesures();
        assertEquals(List.of(1L, 2L), mesures.stream().map(Mesure::getPas).toList());
        assertEquals(new Position(50, -30), mesures.get(0).getPosition());
    }

    @Test
    void sansInstrument_neMesureRien() {
        Balise b1 = baliseEquipee();

        simulation.avancerDUnPas();

        assertTrue(b1.getMemoire().estVide());
    }

    @Test
    void equiper_deuxInstruments_deuxMesuresParPas() {
        Balise b1 = baliseEquipee();
        simulation.planifier(new CommandeEquiper("b1", new InstrumentSimple("salinite", Grandeur.SALINITE)), 1);
        simulation.planifier(new CommandeEquiper("b1", new InstrumentSimple("turbidite", Grandeur.TURBIDITE)), 1);

        simulation.avancerDUnPas();

        assertEquals(2, b1.getInstruments().size());
        assertEquals(2, b1.getMemoire().getTaille());
    }

    @Test
    void desequiper_parSonNom_neMesurePlus() {
        Balise b1 = baliseEquipee(new InstrumentSimple("temperature", Grandeur.TEMPERATURE),
                new InstrumentSimple("salinite", Grandeur.SALINITE));
        simulation.planifier(new CommandeDesequiper("b1", "temperature"), 1);

        simulation.avancerDUnPas();

        assertEquals(1, b1.getInstruments().size());
        assertEquals("salinite", b1.getMemoire().getMesures().get(0).getNomInstrument());
    }

    @Test
    void nouvelInstrumentDefiniHorsDuProjet_collecteSansRienModifier() {
        // EX-4.8 : un instrument inventé ici même, inconnu du projet, fonctionne tel quel
        Grandeur oxygene = new Grandeur("oxygene", "mg/L");
        Instrument oxymetre = new Instrument() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getNom() {
                return "oxymetre";
            }

            @Override
            public List<Grandeur> getGrandeurs() {
                return List.of(oxygene);
            }

            @Override
            public Mesure mesurer(Position p, ContexteSimulation ctx) {
                return new Mesure(getNom(), ctx.getPas(), p, List.of(new ValeurMesuree(oxygene, 7.5)));
            }
        };
        Balise b1 = baliseEquipee(oxymetre);

        simulation.avancerDUnPas();

        assertEquals(7.5, b1.getMemoire().getMesures().get(0).valeurDe(oxygene), 1e-9);
    }

    @Test
    void equiper_publieEquipementModifie() {
        baliseEquipee();
        EcouteurDeBalise ecouteur = new EcouteurDeBalise();
        eventHandler.registerListener(EvenementEquipementModifie.class, ecouteur);
        simulation.planifier(new CommandeEquiper("b1", new InstrumentCourantometre()), 1);

        simulation.avancerDUnPas();

        assertEquals(List.of("équipement b1"), ecouteur.journal);
    }

    @Test
    void mesurer_publieMemoireModifiee() {
        baliseEquipee(new InstrumentCourantometre());
        EcouteurDeBalise ecouteur = new EcouteurDeBalise();
        eventHandler.registerListener(EvenementMemoireModifiee.class, ecouteur);

        simulation.avancerDUnPas();

        assertTrue(ecouteur.journal.contains("mémoire b1"));
    }

    private static final class EcouteurDeBalise implements EcouteurBalises {
        private final List<String> journal = new ArrayList<>();

        @Override
        public void activiteModifiee(EvenementActiviteModifiee e) {
            journal.add("activité " + e.getBalise().getNom());
        }

        @Override
        public void memoireModifiee(EvenementMemoireModifiee e) {
            journal.add("mémoire " + e.getBalise().getNom());
        }

        @Override
        public void equipementModifie(EvenementEquipementModifie e) {
            journal.add("équipement " + e.getBalise().getNom());
        }
    }
}
