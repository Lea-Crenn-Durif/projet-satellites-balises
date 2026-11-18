package satellitesbalises.modele.monde;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Étape 1 — Position, valeur immuable du repère du modèle. */
class PositionTest {

    @Test
    void translater_renvoieUneNouvellePositionDecalee() {
        Position depart = new Position(10, -5);

        Position arrivee = depart.translater(3, -2);

        assertEquals(new Position(13, -7), arrivee);
        assertEquals(new Position(10, -5), depart, "la position de départ ne doit pas changer");
    }

    @Test
    void ecartHorizontal_ignoreLaHauteurEtLeSens() {
        Position satellite = new Position(40, 120);
        Position balise = new Position(55, -30);

        assertEquals(15, satellite.ecartHorizontal(balise), 1e-9);
        assertEquals(15, balise.ecartHorizontal(satellite), 1e-9);
    }
}
