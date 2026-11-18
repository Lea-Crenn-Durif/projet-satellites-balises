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
import satellitesbalises.modele.evenement.EvenementActiviteModifiee;
import satellitesbalises.modele.evenement.EvenementMemoireModifiee;
import satellitesbalises.modele.mesure.Instrument;
import satellitesbalises.modele.mesure.MemoireBalise;
import satellitesbalises.modele.mesure.Mesure;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

public class Balise extends Entite {

    private static final long serialVersionUID = 1L;

    private final MemoireBalise memoire;
    private final List<Instrument> instruments;
    private ConditionRemontee conditionRemontee;
    private final double vitesseVerticale;
    private Activite activite = new ActiviteCollecte();
    private boolean remonteeDemandee;

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

    public void changerConditionRemontee(ConditionRemontee nouvelleCondition) {
        this.conditionRemontee = Objects.requireNonNull(nouvelleCondition, "conditionRemontee");
    }

    public double getVitesseVerticale() {
        return vitesseVerticale;
    }

    public Activite getActivite() {
        return activite;
    }

    public void changerActivite(Activite nouvelleActivite, ContexteSimulation contexte) {
        activite = Objects.requireNonNull(nouvelleActivite, "activite");
        remonteeDemandee = false;
        contexte.getEventHandler().send(new EvenementActiviteModifiee(this));
    }

    public void demanderRemontee() {
        remonteeDemandee = true;
    }

    public boolean doitRemonter(long dureeCollecte) {
        return remonteeDemandee || conditionRemontee.estRemplie(this, dureeCollecte);
    }

    public boolean estEnAttente() {
        return activite.accepteTransmission();
    }

    public void commencerTransmission(ContexteSimulation contexte) {
        activite.surTransfertDemarre(this, contexte);
    }

    public void terminerTransmission(boolean complet, ContexteSimulation contexte) {
        activite.surTransfertTermine(this, complet, contexte);
    }

    public List<Mesure> ceder(int nombre) {
        return memoire.ceder(nombre);
    }

    public void equiper(Instrument instrument) {
        instruments.add(Objects.requireNonNull(instrument, "instrument"));
    }

    public Optional<Instrument> desequiper(String nomInstrument) {
        Optional<Instrument> instrument = instruments.stream()
                .filter(candidat -> candidat.getNom().equals(nomInstrument))
                .findFirst();
        instrument.ifPresent(instruments::remove);
        return instrument;
    }

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

    @Override
    public void agir(ContexteSimulation contexte) {
        activite.agir(this, contexte);
    }

    @Override
    public void accepter(VisiteurEntite visiteur) {
        visiteur.visiter(this);
    }
}
