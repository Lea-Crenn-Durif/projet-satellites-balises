package satellitesbalises.vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import eventHandler.EventHandler;
import satellitesbalises.modele.evenement.EcouteurCommandes;
import satellitesbalises.modele.evenement.EvenementCommandeIgnoree;

/**
 * L'onglet « Commandes » (étape 7) : on y écrit ou on y ouvre un programme, on l'exécute,
 * et on lit les messages (EX-7.5 à EX-7.7, EX-7.12).
 *
 * <p>Le panneau ne connaît pas le langage : il passe le texte à {@link ActionsUtilisateur}
 * et affiche le compte rendu qu'on lui renvoie.</p>
 */
class PanneauCommandes extends JPanel implements EcouteurCommandes {

    private static final long serialVersionUID = 1L;

    private static final Font POLICE_CODE = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    /** Même largeur que le panneau d'observation : les lignes longues défilent au lieu d'élargir la fenêtre. */
    private static final int LARGEUR = 340;
    private static final String DOSSIER_EXEMPLES = "exemples";

    private final transient ActionsUtilisateur actions;
    private final JTextArea zoneProgramme = new JTextArea();
    private final JTextArea zoneMessages = new JTextArea();

    PanneauCommandes(ActionsUtilisateur actions) {
        super(new BorderLayout());
        setPreferredSize(new Dimension(LARGEUR, 0));
        this.actions = Objects.requireNonNull(actions, "actions");

        zoneProgramme.setFont(POLICE_CODE);
        zoneMessages.setFont(POLICE_CODE);
        zoneMessages.setEditable(false);
        zoneMessages.setLineWrap(true);
        zoneMessages.setWrapStyleWord(true);

        JButton boutonOuvrir = new JButton("Ouvrir…");
        JButton boutonExecuter = new JButton("Exécuter");
        boutonOuvrir.addActionListener(clic -> ouvrirUnFichier());
        boutonExecuter.addActionListener(clic -> executer());
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        boutons.add(boutonOuvrir);
        boutons.add(boutonExecuter);

        JSplitPane programmeEtMessages = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                encadre(zoneProgramme, "Programme"), encadre(zoneMessages, "Messages"));
        programmeEtMessages.setResizeWeight(0.65);

        add(boutons, BorderLayout.NORTH);
        add(programmeEtMessages, BorderLayout.CENTER);
    }

    void abonner(EventHandler eventHandler) {
        eventHandler.registerListener(EvenementCommandeIgnoree.class, this);
    }

    /** Une commande prévue n'a pas pu s'appliquer : on le dit, la simulation continue (EX-7.12). */
    @Override
    public void commandeIgnoree(EvenementCommandeIgnoree evenement) {
        afficherMessage("Commande ignorée : " + evenement.getRaison());
    }

    void afficherProgramme(String texte) {
        zoneProgramme.setText(texte);
        zoneProgramme.setCaretPosition(0);
    }

    void afficherMessage(String message) {
        zoneMessages.append(message + "\n");
        zoneMessages.setCaretPosition(zoneMessages.getDocument().getLength());
    }

    private void executer() {
        afficherMessage(actions.executerProgramme(zoneProgramme.getText()));
    }

    private void ouvrirUnFichier() {
        File dossier = new File(DOSSIER_EXEMPLES);
        JFileChooser choix = new JFileChooser(dossier.isDirectory() ? dossier : null);
        choix.setFileFilter(new FileNameExtensionFilter("Programmes (*.sb)", "sb"));
        if (choix.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            afficherProgramme(Files.readString(choix.getSelectedFile().toPath(), StandardCharsets.UTF_8));
        } catch (IOException erreur) {
            afficherMessage("Impossible de lire " + choix.getSelectedFile().getName() + " : " + erreur.getMessage());
        }
    }

    private static JScrollPane encadre(JTextArea zone, String titre) {
        JScrollPane defilement = new JScrollPane(zone);
        defilement.setBorder(BorderFactory.createTitledBorder(titre));
        return defilement;
    }
}
