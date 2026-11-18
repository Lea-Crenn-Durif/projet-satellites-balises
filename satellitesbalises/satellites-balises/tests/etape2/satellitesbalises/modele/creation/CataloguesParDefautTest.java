package satellitesbalises.modele.creation;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.activite.ConditionMemoirePleine;
import satellitesbalises.modele.deplacement.DeplacementImmobile;


class CataloguesParDefautTest {

    private final Catalogues catalogues = CataloguesParDefaut.creer();

    @Test
    void deplacementImmobile_disponible() {
        assertInstanceOf(DeplacementImmobile.class, catalogues.getDeplacements().creer("immobile", new Parametres(Map.of())));
    }

    @Test
    void geostationnaire_estUnDeplacementImmobile() {
        assertTrue(catalogues.getDeplacements().contient("geostationnaire"));
        assertInstanceOf(DeplacementImmobile.class, catalogues.getDeplacements().creer("geostationnaire", new Parametres(Map.of())));
    }

    @Test
    void conditionMemoirePleine_disponible() {
        assertInstanceOf(ConditionMemoirePleine.class,
                catalogues.getConditionsRemontee().creer("memoirePleine", new Parametres(Map.of())));
    }
}
