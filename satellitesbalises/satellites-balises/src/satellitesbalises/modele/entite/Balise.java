package satellitesbalises.modele.entite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import satellitesbalises.modele.activite.Activite;
import satellitesbalises.modele.activite.ActiviteCollecte;
import satellitesbalises.modele.activite.ConditionRemontee;
import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.mesure.Instrument;
import satellitesbalises.modele.mesure.MemoireBalise;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

public class Balise extends Entite {

    private static final long serialVersionUID = 1L;

    private final MemoireBalise memoire;
    private final List<Instrument> instruments;
    private final ConditionRemontee conditionRemontee;
    private final double vitesseVerticale;
    private final Activite activite = new ActiviteCollecte();

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

    public double getVitesseVerticale() {
        return vitesseVerticale;
    }

    public Activite getActivite() {
        return activite;
    }

    @Override
    public void agir(ContexteSimulation contexte) {
        seDeplacer(contexte);
    }

    @Override
    public void accepter(VisiteurEntite visiteur) {
        visiteur.visiter(this);
    }
}
