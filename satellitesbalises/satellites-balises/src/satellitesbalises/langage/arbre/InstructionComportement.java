package satellitesbalises.langage.arbre;

import java.util.Objects;

/**
 * Choisit un comportement : {@code deplacement s1 orbital(vitesse = 2)} ou {@code remontee b1 duree(pas = 150)}.
 */
public final class InstructionComportement extends Instruction {

    private final String nom;
    private final Aspect aspect;
    private final AppelComportement appel;

    public InstructionComportement(int ligne, String nom, Aspect aspect, AppelComportement appel) {
        super(ligne);
        this.nom = Objects.requireNonNull(nom, "nom");
        this.aspect = Objects.requireNonNull(aspect, "aspect");
        this.appel = Objects.requireNonNull(appel, "appel");
    }

    public String getNom() {
        return nom;
    }

    /** Déplacement ou condition de remontée. */
    public Aspect getAspect() {
        return aspect;
    }

    public AppelComportement getAppel() {
        return appel;
    }

    @Override
    public <R> R accepter(VisiteurInstruction<R> visiteur) {
        return visiteur.visiter(this);
    }

    @Override
    public boolean equals(Object objet) {
        if (this == objet) {
            return true;
        }
        if (!(objet instanceof InstructionComportement)) {
            return false;
        }
        InstructionComportement autre = (InstructionComportement) objet;
        return getLigne() == autre.getLigne()
                && nom.equals(autre.nom)
                && aspect == autre.aspect
                && appel.equals(autre.appel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLigne(), nom, aspect, appel);
    }

    @Override
    public String toString() {
        return "InstructionComportement[ligne " + getLigne() + ", nom = " + nom + ", aspect = " + aspect
                + ", appel = " + appel + "]";
    }
}
