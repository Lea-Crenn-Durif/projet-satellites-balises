package satellitesbalises.modele.mesure;

import java.io.Serializable;
import java.util.List;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

public interface Instrument extends Serializable {

    String getNom();

    List<Grandeur> getGrandeurs();

    Mesure mesurer(Position position, ContexteSimulation contexte);
}
