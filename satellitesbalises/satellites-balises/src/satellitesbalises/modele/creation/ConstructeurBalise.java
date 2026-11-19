package satellitesbalises.modele.creation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import satellitesbalises.modele.activite.ConditionMemoirePleine;
import satellitesbalises.modele.activite.ConditionRemontee;
import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.deplacement.DeplacementImmobile;
import satellitesbalises.modele.entite.Apparence;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.mesure.Instrument;
import satellitesbalises.modele.mesure.MemoireBalise;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.monde.Zone;

/**
 * Monteur de balise (EX-2.2, EX-2.3, EX-2.3b, EX-2.3c) : on règle ce qu'on veut, dans l'ordre
 * qu'on veut, puis {@link #construire()} vérifie tout et crée la balise.
 *
 * <p>Seule la position est obligatoire. Par défaut : immobile, mémoire de 50 mesures, remontée quand
 * la mémoire est pleine, vitesse verticale 1, aucun instrument, apparence par défaut.</p>
 */
public class ConstructeurBalise {

    private final String nom;
    private final FabriqueEntites fabrique;
    private Position position;
    private Deplacement deplacement = new DeplacementImmobile();
    private int capaciteMemoire = 50;
    private ConditionRemontee conditionRemontee = new ConditionMemoirePleine();
    private double vitesseVerticale = 1;
    private final List<Instrument> instruments = new ArrayList<>();
    private Apparence apparence = Apparence.parDefaut();

    ConstructeurBalise(String nom, FabriqueEntites fabrique) {
        this.nom = nom;
        this.fabrique = fabrique;
    }

    public ConstructeurBalise en(Position position) {
        this.position = position;
        return this;
    }

    public ConstructeurBalise avecDeplacement(Deplacement deplacement) {
        this.deplacement = deplacement;
        return this;
    }

    public ConstructeurBalise avecMemoire(int capacite) {
        this.capaciteMemoire = capacite;
        return this;
    }

    public ConstructeurBalise remontantQuand(ConditionRemontee condition) {
        this.conditionRemontee = condition;
        return this;
    }

    public ConstructeurBalise avecVitesseVerticale(double vitesse) {
        this.vitesseVerticale = vitesse;
        return this;
    }

    public ConstructeurBalise equipeeDe(Instrument... nouveauxInstruments) {
        instruments.addAll(Arrays.asList(nouveauxInstruments));
        return this;
    }

    public ConstructeurBalise avecApparence(Apparence apparence) {
        this.apparence = apparence;
        return this;
    }

    /**
     * @throws ErreurConfiguration si la position manque, n'est pas dans la mer (la surface compte),
     *                             ou si la capacité mémoire ou la vitesse verticale sont ≤ 0
     */
    public Balise construire() {
        String description = "Balise " + nom;
        fabrique.verifierPlacement(description, position, Zone.MER);
        FabriqueEntites.verifierStrictementPositif(description, "la capacité mémoire", capaciteMemoire);
        FabriqueEntites.verifierStrictementPositif(description, "la vitesse verticale", vitesseVerticale);
        return new Balise(nom, position, deplacement, apparence, new MemoireBalise(capaciteMemoire),
                instruments, conditionRemontee, vitesseVerticale);
    }
}
