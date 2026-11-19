package satellitesbalises.langage.interpretation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import satellitesbalises.langage.arbre.AppelComportement;
import satellitesbalises.langage.arbre.ErreurProgramme;
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
import satellitesbalises.langage.arbre.VisiteurInstruction;
import satellitesbalises.modele.commande.CommandeAjouterBalise;
import satellitesbalises.modele.commande.CommandeAjouterSatellite;
import satellitesbalises.modele.commande.CommandeChangerApparence;
import satellitesbalises.modele.commande.CommandeChangerConditionRemontee;
import satellitesbalises.modele.commande.CommandeChangerDeplacement;
import satellitesbalises.modele.commande.CommandeChangerStatut;
import satellitesbalises.modele.commande.CommandeConfigurer;
import satellitesbalises.modele.commande.CommandeDesequiper;
import satellitesbalises.modele.commande.CommandeEquiper;
import satellitesbalises.modele.commande.CommandeForcerRemontee;
import satellitesbalises.modele.commande.CommandeSimulation;
import satellitesbalises.modele.commande.CommandeSupprimer;
import satellitesbalises.modele.creation.Catalogue;
import satellitesbalises.modele.creation.Catalogues;
import satellitesbalises.modele.creation.ErreurConfiguration;
import satellitesbalises.modele.creation.FabriqueEntites;
import satellitesbalises.modele.creation.Parametres;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.monde.Position;
import satellitesbalises.modele.simulation.CommandePlanifiee;
import satellitesbalises.modele.simulation.Simulation;

/**
 * Transforme un programme en commandes datées, puis les planifie dans la simulation (décision D10).
 *
 * <p>Trois règles :</p>
 * <ul>
 *   <li><b>Dates</b> (EX-7.4) : la première commande est planifiée au pas courant + 1 ;
 *       « attendre n » décale de n pas toutes les commandes qui suivent.</li>
 *   <li><b>Vérifications</b> (EX-7.6, EX-7.8, EX-7.11) : chaque nom d'entité, de comportement et de
 *       propriété est vérifié avant de créer la commande ; chaque erreur garde sa ligne.</li>
 *   <li><b>Tout ou rien</b> (EX-7.7) : si le programme contient une erreur, aucune commande n'est
 *       planifiée, et toutes les erreurs sont rapportées d'un coup.</li>
 * </ul>
 *
 * <p>Les noms de déplacements, d'instruments et de conditions sont cherchés dans les catalogues du
 * modèle : en ajouter un ne demande pas de toucher au langage (EX-7.9).</p>
 */
public class InterpreteurProgramme implements VisiteurInstruction<Void> {

    private final FabriqueEntites fabrique;
    private final Catalogues catalogues;

    // État d'une interprétation, remis à zéro par interpreter().
    private TableDesNoms table;
    private List<CommandePlanifiee> commandes;
    private List<ErreurProgramme> erreurs;
    private long pasVise;

    public InterpreteurProgramme(FabriqueEntites fabrique, Catalogues catalogues) {
        this.fabrique = Objects.requireNonNull(fabrique, "fabrique");
        this.catalogues = Objects.requireNonNull(catalogues, "catalogues");
    }

    /** Vérifie tout le programme, puis planifie ses commandes s'il ne contient aucune erreur. */
    public RapportExecution interpreter(Programme programme, Simulation simulation) {
        table = TableDesNoms.depuis(simulation.getMonde());
        commandes = new ArrayList<>();
        erreurs = new ArrayList<>();
        pasVise = simulation.getPasCourant() + 1;

        for (Instruction instruction : programme.getInstructions()) {
            instruction.accepter(this);
        }
        if (!erreurs.isEmpty()) {
            return new RapportExecution(0, erreurs);
        }
        for (CommandePlanifiee commande : commandes) {
            simulation.planifier(commande.getCommande(), commande.getPas());
        }
        return new RapportExecution(commandes.size(), List.of());
    }

    // ---------- Une méthode par instruction ----------

