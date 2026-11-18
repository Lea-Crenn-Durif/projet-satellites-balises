package satellitesbalises.langage.interpretation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static satellitesbalises.langage.arbre.ProgrammesExemples.appel;
import static satellitesbalises.langage.arbre.ProgrammesExemples.nombre;
import static satellitesbalises.langage.arbre.ProgrammesExemples.texte;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eventHandler.EventHandler;
import satellitesbalises.langage.arbre.Aspect;
import satellitesbalises.langage.arbre.ErreurProgramme;
import satellitesbalises.langage.arbre.Instruction;
import satellitesbalises.langage.arbre.InstructionAgir;
import satellitesbalises.langage.arbre.InstructionApparence;
import satellitesbalises.langage.arbre.InstructionAttendre;
import satellitesbalises.langage.arbre.InstructionComportement;
import satellitesbalises.langage.arbre.InstructionConfigurer;
import satellitesbalises.langage.arbre.InstructionCreer;
import satellitesbalises.langage.arbre.InstructionDesequiper;
import satellitesbalises.langage.arbre.InstructionEquiper;
import satellitesbalises.langage.arbre.InstructionSupprimer;
import satellitesbalises.langage.arbre.Ordre;
import satellitesbalises.langage.arbre.Programme;
import satellitesbalises.langage.arbre.ProgrammesExemples;
import satellitesbalises.langage.arbre.TypeEntite;
import satellitesbalises.modele.activite.ActiviteRemontee;
import satellitesbalises.modele.activite.ConditionDuree;
import satellitesbalises.modele.communication.StatutSatellite;
import satellitesbalises.modele.creation.Catalogues;
import satellitesbalises.modele.creation.CataloguesParDefaut;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.deplacement.DeplacementImmobile;
import satellitesbalises.modele.deplacement.DeplacementOrbital;
import satellitesbalises.modele.entite.Apparence;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.mesure.EnvironnementSimule;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.Simulation;


class InterpreteurProgrammeTest {

    private final GeometrieMonde geometrie = new GeometrieMonde(800, 300, 300);
    private final Catalogues catalogues = CataloguesParDefaut.creer();
    private Simulation simulation;
    private InterpreteurProgramme interpreteur;

    @BeforeEach
    void creer() {
        simulation = new Simulation(new Monde(geometrie), EnvironnementSimule.parDefaut(), new EventHandler());
        interpreteur = new InterpreteurProgramme(new FabriqueEntites(geometrie), catalogues);
    }

    private RapportExecution executer(Instruction... instructions) {
        return interpreteur.interpreter(new Programme(List.of(instructions)), simulation);
    }

    private void avancer(int nombreDePas) {
        for (int i = 0; i < nombreDePas; i++) {
            simulation.avancerDUnPas();
        }
    }

    private Balise balise(String nom) {
        return simulation.getMonde().trouverBalise(nom).orElseThrow();
    }

    private Satellite satellite(String nom) {
        return simulation.getMonde().trouverSatellite(nom).orElseThrow();
    }

    private static InstructionCreer creerBalise(int ligne, String nom) {
        return new InstructionCreer(ligne, TypeEntite.BALISE, nom, 100, -50, List.of());
    }

    private static InstructionCreer creerSatellite(int ligne, String nom) {
        return new InstructionCreer(ligne, TypeEntite.SATELLITE, nom, 100, 200, List.of());
    }

    private static void assertErreurLigne(RapportExecution rapport, int ligne, String motAttendu) {
        assertFalse(rapport.estReussi());
        ErreurProgramme erreur = rapport.getErreurs().get(0);
        assertEquals(ligne, erreur.getLigne());
        assertTrue(erreur.getMessage().contains(motAttendu), "le message doit citer « " + motAttendu + " » : " + erreur.getMessage());
    }

    // ---------- EX-7.2 : les programmes d'exemple ----------

    @Test
    void interpreter_programmeExemple1_creeQuatreEntites() {
        RapportExecution rapport = interpreteur.interpreter(ProgrammesExemples.construction(), simulation);
        avancer(1);

        assertTrue(rapport.estReussi(), rapport.getErreurs().toString());
        assertEquals(2, simulation.getMonde().getSatellites().size());
        assertEquals(2, simulation.getMonde().getBalises().size());
        assertInstanceOf(DeplacementOrbital.class, satellite("s1").getDeplacement());
        assertEquals(2, balise("b1").getInstruments().size());
    }

