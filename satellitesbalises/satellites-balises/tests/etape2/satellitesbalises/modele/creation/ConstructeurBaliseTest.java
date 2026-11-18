package satellitesbalises.modele.creation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.activite.ActiviteCollecte;
import satellitesbalises.modele.activite.ConditionMemoirePleine;
import satellitesbalises.modele.deplacement.DeplacementImmobile;
import satellitesbalises.modele.entite.Apparence;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;


class ConstructeurBaliseTest {

    private final FabriqueEntites fabrique = new FabriqueEntites(new GeometrieMonde(200, 100, 80));

    @Test
    void construire_baliseComplete_activiteCollecte() {
        ConditionMemoirePleine condition = new ConditionMemoirePleine();

        Balise b1 = fabrique.nouvelleBalise("b1")
                .en(new Position(120, -40))
                .avecMemoire(30)
                .remontantQuand(condition)
                .avecVitesseVerticale(2)
                .avecApparence(new Apparence("orange", 12))
                .construire();

        assertEquals("b1", b1.getNom());
        assertEquals(new Position(120, -40), b1.getPosition());
        assertEquals(30, b1.getMemoire().getCapacite());
        assertTrue(b1.getMemoire().estVide());
        assertEquals(condition, b1.getConditionRemontee());
        assertEquals(2, b1.getVitesseVerticale(), 1e-9);
        assertEquals(new Apparence("orange", 12), b1.getApparence());
        assertInstanceOf(ActiviteCollecte.class, b1.getActivite());
    }

    @Test
    void construire_seulementPosition_valeursParDefaut() {
        Balise b1 = fabrique.nouvelleBalise("b1").en(new Position(120, -40)).construire();

        assertInstanceOf(DeplacementImmobile.class, b1.getDeplacement());
        assertEquals(50, b1.getMemoire().getCapacite());
        assertInstanceOf(ConditionMemoirePleine.class, b1.getConditionRemontee());
        assertEquals(1, b1.getVitesseVerticale(), 1e-9);
        assertTrue(b1.getInstruments().isEmpty());
        assertEquals(Apparence.parDefaut(), b1.getApparence());
    }

    @Test
    void construire_ordreInverse_memeBalise() {
        Balise dansUnSens = fabrique.nouvelleBalise("b1")
                .en(new Position(120, -40)).avecMemoire(30).avecVitesseVerticale(2).construire();
        Balise dansLAutre = fabrique.nouvelleBalise("b1")
                .avecVitesseVerticale(2).avecMemoire(30).en(new Position(120, -40)).construire();

        assertEquals(dansUnSens.getPosition(), dansLAutre.getPosition());
        assertEquals(dansUnSens.getMemoire().getCapacite(), dansLAutre.getMemoire().getCapacite());
        assertEquals(dansUnSens.getVitesseVerticale(), dansLAutre.getVitesseVerticale(), 1e-9);
    }

    @Test
    void construire_baliseDansLeCiel_leveErreurConfiguration() {
        ConstructeurBalise constructeur = fabrique.nouvelleBalise("b1").en(new Position(120, 40));

        assertThrows(ErreurConfiguration.class, constructeur::construire);
    }

    @Test
    void construire_baliseALaSurface_acceptee() {
        Balise b1 = fabrique.nouvelleBalise("b1").en(new Position(120, 0)).construire();

        assertEquals(0, b1.getPosition().getY(), 1e-9);
    }

    @Test
    void construire_capaciteNulle_leveErreurConfiguration() {
        ConstructeurBalise constructeur = fabrique.nouvelleBalise("b1").en(new Position(120, -40)).avecMemoire(0);

        assertThrows(ErreurConfiguration.class, constructeur::construire);
    }

    @Test
    void construire_sansPosition_leveErreurNommantPosition() {
        ConstructeurBalise constructeur = fabrique.nouvelleBalise("b1");

        ErreurConfiguration erreur = assertThrows(ErreurConfiguration.class, constructeur::construire);

        assertTrue(erreur.getMessage().contains("b1"), erreur.getMessage());
        assertTrue(erreur.getMessage().contains("position"), erreur.getMessage());
    }
}
