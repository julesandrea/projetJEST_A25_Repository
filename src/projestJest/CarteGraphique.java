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
 * Composant graphique représentant une carte.
 * Affiche l'image de la carte ou son nom si image manquante.
 */
public class CarteGraphique extends JPanel {

    private Carte carte;
    private boolean faceCachee;
    private boolean selectionnable;
    private Runnable actionClick;
    
    private BufferedImage image;

    public CarteGraphique(Carte carte, boolean faceCachee) {
        this.carte = carte;
        this.faceCachee = faceCachee;
        this.selectionnable = false;
        
        setPreferredSize(new Dimension(100, 150));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        chargerImage();
        
        // Tooltip pour aider si l'image n'est pas claire
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
    
    private void chargerImage() {
        try {
            String path = "src/images/";
            if (faceCachee) {
                path += "card_back.png";
            } else if (carte != null) {
                // Tentative de chargement par nom de classe ou nom string
                // Nettoyage basique du nom pour le fichier
                String nomFichier = carte.toString().replaceAll("\\s+", "_") + ".png";
                path += nomFichier;
            } else {
                return; // Pas de carte, pas d'image
            }
            
            File f = new File(path);
            if (f.exists()) {
                image = ImageIO.read(f);
            } else {
                // Fallback: essayons de charger le dos si l'image specifique manque
                if (!faceCachee) {
                    // System.out.println("Image manquante: " + path);
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
            // Dessin texte par défaut
            g.setColor(faceCachee ? Color.LIGHT_GRAY : Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g.getFontMetrics();
            
            String text = faceCachee ? "DOS" : (carte != null ? carte.toString() : "VIDE");
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            
            // Si le texte est trop long, on peut le wrapper, mais ici on simplifie
            g.drawString(text, x, y);
        }
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
