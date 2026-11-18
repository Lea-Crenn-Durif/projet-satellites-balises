package satellitesbalises.vue;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;


class ConvertisseurCoordonneesTest {

    private final ConvertisseurCoordonnees convertisseur =
            new ConvertisseurCoordonnees(new GeometrieMonde(800, 300, 300));

    @Test
    void versEcran_surface_renvoieHauteurCiel() {
        assertEquals(new Point(0, 300), convertisseur.versEcran(new Position(0, 0)));
    }

    @Test
    void versEcran_sommetDuCiel_renvoieZero() {
        assertEquals(new Point(120, 0), convertisseur.versEcran(new Position(120, 300)));
    }

    @Test
    void versEcran_fondDeLaMer_renvoieHauteurTotale() {
        assertEquals(new Point(800, 600), convertisseur.versEcran(new Position(800, -300)));
    }

    @Test
    void versEcran_xConserve() {
        assertEquals(457, convertisseur.versEcran(new Position(457, -12)).x);
    }
}
