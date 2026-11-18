package satellitesbalises.modele.mesure;

import java.util.List;

import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/** Mesure le courant marin : sa vitesse et sa direction, dans une même mesure. */
public class InstrumentCourantometre implements Instrument {

    private static final long serialVersionUID = 1L;

    private static final List<Grandeur> GRANDEURS = List.of(Grandeur.VITESSE_COURANT, Grandeur.DIRECTION_COURANT);

    @Override
    public String getNom() {
        return "courantometre";
    }

    @Override
    public List<Grandeur> getGrandeurs() {
        return GRANDEURS;
    }

    @Override
    public Mesure mesurer(Position position, ContexteSimulation contexte) {
        Environnement environnement = contexte.getEnvironnement();
        long pas = contexte.getPas();
        List<ValeurMesuree> valeurs = List.of(
                new ValeurMesuree(Grandeur.VITESSE_COURANT, environnement.valeur(Grandeur.VITESSE_COURANT, position, pas)),
                new ValeurMesuree(Grandeur.DIRECTION_COURANT, environnement.valeur(Grandeur.DIRECTION_COURANT, position, pas)));
        return new Mesure(getNom(), pas, position, valeurs);
    }
}
