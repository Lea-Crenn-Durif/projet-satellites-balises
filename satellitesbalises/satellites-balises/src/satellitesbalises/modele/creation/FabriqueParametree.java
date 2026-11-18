package satellitesbalises.modele.creation;

@FunctionalInterface
public interface FabriqueParametree<T> {

    T creer(Parametres parametres);
}
