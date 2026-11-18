package satellitesbalises.modele.monde;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.creation.ErreurConfiguration;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Satellite;

/** Étape 2 — EX-2.4, EX-2.5, EX-2.6 : le monde range et retrouve les entités par leur nom. */
class MondeTest {

    private final GeometrieMonde geometrie = new GeometrieMonde(200, 100, 80);
    private final FabriqueEntites fabrique = new FabriqueEntites(geometrie);
    private final Monde monde = new Monde(geometrie);

    private Satellite satellite(String nom, double x) {
        return fabrique.nouveauSatellite(nom).en(new Position(x, 50)).construire();
    }

    private Balise balise(String nom, double x) {
        return fabrique.nouvelleBalise(nom).en(new Position(x, -40)).construire();
    }

    @Test
    void ajouter_puisTrouver_renvoieLaMemeEntite() {
        Balise b1 = balise("b1", 10);
        monde.ajouter(b1);

        assertSame(b1, monde.trouver("b1").orElseThrow());
        assertSame(b1, monde.trouverBalise("b1").orElseThrow());
    }

    @Test
    void trouverSatellite_nomDeBalise_renvoieVide() {
        monde.ajouter(balise("b1", 10));

        assertTrue(monde.trouverSatellite("b1").isEmpty());
    }

    @Test
    void trouver_nomInconnu_renvoieVide() {
        assertTrue(monde.trouver("fantome").isEmpty());
    }

    @Test
    void ajouter_nomDejaUtilise_leveErreur() {
        monde.ajouter(balise("x", 10));

        assertThrows(ErreurConfiguration.class, () -> monde.ajouter(balise("x", 20)));
        assertThrows(ErreurConfiguration.class, () -> monde.ajouter(satellite("x", 20)),
                "un satellite et une balise ne peuvent pas porter le même nom");
    }

    @Test
    void retirer_baliseExistante_introuvableEnsuite() {
        Balise b1 = balise("b1", 10);
        monde.ajouter(b1);

        assertSame(b1, monde.retirer("b1").orElseThrow());
        assertTrue(monde.trouver("b1").isEmpty());
        assertTrue(monde.getBalises().isEmpty());
    }

    @Test
    void retirer_nomInconnu_renvoieVide() {
        assertTrue(monde.retirer("fantome").isEmpty());
    }

    @Test
    void monde_troisSatellitesCinqBalises_tousRetrouves() {
        for (int i = 1; i <= 3; i++) {
            monde.ajouter(satellite("s" + i, i * 20));
        }
        for (int i = 1; i <= 5; i++) {
            monde.ajouter(balise("b" + i, i * 20));
        }

        assertEquals(3, monde.getSatellites().size());
        assertEquals(5, monde.getBalises().size());
        for (int i = 1; i <= 5; i++) {
            assertTrue(monde.trouverBalise("b" + i).isPresent());
        }
    }

    @Test
    void getBalises_ordreDAjoutConserve() {
        monde.ajouter(balise("b2", 10));
        monde.ajouter(balise("b1", 20));

        assertEquals("b2", monde.getBalises().iterator().next().getNom());
    }
}
