package projestJest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import projestJest.Carte.Carte;

/**
 * Composant graphique Swing représentant une carte de jeu à l'écran.
 * Ce composant peut afficher soit l'image associée à la carte, soit le dos de la carte si elle est cachée.
 * En l'absence d'image, le composant utilise un affichage de secours textuel.
 * 
 * Il gère également les interactions souris (clics, survol) si la carte est rendue sélectionnable.
 */
public class CarteGraphique extends JPanel {

    /**
     * L'instance de la carte représentée par ce composant.
     */
    private Carte carte;

    /**
     * Indique si la carte doit être affichée face cachée (dos visible) ou face visible.
     */
    private boolean faceCachee;

    /**
     * Indique si le composant réagit aux clics de souris.
     */
    private boolean selectionnable;

    /**
     * L'action à exécuter (callback) lorsque l'utilisateur clique sur ce composant, s'il est sélectionnable.
     */
    private Runnable actionClick;
    
    /**
     * L'image chargée pour l'affichage de la carte. Peut être null si le fichier n'est pas trouvé.
     */
    private BufferedImage image;

    /**
     * Constructeur du composant graphique Carte.
     * Initialise les dimensions, le style et charge l'image correspondante.
     * 
     * @param carte L'objet Carte à afficher (peut être null si c'est un emplacement vide).
     * @param faceCachee true pour afficher le dos de la carte, false pour afficher la face.
     */
    public CarteGraphique(Carte carte, boolean faceCachee) {
        this.carte = carte;
        this.faceCachee = faceCachee;
        this.selectionnable = false;
        
        setPreferredSize(new Dimension(100, 150));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        chargerImage();
        
        if (!faceCachee && carte != null) {
            setToolTipText(carte.toString());
        }

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
    
    /**
     * Charge l'image correspondant à la carte ou au dos de carte depuis le système de fichiers.
     * Le chemin d'accès est construit dynamiquement en fonction de l'état (caché ou nom de la carte).
     */
    private void chargerImage() {
        try {
            String path = "src/images/";
            if (faceCachee) {
                path += "card_back.png";
            } else if (carte != null) {
                String nomFichier = carte.toString().replaceAll("\\s+", "_") + ".png";
                path += nomFichier;
            } else {
                return; 
            }
            
            File f = new File(path);
            if (f.exists()) {
                image = ImageIO.read(f);
            } else {
                if (!faceCachee) {
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(faceCachee ? Color.LIGHT_GRAY : Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g.getFontMetrics();
            
            String text = faceCachee ? "DOS" : (carte != null ? carte.toString() : "VIDE");
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            
            g.drawString(text, x, y);
        }
    }
    
    /**
     * Définit si ce composant est interactif (cliquable).
     * Modifie le curseur de la souris pour indiquer l'interactivité.
     * 
     * @param b true pour rendre la carte sélectionnable, false sinon.
     * @param action Le code (Runnable) à exécuter lors du clic.
     */
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
