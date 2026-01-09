package projestJest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import projestJest.Carte.Carte;
import projestJest.Joueur.Joueur;

/**
 * La classe VueGraphique représente l'interface graphique du jeu JEST, implémentée avec Swing.
 * Elle se charge d'afficher l'état actuel de la partie (joueurs, cartes, tapis, trophées) et de
 * recueillir les interactions de l'utilisateur (clics sur les cartes, boutons, saisie de texte).
 * 
 * Cette vue est passive d'un point de vue logique : elle reçoit les ordres d'affichage du contrôleur
 * et lui transmet les événements utilisateurs pour traitement.
 */
public class VueGraphique extends JFrame {

    /**
     * Référence vers le contrôleur pour transmettre les interactions utilisateur.
     */
    private Controleur controleur;

    /**
     * Panneau central affichant le tapis de jeu avec les joueurs, leurs offres et leurs JESTs.
     */
    private JPanel centerPanel;

    /**
     * Conteneur situé au sud de la fenêtre regroupant la zone des trophées et les composants d'action.
     */
    private JPanel southContainer;

    /**
     * Panneau dynamique affichant les boutons ou champs de saisie pour l'interaction utilisateur.
     */
    private JPanel actionPanel; 

    /**
     * Panneau affichant les trophées disponibles en jeu.
     */
    private JPanel trophyPanel;

    /**
     * Étiquette située en haut de la fenêtre affichant l'instruction courante ou l'état du jeu.
     */
    private JLabel statusLabel;
    
    /**
     * Liste des trophées en jeu, conservée pour l'affichage final des résultats.
     */
    private List<Carte> listTrophees;
    
    /**
     * Variable temporaire stockant la présélection (clic sur une carte) lors de la phase de vol.
     */
    private Integer choixCartePreselectionne = null;

    /**
     * Constructeur de la VueGraphique.
     * Initialise la fenêtre principale, ses composants graphiques et sa mise en page.
     * 
     * @param controleur Le contrôleur associé à cette vue.
     */
    public VueGraphique(Controleur controleur) {
        this.controleur = controleur; 
        
        setTitle("JEST - Interface Graphique");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        statusLabel = new JLabel("En attente...");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(statusLabel, BorderLayout.NORTH);
        
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(0, 1, 10, 10)); 
        centerPanel.setBackground(new Color(34, 139, 34)); 
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        add(scrollPane, BorderLayout.CENTER);
        
