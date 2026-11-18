package satellitesbalises.modele.entite;

/**
 * Fait quelque chose de différent pour un satellite et pour une balise, sans {@code instanceof}
 * (patron Visiteur, décision D7). Exemple : la vue crée le bon dessin pour chaque entité.
 */
public interface VisiteurEntite {

    void visiter(Satellite satellite);

    void visiter(Balise balise);
}
