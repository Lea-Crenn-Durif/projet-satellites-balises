package satellitesbalises.modele.mesure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import satellitesbalises.modele.creation.ErreurConfiguration;
import satellitesbalises.modele.monde.Position;

class MemoireBaliseTest {

    private static Mesure mesure(long pas) {
        return new Mesure("temperature", pas, new Position(0, -10), List.of(new ValeurMesuree(Grandeur.TEMPERATURE, 12)));
    }

    @Test
    void nouvelleMemoire_videAvecSaCapacite() {
        MemoireBalise memoire = new MemoireBalise(3);

        assertTrue(memoire.estVide());
        assertFalse(memoire.estPleine());
        assertEquals(0, memoire.getTaille());
        assertEquals(3, memoire.getCapacite());
    }

    @Test
    void constructeur_capaciteNulle_leveErreur() {
        assertThrows(IllegalArgumentException.class, () -> new MemoireBalise(0));
    }

    @Test
    void stocker_jusquALaCapacite_devientPleine() {
        MemoireBalise memoire = new MemoireBalise(2);

        assertTrue(memoire.stocker(mesure(1)));
        assertTrue(memoire.stocker(mesure(2)));

        assertTrue(memoire.estPleine());
    }

    @Test
    void stocker_memoirePleine_refuseLaMesure() {
        MemoireBalise memoire = new MemoireBalise(2);
        memoire.stocker(mesure(1));
        memoire.stocker(mesure(2));

        assertFalse(memoire.stocker(mesure(3)));
        assertEquals(2, memoire.getTaille());
    }

    @Test
    void ceder_troisSurCinq_resteDeuxEtLesPlusAnciennesPartent() {
        MemoireBalise memoire = new MemoireBalise(10);
        for (long pas = 1; pas <= 5; pas++) {
            memoire.stocker(mesure(pas));
        }

        List<Mesure> cedees = memoire.ceder(3);

        assertEquals(List.of(1L, 2L, 3L), cedees.stream().map(Mesure::getPas).toList());
        assertEquals(2, memoire.getTaille());
        assertEquals(4, memoire.getMesures().get(0).getPas());
    }

    @Test
    void ceder_plusQueLeContenu_renvoieTout() {
        MemoireBalise memoire = new MemoireBalise(10);
        memoire.stocker(mesure(1));

        assertEquals(1, memoire.ceder(5).size());
        assertTrue(memoire.estVide());
    }

    @Test
    void getMesures_neDonnePasAccesALInterieur() {
        MemoireBalise memoire = new MemoireBalise(10);
        memoire.stocker(mesure(1));

        assertThrows(UnsupportedOperationException.class, () -> memoire.getMesures().clear());
    }

    @Test
    void changerCapacite_inferieureAuContenu_refuseEtGardeAncienne() {
        MemoireBalise memoire = new MemoireBalise(10);
        for (long pas = 1; pas <= 5; pas++) {
            memoire.stocker(mesure(pas));
        }

        assertThrows(ErreurConfiguration.class, () -> memoire.changerCapacite(3));
        assertEquals(10, memoire.getCapacite());
    }

    @Test
    void changerCapacite_superieureAuContenu_acceptee() {
        MemoireBalise memoire = new MemoireBalise(10);
        memoire.stocker(mesure(1));

        memoire.changerCapacite(20);

        assertEquals(20, memoire.getCapacite());
    }
}
