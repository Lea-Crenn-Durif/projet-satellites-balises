package satellitesbalises.langage.analyse;

import java.util.List;
import java.util.stream.Collectors;

import satellitesbalises.langage.arbre.ErreurProgramme;

/**
 * Le texte n'est pas un programme correct (EX-7.5). Contient toutes les erreurs trouvées,
 * chacune avec sa ligne et sa colonne.
 */
public class ErreurSyntaxe extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final transient List<ErreurProgramme> erreurs;

    public ErreurSyntaxe(List<ErreurProgramme> erreurs) {
        super(erreurs.stream().map(ErreurProgramme::toString).collect(Collectors.joining("\n")));
        this.erreurs = List.copyOf(erreurs);
    }

    public List<ErreurProgramme> getErreurs() {
        return erreurs;
    }
}
