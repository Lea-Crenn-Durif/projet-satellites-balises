package satellitesbalises.modele.mesure;

import java.io.Serializable;
import java.util.List;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * Un appareil de mesure porté par une balise (EX-4.2).
 *
 * <p>Un instrument n'invente pas ses valeurs : il les lit dans l'environnement du contexte
 * (décision D8). Ajouter un instrument, c'est écrire une classe de plus, sans toucher
 * à {@code Balise}, {@code MemoireBalise} ni {@code Simulation} (EX-4.8).</p>
 */
public interface Instrument extends Serializable {

    /** Le nom de l'instrument, par exemple « temperature » ou « courantometre ». */
    String getNom();

    /** Les grandeurs qu'il mesure, dans l'ordre où elles apparaissent dans ses mesures. */
    List<Grandeur> getGrandeurs();

    /** Fait une mesure à cette position, au pas du contexte (EX-4.3). */
    Mesure mesurer(Position position, ContexteSimulation contexte);
}
