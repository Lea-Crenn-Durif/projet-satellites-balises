package satellitesbalises.modele.deplacement;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;


public class DeplacementHorizontal implements Deplacement {

    private static final long serialVersionUID = 1L;

    private final double vitesse;
    private final AllerRetour allerRetour = new AllerRetour(1);

    public DeplacementHorizontal(double vitesse) {
        this.vitesse = vitesse;
    }

    @Override
    public Position prochainePosition(Position actuelle, ContexteSimulation contexte) {
        double largeur = contexte.getGeometrie().getLargeur();
        double x = allerRetour.avancer(actuelle.getX(), vitesse, 0, largeur);
        return new Position(x, actuelle.getY());
    }
}
