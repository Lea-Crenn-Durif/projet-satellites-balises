package satellitesbalises.modele.activite;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * La balise se déplace en profondeur, mesure avec chacun de ses instruments (EX-4.4),
 * puis remonte si sa condition de remontée est remplie ou si on le lui a demandé (EX-5.2, EX-5.3).
 * C'est l'activité de départ de toute balise.
 */
public class ActiviteCollecte implements Activite {

    private static final long serialVersionUID = 1L;

    /** Depuis combien de pas la balise collecte ; le pas en cours compte. */
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
