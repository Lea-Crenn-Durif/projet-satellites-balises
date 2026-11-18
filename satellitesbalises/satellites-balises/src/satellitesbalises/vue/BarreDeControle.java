package satellitesbalises.vue;

import java.awt.FlowLayout;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * La barre du haut de la fenêtre (EX-1.10) : le numéro du pas, le bouton Pause / Reprendre
 * et le réglage de la vitesse.
 */
class BarreDeControle extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int PAS_PAR_SECONDE_MIN = 1;
    private static final int PAS_PAR_SECONDE_MAX = 50;
    private static final String TEXTE_PAUSE = "Pause";
    private static final String TEXTE_REPRENDRE = "Reprendre";

    private final transient ActionsUtilisateur actions;
    private final JLabel libellePas = new JLabel();
    private final JButton boutonPause = new JButton(TEXTE_PAUSE);
    private final JSlider curseurVitesse;

    BarreDeControle(ActionsUtilisateur actions, int pasParSecondeInitial) {
        super(new FlowLayout(FlowLayout.LEFT, 12, 6));
        this.actions = Objects.requireNonNull(actions, "actions");
        this.curseurVitesse = new JSlider(PAS_PAR_SECONDE_MIN, PAS_PAR_SECONDE_MAX, pasParSecondeInitial);

        afficherPas(0);
        boutonPause.addActionListener(clic -> basculerPause());
        curseurVitesse.addChangeListener(changement -> transmettreVitesse());

        add(libellePas);
        add(boutonPause);
        add(new JLabel("Vitesse (pas par seconde) :"));
        add(curseurVitesse);
    }

    void afficherPas(long pas) {
        libellePas.setText("Pas " + pas);
    }

    private void basculerPause() {
        actions.basculerPause();
        boolean enPause = TEXTE_PAUSE.equals(boutonPause.getText());
        boutonPause.setText(enPause ? TEXTE_REPRENDRE : TEXTE_PAUSE);
    }

    private void transmettreVitesse() {
        if (!curseurVitesse.getValueIsAdjusting()) {
            actions.changerVitesse(1000 / curseurVitesse.getValue());
        }
    }
}
