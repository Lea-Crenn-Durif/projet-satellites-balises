package satellitesbalises.modele.mesure;

import java.util.HashMap;
import java.util.Map;

import satellitesbalises.modele.creation.ErreurConfiguration;
import satellitesbalises.modele.monde.Position;

public class EnvironnementSimule implements Environnement {

    private static final long serialVersionUID = 1L;

    private final Map<Grandeur, ChampPhysique> champs = new HashMap<>();

    public static EnvironnementSimule parDefaut() {
        EnvironnementSimule ocean = new EnvironnementSimule();
        ocean.definirChamp(Grandeur.TEMPERATURE, new ChampPhysique(20, -0.05, 1));
        ocean.definirChamp(Grandeur.SALINITE, new ChampPhysique(35, 0.005, 0.5));
        ocean.definirChamp(Grandeur.TURBIDITE, new ChampPhysique(3, 0.01, 1));
        ocean.definirChamp(Grandeur.VITESSE_COURANT, new ChampPhysique(1, -0.002, 0.3));
        ocean.definirChamp(Grandeur.DIRECTION_COURANT, new ChampPhysique(180, 0, 90));
        return ocean;
    }

    public void definirChamp(Grandeur grandeur, ChampPhysique champ) {
        champs.put(grandeur, champ);
    }

    @Override
    public double valeur(Grandeur grandeur, Position position, long pas) {
        ChampPhysique champ = champs.get(grandeur);
        if (champ == null) {
            throw new ErreurConfiguration("Aucun champ défini pour la grandeur « " + grandeur.getNom() + " »");
        }
        return champ.valeur(position, pas);
    }
}
