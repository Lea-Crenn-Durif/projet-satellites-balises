package satellitesbalises.modele.entite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import satellitesbalises.modele.activite.Activite;
import satellitesbalises.modele.activite.ActiviteCollecte;
import satellitesbalises.modele.activite.ConditionRemontee;
import satellitesbalises.modele.deplacement.Deplacement;
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

    private final MemoireBalise memoire;
    private final List<Instrument> instruments;
    private final ConditionRemontee conditionRemontee;
    private final double vitesseVerticale;
    private final Activite activite = new ActiviteCollecte();

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

    /** De combien la balise monte ou descend par pas pendant la remontée et la descente. */
    public double getVitesseVerticale() {
        return vitesseVerticale;
    }

    /** Ce que fait la balise en ce moment. Toute balise commence en Collecte. */
    public Activite getActivite() {
        return activite;
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
