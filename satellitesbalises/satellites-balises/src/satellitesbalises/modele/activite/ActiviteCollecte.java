package satellitesbalises.modele.activite;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * La balise se déplace en profondeur, puis chacun de ses instruments fait une mesure (EX-4.4).
 * C'est l'activité de départ de toute balise.
 */
public class ActiviteCollecte implements Activite {

    private static final long serialVersionUID = 1L;

    @Override
    public String getNom() {
        return "Collecte";
    }

    @Override
    public void agir(Balise balise, ContexteSimulation contexte) {
        balise.seDeplacer(contexte);
        balise.mesurer(contexte);
    }
}
