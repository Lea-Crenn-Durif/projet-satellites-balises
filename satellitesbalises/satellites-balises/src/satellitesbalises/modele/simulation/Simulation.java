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

/**
 * Le moteur de la simulation : il fait avancer le temps pas par pas (décision D1).
 *
 * <p>Un pas se déroule toujours dans le même ordre (EX-1.7) :</p>
 * <ol>
 *   <li>le numéro du pas courant augmente de 1 ;</li>
 *   <li>les commandes prévues pour ce pas s'exécutent, dans l'ordre où elles ont été planifiées ;</li>
 *   <li>chaque satellite, puis chaque balise, agit ;</li>
 *   <li>les communications avancent : transferts en cours, puis nouvelles associations ;</li>
 *   <li>l'événement « pas terminé » est publié.</li>
 * </ol>
 *
 * <p>La simulation est la racine de ce qu'on sauvegarde (étape 8). L'EventHandler n'est pas
 * sauvegardé : il appartient à l'affichage du moment.</p>
 */
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

    /**
     * Ajoute un satellite au monde et l'annonce (EX-2.5).
     *
     * @throws satellitesbalises.modele.creation.ErreurConfiguration si le nom est déjà pris
     */
    public void ajouter(Satellite satellite) {
        monde.ajouter(satellite);
        envoyer(new EvenementEntiteAjoutee(satellite));
    }

    /**
     * Ajoute une balise au monde et l'annonce (EX-2.5).
     *
     * @throws satellitesbalises.modele.creation.ErreurConfiguration si le nom est déjà pris
     */
    public void ajouter(Balise balise) {
        monde.ajouter(balise);
        envoyer(new EvenementEntiteAjoutee(balise));
    }

    /**
     * Retire l'entité de ce nom et l'annonce ; ses transferts en cours sont rompus d'abord (EX-2.5).
     * Ne fait rien si elle n'existe pas.
     */
    public void supprimer(String nom) {
        if (monde.trouver(nom).isEmpty()) {
            return;
        }
        communications.rompreTransfertsDe(nom, nouveauContexte());
        monde.retirer(nom).ifPresent(entite -> envoyer(new EvenementEntiteSupprimee(entite)));
    }

    /**
     * Prévoit d'exécuter la commande au début du pas indiqué (EX-1.5).
     * Si ce pas est déjà passé, la commande s'exécute au pas suivant.
     */
    public void planifier(CommandeSimulation commande, long pas) {
        Objects.requireNonNull(commande, "commande");
        commandesPrevues.add(new CommandePlanifiee(pas, prochainNumeroDOrdre++, commande));
    }

    /** Fait avancer la simulation d'un pas (EX-1.4, EX-1.7). */
    public void avancerDUnPas() {
        pasCourant++;
        executerCommandesPrevues();
        ContexteSimulation contexte = nouveauContexte();
        faireAgirLesEntites(contexte);
        communications.mettreAJour(monde, contexte);
        envoyer(new EvenementPasTermine(this, pasCourant));
    }

    /** Publie un événement à tous ceux qui l'écoutent. */
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

    /** Le médiateur des communications : il connaît les transferts en cours. */
    public GestionnaireCommunications getCommunications() {
        return communications;
    }

    /** Chaque satellite, puis chaque balise, dans l'ordre d'ajout (EX-1.7). */
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
