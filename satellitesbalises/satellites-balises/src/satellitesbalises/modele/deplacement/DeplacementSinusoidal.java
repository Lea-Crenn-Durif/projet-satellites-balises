package satellitesbalises.modele.deplacement;

import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;


public class DeplacementSinusoidal implements Deplacement {

    private static final long serialVersionUID = 1L;

    private final double vitesse;
    private final double amplitude;
    private final double periode;
    private final double profondeur;
    private final AllerRetour allerRetour = new AllerRetour(1);

    public DeplacementSinusoidal(double vitesse, double amplitude, double periode, double profondeur) {
        if (periode <= 0) {
            throw new IllegalArgumentException("La période doit être strictement positive : " + periode);
        }
        this.vitesse = vitesse;
        this.amplitude = amplitude;
        this.periode = periode;
        this.profondeur = profondeur;
    }

    @Override
    public Position prochainePosition(Position actuelle, ContexteSimulation contexte) {
        GeometrieMonde geometrie = contexte.getGeometrie();
        double x = allerRetour.avancer(actuelle.getX(), vitesse, 0, geometrie.getLargeur());
        double onde = -profondeur + amplitude * Math.sin(2 * Math.PI * contexte.getPas() / periode);
        double y = Math.max(-geometrie.getProfondeurMer(), Math.min(GeometrieMonde.NIVEAU_SURFACE, onde));
        return new Position(x, y);
    }
}
