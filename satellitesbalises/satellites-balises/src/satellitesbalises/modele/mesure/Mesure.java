package satellitesbalises.modele.mesure;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import satellitesbalises.modele.monde.Position;

/**
 * Une mesure faite par un instrument : qui l'a faite, à quel pas, où, et les valeurs obtenues.
 *
 * <p>Un courantomètre donne deux valeurs dans une même mesure (vitesse et direction du courant) ;
 * les autres instruments en donnent une (EX-4.3).</p>
 */
public final class Mesure implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nomInstrument;
    private final long pas;
    private final Position position;
    private final List<ValeurMesuree> valeurs;

    public Mesure(String nomInstrument, long pas, Position position, List<ValeurMesuree> valeurs) {
        this.nomInstrument = Objects.requireNonNull(nomInstrument, "nomInstrument");
        this.pas = pas;
        this.position = Objects.requireNonNull(position, "position");
        this.valeurs = List.copyOf(valeurs);
    }

    public String getNomInstrument() {
        return nomInstrument;
    }

    public long getPas() {
        return pas;
    }

    public Position getPosition() {
        return position;
    }

    public List<ValeurMesuree> getValeurs() {
        return valeurs;
    }

    /**
     * La valeur mesurée pour cette grandeur.
     *
     * @throws IllegalArgumentException si la mesure ne contient pas cette grandeur
     */
    public double valeurDe(Grandeur grandeur) {
        for (ValeurMesuree valeur : valeurs) {
            if (valeur.getGrandeur().equals(grandeur)) {
                return valeur.getValeur();
            }
        }
        throw new IllegalArgumentException("La mesure " + nomInstrument + " ne contient pas « "
                + grandeur.getNom() + " »");
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof Mesure)) {
            return false;
        }
        Mesure autre = (Mesure) objet;
        return pas == autre.pas && nomInstrument.equals(autre.nomInstrument)
                && position.equals(autre.position) && valeurs.equals(autre.valeurs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomInstrument, pas, position, valeurs);
    }

    @Override
    public String toString() {
        return nomInstrument + " au pas " + pas + " en " + position + " : " + valeurs;
    }
}
