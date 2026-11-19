package satellitesbalises.modele.deplacement;

import java.io.Serializable;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * Une façon de se déplacer (patron Stratégie) : à partir de la position actuelle,
 * donne la position du pas suivant. Chaque entité a la sienne, qu'on peut changer en cours de route.
 */
@FunctionalInterface
public interface Deplacement extends Serializable {

    Position prochainePosition(Position actuelle, ContexteSimulation contexte);
}
