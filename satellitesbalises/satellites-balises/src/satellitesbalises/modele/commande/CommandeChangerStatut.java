package satellitesbalises.modele.commande;

import java.util.Objects;
import java.util.Optional;

import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;
import satellitesbalises.modele.evenement.EvenementStatutModifie;
import satellitesbalises.modele.simulation.Simulation;

/** Arrête ou redémarre un satellite et l'annonce (EX-6.1). Satellite inconnu : commande ignorée et signalée. */
public class CommandeChangerStatut implements CommandeSimulation {

    private static final long serialVersionUID = 1L;

    private final String nomSatellite;
    private final boolean enService;

    /** @param enService vrai pour démarrer, faux pour arrêter */
    public CommandeChangerStatut(String nomSatellite, boolean enService) {
        this.nomSatellite = Objects.requireNonNull(nomSatellite, "nomSatellite");
        this.enService = enService;
    }

    @Override
    public void executer(Simulation simulation) {
        Optional<Satellite> satellite = simulation.getMonde().trouverSatellite(nomSatellite);
        if (satellite.isEmpty()) {
            simulation.envoyer(new EvenementCommandeIgnoree(simulation,
                    (enService ? "demarrer " : "arreter ") + nomSatellite + " ignorée : satellite inconnu"));
            return;
        }
        if (enService) {
            satellite.get().demarrer();
        } else {
            satellite.get().arreter();
        }
        simulation.envoyer(new EvenementStatutModifie(satellite.get()));
    }
}