    @Test
    void interpreter_lesQuatreExemples_sansErreur() {
        for (Programme programme : List.of(ProgrammesExemples.construction(), ProgrammesExemples.evolution(),
                ProgrammesExemples.equipement(), ProgrammesExemples.communication())) {
            creer(); // simulation neuve pour chaque programme
            RapportExecution rapport = interpreteur.interpreter(programme, simulation);
            assertTrue(rapport.estReussi(), rapport.getErreurs().toString());
        }
    }

    // ---------- EX-7.3 : une instruction à la fois ----------

    @Test
    void creer_avecProprietes_appliqueesAuMonteur() {
        executer(new InstructionCreer(1, TypeEntite.SATELLITE, "s1", 0, 200, List.of(nombre("portee", 40), nombre("debit", 3))),
                new InstructionCreer(2, TypeEntite.BALISE, "b1", 120, -80, List.of(nombre("memoire", 30), nombre("vitesseVerticale", 2))));
        avancer(1);

        assertEquals(40, satellite("s1").getPortee(), 1e-9);
        assertEquals(3, satellite("s1").getDebit());
        assertEquals(new Position(120, -80), balise("b1").getPosition());
        assertEquals(30, balise("b1").getMemoire().getCapacite());
        assertEquals(2, balise("b1").getVitesseVerticale(), 1e-9);
    }

    @Test
    void creer_proprietesFacultatives_valeursParDefaut() {
        executer(creerBalise(1, "b1"));
        avancer(1);

        assertEquals(50, balise("b1").getMemoire().getCapacite());
        assertInstanceOf(DeplacementImmobile.class, balise("b1").getDeplacement());
    }

    @Test
    void supprimer_entiteCreeePlusTot() {
        executer(creerBalise(1, "b1"));
        avancer(1);

        executer(new InstructionSupprimer(1, "b1"));
        avancer(1);

        assertTrue(simulation.getMonde().trouver("b1").isEmpty());
    }

    @Test
    void configurer_modifieLaCaracteristique() {
        executer(creerSatellite(1, "s1"), new InstructionConfigurer(2, "s1", List.of(nombre("portee", 75))));
        avancer(1);

        assertEquals(75, satellite("s1").getPortee(), 1e-9);
    }

    @Test
    void comportementDeplacement_resoluParLeCatalogue() {
        executer(creerSatellite(1, "s1"), new InstructionComportement(2, "s1", Aspect.DEPLACEMENT, appel("orbital", nombre("vitesse", 5))));
        avancer(1);

        assertInstanceOf(DeplacementOrbital.class, satellite("s1").getDeplacement());
    }

    @Test
    void comportementRemontee_resoluParLeCatalogue() {
        executer(creerBalise(1, "b1"), new InstructionComportement(2, "b1", Aspect.REMONTEE, appel("duree", nombre("pas", 150))));
        avancer(1);

        assertInstanceOf(ConditionDuree.class, balise("b1").getConditionRemontee());
    }

    @Test
    void equiperPuisDesequiper() {
        executer(creerBalise(1, "b1"), new InstructionEquiper(2, "b1", List.of(appel("temperature"), appel("salinite"))),
                new InstructionDesequiper(3, "b1", "temperature"));
        avancer(1);

        assertEquals(1, balise("b1").getInstruments().size());
        assertEquals("salinite", balise("b1").getInstruments().get(0).getNom());
    }

    @Test
    void agir_arreterEtDemarrer() {
        executer(creerSatellite(1, "s1"), new InstructionAgir(2, "s1", Ordre.ARRETER));
        avancer(1);
        assertEquals(StatutSatellite.HORS_SERVICE, satellite("s1").getStatut());

        executer(new InstructionAgir(1, "s1", Ordre.DEMARRER));
        avancer(1);
        assertEquals(StatutSatellite.DISPONIBLE, satellite("s1").getStatut());
    }

    @Test
    void agir_remonter() {
        executer(creerBalise(1, "b1"), new InstructionAgir(2, "b1", Ordre.REMONTER));
        avancer(1);

        assertInstanceOf(ActiviteRemontee.class, balise("b1").getActivite());
    }

    @Test
    void apparence_couleurEtTaille() {
        executer(creerBalise(1, "b1"), new InstructionApparence(2, "b1", List.of(texte("couleur", "orange"), nombre("taille", 14))));
        avancer(1);

        assertEquals(new Apparence("orange", 14), balise("b1").getApparence());
    }

    // ---------- EX-7.4 ----------

