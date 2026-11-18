package satellitesbalises.vue;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;


class FabriqueVuesTest {

    private final GeometrieMonde geometrie = new GeometrieMonde(800, 300, 300);
    private final FabriqueEntites fabrique = new FabriqueEntites(geometrie);
    private final FabriqueVues fabriqueVues = new FabriqueVues(new ConvertisseurCoordonnees(geometrie));

    @Test
    void creerVue_satellite_renvoieVueSatellite() {
        VueEntite vue = fabriqueVues.creerVue(fabrique.nouveauSatellite("s1").en(new Position(10, 100)).construire());

        assertInstanceOf(VueSatellite.class, vue);
    }

    @Test
    void creerVue_balise_renvoieVueBalise() {
        VueEntite vue = fabriqueVues.creerVue(fabrique.nouvelleBalise("b1").en(new Position(10, -100)).construire());

        assertInstanceOf(VueBalise.class, vue);
    }
}
