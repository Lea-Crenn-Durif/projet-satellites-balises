package satellitesbalises.langage.analyse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static satellitesbalises.langage.arbre.ProgrammesExemples.appel;
import static satellitesbalises.langage.arbre.ProgrammesExemples.nombre;
import static satellitesbalises.langage.arbre.ProgrammesExemples.texte;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.Test;

import satellitesbalises.langage.DossierExemples;
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

/**
 * Étape 7c — EX-7.5 et la syntaxe concrète : du texte à l'arbre abstrait.
 * Syntaxe de référence : section 9 du dossier de conception et dossier exemples/.
 * Si la grammaire finale diffère, adapter ces tests EN MÊME TEMPS que la grammaire.
 */
class AnalyseurProgrammeTest {

    private final AnalyseurProgramme analyseur = new AnalyseurProgramme();

    private Instruction seule(String texte) {
        List<Instruction> instructions = analyseur.analyser(texte).getInstructions();
        assertEquals(1, instructions.size(), "une seule instruction attendue");
        return instructions.get(0);
    }

    @Test
    void creerSatellite_avecProprietes() {
        assertEquals(new InstructionCreer(1, TypeEntite.SATELLITE, "s1", 0, 150, List.of(nombre("portee", 40), nombre("debit", 3))),
                seule("satellite s1 en (0, 150) { portee = 40 ; debit = 3 }"));
    }

    @Test
    void creerBalise_coordonneeNegativeSansProprietes() {
        assertEquals(new InstructionCreer(1, TypeEntite.BALISE, "b1", 120, -80, List.of()), seule("balise b1 en (120, -80)"));
    }

    @Test
    void deplacement_avecEtSansParametres() {
        assertEquals(new InstructionComportement(1, "s1", Aspect.DEPLACEMENT, appel("orbital", nombre("vitesse", 2))),
                seule("deplacement s1 orbital(vitesse = 2)"));
        assertEquals(new InstructionComportement(1, "s2", Aspect.DEPLACEMENT, appel("geostationnaire")),
                seule("deplacement s2 geostationnaire()"));
    }

    @Test
    void remontee() {
        assertEquals(new InstructionComportement(1, "b2", Aspect.REMONTEE, appel("duree", nombre("pas", 150))),
                seule("remontee b2 duree(pas = 150)"));
    }

    @Test
    void equiperPlusieursInstruments_etDesequiper() {
        assertEquals(new InstructionEquiper(1, "b1", List.of(appel("temperature"), appel("salinite"))),
                seule("equiper b1 temperature(), salinite()"));
        assertEquals(new InstructionDesequiper(1, "b1", "turbidite"), seule("desequiper b1 turbidite"));
    }

    @Test
    void apparence_texteEtNombre() {
        assertEquals(new InstructionApparence(1, "b1", List.of(texte("couleur", "orange"), nombre("taille", 14))),
                seule("apparence b1 { couleur = \"orange\" ; taille = 14 }"));
    }

    @Test
    void ordres_attendreConfigurerSupprimer() {
        assertEquals(new InstructionAgir(1, "s1", Ordre.ARRETER), seule("arreter s1"));
        assertEquals(new InstructionAgir(1, "s1", Ordre.DEMARRER), seule("demarrer s1"));
        assertEquals(new InstructionAgir(1, "b2", Ordre.REMONTER), seule("remonter b2"));
        assertEquals(new InstructionAttendre(1, 200), seule("attendre 200"));
        assertEquals(new InstructionConfigurer(1, "s2", List.of(nombre("portee", 60))), seule("configurer s2 { portee = 60 }"));
        assertEquals(new InstructionSupprimer(1, "b2"), seule("supprimer b2"));
    }

    @Test
    void commentairesEtLignesVides_ignores_numerosDeLigneConserves() {
        Programme programme = analyseur.analyser("-- un commentaire\n\nattendre 5   -- fin de ligne\nsupprimer b1\n");

        assertEquals(List.of(new InstructionAttendre(3, 5), new InstructionSupprimer(4, "b1")), programme.getInstructions());
    }

    @Test
    void programmeVide_aucuneInstruction() {
        assertTrue(analyseur.analyser("").getInstructions().isEmpty());
    }

    @Test
    void analyser_fichierExemple1_memeArbreQueLaVersionJava() throws IOException {
        String texte = Files.readString(DossierExemples.fichier("01-construction.sb"), StandardCharsets.UTF_8);

        assertEquals(ProgrammesExemples.construction(), analyseur.analyser(texte));
    }

    @Test
    void analyser_fichierExemple4_memeArbreQueLaVersionJava() throws IOException {
        String texte = Files.readString(DossierExemples.fichier("04-communication.sb"), StandardCharsets.UTF_8);

        assertEquals(ProgrammesExemples.communication(), analyseur.analyser(texte));
    }

    // ---------- EX-7.5 ----------

    @Test
    void analyser_parentheseManquante_erreurLigne3() {
        String texte = "attendre 1\nattendre 2\nbalise b1 en (120, -80\nattendre 3";

        ErreurSyntaxe erreur = assertThrows(ErreurSyntaxe.class, () -> analyseur.analyser(texte));

        ErreurProgramme premiere = erreur.getErreurs().get(0);
        assertEquals(3, premiere.getLigne());
        assertTrue(premiere.getColonne() >= 0);
        assertTrue(!premiere.getMessage().isBlank());
    }

    @Test
    void analyser_motCleInconnu_erreur() {
        assertThrows(ErreurSyntaxe.class, () -> analyseur.analyser("teleporter b1"));
    }
}
