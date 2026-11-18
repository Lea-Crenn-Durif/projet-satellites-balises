package satellitesbalises.modele.mesure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import satellitesbalises.modele.creation.ErreurConfiguration;

public class MemoireBalise implements Serializable {

    private static final long serialVersionUID = 1L;

    private int capacite;
    private final List<Mesure> mesures = new ArrayList<>();

    public MemoireBalise(int capacite) {
        if (capacite <= 0) {
            throw new IllegalArgumentException("La capacité doit être strictement positive : " + capacite);
        }
        this.capacite = capacite;
    }

    public int getCapacite() {
        return capacite;
    }

    public int getTaille() {
        return mesures.size();
    }

    public boolean estVide() {
        return mesures.isEmpty();
    }

    public boolean estPleine() {
        return mesures.size() >= capacite;
    }

     public List<Mesure> getMesures() {
        return Collections.unmodifiableList(mesures);
    }

    public boolean stocker(Mesure mesure) {
        if (estPleine()) {
            return false;
        }
        mesures.add(mesure);
        return true;
    }

    public List<Mesure> ceder(int nombre) {
        List<Mesure> plusAnciennes = mesures.subList(0, Math.min(nombre, mesures.size()));
        List<Mesure> cedees = new ArrayList<>(plusAnciennes);
        plusAnciennes.clear();
        return cedees;
    }

    public void changerCapacite(int nouvelleCapacite) {
        if (nouvelleCapacite < mesures.size() || nouvelleCapacite <= 0) {
            throw new ErreurConfiguration("Capacité " + nouvelleCapacite + " refusée : la mémoire contient déjà "
                    + mesures.size() + " mesures");
        }
        this.capacite = nouvelleCapacite;
    }
}
