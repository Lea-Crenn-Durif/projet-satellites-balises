package satellitesbalises.modele.activite;

import java.io.Serializable;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.simulation.ContexteSimulation;

public interface Activite extends Serializable {

    String getNom();

    void agir(Balise balise, ContexteSimulation contexte);

    default boolean accepteTransmission() {
        return false;
    }

    default void surTransfertDemarre(Balise balise, ContexteSimulation contexte) {
        // Rien à faire en dehors de l'attente.
    }

    default void surTransfertTermine(Balise balise, boolean complet, ContexteSimulation contexte) {
        // Rien à faire en dehors de la transmission.
    }
}
