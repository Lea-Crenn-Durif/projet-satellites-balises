package satellitesbalises.modele.commande;

import java.io.Serializable;

import satellitesbalises.modele.simulation.Simulation;

/**
 * Une action à appliquer à la simulation à un pas précis (patron Commande, décision D9).
 *
 * <p>Les commandes sont planifiées avec {@link Simulation#planifier(CommandeSimulation, long)}
 * et exécutées au début du pas prévu.</p>
 */
@FunctionalInterface
public interface CommandeSimulation extends Serializable {

    void executer(Simulation simulation);
}
