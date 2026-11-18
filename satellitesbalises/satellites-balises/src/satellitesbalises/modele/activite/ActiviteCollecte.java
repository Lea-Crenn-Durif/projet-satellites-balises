package satellitesbalises.modele.activite;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.simulation.ContexteSimulation;

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
