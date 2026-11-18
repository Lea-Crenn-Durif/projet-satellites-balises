package satellitesbalises.modele.entite;

import java.io.Serializable;
import java.util.Objects;

import satellitesbalises.modele.creation.Parametres;
import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.evenement.EvenementPositionModifiee;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

public abstract class Entite implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nom;
    private Position position;
    private Deplacement deplacement;
    private Apparence apparence;

    protected Entite(String nom, Position position, Deplacement deplacement, Apparence apparence) {
        this.nom = Objects.requireNonNull(nom, "nom");
        this.position = Objects.requireNonNull(position, "position");
        this.deplacement = Objects.requireNonNull(deplacement, "deplacement");
        this.apparence = Objects.requireNonNull(apparence, "apparence");
    }

    public String getNom() {
        return nom;
    }

    public Position getPosition() {
        return position;
    }

    public Deplacement getDeplacement() {
        return deplacement;
    }

    public Apparence getApparence() {
        return apparence;
    }

    public void changerDeplacement(Deplacement nouveauDeplacement) {
        this.deplacement = Objects.requireNonNull(nouveauDeplacement, "deplacement");
    }

    public void changerApparence(Apparence nouvelleApparence) {
        this.apparence = Objects.requireNonNull(nouvelleApparence, "apparence");
    }

    public void seDeplacer(ContexteSimulation contexte) {
        placer(deplacement.prochainePosition(position, contexte), contexte);
    }

    public void placer(Position nouvellePosition, ContexteSimulation contexte) {
        if (nouvellePosition.equals(position)) {
            return;
        }
        position = nouvellePosition;
        contexte.getEventHandler().send(new EvenementPositionModifiee(this));
    }

    public abstract void agir(ContexteSimulation contexte);

    public abstract void configurer(Parametres parametres);

    public abstract void accepter(VisiteurEntite visiteur);

    @Override
    public String toString() {
        return nom + " en " + position;
    }
}
