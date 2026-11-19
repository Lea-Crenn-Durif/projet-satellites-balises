package satellitesbalises.modele.deplacement;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * Satellite : fait le tour du monde à altitude constante (EX-3.5) : x = (x + vitesse) modulo largeur.
 * Sorti à droite, il revient par la gauche.
 */
public class DeplacementOrbital implements Deplacement {

    private static final long serialVersionUID = 1L;

    private final double vitesse;

    /** @throws IllegalArgumentException si la vitesse est nulle ou négative : un satellite tourne toujours dans le même sens (EX-3.6) */
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
