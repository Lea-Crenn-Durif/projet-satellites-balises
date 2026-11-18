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

public class GestionnaireCommunications implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Transfert> transferts = new ArrayList<>();

    public void mettreAJour(Monde monde, ContexteSimulation contexte) {
        faireAvancerTransferts(contexte);
        associerBalisesEnAttente(monde, contexte);
    }

    public void rompreTransfertsDe(String nom, ContexteSimulation contexte) {
        for (Transfert transfert : new ArrayList<>(transferts)) {
            if (transfert.getBalise().getNom().equals(nom) || transfert.getSatellite().getNom().equals(nom)) {
                transfert.rompre(contexte);
                transferts.remove(transfert);
            }
        }
    }

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

    private void associerBalisesEnAttente(Monde monde, ContexteSimulation contexte) {
        for (Balise balise : monde.getBalises()) {
            if (balise.estEnAttente()) {
                choisirSatellite(balise, monde).ifPresent(satellite -> demarrerTransfert(balise, satellite, contexte));
            }
        }
    }

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
