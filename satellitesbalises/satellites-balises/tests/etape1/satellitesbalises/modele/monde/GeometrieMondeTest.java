package satellitesbalises.modele.monde;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Étape 1 — EX-1.1, EX-1.2, EX-1.3 : le monde coupé en ciel et mer. */
class GeometrieMondeTest {

    /** Monde de 200 de large, 100 de ciel, 80 de mer. */
    private final GeometrieMonde geometrie = new GeometrieMonde(200, 100, 80);

    // ---------- EX-1.1 : dimensions valides ----------

    @Test
    void constructeur_dimensionNegative_leveErreur() {
        assertThrows(IllegalArgumentException.class, () -> new GeometrieMonde(-1, 100, 80));
    }

    @Test
    void constructeur_dimensionNulle_leveErreur() {
        assertThrows(IllegalArgumentException.class, () -> new GeometrieMonde(200, 0, 80));
    }

    @Test
    void niveauSurface_vautZero() {
        assertEquals(0, GeometrieMonde.NIVEAU_SURFACE);
    }

    // ---------- EX-1.2 : zones ----------

    @Test
    void zoneDe_yPositif_renvoieCiel() {
        assertEquals(Zone.CIEL, geometrie.zoneDe(new Position(50, 10)));
    }

    @Test
    void zoneDe_yNegatif_renvoieMer() {
        assertEquals(Zone.MER, geometrie.zoneDe(new Position(50, -10)));
    }

    @Test
    void zoneDe_surface_renvoieMer() {
        // Règle retenue : la surface (y = 0) appartient à la mer (une balise en attente y flotte).
        assertEquals(Zone.MER, geometrie.zoneDe(new Position(50, 0)));
    }

    // ---------- EX-1.3 : limites du monde ----------

    @Test
    void contient_pointInterieur_renvoieVrai() {
        assertTrue(geometrie.contient(new Position(100, -40)));
    }

    @Test
    void contient_coinsDuMonde_renvoieVrai() {
        assertTrue(geometrie.contient(new Position(0, 100)));
        assertTrue(geometrie.contient(new Position(200, -80)));
    }

    @Test
    void contient_xSuperieurLargeur_renvoieFaux() {
        assertFalse(geometrie.contient(new Position(201, 10)));
    }

    @Test
    void contient_xNegatif_renvoieFaux() {
        assertFalse(geometrie.contient(new Position(-1, 10)));
    }

    @Test
    void contient_audessusDuCiel_renvoieFaux() {
        assertFalse(geometrie.contient(new Position(50, 101)));
    }

    @Test
    void contient_sousLeFond_renvoieFaux() {
        assertFalse(geometrie.contient(new Position(50, -81)));
    }
}
