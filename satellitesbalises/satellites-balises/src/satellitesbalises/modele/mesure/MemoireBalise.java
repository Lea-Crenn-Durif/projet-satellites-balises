package satellitesbalises.modele.mesure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MemoireBalise implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int capacite;
    private final List<Mesure> mesures = new ArrayList<>();

    public MemoireBalise(int capacite) {
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
}