        southContainer = new JPanel();
        southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));
        
        trophyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        trophyPanel.setBackground(new Color(20, 80, 20));
        trophyPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(255, 215, 0)), "Trophées en jeu", 
            0, 0, new Font("Arial", Font.BOLD, 12), new Color(255, 215, 0)));
        southContainer.add(trophyPanel);
        
        actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); 
        actionPanel.setPreferredSize(new Dimension(1200, 110)); 
        actionPanel.setBackground(Color.DARK_GRAY);
        southContainer.add(actionPanel);
        
        add(southContainer, BorderLayout.SOUTH);
    }
    
    /**
     * Met à jour la référence vers l'instance de la partie.
     * Cette méthode est maintenue pour compatibilité mais la vue ne devrait plus dépendre directement de l'état interne de la partie.
     * 
     * @param p L'instance de la partie (non utilisée directement pour l'affichage désormais).
     */
    public void setPartie(Partie p) {
        
    }

    /**
     * Redessine entièrement le tapis de jeu (zone centrale) en fonction de l'état actuel des joueurs.
     * Affiche pour chaque joueur son offre (cartes visibles/cachées) et son Jest (cartes capturées).
     * 
     * @param joueurs La liste des joueurs avec leur état actuel.
     */
    public void mettreAJour(List<Joueur> joueurs) {
        centerPanel.removeAll();
        
        if (joueurs != null) {
            
            int rows = (joueurs.size() > 2) ? 2 : joueurs.size();
            int cols = (joueurs.size() > 2) ? 2 : 1;
            centerPanel.setLayout(new GridLayout(rows, cols, 10, 10));

            for (Joueur j : joueurs) {
                JPanel playerPanel = new JPanel(new BorderLayout());
                playerPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.WHITE), 
                        j.getNom(), 
                        0, 0, 
                        new Font("Arial", Font.BOLD, 14), 
                        Color.WHITE));
                playerPanel.setBackground(new Color(34, 100, 34)); 

                JPanel offerPanel = new JPanel(new FlowLayout());
                offerPanel.setOpaque(false);
                offerPanel.add(new JLabel("Offre: "));
                
                if (j.getOffre() != null) {
                    if (j.getOffre().getFaceVisible() != null) {
                        CarteGraphique cg = new CarteGraphique(j.getOffre().getFaceVisible(), false);
                        cg.setPreferredSize(new Dimension(60, 90)); 
                        offerPanel.add(cg);
                    }
                    if (j.getOffre().getFaceCachee() != null) {
                        CarteGraphique cg = new CarteGraphique(null, true);
                        cg.setPreferredSize(new Dimension(60, 90));
                        offerPanel.add(cg);
                    }
                } else {
                    JLabel lbl = new JLabel("En préparation...");
                    lbl.setForeground(Color.LIGHT_GRAY);
                    offerPanel.add(lbl);
                }
                playerPanel.add(offerPanel, BorderLayout.NORTH);
                
                JPanel jestPanel = new JPanel(new FlowLayout());
                jestPanel.setOpaque(false);
                jestPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Jest", 0,0,null, Color.WHITE));
                
                for (Carte c : j.getJest().getCartes()) {
                    CarteGraphique cg = new CarteGraphique(c, false);
                    cg.setPreferredSize(new Dimension(50, 75)); 
                    jestPanel.add(cg);
                }
                
                playerPanel.add(jestPanel, BorderLayout.CENTER);
                
                centerPanel.add(playerPanel);
            }
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    
    /**
     * Nettoie le panneau d'action pour préparer l'affichage de nouveaux boutons ou champs.
     */
    private void clearActionPanel() {
        actionPanel.removeAll();
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    /**
     * Affiche un message d'information dans la console (log Interface) et ouvre une boîte de dialogue si nécessaire.
     * 
     * @param msg Le message à afficher.
     */
    public void afficherMessage(String msg) {
        System.out.println("[Interface Message] " + msg);
        
        if (msg.contains("vainqueur")) {
            JOptionPane.showMessageDialog(this, msg, "Fin de Partie", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Met à jour l'affichage de la zone des trophées en bas de la fenêtre.
     * 
     * @param trophees La liste des cartes trophées actuellement en jeu.
     */
    public void afficherTrophees(java.util.List<Carte> trophees) {
        this.listTrophees = new java.util.ArrayList<>(trophees);
        if (trophyPanel != null) {
            trophyPanel.removeAll();
            for (Carte c : trophees) {
                CarteGraphique cg = new CarteGraphique(c, false);
                cg.setPreferredSize(new Dimension(60, 90)); 
                trophyPanel.add(cg);
            }
            trophyPanel.revalidate();
            trophyPanel.repaint();
        }
    }

    /**
     * Affiche une fenêtre de dialogue modale présentant les résultats finaux de la partie (Podium).
     * Détaille le classement, les scores, les Jests finaux et l'attribution des trophées.
     * 
     * @param joueurs La liste des joueurs.
     * @param trophees La liste des trophées pour déterminer qui a gagné quoi.
     */
    public void afficherResultats(java.util.List<Joueur> joueurs, java.util.List<Carte> trophees) {
        joueurs.sort((j1, j2) -> Integer.compare(j2.getScore(), j1.getScore()));

        this.listTrophees = (trophees != null) ? new java.util.ArrayList<>(trophees) : new java.util.ArrayList<>();

        JDialog podium = new JDialog(this, "Podium - Résultats Finaux", true);
        podium.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        podium.setLayout(new BorderLayout());

        JPanel mainContent = new JPanel(new BorderLayout());

        JLabel title = new JLabel("RÉSULTATS FINAUX", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(255, 215, 0)); 
        title.setOpaque(true);
        title.setBackground(new Color(34, 100, 34));
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        podium.add(title, BorderLayout.NORTH);

        JPanel recapPanel = new JPanel();
        recapPanel.setLayout(new BorderLayout()); 
        recapPanel.setBackground(new Color(20, 80, 20)); 
        recapPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.CYAN), "Qui a gagné quoi ?",
            0, 0, new Font("Arial", Font.BOLD, 14), Color.CYAN));
        
        JPanel trophiesRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        trophiesRow.setOpaque(false);

        if (listTrophees != null && !listTrophees.isEmpty()) {
            for (Carte t : listTrophees) {
                JPanel trophyItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
                trophyItem.setOpaque(false);
                trophyItem.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 1)); 
                
                CarteGraphique cg = new CarteGraphique(t, false);
                cg.setPreferredSize(new Dimension(60, 90));
                trophyItem.add(cg);
                
                String winnerName = "Personne";
                String condition = ""; 
                if (t instanceof projestJest.Carte.CarteTrophee) {
                    condition = " <i>(" + ((projestJest.Carte.CarteTrophee) t).getCondition() + ")</i>";
                }
                
                for (Joueur j : joueurs) {
                    if (j.getJest().getCartes().stream().anyMatch(c -> c.toString().equals(t.toString()))) {
                        winnerName = j.getNom();
                        break;
                    }
                }
                
                JLabel info = new JLabel("<html><b>" + t.toString() + "</b>" + condition + "<br>Gagné par <font color='yellow'>" + winnerName + "</font></html>");
                info.setForeground(Color.WHITE);
                info.setFont(new Font("Arial", Font.PLAIN, 14));
                trophyItem.add(info);
                
                trophiesRow.add(trophyItem);
            }
        } else {
            JLabel hintLabel = new JLabel("<html><center>Pas de données sur les trophées initiaux.</center></html>");
            hintLabel.setForeground(Color.WHITE);
            trophiesRow.add(hintLabel);
        }
        
        recapPanel.add(trophiesRow, BorderLayout.CENTER);
        mainContent.add(recapPanel, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(34, 139, 34));
        
        mainContent.add(listPanel, BorderLayout.CENTER);

        int rank = 1;
        for (Joueur j : joueurs) {
            JPanel pPanel = new JPanel(new BorderLayout());
            pPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.WHITE, 2)
            ));
            pPanel.setBackground(new Color(34, 100, 34)); 
            
            JLabel lbl = new JLabel("  #" + rank + "  " + j.getNom() + " : " + j.getScore() + " points");
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.BOLD, 18));
            pPanel.add(lbl, BorderLayout.NORTH);
            
            JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            cardPanel.setOpaque(false);
            for (Carte c : j.getJest().getCartes()) {
                 CarteGraphique cg = new CarteGraphique(c, false);
                 cg.setPreferredSize(new Dimension(60, 90)); 
                 cardPanel.add(cg);
            }
            pPanel.add(cardPanel, BorderLayout.CENTER);
            
            listPanel.add(pPanel);
            rank++;
        }

        JScrollPane scroll = new JScrollPane(mainContent);
        podium.add(scroll, BorderLayout.CENTER);

        JButton okBtn = new JButton("Fermer");
        okBtn.addActionListener(e -> podium.dispose());
        podium.add(okBtn, BorderLayout.SOUTH);

        podium.setLocationRelativeTo(this);
        podium.setVisible(true);
    }

    /**
     * Met à jour l'affichage pour indiquer le tour courant.
     * 
     * @param numTour Le numéro du tour.
     * @param joueurs La liste des joueurs pour rafraîchir le plateau.
     */
    public void afficherTour(int numTour, List<Joueur> joueurs) {
        statusLabel.setText("Tour " + numTour);
        mettreAJour(joueurs); 
    }

    /**
     * Met à jour l'affichage des offres des joueurs.
     * 
     * @param joueurs La liste des joueurs avec leurs offres actualisées.
     */
    public void afficherOffres(java.util.List<Joueur> joueurs) {
        mettreAJour(joueurs); 
    }

    /**
     * Met à jour l'affichage pour signaler la fin d'un tour.
     * 
     * @param numTour Le numéro du tour qui se termine.
     * @param joueurs La liste des joueurs.
     */
    public void afficherFinTour(int numTour, List<Joueur> joueurs) {
        mettreAJour(joueurs); 
    }
    
    /**
     * Extrait une étiquette textuelle lisible à partir d'une question formatée.
     * Gère les formats comme "1-Option" ou "1:Option".
     * 
     * @param question La question contenant les options.
     * @param value La valeur numérique associée au bouton.
     * @return Le libellé du bouton.
     */
    private String extractLabel(String question, int value) {
        String[] parts = question.split(",");
        for (String p : parts) {
            p = p.trim();
            String valStr = String.valueOf(value);
            int valIdx = p.indexOf(valStr);
            
            while (valIdx != -1) {
                boolean startBound = (valIdx == 0) || !Character.isDigit(p.charAt(valIdx - 1));
                if (startBound) {
                    int afterValIdx = valIdx + valStr.length();
                    while (afterValIdx < p.length() && p.charAt(afterValIdx) == ' ') afterValIdx++;
                    
                    if (afterValIdx < p.length()) {
                        char c = p.charAt(afterValIdx);
                        if (c == '-' || c == ':') {
                            String potential = p.substring(afterValIdx + 1).trim();
                            if (!potential.isEmpty() && !Character.isDigit(potential.charAt(0))) {
                                return potential;
                            }
                        }
                    }
                }
                valIdx = p.indexOf(valStr, valIdx + 1);
            }
        }
        return String.valueOf(value); 
    }

    /**
     * Affiche une demande de choix numérique à l'utilisateur sous forme de boutons.
     * 
     * @param question La question à poser.
     * @param min La valeur minimale autorisée.
     * @param max La valeur maximale autorisée.
     */
    public void demanderChoixInt(String question, int min, int max) {
        clearActionPanel();
        statusLabel.setText(question.split("[:]")[0]); 
        
        if (question.contains(min + "-") || question.contains(min + ":")) {
            for (int i = min; i <= max; i++) {
                final int val = i;
                JButton btn = new JButton(extractLabel(question, val));
                btn.setPreferredSize(new Dimension(150, 50));
                btn.addActionListener(e -> { clearActionPanel(); controleur.setInputInt(val); });
                actionPanel.add(btn);
            }
        } else {
             if (min == 0 && max == 1) {
                JButton btnContinuer = new JButton("Continuer");
                JButton btnSauvegarder = new JButton("Sauvegarder");
                btnContinuer.addActionListener(e -> { clearActionPanel(); controleur.setInputInt(0); });
                btnSauvegarder.addActionListener(e -> { clearActionPanel(); controleur.setInputInt(1); });
                actionPanel.add(btnContinuer);
                actionPanel.add(btnSauvegarder);
            } else {
                for (int i = min; i <= max; i++) {
                    final int val = i;
                    JButton btn = new JButton(String.valueOf(val));
                    btn.setPreferredSize(new Dimension(80, 50));
                    btn.addActionListener(e -> { clearActionPanel(); controleur.setInputInt(val); });
                    actionPanel.add(btn);
                }
            }
        }
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    /**
     * Affiche un champ de texte pour demander une chaîne de caractères à l'utilisateur.
     * 
     * @param question La question à poser.
     */
    public void demanderChaine(String question) {
        clearActionPanel();
        statusLabel.setText(question);
        
        JTextField field = new JTextField(20);
        JButton btn = new JButton("Valider");
        btn.addActionListener(e -> {
             clearActionPanel();
             controleur.setInputString(field.getText());
        });
        
        actionPanel.add(field);
        actionPanel.add(btn);
        
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    /**
     * Affiche l'interface permettant à un joueur de choisir quelle carte de son offre restera visible.
     * 
     * @param j Le joueur concerné.
     * @param c1 La première carte de l'offre.
     * @param c2 La seconde carte de l'offre.
     */
    public void demanderChoixOffre(Joueur j, Carte c1, Carte c2) {
        clearActionPanel();
        statusLabel.setText(j.getNom() + " : Cliquez sur la carte à garder VISIBLE");
        
        CarteGraphique cg1 = new CarteGraphique(c1, false);
        cg1.setPreferredSize(new Dimension(60, 90)); 
        cg1.setSelectionnable(true, () -> {
            clearActionPanel();
            statusLabel.setText("En attente...");
            controleur.setInputInt(1);
        });
        
        CarteGraphique cg2 = new CarteGraphique(c2, false);
        cg2.setPreferredSize(new Dimension(60, 90)); 
        cg2.setSelectionnable(true, () -> {
            clearActionPanel();
            statusLabel.setText("En attente...");
            controleur.setInputInt(2);
        });
        
        actionPanel.add(cg1);
        actionPanel.add(Box.createHorizontalStrut(50));
        actionPanel.add(cg2);
        
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    /**
     * Affiche l'interface permettant à un joueur de choisir quelle carte prendre dans l'offre d'un autre joueur.
     * 
     * @param j Le joueur qui doit choisir.
     * @param o L'offre cible contenant une carte visible et une carte cachée.
     */
    public void demanderChoixPrise(Joueur j, projestJest.Offre o) {
        if (choixCartePreselectionne != null) {
            int choix = choixCartePreselectionne;
            choixCartePreselectionne = null; 
            
            SwingUtilities.invokeLater(() -> controleur.setInputInt(choix));
            return; 
        }

        clearActionPanel();
        statusLabel.setText(j.getNom() + " : Cliquez sur la carte à PRENDRE");
        
        if (o.getFaceVisible() != null) {
            CarteGraphique cgVis = new CarteGraphique(o.getFaceVisible(), false);
            cgVis.setPreferredSize(new Dimension(60, 90)); 
            cgVis.setSelectionnable(true, () -> {
                clearActionPanel();
                controleur.setInputInt(1); 
            });
            actionPanel.add(cgVis);
        }
        
        actionPanel.add(Box.createHorizontalStrut(50));
        
        CarteGraphique cgCache = new CarteGraphique(null, true);
        cgCache.setPreferredSize(new Dimension(60, 90)); 
        cgCache.setSelectionnable(true, () -> {
            clearActionPanel();
            controleur.setInputInt(2); 
        });
        actionPanel.add(cgCache);
        
        actionPanel.revalidate();
        actionPanel.repaint();
    }
    
    /**
     * Affiche l'interface permettant à un joueur de choisir un adversaire cible pour le vol de carte.
     * Rend les cartes sur le tapis cliquables pour une interaction directe.
     * 
     * @param j Le joueur qui doit choisir.
     * @param adversaires La liste des adversaires valides.
     * @param tousLesJoueurs La liste complète des joueurs pour le réaffichage interactif du plateau.
     */
    public void demanderChoixAdversaire(Joueur j, List<Joueur> adversaires, List<Joueur> tousLesJoueurs) {
        clearActionPanel();
        statusLabel.setText(j.getNom() + " : Cliquez sur une carte adverse sur le tapis pour la voler !");
        choixCartePreselectionne = null;
        
        mettreAJourInteractif(tousLesJoueurs, adversaires);
    }
    
    /**
     * Version interactive de la méthode mettreAJour, utilisée spécifiquement pour la phase de choix d'adversaire.
     * Rend les cartes des adversaires ciblés "cliquables" et définit les actions de retour vers le contrôleur.
     * 
     * @param joueurs La liste de tous les joueurs à afficher.
     * @param cibles La liste des joueurs cibles valides pour le vol.
     */
    private void mettreAJourInteractif(List<Joueur> joueurs, List<Joueur> cibles) {
        centerPanel.removeAll();
        
        if (joueurs != null) {
            
            int rows = (joueurs.size() > 2) ? 2 : joueurs.size();
            int cols = (joueurs.size() > 2) ? 2 : 1;
            centerPanel.setLayout(new GridLayout(rows, cols, 10, 10));

            for (Joueur j : joueurs) {
                JPanel playerPanel = new JPanel(new BorderLayout());
                playerPanel.setBorder(BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(Color.WHITE), 
                        j.getNom(), 
                        0, 0, 
                        new Font("Arial", Font.BOLD, 14), 
                        Color.WHITE));
                playerPanel.setBackground(new Color(34, 100, 34)); 

                JPanel offerPanel = new JPanel(new FlowLayout());
                offerPanel.setOpaque(false);
                
                boolean estCible = cibles.contains(j);

                if (j.getOffre() != null) {
                    if (j.getOffre().getFaceVisible() != null) {
                        CarteGraphique cg = new CarteGraphique(j.getOffre().getFaceVisible(), false);
                        cg.setPreferredSize(new Dimension(60, 90));
                        if (estCible) {
                            cg.setSelectionnable(true, () -> {
                                choixCartePreselectionne = 1; 
                                mettreAJour(joueurs); 
                                clearActionPanel();
                                controleur.setInputJoueur(j);
                            });
                        }
                        offerPanel.add(cg);
                    }
                    if (j.getOffre().getFaceCachee() != null) {
                        CarteGraphique cg = new CarteGraphique(null, true);
                        cg.setPreferredSize(new Dimension(60, 90));
                        if (estCible) {
                            cg.setSelectionnable(true, () -> {
                                mettreAJour(joueurs); 
                                clearActionPanel();
                                controleur.setInputJoueur(j);
                            });
                        }
                        offerPanel.add(cg);
                    }
                }
                playerPanel.add(offerPanel, BorderLayout.NORTH);
                
                JPanel jestPanel = new JPanel(new FlowLayout());
                jestPanel.setOpaque(false);
                jestPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Jest", 0,0,null, Color.WHITE));
                for (Carte c : j.getJest().getCartes()) {
                    CarteGraphique cg = new CarteGraphique(c, false);
                    cg.setPreferredSize(new Dimension(50, 75));
                    jestPanel.add(cg);
                }
                playerPanel.add(jestPanel, BorderLayout.CENTER);
                centerPanel.add(playerPanel);
            }
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }
}
