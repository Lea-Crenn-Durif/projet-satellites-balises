package satellitesbalises.modele.entite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import satellitesbalises.modele.activite.Activite;
import satellitesbalises.modele.activite.ActiviteCollecte;
import satellitesbalises.modele.activite.ConditionRemontee;
import satellitesbalises.modele.creation.ErreurConfiguration;
import satellitesbalises.modele.creation.Parametres;
import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.evenement.EvenementActiviteModifiee;
import satellitesbalises.modele.evenement.EvenementMemoireModifiee;
import satellitesbalises.modele.mesure.Instrument;
import satellitesbalises.modele.mesure.MemoireBalise;
import satellitesbalises.modele.mesure.Mesure;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * Une balise, dans la mer. Elle mesure avec ses instruments, range les mesures dans sa mémoire,
 * puis remonte à la surface pour les envoyer à un satellite (étapes 4 à 6).
 */
public class Balise extends Entite {

    private static final long serialVersionUID = 1L;

    /** Les caractéristiques qu'on peut régler à la création ou avec « configurer » (exemple : memoire = 30 ; vitesseVerticale = 2). */
    public static final Set<String> PROPRIETES = Set.of("memoire", "vitesseVerticale");

    private final MemoireBalise memoire;
    private final List<Instrument> instruments;
    private ConditionRemontee conditionRemontee;
    private double vitesseVerticale;
    private Activite activite = new ActiviteCollecte();
    private boolean remonteeDemandee;

    /** Utiliser {@code FabriqueEntites.nouvelleBalise(nom)}, qui vérifie les valeurs. */
    public Balise(String nom, Position position, Deplacement deplacement, Apparence apparence,
                  MemoireBalise memoire, List<Instrument> instruments,
                  ConditionRemontee conditionRemontee, double vitesseVerticale) {
        super(nom, position, deplacement, apparence);
        this.memoire = Objects.requireNonNull(memoire, "memoire");
        this.instruments = new ArrayList<>(instruments);
        this.conditionRemontee = Objects.requireNonNull(conditionRemontee, "conditionRemontee");
        this.vitesseVerticale = vitesseVerticale;
    }

    public MemoireBalise getMemoire() {
        return memoire;
    }

    public List<Instrument> getInstruments() {
        return Collections.unmodifiableList(instruments);
    }

    public ConditionRemontee getConditionRemontee() {
        return conditionRemontee;
    }

    /** Change la condition de remontée ; elle compte dès le prochain pas de collecte (EX-5.2). */
    public void changerConditionRemontee(ConditionRemontee nouvelleCondition) {
        this.conditionRemontee = Objects.requireNonNull(nouvelleCondition, "conditionRemontee");
    }

    /** De combien la balise monte ou descend par pas pendant la remontée et la descente. */
    public double getVitesseVerticale() {
        return vitesseVerticale;
    }

    /** Ce que fait la balise en ce moment. Toute balise commence en Collecte. */
    public Activite getActivite() {
        return activite;
    }

    /** Passe à une autre activité et l'annonce (EX-5.8). Appelé par les activités elles-mêmes. */
    public void changerActivite(Activite nouvelleActivite, ContexteSimulation contexte) {
        activite = Objects.requireNonNull(nouvelleActivite, "activite");
        remonteeDemandee = false;
        contexte.getEventHandler().send(new EvenementActiviteModifiee(this));
    }

    /** Demande à la balise de remonter à la fin de son prochain pas de collecte (EX-5.3). */
    public void demanderRemontee() {
        remonteeDemandee = true;
    }

    /** Vrai si la remontée a été demandée, ou si la condition de remontée est remplie (EX-5.2). */
    public boolean doitRemonter(long dureeCollecte) {
        return remonteeDemandee || conditionRemontee.estRemplie(this, dureeCollecte);
    }

    /** Vrai si la balise attend un satellite à la surface. */
    public boolean estEnAttente() {
        return activite.accepteTransmission();
    }

    /** Un satellite commence à recevoir ses mesures. Sans effet si elle n'est pas en attente. */
    public void commencerTransmission(ContexteSimulation contexte) {
        activite.surTransfertDemarre(this, contexte);
    }

    /** Le transfert est fini : complet → Descente, interrompu → retour en Attente. */
    public void terminerTransmission(boolean complet, ContexteSimulation contexte) {
        activite.surTransfertTermine(this, complet, contexte);
    }

    /** Retire et renvoie ses {@code nombre} mesures les plus anciennes (pour un transfert). */
    public List<Mesure> ceder(int nombre) {
        return memoire.ceder(nombre);
    }

    /** Ajoute un instrument (EX-4.1). Il mesurera dès le prochain pas de collecte. */
    public void equiper(Instrument instrument) {
        instruments.add(Objects.requireNonNull(instrument, "instrument"));
    }

    /** Retire l'instrument de ce nom et le renvoie, ou renvoie vide si la balise ne l'a pas (EX-4.1). */
    public Optional<Instrument> desequiper(String nomInstrument) {
        Optional<Instrument> instrument = instruments.stream()
                .filter(candidat -> candidat.getNom().equals(nomInstrument))
                .findFirst();
        instrument.ifPresent(instruments::remove);
        return instrument;
    }

    /**
     * Chaque instrument fait une mesure à la position actuelle, rangée en mémoire (EX-4.4).
     * Mémoire pleine : la mesure est perdue (EX-4.5). Annonce le changement s'il y en a un.
     */
    public void mesurer(ContexteSimulation contexte) {
        boolean memoireModifiee = false;
        for (Instrument instrument : instruments) {
            Mesure mesure = instrument.mesurer(getPosition(), contexte);
            memoireModifiee |= memoire.stocker(mesure);
        }
        if (memoireModifiee) {
            contexte.getEventHandler().send(new EvenementMemoireModifiee(this));
        }
    }

    /**
     * Propriétés : « memoire » (capacité, jamais sous le nombre de mesures déjà rangées, EX-6.12)
     * et « vitesseVerticale » (strictement positive).
     */
    @Override
    public void configurer(Parametres parametres) {
        for (String nom : parametres.getValeurs().keySet()) {
            if (!PROPRIETES.contains(nom)) {
                throw new ErreurConfiguration("Balise " + getNom() + " : propriété inconnue « " + nom + " »");
            }
        }
        double nouvelleVitesse = parametres.nombre("vitesseVerticale", vitesseVerticale);
        if (nouvelleVitesse <= 0) {
            throw new ErreurConfiguration("Balise " + getNom() + " : la vitesse verticale doit être > 0");
        }
        memoire.changerCapacite(parametres.entier("memoire", memoire.getCapacite()));
        vitesseVerticale = nouvelleVitesse;
    }

    /** La balise confie le pas à son activité courante (patron État). */
    @Override
    public void agir(ContexteSimulation contexte) {
        activite.agir(this, contexte);
    }

    @Override
    public void accepter(VisiteurEntite visiteur) {
        visiteur.visiter(this);
    }
}
