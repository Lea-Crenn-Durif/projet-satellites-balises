package satellitesbalises.langage.analyse;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.ParserRuleContext;

import satellitesbalises.langage.analyse.genere.SatellitesBalisesBaseVisitor;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.ActionContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.AppelContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.ApparenceContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.AttenteContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.BlocProprietesContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.ChoixDeplacementContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.ChoixRemonteeContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.ConfigurationContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.CreationContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.DesequipementContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.EquipementContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.NombreContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.ProgrammeContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.ProprieteContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.SuppressionContext;
import satellitesbalises.langage.analyse.genere.SatellitesBalisesParser.ValeurContext;
import satellitesbalises.langage.arbre.AppelComportement;
import satellitesbalises.langage.arbre.Aspect;
import satellitesbalises.langage.arbre.Instruction;
import satellitesbalises.langage.arbre.InstructionAgir;
import satellitesbalises.langage.arbre.InstructionApparence;
import satellitesbalises.langage.arbre.InstructionAttendre;
import satellitesbalises.langage.arbre.InstructionComportement;
import satellitesbalises.langage.arbre.InstructionConfigurer;
import satellitesbalises.langage.arbre.InstructionCreer;
import satellitesbalises.langage.arbre.InstructionDesequiper;
import satellitesbalises.langage.arbre.InstructionEquiper;
import satellitesbalises.langage.arbre.InstructionSupprimer;
import satellitesbalises.langage.arbre.Ordre;
import satellitesbalises.langage.arbre.Programme;
import satellitesbalises.langage.arbre.Propriete;
import satellitesbalises.langage.arbre.TypeEntite;
import satellitesbalises.langage.arbre.Valeur;
import satellitesbalises.langage.arbre.ValeurNombre;
import satellitesbalises.langage.arbre.ValeurTexte;

/**
 * Parcourt l'arbre syntaxique produit par ANTLR et construit l'arbre abstrait du programme.
 *
 * <p>C'est la seule classe qui connaît à la fois ANTLR et l'arbre abstrait : l'interpréteur,
 * lui, ne voit jamais ANTLR.</p>
 */
class ConstructeurArbre extends SatellitesBalisesBaseVisitor<Instruction> {

    Programme construire(ProgrammeContext programme) {
        return new Programme(programme.ligne().stream()
                .filter(ligne -> ligne.instruction() != null)
                .map(ligne -> visit(ligne.instruction()))
                .collect(Collectors.toList()));
    }

    @Override
    public Instruction visitCreation(CreationContext ctx) {
        TypeEntite type = ctx.typeEntite().getText().equals("satellite") ? TypeEntite.SATELLITE : TypeEntite.BALISE;
        return new InstructionCreer(ligne(ctx), type, ctx.NOM().getText(),
                nombre(ctx.nombre(0)), nombre(ctx.nombre(1)), proprietes(ctx.blocProprietes()));
    }

    @Override
    public Instruction visitSuppression(SuppressionContext ctx) {
        return new InstructionSupprimer(ligne(ctx), ctx.NOM().getText());
    }

    @Override
    public Instruction visitConfiguration(ConfigurationContext ctx) {
        return new InstructionConfigurer(ligne(ctx), ctx.NOM().getText(), proprietes(ctx.blocProprietes()));
    }

    @Override
    public Instruction visitChoixDeplacement(ChoixDeplacementContext ctx) {
        return new InstructionComportement(ligne(ctx), ctx.NOM().getText(), Aspect.DEPLACEMENT, appel(ctx.appel()));
    }

    @Override
    public Instruction visitChoixRemontee(ChoixRemonteeContext ctx) {
        return new InstructionComportement(ligne(ctx), ctx.NOM().getText(), Aspect.REMONTEE, appel(ctx.appel()));
    }

    @Override
    public Instruction visitEquipement(EquipementContext ctx) {
        List<AppelComportement> instruments = ctx.appel().stream().map(this::appel).collect(Collectors.toList());
        return new InstructionEquiper(ligne(ctx), ctx.NOM().getText(), instruments);
    }

    @Override
    public Instruction visitDesequipement(DesequipementContext ctx) {
        return new InstructionDesequiper(ligne(ctx), ctx.NOM(0).getText(), ctx.NOM(1).getText());
    }

    @Override
    public Instruction visitAction(ActionContext ctx) {
        Ordre ordre = Ordre.valueOf(ctx.ordre().getText().toUpperCase(Locale.ROOT));
        return new InstructionAgir(ligne(ctx), ctx.NOM().getText(), ordre);
    }

    @Override
    public Instruction visitApparence(ApparenceContext ctx) {
        return new InstructionApparence(ligne(ctx), ctx.NOM().getText(), proprietes(ctx.blocProprietes()));
    }

    @Override
    public Instruction visitAttente(AttenteContext ctx) {
        return new InstructionAttendre(ligne(ctx), Long.parseLong(ctx.ENTIER().getText()));
    }

    // ---------- Les morceaux qui reviennent ----------

    private static int ligne(ParserRuleContext ctx) {
        return ctx.getStart().getLine();
    }

    /** Un bloc absent vaut un bloc vide : les propriétés sont facultatives (EX-7.11). */
    private static List<Propriete> proprietes(BlocProprietesContext bloc) {
        if (bloc == null) {
            return List.of();
        }
        return bloc.propriete().stream().map(ConstructeurArbre::propriete).collect(Collectors.toList());
    }

    private AppelComportement appel(AppelContext ctx) {
        List<Propriete> parametres = ctx.propriete().stream()
                .map(ConstructeurArbre::propriete).collect(Collectors.toList());
        return new AppelComportement(ctx.NOM().getText(), parametres);
    }

    private static Propriete propriete(ProprieteContext ctx) {
        return new Propriete(ctx.NOM().getText(), valeur(ctx.valeur()));
    }

    private static Valeur valeur(ValeurContext ctx) {
        if (ctx.TEXTE() != null) {
            String texte = ctx.TEXTE().getText();
            return new ValeurTexte(texte.substring(1, texte.length() - 1));
        }
        return new ValeurNombre(nombre(ctx.nombre()));
    }

    private static double nombre(NombreContext ctx) {
        return Double.parseDouble(ctx.getText());
    }
}
