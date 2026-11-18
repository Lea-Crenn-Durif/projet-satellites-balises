package satellitesbalises.modele.creation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.communication.StatutSatellite;
import satellitesbalises.modele.deplacement.DeplacementImmobile;
import satellitesbalises.modele.entite.Apparence;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;

/**
 * Étape 2 — EX-2.1, EX-2.3, EX-2.3b, EX-2.3c : construire un satellite avec un monteur.
 * Valeurs par défaut attendues : déplacement immobile, portée 30, débit 2, apparence par défaut.
 */
class ConstructeurSatelliteTest {

    private final FabriqueEntites fabrique = new FabriqueEntites(new GeometrieMonde(200, 100, 80));

    @Test
    void construire_satelliteComplet_renvoieSatelliteDisponible() {
        DeplacementImmobile immobile = new DeplacementImmobile();
        Apparence rouge = new Apparence("rouge", 14);

        Satellite s1 = fabrique.nouveauSatellite("s1")
                .en(new Position(40, 60))
                .avecDeplacement(immobile)
                .avecPortee(25)
                .avecDebit(4)
                .avecApparence(rouge)
                .construire();

        assertEquals("s1", s1.getNom());
        assertEquals(new Position(40, 60), s1.getPosition());
        assertEquals(immobile, s1.getDeplacement());
        assertEquals(25, s1.getPortee(), 1e-9);
        assertEquals(4, s1.getDebit());
        assertEquals(rouge, s1.getApparence());
        assertEquals(StatutSatellite.DISPONIBLE, s1.getStatut());
        assertTrue(s1.estDisponible());
    }

    @Test
    void construire_seulementPosition_valeursParDefaut() {
        Satellite s1 = fabrique.nouveauSatellite("s1").en(new Position(40, 60)).construire();

        assertInstanceOf(DeplacementImmobile.class, s1.getDeplacement());
        assertEquals(30, s1.getPortee(), 1e-9);
        assertEquals(2, s1.getDebit());
        assertEquals(Apparence.parDefaut(), s1.getApparence());
    }

    @Test
    void construire_satelliteDansLaMer_leveErreurConfiguration() {
        ConstructeurSatellite constructeur = fabrique.nouveauSatellite("s1").en(new Position(40, -10));

        assertThrows(ErreurConfiguration.class, constructeur::construire);
    }

    @Test
    void construire_satelliteHorsDuMonde_leveErreurConfiguration() {
        ConstructeurSatellite constructeur = fabrique.nouveauSatellite("s1").en(new Position(500, 50));

        assertThrows(ErreurConfiguration.class, constructeur::construire);
    }

    @Test
    void construire_sansPosition_leveErreurNommantPosition() {
        ConstructeurSatellite constructeur = fabrique.nouveauSatellite("s1");

        ErreurConfiguration erreur = assertThrows(ErreurConfiguration.class, constructeur::construire);

        assertTrue(erreur.getMessage().contains("s1"), "le message nomme l'entité : " + erreur.getMessage());
        assertTrue(erreur.getMessage().contains("position"), "le message nomme le paramètre : " + erreur.getMessage());
    }

    @Test
    void construire_debitNul_leveErreurConfiguration() {
        ConstructeurSatellite constructeur = fabrique.nouveauSatellite("s1").en(new Position(40, 60)).avecDebit(0);

        assertThrows(ErreurConfiguration.class, constructeur::construire);
    }

    @Test
    void construire_porteeNegative_leveErreurConfiguration() {
        ConstructeurSatellite constructeur = fabrique.nouveauSatellite("s1").en(new Position(40, 60)).avecPortee(-5);

        assertThrows(ErreurConfiguration.class, constructeur::construire);
    }
}
