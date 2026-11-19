package satellitesbalises.langage.analyse;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import satellitesbalises.langage.analyse.genere.SatellitesBalisesLexer;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser;
import satellitesbalises.langage.arbre.Programme;

/**
 * Transforme le texte d'un programme en arbre abstrait (étape 7c, décision D10).
 *
 * <pre>
 * texte ──► lexer ──► parser ──► arbre syntaxique ANTLR ──► ConstructeurArbre ──► Programme
 * </pre>
 */
public class AnalyseurProgramme {

    /**
     * @throws ErreurSyntaxe si le texte n'est pas correct ; elle contient toutes les erreurs trouvées
     */
    public Programme analyser(String texte) {
        EcouteurErreursSyntaxe ecouteur = new EcouteurErreursSyntaxe();

        SatellitesBalisesLexer lexer = new SatellitesBalisesLexer(CharStreams.fromString(avecFinDeLigne(texte)));
        lexer.removeErrorListeners();
        lexer.addErrorListener(ecouteur);

        SatellitesBalisesParser parser = new SatellitesBalisesParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(ecouteur);

        SatellitesBalisesParser.ProgrammeContext arbreSyntaxique = parser.programme();
        if (ecouteur.aDesErreurs()) {
            throw new ErreurSyntaxe(ecouteur.getErreurs());
        }
        return new ConstructeurArbre().construire(arbreSyntaxique);
    }

    /** La grammaire termine chaque ligne par une fin de ligne, y compris la dernière. */
    private static String avecFinDeLigne(String texte) {
        return texte.endsWith("\n") ? texte : texte + "\n";
    }
}
