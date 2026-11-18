package satellitesbalises.modele.mesure;

import java.io.Serializable;

import satellitesbalises.modele.monde.Position;


@FunctionalInterface
public interface Environnement extends Serializable {

    double valeur(Grandeur grandeur, Position position, long pas);
}
