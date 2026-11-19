package satellitesbalises.modele.mesure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import satellitesbalises.modele.creation.ErreurConfiguration;

/**
 * La mémoire d'une balise (EX-4.5, EX-4.6) : les mesures en attente d'envoi, de la plus ancienne
 * à la plus récente, avec une capacité maximale.
 */
public class MemoireBalise implements Serializable {

    private static final long serialVersionUID = 1L;

    private int capacite;
    private final List<Mesure> mesures = new ArrayList<>();

    /** @throws IllegalArgumentException si la capacité est nulle ou négative */
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

    /** Les mesures, de la plus ancienne à la plus récente. La liste ne peut pas être modifiée. */
    public List<Mesure> getMesures() {
        return Collections.unmodifiableList(mesures);
    }

    /**
     * Range la mesure si la mémoire n'est pas pleine.
     *
     * @return faux si la mémoire était pleine : la mesure est alors perdue (EX-4.5)
     */
    public boolean stocker(Mesure mesure) {
        if (estPleine()) {
            return false;
        }
        mesures.add(mesure);
        return true;
    }

    /**
     * Retire et renvoie les {@code nombre} mesures les plus anciennes, ou toutes s'il y en a moins (EX-4.6).
     */
    public List<Mesure> ceder(int nombre) {
        List<Mesure> plusAnciennes = mesures.subList(0, Math.min(nombre, mesures.size()));
        List<Mesure> cedees = new ArrayList<>(plusAnciennes);
        plusAnciennes.clear();
        return cedees;
    }

    /**
     * Change la capacité (EX-6.12).
     *
     * @throws ErreurConfiguration si la nouvelle capacité est plus petite que le nombre de mesures déjà rangées ;
     *                             l'ancienne capacité est alors gardée
     */
    public void changerCapacite(int nouvelleCapacite) {
        if (nouvelleCapacite < mesures.size() || nouvelleCapacite <= 0) {
            throw new ErreurConfiguration("Capacité " + nouvelleCapacite + " refusée : la mémoire contient déjà "
                    + mesures.size() + " mesures");
        }
        this.capacite = nouvelleCapacite;
    }
}
