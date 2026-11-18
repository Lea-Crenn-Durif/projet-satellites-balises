package satellitesbalises.modele.mesure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import eventHandler.EventHandler;
import satellitesbalises.modele.creation.Catalogue;
import satellitesbalises.modele.creation.CataloguesParDefaut;
import satellitesbalises.modele.creation.Parametres;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/** Étape 4 — EX-4.2, EX-4.3 : les instruments produisent des mesures complètes. */
class InstrumentsTest {

    /** Environnement de test : chaque grandeur a une valeur fixe et reconnaissable. */
    private static final Environnement ENVIRONNEMENT_FIXE = (grandeur, position, pas) -> switch (grandeur.getNom()) {
        case "temperature" -> 14.5;
        case "salinite" -> 35;
        case "vitesseCourant" -> 0.8;
        case "directionCourant" -> 270;
        default -> -1;
    };

    private static ContexteSimulation contexte(long pas) {
        return new ContexteSimulation(pas, new GeometrieMonde(200, 100, 80), ENVIRONNEMENT_FIXE, new EventHandler());
    }

    @Test
    void mesurer_renseigneTousLesChamps() {
        InstrumentSimple thermometre = new InstrumentSimple("temperature", Grandeur.TEMPERATURE);
        Position ici = new Position(30, -20);

        Mesure mesure = thermometre.mesurer(ici, contexte(42));

        assertEquals("temperature", mesure.getNomInstrument());
        assertEquals(42, mesure.getPas());
        assertEquals(ici, mesure.getPosition());
        assertEquals(List.of(new ValeurMesuree(Grandeur.TEMPERATURE, 14.5)), mesure.getValeurs());
        assertEquals("°C", mesure.getValeurs().get(0).getGrandeur().getUnite());
    }

    @Test
    void instrumentSimple_uneSeuleGrandeur() {
        assertEquals(List.of(Grandeur.SALINITE), new InstrumentSimple("salinite", Grandeur.SALINITE).getGrandeurs());
    }

    @Test
    void courantometre_mesurer_deuxValeurs() {
        Mesure mesure = new InstrumentCourantometre().mesurer(new Position(30, -20), contexte(1));

        assertEquals(2, mesure.getValeurs().size());
        assertEquals(0.8, mesure.valeurDe(Grandeur.VITESSE_COURANT), 1e-9);
        assertEquals(270, mesure.valeurDe(Grandeur.DIRECTION_COURANT), 1e-9);
    }

    @Test
    void courantometre_nom() {
        assertEquals("courantometre", new InstrumentCourantometre().getNom());
    }

    @Test
    void catalogue_contientLesQuatreInstruments() {
        Catalogue<Instrument> instruments = CataloguesParDefaut.creer().getInstruments();

        assertEquals(Set.of("temperature", "salinite", "courantometre", "turbidite"), instruments.getNoms());
        assertEquals(List.of(Grandeur.TURBIDITE),
                instruments.creer("turbidite", new Parametres(Map.of())).getGrandeurs());
    }
}
