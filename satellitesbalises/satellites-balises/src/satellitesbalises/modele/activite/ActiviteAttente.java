package satellitesbalises.modele.activite;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.simulation.ContexteSimulation;

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
