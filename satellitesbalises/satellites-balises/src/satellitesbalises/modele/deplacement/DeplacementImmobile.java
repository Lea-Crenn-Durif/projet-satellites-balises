package satellitesbalises.modele.deplacement;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/** Ne bouge pas. C'est le déplacement par défaut, et celui d'un satellite géostationnaire. */
public class DeplacementImmobile implements Deplacement {

    private static final long serialVersionUID = 1L;

    @Override
    public Position prochainePosition(Position actuelle, ContexteSimulation contexte) {
        return actuelle;
    }
}
