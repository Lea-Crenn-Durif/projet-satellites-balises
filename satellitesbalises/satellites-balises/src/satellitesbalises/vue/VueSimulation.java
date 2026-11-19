package satellitesbalises.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import eventHandler.EventHandler;
import nicellipse.component.NiRectangle;
import nicellipse.component.NiSpace;
import satellitesbalises.modele.communication.Transfert;
import satellitesbalises.modele.evenement.EcouteurEntites;
import satellitesbalises.modele.evenement.EcouteurSatellites;
import satellitesbalises.modele.evenement.EcouteurTransferts;
import satellitesbalises.modele.evenement.EcouteurTemps;
import satellitesbalises.modele.evenement.EvenementApparenceModifiee;
import satellitesbalises.modele.evenement.EvenementEntiteAjoutee;
import satellitesbalises.modele.evenement.EvenementEntiteSupprimee;
import satellitesbalises.modele.evenement.EvenementPasTermine;
import satellitesbalises.modele.evenement.EvenementPositionModifiee;
import satellitesbalises.modele.evenement.EvenementStatutModifie;
import satellitesbalises.modele.evenement.EvenementTransfertDemarre;
import satellitesbalises.modele.evenement.EvenementTransfertProgresse;
import satellitesbalises.modele.evenement.EvenementTransfertTermine;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;

/**
 * La fenêtre de l'application : la barre de contrôle en haut, deux onglets à gauche (Observation,
 * Commandes), le monde au centre.
 *
 * <p>Le monde est dessiné avec Nicellipse : le ciel en blanc (le fond de l'espace), la mer en bleu
 * sous la surface, et une icône par entité. La vue écoute les événements du modèle (décision D6) ;
 * le modèle, lui, ne connaît pas la vue.</p>
 */
public class VueSimulation implements EcouteurTemps, EcouteurEntites, EcouteurSatellites, EcouteurTransferts {

    static final String TITRE = "Satellites et balises";
    static final Color COULEUR_CIEL = Color.WHITE;
    static final Color COULEUR_MER = new Color(30, 0, 255);
    static final int PAS_PAR_SECONDE_INITIAL = 10;

    private final GeometrieMonde geometrie;
    private final ConvertisseurCoordonnees convertisseur;
    private final NiSpace espace;
    private final BarreDeControle barre;
    private final PanneauObservation panneauObservation = new PanneauObservation();
    private final PanneauCommandes panneauCommandes;
    private final FabriqueVues fabriqueVues;
    private final Map<String, VueEntite> vuesEntites = new HashMap<>();
    private final Map<Transfert, VueTransfert> vuesTransferts = new HashMap<>();

    public VueSimulation(GeometrieMonde geometrie, ActionsUtilisateur actions) {
        this.geometrie = Objects.requireNonNull(geometrie, "geometrie");
        this.convertisseur = new ConvertisseurCoordonnees(geometrie);
        this.espace = new NiSpace(TITRE, tailleDuMonde());
        this.barre = new BarreDeControle(actions, PAS_PAR_SECONDE_INITIAL);
        this.panneauCommandes = new PanneauCommandes(actions);
        this.fabriqueVues = new FabriqueVues(convertisseur);
        dessinerDecor();
    }

    /** Abonne la vue aux événements qu'elle affiche. */
    public void abonner(EventHandler eventHandler) {
        eventHandler.registerListener(EvenementPasTermine.class, this);
        eventHandler.registerListener(EvenementEntiteAjoutee.class, this);
        eventHandler.registerListener(EvenementEntiteSupprimee.class, this);
        eventHandler.registerListener(EvenementPositionModifiee.class, this);
        eventHandler.registerListener(EvenementApparenceModifiee.class, this);
        eventHandler.registerListener(EvenementStatutModifie.class, this);
        eventHandler.registerListener(EvenementTransfertDemarre.class, this);
        eventHandler.registerListener(EvenementTransfertProgresse.class, this);
        eventHandler.registerListener(EvenementTransfertTermine.class, this);
        panneauObservation.abonner(eventHandler);
        panneauCommandes.abonner(eventHandler);
    }

