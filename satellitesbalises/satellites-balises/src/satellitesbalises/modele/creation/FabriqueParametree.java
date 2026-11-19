package satellitesbalises.modele.creation;

/** Crée un élément à partir de ses paramètres. Exemple : {@code p -> new DeplacementOrbital(p.nombre("vitesse", 1))}. */
@FunctionalInterface
public interface FabriqueParametree<T> {

    T creer(Parametres parametres);
}
