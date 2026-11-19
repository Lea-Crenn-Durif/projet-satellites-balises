package satellitesbalises.langage.analyse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.misc.IntervalSet;

import satellitesbalises.langage.analyse.genere.SatellitesBalisesLexer;
import satellitesbalises.langage.arbre.ErreurProgramme;

/**
 * Recueille les erreurs du lexer et du parser, avec un message en français (EX-7.5),
 * au lieu de les afficher dans la console comme ANTLR le fait par défaut.
 */
class EcouteurErreursSyntaxe extends BaseErrorListener {

    /** Au-delà, on résume : « une instruction » plutôt que la liste de tous les mots-clés. */
    private static final int ATTENDUS_DETAILLES_MAX = 4;

    private final List<ErreurProgramme> erreurs = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> source, Object symboleFautif, int ligne, int positionDansLaLigne,
                            String messageAntlr, RecognitionException exception) {
        String message = source instanceof Lexer
                ? "caractère inattendu " + extraitApres(messageAntlr, "at: ")
                : messageDuParser((Parser) source, (Token) symboleFautif);
        erreurs.add(new ErreurProgramme(ligne, positionDansLaLigne + 1, message));
    }

    boolean aDesErreurs() {
        return !erreurs.isEmpty();
    }

    List<ErreurProgramme> getErreurs() {
        return erreurs;
    }

    private static String messageDuParser(Parser parser, Token trouve) {
        return attendus(parser.getExpectedTokens(), parser.getVocabulary()) + ", trouvé : " + decrire(trouve);
    }

    private static String attendus(IntervalSet types, Vocabulary vocabulaire) {
        List<Integer> liste = types.toList();
        if (liste.size() > ATTENDUS_DETAILLES_MAX) {
            return "on attendait une instruction (satellite, balise, deplacement, equiper, attendre…)";
        }
        return "on attendait " + liste.stream()
                .map(type -> decrire(type, vocabulaire))
                .collect(Collectors.joining(" ou "));
    }

    private static String decrire(Token token) {
        switch (token.getType()) {
            case Token.EOF:
                return "la fin du programme";
            case SatellitesBalisesLexer.FIN_LIGNE:
                return "la fin de la ligne";
            default:
                return "« " + token.getText() + " »";
        }
    }

    private static String decrire(int type, Vocabulary vocabulaire) {
        switch (type) {
            case Token.EOF:
                return "la fin du programme";
            case SatellitesBalisesLexer.FIN_LIGNE:
                return "une fin de ligne";
            case SatellitesBalisesLexer.NOM:
                return "un nom";
            case SatellitesBalisesLexer.ENTIER:
                return "un nombre entier";
            case SatellitesBalisesLexer.DECIMAL:
                return "un nombre décimal";
            case SatellitesBalisesLexer.TEXTE:
                return "un texte entre guillemets";
            default:
                return "« " + vocabulaire.getLiteralName(type).replace("'", "") + " »";
        }
    }

    private static String extraitApres(String texte, String repere) {
        int position = texte.indexOf(repere);
        return position < 0 ? texte : "« " + texte.substring(position + repere.length()).replace("'", "") + " »";
    }
}
