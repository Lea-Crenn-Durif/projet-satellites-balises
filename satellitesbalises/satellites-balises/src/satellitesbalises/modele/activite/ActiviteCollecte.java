package satellitesbalises.modele.activite;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.simulation.ContexteSimulation;

public class ActiviteCollecte implements Activite {

    private static final long serialVersionUID = 1L;

    private long dureeCollecte;

    @Override
    public String getNom() {
        return "Collecte";
    }

    @Override
    public void agir(Balise balise, ContexteSimulation contexte) {
        dureeCollecte++;
        balise.seDeplacer(contexte);
        balise.mesurer(contexte);
        if (balise.doitRemonter(dureeCollecte)) {
            double profondeurDeTravail = balise.getPosition().getY();
            balise.changerActivite(new ActiviteRemontee(profondeurDeTravail), contexte);
        }
    }
}
