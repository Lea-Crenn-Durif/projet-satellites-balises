// Généré par ANTLR 4.13.2 à partir de SatellitesBalises.g4 : ne pas modifier à la main, regénérer.
package satellitesbalises.langage.analyse.genere;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class SatellitesBalisesParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, NOM=23, DECIMAL=24, 
		ENTIER=25, TEXTE=26, COMMENTAIRE=27, FIN_LIGNE=28, ESPACES=29;
	public static final int
		RULE_programme = 0, RULE_ligne = 1, RULE_instruction = 2, RULE_typeEntite = 3, 
		RULE_ordre = 4, RULE_blocProprietes = 5, RULE_propriete = 6, RULE_appel = 7, 
		RULE_valeur = 8, RULE_nombre = 9;
	private static String[] makeRuleNames() {
		return new String[] {
			"programme", "ligne", "instruction", "typeEntite", "ordre", "blocProprietes", 
			"propriete", "appel", "valeur", "nombre"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'en'", "'('", "','", "')'", "'supprimer'", "'configurer'", "'deplacement'", 
			"'remontee'", "'equiper'", "'desequiper'", "'apparence'", "'attendre'", 
			"'satellite'", "'balise'", "'arreter'", "'demarrer'", "'remonter'", "'{'", 
			"';'", "'}'", "'='", "'-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, "NOM", 
			"DECIMAL", "ENTIER", "TEXTE", "COMMENTAIRE", "FIN_LIGNE", "ESPACES"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "SatellitesBalises.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SatellitesBalisesParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgrammeContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(SatellitesBalisesParser.EOF, 0); }
		public List<LigneContext> ligne() {
			return getRuleContexts(LigneContext.class);
		}
		public LigneContext ligne(int i) {
			return getRuleContext(LigneContext.class,i);
		}
		public ProgrammeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programme; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitProgramme(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgrammeContext programme() throws RecognitionException {
		ProgrammeContext _localctx = new ProgrammeContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_programme);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(23);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 268697568L) != 0)) {
				{
				{
				setState(20);
				ligne();
				}
				}
				setState(25);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(26);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LigneContext extends ParserRuleContext {
		public TerminalNode FIN_LIGNE() { return getToken(SatellitesBalisesParser.FIN_LIGNE, 0); }
		public InstructionContext instruction() {
			return getRuleContext(InstructionContext.class,0);
		}
		public LigneContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ligne; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitLigne(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LigneContext ligne() throws RecognitionException {
		LigneContext _localctx = new LigneContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_ligne);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 262112L) != 0)) {
				{
				setState(28);
				instruction();
				}
			}

			setState(31);
			match(FIN_LIGNE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class InstructionContext extends ParserRuleContext {
		public InstructionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_instruction; }
	 
		public InstructionContext() { }
		public void copyFrom(InstructionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class CreationContext extends InstructionContext {
		public TypeEntiteContext typeEntite() {
			return getRuleContext(TypeEntiteContext.class,0);
		}
		public TerminalNode NOM() { return getToken(SatellitesBalisesParser.NOM, 0); }
		public List<NombreContext> nombre() {
			return getRuleContexts(NombreContext.class);
		}
		public NombreContext nombre(int i) {
			return getRuleContext(NombreContext.class,i);
		}
		public BlocProprietesContext blocProprietes() {
			return getRuleContext(BlocProprietesContext.class,0);
		}
		public CreationContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitCreation(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class SuppressionContext extends InstructionContext {
		public TerminalNode NOM() { return getToken(SatellitesBalisesParser.NOM, 0); }
		public SuppressionContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitSuppression(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ConfigurationContext extends InstructionContext {
		public TerminalNode NOM() { return getToken(SatellitesBalisesParser.NOM, 0); }
		public BlocProprietesContext blocProprietes() {
			return getRuleContext(BlocProprietesContext.class,0);
		}
		public ConfigurationContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitConfiguration(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ChoixDeplacementContext extends InstructionContext {
		public TerminalNode NOM() { return getToken(SatellitesBalisesParser.NOM, 0); }
		public AppelContext appel() {
			return getRuleContext(AppelContext.class,0);
		}
		public ChoixDeplacementContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitChoixDeplacement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ChoixRemonteeContext extends InstructionContext {
		public TerminalNode NOM() { return getToken(SatellitesBalisesParser.NOM, 0); }
		public AppelContext appel() {
			return getRuleContext(AppelContext.class,0);
		}
		public ChoixRemonteeContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitChoixRemontee(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class EquipementContext extends InstructionContext {
		public TerminalNode NOM() { return getToken(SatellitesBalisesParser.NOM, 0); }
		public List<AppelContext> appel() {
			return getRuleContexts(AppelContext.class);
		}
		public AppelContext appel(int i) {
			return getRuleContext(AppelContext.class,i);
		}
		public EquipementContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitEquipement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class DesequipementContext extends InstructionContext {
		public List<TerminalNode> NOM() { return getTokens(SatellitesBalisesParser.NOM); }
		public TerminalNode NOM(int i) {
			return getToken(SatellitesBalisesParser.NOM, i);
		}
		public DesequipementContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitDesequipement(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ActionContext extends InstructionContext {
		public OrdreContext ordre() {
			return getRuleContext(OrdreContext.class,0);
		}
		public TerminalNode NOM() { return getToken(SatellitesBalisesParser.NOM, 0); }
		public ActionContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitAction(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class ApparenceContext extends InstructionContext {
		public TerminalNode NOM() { return getToken(SatellitesBalisesParser.NOM, 0); }
		public BlocProprietesContext blocProprietes() {
			return getRuleContext(BlocProprietesContext.class,0);
		}
		public ApparenceContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitApparence(this);
			else return visitor.visitChildren(this);
		}
	}
	@SuppressWarnings("CheckReturnValue")
	public static class AttenteContext extends InstructionContext {
		public TerminalNode ENTIER() { return getToken(SatellitesBalisesParser.ENTIER, 0); }
		public AttenteContext(InstructionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitAttente(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InstructionContext instruction() throws RecognitionException {
		InstructionContext _localctx = new InstructionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_instruction);
		int _la;
		try {
			setState(76);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__12:
			case T__13:
				_localctx = new CreationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(33);
				typeEntite();
				setState(34);
				match(NOM);
				setState(35);
				match(T__0);
				setState(36);
				match(T__1);
				setState(37);
				nombre();
				setState(38);
				match(T__2);
				setState(39);
				nombre();
				setState(40);
				match(T__3);
				setState(42);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__17) {
					{
					setState(41);
					blocProprietes();
					}
				}

				}
				break;
			case T__4:
				_localctx = new SuppressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(44);
				match(T__4);
				setState(45);
				match(NOM);
				}
				break;
			case T__5:
				_localctx = new ConfigurationContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(46);
				match(T__5);
				setState(47);
				match(NOM);
				setState(48);
				blocProprietes();
				}
				break;
			case T__6:
				_localctx = new ChoixDeplacementContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(49);
				match(T__6);
				setState(50);
				match(NOM);
				setState(51);
				appel();
				}
				break;
			case T__7:
				_localctx = new ChoixRemonteeContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(52);
				match(T__7);
				setState(53);
				match(NOM);
				setState(54);
				appel();
				}
				break;
			case T__8:
				_localctx = new EquipementContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(55);
				match(T__8);
				setState(56);
				match(NOM);
				setState(57);
				appel();
				setState(62);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(58);
					match(T__2);
					setState(59);
					appel();
					}
					}
					setState(64);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case T__9:
				_localctx = new DesequipementContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(65);
				match(T__9);
				setState(66);
				match(NOM);
				setState(67);
				match(NOM);
				}
				break;
			case T__14:
			case T__15:
			case T__16:
				_localctx = new ActionContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(68);
				ordre();
				setState(69);
				match(NOM);
				}
				break;
			case T__10:
				_localctx = new ApparenceContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(71);
				match(T__10);
				setState(72);
				match(NOM);
				setState(73);
				blocProprietes();
				}
				break;
			case T__11:
				_localctx = new AttenteContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(74);
				match(T__11);
				setState(75);
				match(ENTIER);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeEntiteContext extends ParserRuleContext {
		public TypeEntiteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeEntite; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitTypeEntite(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeEntiteContext typeEntite() throws RecognitionException {
		TypeEntiteContext _localctx = new TypeEntiteContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_typeEntite);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(78);
			_la = _input.LA(1);
			if ( !(_la==T__12 || _la==T__13) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OrdreContext extends ParserRuleContext {
		public OrdreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ordre; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitOrdre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OrdreContext ordre() throws RecognitionException {
		OrdreContext _localctx = new OrdreContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_ordre);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 229376L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlocProprietesContext extends ParserRuleContext {
		public List<ProprieteContext> propriete() {
			return getRuleContexts(ProprieteContext.class);
		}
		public ProprieteContext propriete(int i) {
			return getRuleContext(ProprieteContext.class,i);
		}
		public BlocProprietesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blocProprietes; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitBlocProprietes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlocProprietesContext blocProprietes() throws RecognitionException {
		BlocProprietesContext _localctx = new BlocProprietesContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_blocProprietes);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			match(T__17);
			setState(91);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOM) {
				{
				setState(83);
				propriete();
				setState(88);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__18) {
					{
					{
					setState(84);
					match(T__18);
					setState(85);
					propriete();
					}
					}
					setState(90);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(93);
			match(T__19);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProprieteContext extends ParserRuleContext {
		public TerminalNode NOM() { return getToken(SatellitesBalisesParser.NOM, 0); }
		public ValeurContext valeur() {
			return getRuleContext(ValeurContext.class,0);
		}
		public ProprieteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propriete; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitPropriete(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProprieteContext propriete() throws RecognitionException {
		ProprieteContext _localctx = new ProprieteContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_propriete);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95);
			match(NOM);
			setState(96);
			match(T__20);
			setState(97);
			valeur();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AppelContext extends ParserRuleContext {
		public TerminalNode NOM() { return getToken(SatellitesBalisesParser.NOM, 0); }
		public List<ProprieteContext> propriete() {
			return getRuleContexts(ProprieteContext.class);
		}
		public ProprieteContext propriete(int i) {
			return getRuleContext(ProprieteContext.class,i);
		}
		public AppelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_appel; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitAppel(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AppelContext appel() throws RecognitionException {
		AppelContext _localctx = new AppelContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_appel);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			match(NOM);
			setState(100);
			match(T__1);
			setState(109);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOM) {
				{
				setState(101);
				propriete();
				setState(106);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(102);
					match(T__2);
					setState(103);
					propriete();
					}
					}
					setState(108);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(111);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ValeurContext extends ParserRuleContext {
		public NombreContext nombre() {
			return getRuleContext(NombreContext.class,0);
		}
		public TerminalNode TEXTE() { return getToken(SatellitesBalisesParser.TEXTE, 0); }
		public ValeurContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_valeur; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitValeur(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValeurContext valeur() throws RecognitionException {
		ValeurContext _localctx = new ValeurContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_valeur);
		try {
			setState(115);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__21:
			case DECIMAL:
			case ENTIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(113);
				nombre();
				}
				break;
			case TEXTE:
				enterOuterAlt(_localctx, 2);
				{
				setState(114);
				match(TEXTE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NombreContext extends ParserRuleContext {
		public TerminalNode ENTIER() { return getToken(SatellitesBalisesParser.ENTIER, 0); }
		public TerminalNode DECIMAL() { return getToken(SatellitesBalisesParser.DECIMAL, 0); }
		public NombreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nombre; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SatellitesBalisesVisitor ) return ((SatellitesBalisesVisitor<? extends T>)visitor).visitNombre(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NombreContext nombre() throws RecognitionException {
		NombreContext _localctx = new NombreContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_nombre);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__21) {
				{
				setState(117);
				match(T__21);
				}
			}

			setState(120);
			_la = _input.LA(1);
			if ( !(_la==DECIMAL || _la==ENTIER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u001d{\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0001\u0000\u0005\u0000\u0016\b\u0000\n\u0000"+
		"\f\u0000\u0019\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0003\u0001"+
		"\u001e\b\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0003\u0002+\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0005\u0002=\b\u0002\n\u0002\f\u0002@\t\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0003\u0002M\b\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0005\u0005W\b\u0005\n\u0005\f\u0005Z\t\u0005\u0003\u0005"+
		"\\\b\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0005\u0007i\b\u0007\n\u0007\f\u0007l\t\u0007\u0003\u0007n\b\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\b\u0001\b\u0003\bt\b\b\u0001\t\u0003\tw\b\t\u0001"+
		"\t\u0001\t\u0001\t\u0000\u0000\n\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0000\u0003\u0001\u0000\r\u000e\u0001\u0000\u000f\u0011\u0001\u0000"+
		"\u0018\u0019\u0083\u0000\u0017\u0001\u0000\u0000\u0000\u0002\u001d\u0001"+
		"\u0000\u0000\u0000\u0004L\u0001\u0000\u0000\u0000\u0006N\u0001\u0000\u0000"+
		"\u0000\bP\u0001\u0000\u0000\u0000\nR\u0001\u0000\u0000\u0000\f_\u0001"+
		"\u0000\u0000\u0000\u000ec\u0001\u0000\u0000\u0000\u0010s\u0001\u0000\u0000"+
		"\u0000\u0012v\u0001\u0000\u0000\u0000\u0014\u0016\u0003\u0002\u0001\u0000"+
		"\u0015\u0014\u0001\u0000\u0000\u0000\u0016\u0019\u0001\u0000\u0000\u0000"+
		"\u0017\u0015\u0001\u0000\u0000\u0000\u0017\u0018\u0001\u0000\u0000\u0000"+
		"\u0018\u001a\u0001\u0000\u0000\u0000\u0019\u0017\u0001\u0000\u0000\u0000"+
		"\u001a\u001b\u0005\u0000\u0000\u0001\u001b\u0001\u0001\u0000\u0000\u0000"+
		"\u001c\u001e\u0003\u0004\u0002\u0000\u001d\u001c\u0001\u0000\u0000\u0000"+
		"\u001d\u001e\u0001\u0000\u0000\u0000\u001e\u001f\u0001\u0000\u0000\u0000"+
		"\u001f \u0005\u001c\u0000\u0000 \u0003\u0001\u0000\u0000\u0000!\"\u0003"+
		"\u0006\u0003\u0000\"#\u0005\u0017\u0000\u0000#$\u0005\u0001\u0000\u0000"+
		"$%\u0005\u0002\u0000\u0000%&\u0003\u0012\t\u0000&\'\u0005\u0003\u0000"+
		"\u0000\'(\u0003\u0012\t\u0000(*\u0005\u0004\u0000\u0000)+\u0003\n\u0005"+
		"\u0000*)\u0001\u0000\u0000\u0000*+\u0001\u0000\u0000\u0000+M\u0001\u0000"+
		"\u0000\u0000,-\u0005\u0005\u0000\u0000-M\u0005\u0017\u0000\u0000./\u0005"+
		"\u0006\u0000\u0000/0\u0005\u0017\u0000\u00000M\u0003\n\u0005\u000012\u0005"+
		"\u0007\u0000\u000023\u0005\u0017\u0000\u00003M\u0003\u000e\u0007\u0000"+
		"45\u0005\b\u0000\u000056\u0005\u0017\u0000\u00006M\u0003\u000e\u0007\u0000"+
		"78\u0005\t\u0000\u000089\u0005\u0017\u0000\u00009>\u0003\u000e\u0007\u0000"+
		":;\u0005\u0003\u0000\u0000;=\u0003\u000e\u0007\u0000<:\u0001\u0000\u0000"+
		"\u0000=@\u0001\u0000\u0000\u0000><\u0001\u0000\u0000\u0000>?\u0001\u0000"+
		"\u0000\u0000?M\u0001\u0000\u0000\u0000@>\u0001\u0000\u0000\u0000AB\u0005"+
		"\n\u0000\u0000BC\u0005\u0017\u0000\u0000CM\u0005\u0017\u0000\u0000DE\u0003"+
		"\b\u0004\u0000EF\u0005\u0017\u0000\u0000FM\u0001\u0000\u0000\u0000GH\u0005"+
		"\u000b\u0000\u0000HI\u0005\u0017\u0000\u0000IM\u0003\n\u0005\u0000JK\u0005"+
		"\f\u0000\u0000KM\u0005\u0019\u0000\u0000L!\u0001\u0000\u0000\u0000L,\u0001"+
		"\u0000\u0000\u0000L.\u0001\u0000\u0000\u0000L1\u0001\u0000\u0000\u0000"+
		"L4\u0001\u0000\u0000\u0000L7\u0001\u0000\u0000\u0000LA\u0001\u0000\u0000"+
		"\u0000LD\u0001\u0000\u0000\u0000LG\u0001\u0000\u0000\u0000LJ\u0001\u0000"+
		"\u0000\u0000M\u0005\u0001\u0000\u0000\u0000NO\u0007\u0000\u0000\u0000"+
		"O\u0007\u0001\u0000\u0000\u0000PQ\u0007\u0001\u0000\u0000Q\t\u0001\u0000"+
		"\u0000\u0000R[\u0005\u0012\u0000\u0000SX\u0003\f\u0006\u0000TU\u0005\u0013"+
		"\u0000\u0000UW\u0003\f\u0006\u0000VT\u0001\u0000\u0000\u0000WZ\u0001\u0000"+
		"\u0000\u0000XV\u0001\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000Y\\\u0001"+
		"\u0000\u0000\u0000ZX\u0001\u0000\u0000\u0000[S\u0001\u0000\u0000\u0000"+
		"[\\\u0001\u0000\u0000\u0000\\]\u0001\u0000\u0000\u0000]^\u0005\u0014\u0000"+
		"\u0000^\u000b\u0001\u0000\u0000\u0000_`\u0005\u0017\u0000\u0000`a\u0005"+
		"\u0015\u0000\u0000ab\u0003\u0010\b\u0000b\r\u0001\u0000\u0000\u0000cd"+
		"\u0005\u0017\u0000\u0000dm\u0005\u0002\u0000\u0000ej\u0003\f\u0006\u0000"+
		"fg\u0005\u0003\u0000\u0000gi\u0003\f\u0006\u0000hf\u0001\u0000\u0000\u0000"+
		"il\u0001\u0000\u0000\u0000jh\u0001\u0000\u0000\u0000jk\u0001\u0000\u0000"+
		"\u0000kn\u0001\u0000\u0000\u0000lj\u0001\u0000\u0000\u0000me\u0001\u0000"+
		"\u0000\u0000mn\u0001\u0000\u0000\u0000no\u0001\u0000\u0000\u0000op\u0005"+
		"\u0004\u0000\u0000p\u000f\u0001\u0000\u0000\u0000qt\u0003\u0012\t\u0000"+
		"rt\u0005\u001a\u0000\u0000sq\u0001\u0000\u0000\u0000sr\u0001\u0000\u0000"+
		"\u0000t\u0011\u0001\u0000\u0000\u0000uw\u0005\u0016\u0000\u0000vu\u0001"+
		"\u0000\u0000\u0000vw\u0001\u0000\u0000\u0000wx\u0001\u0000\u0000\u0000"+
		"xy\u0007\u0002\u0000\u0000y\u0013\u0001\u0000\u0000\u0000\u000b\u0017"+
		"\u001d*>LX[jmsv";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}