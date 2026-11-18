package satellitesbalises.modele.entite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.communication.StatutSatellite;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.mesure.Grandeur;
import satellitesbalises.modele.mesure.Mesure;
import satellitesbalises.modele.mesure.ValeurMesuree;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;

/** Étape 6 — EX-6.1, EX-6.9, EX-6.11 : statut, portée et archive d'un satellite. */
class SatelliteTest {

    private final FabriqueEntites fabrique = new FabriqueEntites(new GeometrieMonde(200, 100, 80));

    private Satellite satelliteEn(double x, double portee) {
        return fabrique.nouveauSatellite("s1").en(new Position(x, 50)).avecPortee(portee).construire();
    }

    // ---------- EX-6.1 : statuts ----------

    @Test
    void reserver_puisLiberer_redevientDisponible() {
        Satellite s1 = satelliteEn(50, 20);

        s1.reserver();
        assertEquals(StatutSatellite.OCCUPE, s1.getStatut());
        assertFalse(s1.estDisponible());

        s1.liberer();
        assertEquals(StatutSatellite.DISPONIBLE, s1.getStatut());
    }

    @Test
    void arreter_puisDemarrer_redevientDisponible() {
        Satellite s1 = satelliteEn(50, 20);

        s1.arreter();
        assertEquals(StatutSatellite.HORS_SERVICE, s1.getStatut());

        s1.demarrer();
        assertEquals(StatutSatellite.DISPONIBLE, s1.getStatut());
    }

    // ---------- EX-6.9 ----------

    @Test
    void liberer_satelliteHorsService_resteHorsService() {
        Satellite s1 = satelliteEn(50, 20);
        s1.reserver();
        s1.arreter();

        s1.liberer();

        assertEquals(StatutSatellite.HORS_SERVICE, s1.getStatut(), "la fin d'un transfert n'annule pas un arrêt");
    }

    // ---------- EX-6.2 / EX-6.11 : portée ----------

    @Test
    void couvre_baliseDansLaPortee_renvoieVrai() {
        Satellite s1 = satelliteEn(50, 20);

        assertTrue(s1.couvre(new Position(70, 0)), "à exactement 20 : couverte");
        assertTrue(s1.couvre(new Position(35, 0)));
    }

    @Test
    void couvre_baliseHorsPortee_renvoieFaux() {
        assertFalse(satelliteEn(50, 20).couvre(new Position(71, 0)));
    }

    @Test
    void couvre_satelliteBordDroitBaliseBordGauche_renvoieFaux() {
        // pas de « tour du monde » : l'écart se mesure à l'intérieur du rectangle
        assertFalse(satelliteEn(195, 20).couvre(new Position(5, 0)));
    }

    // ---------- archive ----------

    @Test
    void recevoir_rangeLesMesuresParBaliseDOrigine() {
        Satellite s1 = satelliteEn(50, 20);
        Mesure m = new Mesure("temperature", 3, new Position(50, -10), List.of(new ValeurMesuree(Grandeur.TEMPERATURE, 11)));

        s1.recevoir("b1", List.of(m, m));
        s1.recevoir("b2", List.of(m));

        assertEquals(3, s1.getArchive().getNombreMesures());
        assertEquals(2, s1.getArchive().getMesuresDe("b1").size());
        assertTrue(s1.getArchive().getMesuresDe("inconnue").isEmpty());
    }
}
