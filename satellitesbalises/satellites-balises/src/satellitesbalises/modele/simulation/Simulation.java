package satellitesbalises.modele.simulation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.PriorityQueue;

import eventHandler.AbstractEvent;
import eventHandler.EventHandler;
import satellitesbalises.modele.commande.CommandeSimulation;
import satellitesbalises.modele.communication.GestionnaireCommunications;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.evenement.EvenementEntiteAjoutee;
import satellitesbalises.modele.evenement.EvenementEntiteSupprimee;
import satellitesbalises.modele.evenement.EvenementPasTermine;
import satellitesbalises.modele.mesure.Environnement;
import satellitesbalises.modele.monde.Monde;

public class Simulation implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Monde monde;
    private final Environnement environnement;
    private final PriorityQueue<CommandePlanifiee> commandesPrevues = new PriorityQueue<>();
    private final GestionnaireCommunications communications = new GestionnaireCommunications();
    private long pasCourant;
    private long prochainNumeroDOrdre;
    private transient EventHandler eventHandler;

    public Simulation(Monde monde, Environnement environnement, EventHandler eventHandler) {
        this.monde = Objects.requireNonNull(monde, "monde");
        this.environnement = Objects.requireNonNull(environnement, "environnement");
        this.eventHandler = Objects.requireNonNull(eventHandler, "eventHandler");
    }

    public void ajouter(Satellite satellite) {
        monde.ajouter(satellite);
        envoyer(new EvenementEntiteAjoutee(satellite));
    }

    public void ajouter(Balise balise) {
        monde.ajouter(balise);
        envoyer(new EvenementEntiteAjoutee(balise));
    }

    public void supprimer(String nom) {
        if (monde.trouver(nom).isEmpty()) {
            return;
        }
        communications.rompreTransfertsDe(nom, nouveauContexte());
        monde.retirer(nom).ifPresent(entite -> envoyer(new EvenementEntiteSupprimee(entite)));
    }

    public void planifier(CommandeSimulation commande, long pas) {
        Objects.requireNonNull(commande, "commande");
        commandesPrevues.add(new CommandePlanifiee(pas, prochainNumeroDOrdre++, commande));
    }

    public void avancerDUnPas() {
        pasCourant++;
        executerCommandesPrevues();
        ContexteSimulation contexte = nouveauContexte();
        faireAgirLesEntites(contexte);
        communications.mettreAJour(monde, contexte);
        envoyer(new EvenementPasTermine(this, pasCourant));
    }

    public void envoyer(AbstractEvent evenement) {
        eventHandler.send(evenement);
    }

    public long getPasCourant() {
        return pasCourant;
    }

    public Monde getMonde() {
        return monde;
    }

    public Environnement getEnvironnement() {
        return environnement;
    }

    public GestionnaireCommunications getCommunications() {
        return communications;
    }

    private void faireAgirLesEntites(ContexteSimulation contexte) {
        new ArrayList<>(monde.getSatellites()).forEach(satellite -> satellite.agir(contexte));
        new ArrayList<>(monde.getBalises()).forEach(balise -> balise.agir(contexte));
    }

    private ContexteSimulation nouveauContexte() {
        return new ContexteSimulation(pasCourant, monde.getGeometrie(), environnement, eventHandler);
    }

    private void executerCommandesPrevues() {
        while (!commandesPrevues.isEmpty() && commandesPrevues.peek().getPas() <= pasCourant) {
            commandesPrevues.poll().getCommande().executer(this);
        }
    }
}