    @Test
    void attendre100_commandeSuivantePlanifieeAuPas101() {
        executer(creerBalise(1, "b1"), new InstructionAttendre(2, 100), creerBalise(3, "b2"));

        avancer(1);
        assertTrue(simulation.getMonde().trouver("b1").isPresent());
        avancer(99); // pas 100
        assertTrue(simulation.getMonde().trouver("b2").isEmpty());
        avancer(1); // pas 101
        assertTrue(simulation.getMonde().trouver("b2").isPresent());
    }

    @Test
    void rapport_compteLesCommandes() {
        RapportExecution rapport = executer(creerBalise(1, "b1"), new InstructionAttendre(2, 10),
                new InstructionAgir(3, "b1", Ordre.REMONTER));

        assertEquals(2, rapport.getNombreDeCommandes(), "« attendre » ne produit pas de commande");
    }

    // ---------- EX-7.6 : erreurs de sens ----------

    @Test
    void equiper_entiteInconnue_erreur() {
        assertErreurLigne(executer(new InstructionEquiper(4, "b9", List.of(appel("temperature")))), 4, "b9");
    }

    @Test
    void deplacement_comportementInconnu_erreur() {
        assertErreurLigne(executer(creerBalise(1, "b1"),
                new InstructionComportement(2, "b1", Aspect.DEPLACEMENT, appel("teleportation"))), 2, "teleportation");
    }

    @Test
    void creer_baliseDansLeCiel_erreur() {
        assertErreurLigne(executer(new InstructionCreer(3, TypeEntite.BALISE, "b1", 100, 50, List.of())), 3, "b1");
    }

    @Test
    void equiper_satellite_erreurTypeEntite() {
        assertErreurLigne(executer(creerSatellite(1, "s1"), new InstructionEquiper(2, "s1", List.of(appel("temperature")))), 2, "s1");
    }

    @Test
    void arreter_balise_erreurTypeEntite() {
        assertErreurLigne(executer(creerBalise(1, "b1"), new InstructionAgir(2, "b1", Ordre.ARRETER)), 2, "b1");
    }

    @Test
    void creer_nomDejaPris_erreur() {
        assertErreurLigne(executer(creerBalise(1, "b1"), creerBalise(2, "b1")), 2, "b1");
    }

    // ---------- EX-7.7 ----------

    @Test
    void interpreter_uneErreur_aucuneCommandePlanifiee() {
        RapportExecution rapport = executer(creerBalise(1, "b1"), creerSatellite(2, "s1"),
                new InstructionEquiper(3, "inconnue", List.of(appel("temperature"))));
        avancer(5);

        assertFalse(rapport.estReussi());
        assertEquals(0, rapport.getNombreDeCommandes());
        assertTrue(simulation.getMonde().getBalises().isEmpty());
        assertTrue(simulation.getMonde().getSatellites().isEmpty());
    }

    @Test
    void interpreter_plusieursErreurs_toutesSignalees() {
        RapportExecution rapport = executer(new InstructionSupprimer(1, "x"), new InstructionSupprimer(2, "y"));

        assertEquals(2, rapport.getErreurs().size());
    }

    // ---------- EX-7.8 ----------

    @Test
    void creerPuisEquiperMemeProgramme_valide() {
        assertTrue(executer(creerBalise(1, "b1"), new InstructionEquiper(2, "b1", List.of(appel("temperature")))).estReussi());
    }

    @Test
    void supprimerPuisUtiliserMemeProgramme_erreur() {
        assertErreurLigne(executer(creerBalise(1, "b1"), new InstructionSupprimer(2, "b1"),
                new InstructionAgir(3, "b1", Ordre.REMONTER)), 3, "b1");
    }

    @Test
    void entiteDejaDansLaSimulation_connueDuProgramme() {
        executer(creerBalise(1, "b1"));
        avancer(1);

        assertTrue(executer(new InstructionAgir(1, "b1", Ordre.REMONTER)).estReussi());
    }

    // ---------- EX-7.9 ----------

    @Test
    void nouvelleVarianteAuCatalogue_utilisableSansToucherAuLangage() {
        catalogues.getDeplacements().enregistrer("surPlace", p -> new DeplacementImmobile());

        assertTrue(executer(creerBalise(1, "b1"),
                new InstructionComportement(2, "b1", Aspect.DEPLACEMENT, appel("surPlace"))).estReussi());
    }

    // ---------- EX-7.11 ----------

    @Test
    void creer_proprieteInconnue_erreurAvecNom() {
        assertErreurLigne(executer(new InstructionCreer(5, TypeEntite.BALISE, "b1", 100, -50, List.of(nombre("couleurDuCiel", 3)))),
                5, "couleurDuCiel");
    }
}
