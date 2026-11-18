package satellitesbalises.modele.creation;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;


public final class Parametres implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, Double> valeurs;

    public Parametres(Map<String, Double> valeurs) {
        this.valeurs = Map.copyOf(valeurs);
    }

    public Map<String, Double> getValeurs() {
        return valeurs;
    }

    /** La valeur du paramètre, ou {@code defaut} s'il n'a pas été donné. */
    public double nombre(String nom, double defaut) {
        return valeurs.getOrDefault(nom, defaut);
    }

    /** Comme {@link #nombre}, arrondi à l'entier le plus proche. */
    public int entier(String nom, int defaut) {
        return valeurs.containsKey(nom) ? (int) Math.round(valeurs.get(nom)) : defaut;
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof Parametres)) {
            return false;
        }
        return valeurs.equals(((Parametres) objet).valeurs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valeurs);
    }

    @Override
    public String toString() {
        return valeurs.toString();
    }
}
