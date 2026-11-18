package satellitesbalises.modele.deplacement;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;


public class DeplacementOrbital implements Deplacement {

    private static final long serialVersionUID = 1L;

    private final double vitesse;

    public DeplacementOrbital(double vitesse) {
        if (vitesse <= 0) {
            throw new IllegalArgumentException("La vitesse orbitale doit être strictement positive : " + vitesse);
        }
        this.vitesse = vitesse;
    }

    @Override
    public Position prochainePosition(Position actuelle, ContexteSimulation contexte) {
        double largeur = contexte.getGeometrie().getLargeur();
        return new Position((actuelle.getX() + vitesse) % largeur, actuelle.getY());
    }
}