    @Override
    public Void visiter(InstructionCreer instruction) {
        return enRelevantLesErreurs(instruction, () -> {
            String nom = instruction.getNom();
            if (table.typeDe(nom).isPresent()) {
                throw new ErreurDeSens("le nom « " + nom + " » est déjà pris");
            }
            Parametres proprietes = enParametres(instruction.getProprietes());
            Position position = new Position(instruction.getX(), instruction.getY());
            if (instruction.getType() == TypeEntite.SATELLITE) {
                Satellite satellite = fabrique.nouveauSatellite(nom).en(position).construire();
                satellite.configurer(proprietes);
                ajouter(new CommandeAjouterSatellite(satellite));
            } else {
                Balise balise = fabrique.nouvelleBalise(nom).en(position).construire();
                balise.configurer(proprietes);
                ajouter(new CommandeAjouterBalise(balise));
            }
            table.declarer(nom, instruction.getType());
        });
    }

    @Override
    public Void visiter(InstructionSupprimer instruction) {
        return enRelevantLesErreurs(instruction, () -> {
            exigerEntite(instruction.getNom());
            ajouter(new CommandeSupprimer(instruction.getNom()));
            table.oublier(instruction.getNom());
        });
    }

    @Override
    public Void visiter(InstructionConfigurer instruction) {
        return enRelevantLesErreurs(instruction, () -> {
            String nom = instruction.getNom();
            TypeEntite type = exigerEntite(nom);
            Parametres parametres = enParametres(instruction.getProprietes());
            for (String propriete : parametres.getValeurs().keySet()) {
                if (!proprietesConfigurables(type).contains(propriete)) {
                    throw new ErreurDeSens("propriété inconnue « " + propriete + " » pour " + nom
                            + " (connues : " + proprietesConfigurables(type) + ")");
                }
            }
            ajouter(new CommandeConfigurer(nom, parametres));
        });
    }

    @Override
    public Void visiter(InstructionComportement instruction) {
        return enRelevantLesErreurs(instruction, () -> {
            String nom = instruction.getNom();
            AppelComportement appel = instruction.getAppel();
            switch (instruction.getAspect()) {
                case DEPLACEMENT:
                    exigerEntite(nom);
                    ajouter(new CommandeChangerDeplacement(nom, creer(catalogues.getDeplacements(), appel)));
                    break;
                case REMONTEE:
                    exigerType(nom, TypeEntite.BALISE, "remontee");
                    ajouter(new CommandeChangerConditionRemontee(nom, creer(catalogues.getConditionsRemontee(), appel)));
                    break;
                default:
                    throw new IllegalStateException("Aspect non traité : " + instruction.getAspect());
            }
        });
    }

    @Override
    public Void visiter(InstructionEquiper instruction) {
        return enRelevantLesErreurs(instruction, () -> {
            exigerType(instruction.getNom(), TypeEntite.BALISE, "equiper");
            for (AppelComportement appel : instruction.getInstruments()) {
                ajouter(new CommandeEquiper(instruction.getNom(), creer(catalogues.getInstruments(), appel)));
            }
        });
    }

    @Override
    public Void visiter(InstructionDesequiper instruction) {
        return enRelevantLesErreurs(instruction, () -> {
            exigerType(instruction.getNom(), TypeEntite.BALISE, "desequiper");
            ajouter(new CommandeDesequiper(instruction.getNom(), instruction.getNomInstrument()));
        });
    }

    @Override
    public Void visiter(InstructionAgir instruction) {
        return enRelevantLesErreurs(instruction, () -> {
            String nom = instruction.getNom();
            Ordre ordre = instruction.getOrdre();
            if (ordre == Ordre.REMONTER) {
                exigerType(nom, TypeEntite.BALISE, "remonter");
                ajouter(new CommandeForcerRemontee(nom));
            } else {
                exigerType(nom, TypeEntite.SATELLITE, ordre == Ordre.ARRETER ? "arreter" : "demarrer");
                ajouter(new CommandeChangerStatut(nom, ordre == Ordre.DEMARRER));
            }
        });
    }

