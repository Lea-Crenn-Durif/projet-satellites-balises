package satellitesbalises.modele.mesure;

import java.io.Serializable;

import satellitesbalises.modele.monde.Position;

/**
 * L'océan simulé : donne la valeur d'une grandeur en un lieu et à un pas donnés (décision D8).
 *
 * <p>Les instruments interrogent l'environnement au lieu d'inventer leurs valeurs : deux balises
 * au même endroit, au même pas, mesurent donc la même chose. Version minimale (étape 1) :
 * l'implémentation simulée arrive à l'étape 4.</p>
 */
@FunctionalInterface
public interface Environnement extends Serializable {

    double valeur(Grandeur grandeur, Position position, long pas);
}
