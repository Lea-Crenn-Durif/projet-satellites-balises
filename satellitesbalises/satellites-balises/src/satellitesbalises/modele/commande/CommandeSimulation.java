package satellitesbalises.modele.commande;

import java.io.Serializable;

import satellitesbalises.modele.simulation.Simulation;


@FunctionalInterface
public interface CommandeSimulation extends Serializable {

    void executer(Simulation simulation);
}
