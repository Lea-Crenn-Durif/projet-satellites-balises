package satellitesbalises.modele.creation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Associe un nom à une façon de créer un élément (EX-2.7, EX-2.8).
 * Exemple : « orbital » → crée un déplacement orbital avec la vitesse demandée.
 *
 * <p>Ajouter une variante, c'est enregistrer un nom de plus : aucune autre classe ne change.
 * Le langage de commandes (étape 7) passe par les catalogues pour trouver ce qu'on lui demande.</p>
 *
 * @param <T> le type des éléments créés (Deplacement, Instrument, ConditionRemontee…)
 */
public class Catalogue<T> {

    private final Map<String, FabriqueParametree<T>> fabriques = new LinkedHashMap<>();

    public void enregistrer(String nom, FabriqueParametree<T> fabrique) {
        fabriques.put(nom, fabrique);
    }

    /** @throws ErreurConfiguration si le nom est inconnu ; le message cite ce nom */
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
