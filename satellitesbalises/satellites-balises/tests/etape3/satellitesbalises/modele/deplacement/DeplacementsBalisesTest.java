package satellitesbalises.modele.deplacement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static satellitesbalises.modele.deplacement.OutilsDeplacement.GEOMETRIE;
import static satellitesbalises.modele.deplacement.OutilsDeplacement.contexte;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.monde.Position;

class DeplacementsBalisesTest {

    // ---------- EX-3.1 : horizontal ----------

    @Test
    void horizontal_avanceVersLaDroiteAProfondeurConstante() {
        DeplacementHorizontal horizontal = new DeplacementHorizontal(2);

        Position suivante = horizontal.prochainePosition(new Position(50, -30), contexte(1));

        assertEquals(new Position(52, -30), suivante);
    }

    @Test
    void horizontal_auBordDroit_repartAGauche() {
        DeplacementHorizontal horizontal = new DeplacementHorizontal(2);
        Position position = new Position(199, -30);

        position = horizontal.prochainePosition(position, contexte(1));
        assertTrue(position.getX() <= GEOMETRIE.getLargeur(), "ne sort jamais du monde");
        double apresDemiTour = position.getX();

        position = horizontal.prochainePosition(position, contexte(2));
        assertTrue(position.getX() < apresDemiTour, "repart vers la gauche");
        assertEquals(-30, position.getY(), 1e-9);
    }

    @Test
    void horizontal_auBordGauche_repartADroite() {
        DeplacementHorizontal horizontal = new DeplacementHorizontal(3);
        Position position = new Position(150, -30);
        double xMinimum = Double.MAX_VALUE;
        boolean repartiADroite = false;

        for (long pas = 1; pas <= 200; pas++) {
            Position suivante = horizontal.prochainePosition(position, contexte(pas));
            assertTrue(suivante.getX() >= 0 && suivante.getX() <= GEOMETRIE.getLargeur());
            if (suivante.getX() > position.getX() && xMinimum < 10) {
                repartiADroite = true;
            }
            xMinimum = Math.min(xMinimum, suivante.getX());
            position = suivante;
        }

        assertTrue(repartiADroite, "après le bord gauche, la balise repart vers la droite");
    }

    // ---------- EX-3.2 : vertical ----------

    @Test
    void vertical_oscilleEntreSesDeuxProfondeurs() {
        // profondeurs 10 et 50 : y reste entre -50 et -10
        DeplacementVertical vertical = new DeplacementVertical(5, 10, 50);
        Position position = new Position(80, -48);
        boolean aMonte = false;
        boolean aDescendu = false;

        for (long pas = 1; pas <= 40; pas++) {
            Position suivante = vertical.prochainePosition(position, contexte(pas));
            assertEquals(80, suivante.getX(), 1e-9, "x ne bouge pas");
            assertTrue(suivante.getY() >= -50 && suivante.getY() <= -10, "y hors limites : " + suivante.getY());
            aMonte |= suivante.getY() > position.getY();
            aDescendu |= suivante.getY() < position.getY();
            position = suivante;
        }

        assertTrue(aMonte && aDescendu, "elle doit monter ET descendre");
    }

    @Test
    void vertical_profondeurMax_remonte() {
        DeplacementVertical vertical = new DeplacementVertical(5, 10, 50);
        Position position = new Position(80, -12);

        // on laisse le temps d'atteindre le fond puis de repartir
        double yMinimum = 0;
        for (long pas = 1; pas <= 20; pas++) {
            position = vertical.prochainePosition(position, contexte(pas));
            yMinimum = Math.min(yMinimum, position.getY());
        }

        assertEquals(-50, yMinimum, 1e-9, "elle atteint exactement la profondeur maximale");
        assertTrue(position.getY() > -50, "puis elle remonte");
    }

    // ---------- EX-3.3 : sinusoïdal ----------

    @Test
    void sinusoidal_pasZero_auCentre() {
        DeplacementSinusoidal sinus = new DeplacementSinusoidal(1, 15, 60, 40);

        assertEquals(-40, sinus.prochainePosition(new Position(20, -40), contexte(0)).getY(), 1e-9);
    }

    @Test
    void sinusoidal_quartDePeriode_yAuMaximum() {
        // période 60 : au pas 15, sin(2π·15/60) = 1 → y = −40 + 15 = −25
        DeplacementSinusoidal sinus = new DeplacementSinusoidal(1, 15, 60, 40);

        assertEquals(-25, sinus.prochainePosition(new Position(20, -40), contexte(15)).getY(), 1e-9);
    }

    @Test
    void sinusoidal_troisQuartsDePeriode_yAuMinimum() {
        DeplacementSinusoidal sinus = new DeplacementSinusoidal(1, 15, 60, 40);

        assertEquals(-55, sinus.prochainePosition(new Position(20, -40), contexte(45)).getY(), 1e-9);
    }

    @Test
    void sinusoidal_xAvanceDeSaVitesse() {
        DeplacementSinusoidal sinus = new DeplacementSinusoidal(2, 15, 60, 40);

        assertEquals(22, sinus.prochainePosition(new Position(20, -40), contexte(1)).getX(), 1e-9);
    }
}
