package satellitesbalises.modele.deplacement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.creation.CataloguesParDefaut;
import satellitesbalises.modele.creation.Catalogue;
import satellitesbalises.modele.creation.Parametres;
import satellitesbalises.modele.monde.Position;

/** Étape 3 — EX-2.7 complété : tous les déplacements sont au catalogue, avec leurs paramètres. */
class CataloguesDeplacementsTest {

    private final Catalogue<Deplacement> deplacements = CataloguesParDefaut.creer().getDeplacements();

    @Test
    void catalogue_contientLesSixDeplacements() {
        assertEquals(Set.of("immobile", "horizontal", "vertical", "sinusoidal", "orbital", "geostationnaire"),
                deplacements.getNoms());
    }

    @Test
    void orbital_parametreVitesseUtilise() {
        Deplacement orbital = deplacements.creer("orbital", new Parametres(Map.of("vitesse", 4.0)));

        assertInstanceOf(DeplacementOrbital.class, orbital);
        assertEquals(14, orbital.prochainePosition(new Position(10, 50), OutilsDeplacement.contexte(1)).getX(), 1e-9);
    }

    @Test
    void horizontal_parametreVitesseUtilise() {
        Deplacement horizontal = deplacements.creer("horizontal", new Parametres(Map.of("vitesse", 3.0)));

        assertEquals(13, horizontal.prochainePosition(new Position(10, -50), OutilsDeplacement.contexte(1)).getX(), 1e-9);
    }

    @Test
    void chaqueCreation_renvoieUneNouvelleInstance() {
        // les stratégies gardent un état (le sens) : deux entités ne doivent pas partager la même
        Parametres p = new Parametres(Map.of("vitesse", 1.0));

        org.junit.jupiter.api.Assertions.assertNotSame(deplacements.creer("horizontal", p), deplacements.creer("horizontal", p));
    }
}
