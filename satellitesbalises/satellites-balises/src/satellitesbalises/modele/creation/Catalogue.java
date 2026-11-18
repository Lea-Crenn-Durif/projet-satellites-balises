package satellitesbalises.modele.creation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Catalogue<T> {

    private final Map<String, FabriqueParametree<T>> fabriques = new LinkedHashMap<>();

    public void enregistrer(String nom, FabriqueParametree<T> fabrique) {
        fabriques.put(nom, fabrique);
    }

    public T creer(String nom, Parametres parametres) {
        FabriqueParametree<T> fabrique = fabriques.get(nom);
        if (fabrique == null) {
            throw new ErreurConfiguration("Nom inconnu : « " + nom + " » (connus : " + fabriques.keySet() + ")");
        }
        return fabrique.creer(parametres);
    }

    public boolean contient(String nom) {
        return fabriques.containsKey(nom);
    }

    public Set<String> getNoms() {
        return Collections.unmodifiableSet(fabriques.keySet());
    }
}
