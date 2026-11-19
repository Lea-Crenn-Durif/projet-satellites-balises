package satellitesbalises.modele.activite;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * La balise monte tout droit de « vitesse verticale » par pas, sans mesurer, s'arrête exactement
 * à la surface et passe en Attente dans le même pas (EX-5.4).
 */
public class ActiviteRemontee extends ActiviteAvecProfondeur {

    private static final long serialVersionUID = 1L;

    public ActiviteRemontee(double profondeurDeTravail) {
        super(profondeurDeTravail);
    }

    @Override
    public String getNom() {
        return "Remontée";
    }

    @Override
    public void agir(Balise balise, ContexteSimulation contexte) {
        Position actuelle = balise.getPosition();
        double y = Math.min(GeometrieMonde.NIVEAU_SURFACE, actuelle.getY() + balise.getVitesseVerticale());
        balise.placer(new Position(actuelle.getX(), y), contexte);
        if (y == GeometrieMonde.NIVEAU_SURFACE) {
            balise.changerActivite(new ActiviteAttente(getProfondeurDeTravail()), contexte);
        }
    }
}
