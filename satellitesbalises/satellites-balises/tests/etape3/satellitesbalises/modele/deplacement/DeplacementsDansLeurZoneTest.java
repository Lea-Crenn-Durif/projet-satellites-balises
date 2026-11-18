package satellitesbalises.modele.deplacement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static satellitesbalises.modele.deplacement.OutilsDeplacement.GEOMETRIE;
import static satellitesbalises.modele.deplacement.OutilsDeplacement.contexte;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.monde.Zone;


class DeplacementsDansLeurZoneTest {

    static Stream<Deplacement> deplacementsDeBalise() {
        return Stream.of(
                new DeplacementImmobile(),
                new DeplacementHorizontal(3),
                new DeplacementVertical(4, 5, 75),
                new DeplacementSinusoidal(2, 30, 50, 40));
    }

    static Stream<Deplacement> deplacementsDeSatellite() {
        return Stream.of(
                new DeplacementImmobile(),
                new DeplacementOrbital(1),
                new DeplacementOrbital(9));
    }

    @ParameterizedTest
    @MethodSource("deplacementsDeBalise")
    void balise_milleePas_resteDansLaMer(Deplacement deplacement) {
        Position position = new Position(100, -40);

        for (long pas = 1; pas <= 1000; pas++) {
            position = deplacement.prochainePosition(position, contexte(pas));
            assertTrue(GEOMETRIE.contient(position), "sortie du monde au pas " + pas + " : " + position);
            assertEquals(Zone.MER, GEOMETRIE.zoneDe(position), "sortie de la mer au pas " + pas);
        }
    }

    @ParameterizedTest
    @MethodSource("deplacementsDeSatellite")
    void satellite_millePas_resteDansLeCiel(Deplacement deplacement) {
        Position position = new Position(100, 60);

        for (long pas = 1; pas <= 1000; pas++) {
            position = deplacement.prochainePosition(position, contexte(pas));
            assertTrue(GEOMETRIE.contient(position), "sortie du monde au pas " + pas + " : " + position);
            assertEquals(Zone.CIEL, GEOMETRIE.zoneDe(position), "sortie du ciel au pas " + pas);
        }
    }
}
