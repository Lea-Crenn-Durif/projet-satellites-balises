package satellitesbalises.modele.creation;

public class ErreurConfiguration extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ErreurConfiguration(String message) {
        super(message);
    }
}
