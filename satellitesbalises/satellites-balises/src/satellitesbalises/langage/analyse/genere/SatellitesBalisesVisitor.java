// Généré par ANTLR 4.13.2 à partir de SatellitesBalises.g4 : ne pas modifier à la main, regénérer.
package satellitesbalises.langage.analyse.genere;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SatellitesBalisesParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SatellitesBalisesVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SatellitesBalisesParser#programme}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramme(SatellitesBalisesParser.ProgrammeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SatellitesBalisesParser#ligne}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLigne(SatellitesBalisesParser.LigneContext ctx);
	/**
	 * Visit a parse tree produced by the {@code creation}
	 * labeled alternative in {@link SatellitesBalisesParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreation(SatellitesBalisesParser.CreationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code suppression}
	 * labeled alternative in {@link SatellitesBalisesParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuppression(SatellitesBalisesParser.SuppressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code configuration}
	 * labeled alternative in {@link SatellitesBalisesParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConfiguration(SatellitesBalisesParser.ConfigurationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code choixDeplacement}
	 * labeled alternative in {@link SatellitesBalisesParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChoixDeplacement(SatellitesBalisesParser.ChoixDeplacementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code choixRemontee}
	 * labeled alternative in {@link SatellitesBalisesParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitChoixRemontee(SatellitesBalisesParser.ChoixRemonteeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code equipement}
	 * labeled alternative in {@link SatellitesBalisesParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquipement(SatellitesBalisesParser.EquipementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code desequipement}
	 * labeled alternative in {@link SatellitesBalisesParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDesequipement(SatellitesBalisesParser.DesequipementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code action}
	 * labeled alternative in {@link SatellitesBalisesParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction(SatellitesBalisesParser.ActionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code apparence}
	 * labeled alternative in {@link SatellitesBalisesParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApparence(SatellitesBalisesParser.ApparenceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code attente}
	 * labeled alternative in {@link SatellitesBalisesParser#instruction}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttente(SatellitesBalisesParser.AttenteContext ctx);
	/**
	 * Visit a parse tree produced by {@link SatellitesBalisesParser#typeEntite}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeEntite(SatellitesBalisesParser.TypeEntiteContext ctx);
	/**
	 * Visit a parse tree produced by {@link SatellitesBalisesParser#ordre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrdre(SatellitesBalisesParser.OrdreContext ctx);
	/**
	 * Visit a parse tree produced by {@link SatellitesBalisesParser#blocProprietes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlocProprietes(SatellitesBalisesParser.BlocProprietesContext ctx);
	/**
	 * Visit a parse tree produced by {@link SatellitesBalisesParser#propriete}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropriete(SatellitesBalisesParser.ProprieteContext ctx);
	/**
	 * Visit a parse tree produced by {@link SatellitesBalisesParser#appel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAppel(SatellitesBalisesParser.AppelContext ctx);
	/**
	 * Visit a parse tree produced by {@link SatellitesBalisesParser#valeur}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValeur(SatellitesBalisesParser.ValeurContext ctx);
	/**
	 * Visit a parse tree produced by {@link SatellitesBalisesParser#nombre}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNombre(SatellitesBalisesParser.NombreContext ctx);
}