    @Override
    public Void visiter(InstructionApparence instruction) {
        return enRelevantLesErreurs(instruction, () -> {
            exigerEntite(instruction.getNom());
            String couleur = null;
            Integer taille = null;
            for (Propriete propriete : instruction.getProprietes()) {
                switch (propriete.getNom()) {
                    case "couleur":
                        couleur = texte(propriete);
                        break;
                    case "taille":
                        taille = (int) Math.round(nombre(propriete));
                        break;
                    default:
                        throw new ErreurDeSens("propriété d'apparence inconnue « " + propriete.getNom()
                                + " » (connues : couleur, taille)");
                }
            }
            ajouter(new CommandeChangerApparence(instruction.getNom(), couleur, taille));
        });
    }

    @Override
    public Void visiter(InstructionAttendre instruction) {
        return enRelevantLesErreurs(instruction, () -> {
            if (instruction.getNombreDePas() < 0) {
                throw new ErreurDeSens("on ne peut pas attendre un nombre de pas négatif");
            }
            pasVise += instruction.getNombreDePas();
        });
    }

    // ---------- Outils ----------

    /** Exécute la vérification ; une erreur est notée avec la ligne de l'instruction, et on passe à la suivante. */
    private Void enRelevantLesErreurs(Instruction instruction, Runnable verification) {
        try {
            verification.run();
        } catch (ErreurDeSens | ErreurConfiguration erreur) {
            erreurs.add(new ErreurProgramme(instruction.getLigne(), 0, erreur.getMessage()));
        }
        return null;
    }

    private void ajouter(CommandeSimulation commande) {
        commandes.add(new CommandePlanifiee(pasVise, commandes.size(), commande));
    }

    private TypeEntite exigerEntite(String nom) {
        return table.typeDe(nom).orElseThrow(() -> new ErreurDeSens("entité inconnue « " + nom + " »"));
    }

    private void exigerType(String nom, TypeEntite typeAttendu, String motCle) {
        if (exigerEntite(nom) != typeAttendu) {
            String genre = typeAttendu == TypeEntite.SATELLITE ? "aux satellites" : "aux balises";
            throw new ErreurDeSens("« " + motCle + " » ne s'applique qu'" + genre + ", pas à « " + nom + " »");
        }
    }

    private static Set<String> proprietesConfigurables(TypeEntite type) {
        return type == TypeEntite.SATELLITE ? Satellite.PROPRIETES : Balise.PROPRIETES;
    }

    /** Crée l'élément nommé par l'appel, avec ses paramètres. Nom inconnu : ErreurConfiguration qui le cite. */
    private <T> T creer(Catalogue<T> catalogue, AppelComportement appel) {
        return catalogue.creer(appel.getNom(), enParametres(appel.getParametres()));
    }

    /** Des propriétés numériques, transformées en paramètres pour le modèle. */
    private static Parametres enParametres(List<Propriete> proprietes) {
        Map<String, Double> valeurs = new HashMap<>();
        for (Propriete propriete : proprietes) {
            valeurs.put(propriete.getNom(), nombre(propriete));
        }
        return new Parametres(valeurs);
    }

    private static double nombre(Propriete propriete) {
        Valeur valeur = propriete.getValeur();
        if (!(valeur instanceof ValeurNombre)) {
            throw new ErreurDeSens("« " + propriete.getNom() + " » attend un nombre");
        }
        return ((ValeurNombre) valeur).getNombre();
    }

    private static String texte(Propriete propriete) {
        Valeur valeur = propriete.getValeur();
        if (!(valeur instanceof ValeurTexte)) {
            throw new ErreurDeSens("« " + propriete.getNom() + " » attend un texte entre guillemets");
        }
        return ((ValeurTexte) valeur).getTexte();
    }

    /** Une erreur de sens trouvée pendant la vérification d'une instruction. */
    private static final class ErreurDeSens extends RuntimeException {

        private static final long serialVersionUID = 1L;

        ErreurDeSens(String message) {
            super(message);
        }
    }
}
