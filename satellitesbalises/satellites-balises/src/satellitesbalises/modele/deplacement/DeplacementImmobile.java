package satellitesbalises.modele.deplacement;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

public class DeplacementImmobile implements Deplacement {

    private static final long serialVersionUID = 1L;

    @Override
    public Position prochainePosition(Position actuelle, ContexteSimulation contexte) {
        return actuelle;
    }
}
