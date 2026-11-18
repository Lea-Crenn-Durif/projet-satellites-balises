package satellitesbalises.modele.deplacement;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

public class DeplacementVertical implements Deplacement {

    private static final long serialVersionUID = 1L;

    private final double vitesse;
    private final double profondeurMin;
    private final double profondeurMax;
    private final AllerRetour allerRetour = new AllerRetour(-1);

    public DeplacementVertical(double vitesse, double profondeurMin, double profondeurMax) {
        this.vitesse = vitesse;
        this.profondeurMin = profondeurMin;
        this.profondeurMax = profondeurMax;
    }

    @Override
    public Position prochainePosition(Position actuelle, ContexteSimulation contexte) {
        double y = allerRetour.avancer(actuelle.getY(), vitesse, -profondeurMax, -profondeurMin);
        return new Position(actuelle.getX(), y);
    }
}
