package satellitesbalises.modele.creation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;


class CatalogueTest {

    private static final Parametres AUCUN_PARAMETRE = new Parametres(Map.of());

    @Test
    void creer_nomEnregistre_appelleLaBonneFabrique() {
        Catalogue<String> catalogue = new Catalogue<>();
        catalogue.enregistrer("salut", p -> "bonjour");
        catalogue.enregistrer("adieu", p -> "au revoir");

        assertEquals("au revoir", catalogue.creer("adieu", AUCUN_PARAMETRE));
    }

    @Test
    void creer_nomInconnu_leveErreurAvecNom() {
        Catalogue<String> catalogue = new Catalogue<>();

        ErreurConfiguration erreur = assertThrows(ErreurConfiguration.class,
                () -> catalogue.creer("teleportation", AUCUN_PARAMETRE));

        assertTrue(erreur.getMessage().contains("teleportation"), erreur.getMessage());
    }

    @Test
    void creer_parametreTransmisALaFabrique() {
        Catalogue<String> catalogue = new Catalogue<>();
        catalogue.enregistrer("vitesse", p -> "v=" + p.nombre("vitesse", 1));

        assertEquals("v=3.5", catalogue.creer("vitesse", new Parametres(Map.of("vitesse", 3.5))));
    }

    @Test
    void creer_parametreAbsent_utiliseDefaut() {
        Catalogue<String> catalogue = new Catalogue<>();
        catalogue.enregistrer("vitesse", p -> "v=" + p.nombre("vitesse", 7));

        assertEquals("v=7.0", catalogue.creer("vitesse", AUCUN_PARAMETRE));
    }

    @Test
    void enregistrer_nouvelleVariante_creableSansRienModifierDAutre() {
        Catalogue<String> catalogue = new Catalogue<>();
        assertFalse(catalogue.contient("nouvelle"));

        catalogue.enregistrer("nouvelle", p -> "ok");

        assertTrue(catalogue.contient("nouvelle"));
        assertEquals("ok", catalogue.creer("nouvelle", AUCUN_PARAMETRE));
    }

    @Test
    void getNoms_renvoieTousLesNomsEnregistres() {
        Catalogue<String> catalogue = new Catalogue<>();
        catalogue.enregistrer("a", p -> "a");
        catalogue.enregistrer("b", p -> "b");

        assertEquals(Set.of("a", "b"), catalogue.getNoms());
    }

    @Test
    void parametres_entierEtNombre() {
        Parametres parametres = new Parametres(Map.of("debit", 3.0));

        assertEquals(3, parametres.entier("debit", 1));
        assertEquals(1, parametres.entier("absent", 1));
        assertEquals(2.5, parametres.nombre("absent", 2.5), 1e-9);
    }
}
