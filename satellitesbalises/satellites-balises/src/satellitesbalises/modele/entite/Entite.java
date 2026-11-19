package satellitesbalises.modele.entite;

import java.io.Serializable;
import java.util.Objects;

import satellitesbalises.modele.creation.Parametres;
import satellitesbalises.modele.deplacement.Deplacement;
import satellitesbalises.modele.evenement.EvenementPositionModifiee;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.ContexteSimulation;

/**
 * Ce qu'ont en commun un satellite et une balise : un nom unique, une position,
 * une façon de se déplacer et une apparence.
 *
 * <p>Une entité ne connaît ni la vue ni Nicellipse. On la crée avec la fabrique
 * ({@code FabriqueEntites}), qui vérifie qu'elle est bien placée.</p>
 */
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

    /** Le nouveau déplacement s'appliquera au prochain pas (EX-3.9). */
    public void changerDeplacement(Deplacement nouveauDeplacement) {
        this.deplacement = Objects.requireNonNull(nouveauDeplacement, "deplacement");
    }

    /** Change l'apparence. C'est l'appelant qui annonce le changement (EX-2.9). */
    public void changerApparence(Apparence nouvelleApparence) {
        this.apparence = Objects.requireNonNull(nouvelleApparence, "apparence");
    }

    /** Avance d'un pas selon son déplacement (EX-3.9, EX-3.10). */
    public void seDeplacer(ContexteSimulation contexte) {
        placer(deplacement.prochainePosition(position, contexte), contexte);
    }

    /**
     * Met l'entité à cette position et l'annonce. Une position inchangée n'est pas annoncée (EX-3.4),
     * pour ne pas redessiner pour rien.
     */
    public void placer(Position nouvellePosition, ContexteSimulation contexte) {
        if (nouvellePosition.equals(position)) {
            return;
        }
        position = nouvellePosition;
        contexte.getEventHandler().send(new EvenementPositionModifiee(this));
    }

    /** Ce que fait l'entité pendant un pas de simulation. */
    public abstract void agir(ContexteSimulation contexte);

    /**
     * Change des propriétés par leur nom (EX-6.12). Tout ou rien : si une propriété est refusée,
     * aucune n'est changée.
     *
     * @throws satellitesbalises.modele.creation.ErreurConfiguration si une propriété est inconnue ou sa valeur refusée ;
     *                                                                 le message cite la propriété
     */
    public abstract void configurer(Parametres parametres);

    /** Appelle la méthode du visiteur qui correspond au type réel de l'entité. */
    public abstract void accepter(VisiteurEntite visiteur);

    @Override
    public String toString() {
        return nom + " en " + position;
    }
}
