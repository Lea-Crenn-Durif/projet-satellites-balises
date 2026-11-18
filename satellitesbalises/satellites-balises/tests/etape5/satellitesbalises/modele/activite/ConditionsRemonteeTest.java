package satellitesbalises.modele.activite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.creation.Catalogue;
import satellitesbalises.modele.creation.CataloguesParDefaut;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.creation.Parametres;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.mesure.Grandeur;
import satellitesbalises.modele.mesure.Mesure;
import satellitesbalises.modele.mesure.ValeurMesuree;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;

class ConditionsRemonteeTest {

    private final FabriqueEntites fabrique = new FabriqueEntites(new GeometrieMonde(200, 100, 80));

    private Balise baliseDeCapacite(int capacite) {
        return fabrique.nouvelleBalise("b1").en(new Position(50, -30)).avecMemoire(capacite).construire();
    }

    private static Mesure mesure() {
        return new Mesure("temperature", 1, new Position(50, -30), List.of(new ValeurMesuree(Grandeur.TEMPERATURE, 12)));
    }

    @Test
    void memoirePleine_memoireNonPleine_nonRemplie() {
        Balise b1 = baliseDeCapacite(2);
        b1.getMemoire().stocker(mesure());

        assertFalse(new ConditionMemoirePleine().estRemplie(b1, 1000));
    }

    @Test
    void memoirePleine_memoirePleine_remplie() {
        Balise b1 = baliseDeCapacite(2);
        b1.getMemoire().stocker(mesure());
        b1.getMemoire().stocker(mesure());

        assertTrue(new ConditionMemoirePleine().estRemplie(b1, 1));
    }

    @Test
    void conditionDuree_dureeAtteinte_remplie() {
        ConditionDuree centCinquante = new ConditionDuree(150);
        Balise b1 = baliseDeCapacite(10);

        assertFalse(centCinquante.estRemplie(b1, 149));
        assertTrue(centCinquante.estRemplie(b1, 150));
    }

    @Test
    void conditionOu_uneVraie_remplie() {
        Balise b1 = baliseDeCapacite(10);
        ConditionRemontee toujours = (b, d) -> true;
        ConditionRemontee jamais = (b, d) -> false;

        assertTrue(new ConditionOu(jamais, toujours).estRemplie(b1, 0));
        assertTrue(new ConditionOu(toujours, jamais).estRemplie(b1, 0));
        assertFalse(new ConditionOu(jamais, jamais).estRemplie(b1, 0));
    }

    @Test
    void conditionOu_memoirePleineOuDuree() {
        Balise b1 = baliseDeCapacite(10);
        ConditionOu condition = new ConditionOu(new ConditionMemoirePleine(), new ConditionDuree(5));

        assertFalse(condition.estRemplie(b1, 4));
        assertTrue(condition.estRemplie(b1, 5));
    }

    @Test
    void catalogue_conditions() {
        Catalogue<ConditionRemontee> conditions = CataloguesParDefaut.creer().getConditionsRemontee();

        assertEquals(Set.of("memoirePleine", "duree"), conditions.getNoms());
        ConditionRemontee duree = conditions.creer("duree", new Parametres(Map.of("pas", 20.0)));
        assertFalse(duree.estRemplie(baliseDeCapacite(10), 19));
        assertTrue(duree.estRemplie(baliseDeCapacite(10), 20));
    }
}
