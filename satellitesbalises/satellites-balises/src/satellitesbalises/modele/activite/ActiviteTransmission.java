package satellitesbalises.modele.activite;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.simulation.ContexteSimulation;

public class ActiviteTransmission extends ActiviteAvecProfondeur {

    private static final long serialVersionUID = 1L;

    public ActiviteTransmission(double profondeurDeTravail) {
        super(profondeurDeTravail);
    }

    @Override
    public String getNom() {
        return "Transmission";
    }

    @Override
    public void agir(Balise balise, ContexteSimulation contexte) {
        // Elle reste à la surface pendant que le transfert avance.
    }

    @Override
    public void surTransfertTermine(Balise balise, boolean complet, ContexteSimulation contexte) {
        Activite suivante = complet
                ? new ActiviteDescente(getProfondeurDeTravail())
                : new ActiviteAttente(getProfondeurDeTravail());
        balise.changerActivite(suivante, contexte);
    }
}
