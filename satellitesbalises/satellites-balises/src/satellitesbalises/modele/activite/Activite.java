package satellitesbalises.modele.activite;

import java.io.Serializable;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.simulation.ContexteSimulation;


public interface Activite extends Serializable {

    String getNom();

    void agir(Balise balise, ContexteSimulation contexte);
}
