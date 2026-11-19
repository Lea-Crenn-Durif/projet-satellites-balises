package satellitesbalises.modele.creation;

/**
 * Une configuration impossible : entité mal placée, paramètre invalide, nom inconnu ou déjà pris.
 * Le message cite toujours l'entité ou le nom concerné et ce qui ne va pas.
 */
public class ErreurConfiguration extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ErreurConfiguration(String message) {
        super(message);
    }
}
