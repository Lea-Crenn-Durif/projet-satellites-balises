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
import javax.swing.WindowConstants;

import eventHandler.EventHandler;
import nicellipse.component.NiRectangle;
import nicellipse.component.NiSpace;
import satellitesbalises.modele.evenement.EcouteurEntites;
import satellitesbalises.modele.evenement.EcouteurTemps;
import satellitesbalises.modele.evenement.EvenementApparenceModifiee;
import satellitesbalises.modele.evenement.EvenementEntiteAjoutee;
import satellitesbalises.modele.evenement.EvenementEntiteSupprimee;
import satellitesbalises.modele.evenement.EvenementPasTermine;
import satellitesbalises.modele.evenement.EvenementPositionModifiee;
import satellitesbalises.modele.monde.GeometrieMonde;
import satellitesbalises.modele.monde.Position;

/**
 * La fenêtre de l'application : la barre de contrôle en haut, le panneau d'observation à gauche,
 * le monde au centre.
 *
 * <p>Le monde est dessiné avec Nicellipse : le ciel en blanc (le fond de l'espace), la mer en bleu
 * sous la surface, et une icône par entité. La vue écoute les événements du modèle (décision D6) ;
 * le modèle, lui, ne connaît pas la vue.</p>
 */
public class VueSimulation implements EcouteurTemps, EcouteurEntites {

    static final String TITRE = "Satellites et balises";
    static final Color COULEUR_CIEL = Color.WHITE;
    static final Color COULEUR_MER = new Color(30, 0, 255);
    static final int PAS_PAR_SECONDE_INITIAL = 10;

    private final GeometrieMonde geometrie;
    private final ConvertisseurCoordonnees convertisseur;
    private final NiSpace espace;
    private final BarreDeControle barre;
    private final PanneauObservation panneauObservation = new PanneauObservation();
    private final FabriqueVues fabriqueVues;
    private final Map<String, VueEntite> vuesEntites = new HashMap<>();

    public VueSimulation(GeometrieMonde geometrie, ActionsUtilisateur actions) {
        this.geometrie = Objects.requireNonNull(geometrie, "geometrie");
        this.convertisseur = new ConvertisseurCoordonnees(geometrie);
        this.espace = new NiSpace(TITRE, tailleDuMonde());
        this.barre = new BarreDeControle(actions, PAS_PAR_SECONDE_INITIAL);
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
        panneauObservation.abonner(eventHandler);
    }

    /** Ouvre la fenêtre. À appeler sur le fil d'affichage Swing. */
    public void ouvrir() {
        JFrame fenetre = new JFrame(TITRE);
        fenetre.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fenetre.setResizable(false);
        fenetre.add(barre, BorderLayout.NORTH);
        fenetre.add(panneauObservation, BorderLayout.WEST);
        fenetre.add(espace, BorderLayout.CENTER);
        fenetre.pack();
        fenetre.setLocationRelativeTo(null);
        fenetre.setVisible(true);
    }

    @Override
    public void pasTermine(EvenementPasTermine evenement) {
        barre.afficherPas(evenement.getPas());
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
