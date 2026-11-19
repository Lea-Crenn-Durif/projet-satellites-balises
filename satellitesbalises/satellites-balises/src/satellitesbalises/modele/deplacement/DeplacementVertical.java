package satellitesbalises.modele.deplacement;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * Balise : monte et descend de « vitesse » par pas entre deux profondeurs, sans bouger
 * horizontalement (EX-3.2). Elle part vers le fond.
 *
 * <p>Les profondeurs sont positives : profondeurs 10 et 50 veulent dire y entre −50 et −10.</p>
 */
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
