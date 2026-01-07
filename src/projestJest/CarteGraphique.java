package projestJest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import projestJest.Carte.Carte;

/**
 * Composant graphique représentant une carte.
 * Affiche le nom de la carte dans un rectangle avec bordure.
 */
public class CarteGraphique extends JPanel {

    private Carte carte;
    private boolean faceCachee;
    private boolean selectionnable;
    private Runnable actionClick;

    public CarteGraphique(Carte carte, boolean faceCachee) {
        this.carte = carte;
        this.faceCachee = faceCachee;
        this.selectionnable = false;
        
        setPreferredSize(new Dimension(100, 150));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        if (faceCachee) {
            label.setText("DOS");
            setBackground(Color.LIGHT_GRAY);
        } else {
            label.setText("<html><center>" + carte.toString() + "</center></html>");
        }
        add(label, BorderLayout.CENTER);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectionnable && actionClick != null) {
                    actionClick.run();
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectionnable) setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (selectionnable) setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
        });
    }
    
    public void setSelectionnable(boolean b, Runnable action) {
        this.selectionnable = b;
        this.actionClick = action;
        if (b) {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        } else {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
