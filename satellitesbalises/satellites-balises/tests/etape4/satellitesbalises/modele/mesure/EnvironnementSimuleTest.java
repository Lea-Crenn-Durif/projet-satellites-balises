package satellitesbalises.modele.mesure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.creation.ErreurConfiguration;
import satellitesbalises.modele.monde.Position;


class EnvironnementSimuleTest {

    @Test
    void valeur_memeLieuMemePas_memeResultat() {
        EnvironnementSimule ocean = EnvironnementSimule.parDefaut();
        Position ici = new Position(80, -35);

        assertEquals(ocean.valeur(Grandeur.TEMPERATURE, ici, 120), ocean.valeur(Grandeur.TEMPERATURE, ici, 120));
    }

    @Test
    void valeur_deuxEnvironnementsParDefaut_memeResultat() {
        Position ici = new Position(80, -35);

        assertEquals(EnvironnementSimule.parDefaut().valeur(Grandeur.SALINITE, ici, 7),
                EnvironnementSimule.parDefaut().valeur(Grandeur.SALINITE, ici, 7));
    }

    @Test
    void parDefaut_connaitLesCinqGrandeurs() {
        EnvironnementSimule ocean = EnvironnementSimule.parDefaut();
        Position ici = new Position(80, -35);

        for (Grandeur g : new Grandeur[] {Grandeur.TEMPERATURE, Grandeur.SALINITE, Grandeur.TURBIDITE,
                Grandeur.VITESSE_COURANT, Grandeur.DIRECTION_COURANT}) {
            ocean.valeur(g, ici, 1); // ne doit pas lever d'erreur
        }
    }

    @Test
    void champSansOndulation_suitLaProfondeur() {
        ChampPhysique temperature = new ChampPhysique(20, -0.1, 0);

        assertEquals(20, temperature.valeur(new Position(10, 0), 5), 1e-9);
        assertEquals(15, temperature.valeur(new Position(10, -50), 5), 1e-9);
    }

    @Test
    void champAvecOndulation_resteDansSesBornes() {
        ChampPhysique champ = new ChampPhysique(10, 0, 2);

        for (long pas = 0; pas < 500; pas += 7) {
            double v = champ.valeur(new Position(pas % 200, -10), pas);
            org.junit.jupiter.api.Assertions.assertTrue(v >= 8 && v <= 12, "hors bornes : " + v);
        }
    }

    @Test
    void champAvecOndulation_varieDansLeTemps() {
        ChampPhysique champ = new ChampPhysique(10, 0, 2);
        Position ici = new Position(40, -10);

        assertNotEquals(champ.valeur(ici, 0), champ.valeur(ici, 150));
    }

    @Test
    void valeur_grandeurInconnue_leveErreurConfiguration() {
        EnvironnementSimule ocean = new EnvironnementSimule();

        assertThrows(ErreurConfiguration.class, () -> ocean.valeur(new Grandeur("oxygene", "mg/L"), new Position(0, -1), 1));
    }

    @Test
    void definirChamp_nouvelleGrandeur_utilisable() {
        EnvironnementSimule ocean = new EnvironnementSimule();
        Grandeur oxygene = new Grandeur("oxygene", "mg/L");

        ocean.definirChamp(oxygene, new ChampPhysique(8, 0, 0));

        assertEquals(8, ocean.valeur(oxygene, new Position(0, -1), 1), 1e-9);
    }
}
