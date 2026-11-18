package satellitesbalises.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import eventHandler.EventHandler;
import satellitesbalises.modele.entite.Balise;
import satellitesbalises.modele.entite.Satellite;
import satellitesbalises.modele.entite.VisiteurEntite;
import satellitesbalises.modele.evenement.EcouteurBalises;
import satellitesbalises.modele.evenement.EcouteurEntites;
import satellitesbalises.modele.evenement.EvenementActiviteModifiee;
import satellitesbalises.modele.evenement.EvenementApparenceModifiee;
import satellitesbalises.modele.evenement.EvenementEntiteAjoutee;
import satellitesbalises.modele.evenement.EvenementEntiteSupprimee;
import satellitesbalises.modele.evenement.EvenementEquipementModifie;
import satellitesbalises.modele.evenement.EvenementMemoireModifiee;
import satellitesbalises.modele.evenement.EvenementPositionModifiee;

/**
 * La colonne de gauche (EX-2.11) : une ligne par entité, avec ce qu'on ne peut pas dessiner
 * dans le monde (statut, portée, activité, mémoire, instruments…).
 *
 * <p>C'est un abonné de plus à l'EventHandler (décision D6) : l'ajouter n'a modifié aucune classe
 * du modèle. Il est aussi visiteur d'entités, pour écrire la bonne ligne sans {@code instanceof}.</p>
 */
class PanneauObservation extends JPanel implements EcouteurEntites, EcouteurBalises, VisiteurEntite {

    private static final long serialVersionUID = 1L;

    private static final int LARGEUR = 300;
    private static final Font POLICE_LIGNE = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    /** Abréviations des instruments connus ; un instrument inconnu s'affiche sous son nom complet. */
    private static final Map<String, String> ABREVIATIONS = Map.of(
            "temperature", "T", "salinite", "S", "courantometre", "C", "turbidite", "Tu");

    private final JPanel lignesSatellites = colonne();
    private final JPanel lignesBalises = colonne();
    private final Map<String, JLabel> lignes = new HashMap<>();

    PanneauObservation() {
        super(new BorderLayout());
        setPreferredSize(new Dimension(LARGEUR, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY));

        JPanel contenu = colonne();
        contenu.add(titre("SATELLITES"));
        contenu.add(lignesSatellites);
        contenu.add(Box.createVerticalStrut(12));
        contenu.add(titre("BALISES"));
        contenu.add(lignesBalises);
        add(contenu, BorderLayout.NORTH);
    }

    void abonner(EventHandler eventHandler) {
        eventHandler.registerListener(EvenementEntiteAjoutee.class, this);
        eventHandler.registerListener(EvenementEntiteSupprimee.class, this);
        eventHandler.registerListener(EvenementActiviteModifiee.class, this);
        eventHandler.registerListener(EvenementMemoireModifiee.class, this);
        eventHandler.registerListener(EvenementEquipementModifie.class, this);
    }

    @Override
    public void entiteAjoutee(EvenementEntiteAjoutee evenement) {
        evenement.getEntite().accepter(this);
        revalidate();
    }

    @Override
    public void entiteSupprimee(EvenementEntiteSupprimee evenement) {
        JLabel ligne = lignes.remove(evenement.getEntite().getNom());
        if (ligne != null) {
            ligne.getParent().remove(ligne);
            revalidate();
            repaint();
        }
    }

    @Override
    public void positionModifiee(EvenementPositionModifiee evenement) {
        // La position se voit dans le monde : rien à afficher ici.
    }

    @Override
    public void apparenceModifiee(EvenementApparenceModifiee evenement) {
        // L'apparence se voit dans le monde : rien à afficher ici.
    }

    @Override
    public void activiteModifiee(EvenementActiviteModifiee evenement) {
        visiter(evenement.getBalise());
    }

    @Override
    public void memoireModifiee(EvenementMemoireModifiee evenement) {
        visiter(evenement.getBalise());
    }

    @Override
    public void equipementModifie(EvenementEquipementModifie evenement) {
        visiter(evenement.getBalise());
    }

    /** Ligne d'un satellite, par exemple « s1  DISPONIBLE  portée 30 ». */
    @Override
    public void visiter(Satellite satellite) {
        String texte = String.format("%-4s %-11s portée %d", satellite.getNom(), satellite.getStatut(),
                Math.round(satellite.getPortee()));
        ecrire(satellite.getNom(), texte, lignesSatellites);
    }

    /** Ligne d'une balise, par exemple « b1  Collecte  12/50  T S ». */
    @Override
    public void visiter(Balise balise) {
        String instruments = balise.getInstruments().isEmpty() ? "-"
                : balise.getInstruments().stream().map(instrument -> ABREVIATIONS.getOrDefault(instrument.getNom(), instrument.getNom()))
                        .collect(Collectors.joining(" "));
        String texte = String.format("%-4s %-12s %d/%d  %s", balise.getNom(), balise.getActivite().getNom(),
                balise.getMemoire().getTaille(), balise.getMemoire().getCapacite(), instruments);
        ecrire(balise.getNom(), texte, lignesBalises);
    }

    /** Crée la ligne de l'entité si elle n'existe pas encore, puis remplace son texte. */
    private void ecrire(String nom, String texte, JPanel section) {
        JLabel ligne = lignes.computeIfAbsent(nom, inconnu -> {
            JLabel nouvelle = new JLabel();
            nouvelle.setFont(POLICE_LIGNE);
            section.add(nouvelle);
            return nouvelle;
        });
        ligne.setText(texte);
    }

    private static JPanel colonne() {
        JPanel panneau = new JPanel();
        panneau.setLayout(new BoxLayout(panneau, BoxLayout.Y_AXIS));
        panneau.setAlignmentX(LEFT_ALIGNMENT);
        panneau.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        return panneau;
    }

    private static JLabel titre(String texte) {
        JLabel titre = new JLabel(texte);
        titre.setFont(titre.getFont().deriveFont(Font.BOLD));
        titre.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
        return titre;
    }
}