    /** Met un programme dans l'onglet « Commandes », prêt à être exécuté. */
    public void afficherProgramme(String texte) {
        panneauCommandes.afficherProgramme(texte);
    }

    /** Ajoute un message dans l'onglet « Commandes ». */
    public void afficherMessage(String message) {
        panneauCommandes.afficherMessage(message);
    }

    /** Ouvre la fenêtre. À appeler sur le fil d'affichage Swing. */
    public void ouvrir() {
        JFrame fenetre = new JFrame(TITRE);
        fenetre.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fenetre.setResizable(false);
        fenetre.add(barre, BorderLayout.NORTH);
        JTabbedPane onglets = new JTabbedPane();
        onglets.addTab("Observation", panneauObservation);
        onglets.addTab("Commandes", panneauCommandes);
        fenetre.add(onglets, BorderLayout.WEST);
        fenetre.add(espace, BorderLayout.CENTER);
        fenetre.pack();
        fenetre.setLocationRelativeTo(null);
        fenetre.setVisible(true);
    }

    @Override
    public void pasTermine(EvenementPasTermine evenement) {
        barre.afficherPas(evenement.getPas());
        vuesTransferts.values().forEach(VueTransfert::mettreAJour);
        espace.repaint();
    }

    @Override
    public void entiteAjoutee(EvenementEntiteAjoutee evenement) {
        VueEntite vue = fabriqueVues.creerVue(evenement.getEntite());
        vuesEntites.put(evenement.getEntite().getNom(), vue);
        vue.afficherDans(espace);
        espace.repaint();
    }

    @Override
    public void entiteSupprimee(EvenementEntiteSupprimee evenement) {
        VueEntite vue = vuesEntites.remove(evenement.getEntite().getNom());
        if (vue != null) {
            vue.retirerDe(espace);
            espace.repaint();
        }
    }

    @Override
    public void positionModifiee(EvenementPositionModifiee evenement) {
        vueDe(evenement.getEntite().getNom()).mettreAJourPosition();
        espace.repaint();
    }

    @Override
    public void apparenceModifiee(EvenementApparenceModifiee evenement) {
        vueDe(evenement.getEntite().getNom()).mettreAJourApparence();
        espace.repaint();
    }

    @Override
    public void statutModifie(EvenementStatutModifie evenement) {
        vueDe(evenement.getSatellite().getNom()).mettreAJourStatut();
        espace.repaint();
    }

    @Override
    public void transfertDemarre(EvenementTransfertDemarre evenement) {
        VueTransfert vue = new VueTransfert(evenement.getTransfert(), convertisseur);
        vuesTransferts.put(evenement.getTransfert(), vue);
        vue.afficherDans(espace);
        espace.repaint();
    }

    @Override
    public void transfertProgresse(EvenementTransfertProgresse evenement) {
        // Le pourcentage est affiché par le panneau d'observation ; les cercles suivent au pas terminé.
    }

    @Override
    public void transfertTermine(EvenementTransfertTermine evenement) {
        VueTransfert vue = vuesTransferts.remove(evenement.getTransfert());
        if (vue != null) {
            vue.retirerDe(espace);
            espace.repaint();
        }
    }

    private VueEntite vueDe(String nom) {
        return Objects.requireNonNull(vuesEntites.get(nom), () -> "Aucun dessin pour l'entité " + nom);
    }

    private Dimension tailleDuMonde() {
        Point coinBasDroit = convertisseur.versEcran(new Position(geometrie.getLargeur(), -geometrie.getProfondeurMer()));
        return new Dimension(coinBasDroit.x, coinBasDroit.y);
    }

    /** Le ciel est le fond de l'espace ; la mer est un rectangle bleu qui commence à la surface. */
    private void dessinerDecor() {
        espace.setBackground(COULEUR_CIEL);
        Point surfaceAGauche = convertisseur.versEcran(new Position(0, GeometrieMonde.NIVEAU_SURFACE));
        Dimension taille = tailleDuMonde();

        NiRectangle mer = new NiRectangle();
        mer.setBackground(COULEUR_MER);
        mer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        mer.setBounds(surfaceAGauche.x, surfaceAGauche.y, taille.width, taille.height - surfaceAGauche.y);
        espace.add(mer);
    }
}
