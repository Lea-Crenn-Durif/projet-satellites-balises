package satellitesbalises.modele.activite;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * La balise flotte à la surface, sans bouger ni mesurer, en attendant un satellite (EX-5.5).
 * C'est la seule activité où un transfert peut commencer.
 */
public class ActiviteAttente extends ActiviteAvecProfondeur {

    private static final long serialVersionUID = 1L;

    public ActiviteAttente(double profondeurDeTravail) {
        super(profondeurDeTravail);
    }

    @Override
    public String getNom() {
        return "Attente";
    }

    @Override
    public void agir(Balise balise, ContexteSimulation contexte) {
        // Elle attend : ni déplacement, ni mesure.
    }

    @Override
    public boolean accepteTransmission() {
        return true;
    }

    @Override
    public void surTransfertDemarre(Balise balise, ContexteSimulation contexte) {
        balise.changerActivite(new ActiviteTransmission(getProfondeurDeTravail()), contexte);
    }
}
