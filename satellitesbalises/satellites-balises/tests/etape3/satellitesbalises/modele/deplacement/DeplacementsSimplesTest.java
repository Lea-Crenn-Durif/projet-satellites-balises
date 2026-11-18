package satellitesbalises.modele.deplacement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static satellitesbalises.modele.deplacement.OutilsDeplacement.contexte;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.monde.Position;

/** Étape 3 — EX-3.4, EX-3.5, EX-3.6, EX-3.7 : immobile, géostationnaire et orbital. */
class DeplacementsSimplesTest {

    // ---------- EX-3.4 et EX-3.7 ----------

    @Test
    void immobile_renvoieMemePosition() {
        DeplacementImmobile immobile = new DeplacementImmobile();
        Position position = new Position(60, -25);

        assertEquals(position, immobile.prochainePosition(position, contexte(1)));
    }

    @Test
    void immobile_centPas_memePosition() {
        DeplacementImmobile geostationnaire = new DeplacementImmobile();
        Position position = new Position(60, 80);

        for (long pas = 1; pas <= 100; pas++) {
            position = geostationnaire.prochainePosition(position, contexte(pas));
        }

        assertEquals(new Position(60, 80), position);
    }

    // ---------- EX-3.5 et EX-3.6 ----------

    @Test
    void orbital_avanceDeSaVitesseAAltitudeConstante() {
        DeplacementOrbital orbital = new DeplacementOrbital(3);

        Position suivante = orbital.prochainePosition(new Position(10, 70), contexte(1));

        assertEquals(new Position(13, 70), suivante);
    }

    @Test
    void orbital_depasseLargeur_revientAGauche() {
        DeplacementOrbital orbital = new DeplacementOrbital(5);

        Position suivante = orbital.prochainePosition(new Position(198, 70), contexte(1));

        assertEquals(3, suivante.getX(), 1e-9);
        assertEquals(70, suivante.getY(), 1e-9);
    }

    @Test
    void orbital_xToujoursCroissantSaufAuRetour() {
        DeplacementOrbital orbital = new DeplacementOrbital(7);
        Position position = new Position(0, 70);
        int retours = 0;

        for (long pas = 1; pas <= 100; pas++) {
            Position suivante = orbital.prochainePosition(position, contexte(pas));
            if (suivante.getX() < position.getX()) {
                retours++;
            }
            position = suivante;
        }

        assertEquals(3, retours, "700 unités parcourues dans un monde de 200 : 3 tours complets");
    }

    @Test
    void orbital_vitesseNulle_leveErreur() {
        assertThrows(IllegalArgumentException.class, () -> new DeplacementOrbital(0));
    }

    @Test
    void orbital_vitesseNegative_leveErreur() {
        assertThrows(IllegalArgumentException.class, () -> new DeplacementOrbital(-2));
    }

    @Test
    void orbital_deuxSatellites_vitessesPropres() {
        DeplacementOrbital lent = new DeplacementOrbital(1);
        DeplacementOrbital rapide = new DeplacementOrbital(4);
        Position depart = new Position(0, 70);

        assertTrue(rapide.prochainePosition(depart, contexte(1)).getX() > lent.prochainePosition(depart, contexte(1)).getX());
    }
}
