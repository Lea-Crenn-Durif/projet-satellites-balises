package satellitesbalises.modele.communication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.evenement.EvenementStatutModifie;
import satellitesbalises.modele.evenement.EvenementTransfertDemarre;
import satellitesbalises.modele.monde.Monde;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * Met en relation les balises et les satellites (patron Médiateur, décision D5) :
 * balises et satellites ne se connaissent pas, c'est lui qui crée et fait avancer les transferts.
 *
 * <p>À chaque pas, après que les entités ont agi : d'abord les transferts en cours avancent,
 * puis les balises en attente sont associées à un satellite. Un transfert créé ce pas-ci
 * n'envoie donc rien avant le pas suivant (EX-6.5).</p>
 */
public class GestionnaireCommunications implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Transfert> transferts = new ArrayList<>();

    /** La phase « communications » d'un pas. */
    public void mettreAJour(Monde monde, ContexteSimulation contexte) {
        faireAvancerTransferts(contexte);
        associerBalisesEnAttente(monde, contexte);
    }

    /** Rompt les transferts où intervient cette entité (par exemple quand on la supprime, EX-2.5). */
    public void rompreTransfertsDe(String nom, ContexteSimulation contexte) {
        for (Transfert transfert : new ArrayList<>(transferts)) {
            if (transfert.getBalise().getNom().equals(nom) || transfert.getSatellite().getNom().equals(nom)) {
                transfert.rompre(contexte);
                transferts.remove(transfert);
            }
        }
    }

    /** Les transferts en cours. La liste ne peut pas être modifiée. */
    public List<Transfert> getTransferts() {
        return Collections.unmodifiableList(transferts);
    }

    private void faireAvancerTransferts(ContexteSimulation contexte) {
        for (Transfert transfert : new ArrayList<>(transferts)) {
            transfert.avancer(contexte);
            if (transfert.estTermine()) {
                transferts.remove(transfert);
            }
        }
    }

    /** Les balises sont servies dans l'ordre où elles ont été ajoutées (EX-6.3). */
    private void associerBalisesEnAttente(Monde monde, ContexteSimulation contexte) {
        for (Balise balise : monde.getBalises()) {
            if (balise.estEnAttente()) {
                choisirSatellite(balise, monde).ifPresent(satellite -> demarrerTransfert(balise, satellite, contexte));
            }
        }
    }

    /** Parmi les satellites disponibles qui couvrent la balise, le plus proche (EX-6.2, EX-6.3). */
    private Optional<Satellite> choisirSatellite(Balise balise, Monde monde) {
        return monde.getSatellites().stream()
                .filter(Satellite::estDisponible)
                .filter(satellite -> satellite.couvre(balise.getPosition()))
                .min(Comparator.comparingDouble(satellite -> satellite.getPosition().ecartHorizontal(balise.getPosition())));
    }

    private void demarrerTransfert(Balise balise, Satellite satellite, ContexteSimulation contexte) {
        Transfert transfert = new Transfert(balise, satellite);
        transferts.add(transfert);
        satellite.reserver();
        balise.commencerTransmission(contexte);
        contexte.getEventHandler().send(new EvenementStatutModifie(satellite));
        contexte.getEventHandler().send(new EvenementTransfertDemarre(transfert));
    }
}
