package satellitesbalises.modele.communication;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import eventHandler.EventHandler;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.evenement.EvenementMemoireModifiee;
import satellitesbalises.modele.evenement.EvenementStatutModifie;
import satellitesbalises.modele.evenement.EvenementTransfertProgresse;
import satellitesbalises.modele.evenement.EvenementTransfertTermine;
import satellitesbalises.modele.mesure.Mesure;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * Le passage des mesures d'une balise vers un satellite, sur plusieurs pas (EX-6.5 à EX-6.8).
 *
 * <p>À chaque pas : s'il n'est pas rompu, il fait partir min(débit, reste) mesures de la mémoire
 * de la balise vers l'archive du satellite. Il se termine au pas où part la dernière mesure.</p>
 */
public class Transfert implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Balise balise;
    private final Satellite satellite;
    private final int aTransmettre;
    private int transmis;
    private boolean termine;
    private boolean rompu;

    /** Le transfert porte sur les mesures présentes dans la mémoire au moment où il est créé. */
    public Transfert(Balise balise, Satellite satellite) {
        this.balise = Objects.requireNonNull(balise, "balise");
        this.satellite = Objects.requireNonNull(satellite, "satellite");
        this.aTransmettre = balise.getMemoire().getTaille();
    }

    /** Un pas de transfert : vérifie qu'il peut continuer, puis envoie au plus « débit » mesures. */
    public void avancer(ContexteSimulation contexte) {
        if (termine) {
            return;
        }
        if (satellite.getStatut() == StatutSatellite.HORS_SERVICE || !satellite.couvre(balise.getPosition())) {
            rompre(contexte);
            return;
        }
        int nombre = Math.min(satellite.getDebit(), balise.getMemoire().getTaille());
        if (nombre > 0) {
            List<Mesure> mesures = balise.ceder(nombre);
            satellite.recevoir(balise.getNom(), mesures);
            transmis += mesures.size();
            EventHandler eventHandler = contexte.getEventHandler();
            eventHandler.send(new EvenementMemoireModifiee(balise));
            eventHandler.send(new EvenementTransfertProgresse(this));
        }
        if (balise.getMemoire().estVide()) {
            finir(true, contexte);
        }
    }

    /** Arrête le transfert avant la fin : la balise garde ce qui n'est pas parti et revient en Attente (EX-6.7). */
    public void rompre(ContexteSimulation contexte) {
        if (!termine) {
            rompu = true;
            finir(false, contexte);
        }
    }

    public boolean estTermine() {
        return termine;
    }

    public boolean estRompu() {
        return rompu;
    }

    /** Part des mesures déjà envoyées, entre 0 et 1. */
    public double getProgression() {
        return aTransmettre == 0 ? 1 : (double) transmis / aTransmettre;
    }

    public int getTransmis() {
        return transmis;
    }

    public Balise getBalise() {
        return balise;
    }

    public Satellite getSatellite() {
        return satellite;
    }

    /** Libère le satellite (EX-6.8, EX-6.9), fait changer d'activité la balise, puis l'annonce. */
    private void finir(boolean complet, ContexteSimulation contexte) {
        termine = true;
        satellite.liberer();
        balise.terminerTransmission(complet, contexte);
        EventHandler eventHandler = contexte.getEventHandler();
        eventHandler.send(new EvenementStatutModifie(satellite));
        eventHandler.send(new EvenementTransfertTermine(this, complet));
    }
}
