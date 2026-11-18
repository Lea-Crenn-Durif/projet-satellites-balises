package satellitesbalises.modele.deplacement;

import java.io.Serializable;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;


@FunctionalInterface
public interface Deplacement extends Serializable {

    Position prochainePosition(Position actuelle, ContexteSimulation contexte);
}
