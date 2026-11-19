package satellitesbalises.modele.activite;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * La balise redescend tout droit jusqu'à sa profondeur de travail, s'y arrête exactement,
 * puis reprend la collecte avec son déplacement d'origine (EX-5.6, EX-5.7).
 */
public class ActiviteDescente extends ActiviteAvecProfondeur {

    private static final long serialVersionUID = 1L;

    public ActiviteDescente(double profondeurDeTravail) {
        super(profondeurDeTravail);
    }

    @Override
    public String getNom() {
        return "Descente";
    }

    @Override
    public void agir(Balise balise, ContexteSimulation contexte) {
        Position actuelle = balise.getPosition();
        double y = Math.max(getProfondeurDeTravail(), actuelle.getY() - balise.getVitesseVerticale());
        balise.placer(new Position(actuelle.getX(), y), contexte);
        if (y == getProfondeurDeTravail()) {
            balise.changerActivite(new ActiviteCollecte(), contexte);
        }
    }
}
