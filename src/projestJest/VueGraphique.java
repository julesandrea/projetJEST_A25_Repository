package projestJest;

import java.awt.*;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import projestJest.Carte.Carte;
import projestJest.Joueur.Joueur;

public class VueGraphique extends JFrame implements Observer {

    private Controleur controleur;
    private Partie partie;
    
    private JPanel centerPanel; // Le tapis de jeu (Joueurs + Offres/Jest)
    private JPanel actionPanel; // Zone d'action (Main du joueur, Choix)
    private JLabel statusLabel; // Instruction courante
    
    public VueGraphique(Controleur controleur, Partie partie) {
        this.controleur = controleur;
        this.partie = partie;
        partie.addObserver(this); 
        
        setTitle("JEST - Interface Graphique");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // --- TOP : Status ---
        statusLabel = new JLabel("En attente...");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(statusLabel, BorderLayout.NORTH);
        
        // --- CENTER : Table de jeu ---
        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(0, 1, 10, 10)); // 1 ligne par joueur (ou grille selon nb)
        centerPanel.setBackground(new Color(34, 139, 34)); // Tapis vert
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        add(scrollPane, BorderLayout.CENTER);
        
        // --- BOTTOM : Actions ---
        actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        actionPanel.setPreferredSize(new Dimension(1200, 250));
        actionPanel.setBackground(Color.DARK_GRAY);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    // --- Observer Update ---
    @Override
    public void update(Observable o, Object arg) {
        mettreAJour();
    }
    
    public void setPartie(Partie p) {
        this.partie = p;
        if (p != null) p.addObserver(this);
    }

    // Redessine tout le plateau (Modèle -> Vue)
    public void mettreAJour() {
        centerPanel.removeAll();
        
        if (partie == null) return;
        List<Joueur> joueurs = partie.getJoueurs();
        if (joueurs != null) {
            
            // Adapter le grid layout selon le nombre de joueurs
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
                playerPanel.setBackground(new Color(34, 100, 34)); // Vert un peu plus sombre

                // -- Zone Offre (Haut du Panel Joueur) --
                JPanel offerPanel = new JPanel(new FlowLayout());
                offerPanel.setOpaque(false);
                offerPanel.add(new JLabel("Offre: "));
                
                if (j.getOffre() != null) {
                    if (j.getOffre().getFaceVisible() != null) {
                        CarteGraphique cg = new CarteGraphique(j.getOffre().getFaceVisible(), false);
                        cg.setPreferredSize(new Dimension(60, 90)); // Plus petit pour le plateau
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
                
                // -- Zone Jest (Centre/Bas du Panel Joueur) --
                JPanel jestPanel = new JPanel(new FlowLayout());
                jestPanel.setOpaque(false);
                jestPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Jest", 0,0,null, Color.WHITE));
                
                for (Carte c : j.getJest().getCartes()) {
                    CarteGraphique cg = new CarteGraphique(c, false);
                    cg.setPreferredSize(new Dimension(50, 75)); // Encore plus petit pour le Jest
                    jestPanel.add(cg);
                }
                
                playerPanel.add(jestPanel, BorderLayout.CENTER);
                
                centerPanel.add(playerPanel);
            }
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    // --- Inputs Methods called by Controleur ---
    
    // Etat pour la présélection (Clic sur table -> Joueur + Choix Carte)
    private Integer choixCartePreselectionne = null;

    // --- Inputs Methods called by Controleur ---
    
    private void clearActionPanel() {
        actionPanel.removeAll();
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    public void afficherMessage(String msg) {
        System.out.println("[GUI Log] " + msg);
        // Popup pour les événements importants (Trophées)
        if (msg.toLowerCase().contains("trophée") || msg.contains("vainqueur")) {
            JOptionPane.showMessageDialog(this, msg, "Information Jeu", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void afficherTrophees(java.util.List<Carte> trophees) {
        StringBuilder sb = new StringBuilder("Trophées en jeu :\n");
        for (Carte c : trophees) {
            sb.append("- ").append(c.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Trophées", JOptionPane.INFORMATION_MESSAGE);
    }

    public void afficherResultats(java.util.List<Joueur> joueurs, Joueur vainqueur, int scoreMax) {
        // Trier les joueurs par score (descendant)
        joueurs.sort((j1, j2) -> Integer.compare(j2.getScore(), j1.getScore()));

        JDialog podium = new JDialog(this, "Podium - Résultats Finaux", true);
        podium.setSize(600, 600);
        podium.setLayout(new BorderLayout());

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(34, 139, 34));

        JLabel title = new JLabel("RÉSULTATS FINAUX", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(255, 215, 0)); // Gold
        title.setOpaque(true);
        title.setBackground(new Color(34, 100, 34));
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        podium.add(title, BorderLayout.NORTH);

        int rank = 1;
        for (Joueur j : joueurs) {
            JPanel pPanel = new JPanel(new BorderLayout());
            pPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.WHITE, 2)
            ));
            pPanel.setBackground(new Color(34, 100, 34)); // Dark Green
            
            // Header: Rank - Name - Score
            JLabel lbl = new JLabel("  #" + rank + "  " + j.getNom() + " : " + j.getScore() + " points");
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.BOLD, 18));
            pPanel.add(lbl, BorderLayout.NORTH);
            
            // Jest Cards
            JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            cardPanel.setOpaque(false);
            for (Carte c : j.getJest().getCartes()) {
                 CarteGraphique cg = new CarteGraphique(c, false);
                 cg.setPreferredSize(new Dimension(50, 75));
                 cardPanel.add(cg);
            }
            pPanel.add(cardPanel, BorderLayout.CENTER);
            
            listPanel.add(pPanel);
            rank++;
        }

        JScrollPane scroll = new JScrollPane(listPanel);
        podium.add(scroll, BorderLayout.CENTER);

        JButton okBtn = new JButton("Fermer");
        okBtn.addActionListener(e -> podium.dispose());
        podium.add(okBtn, BorderLayout.SOUTH);

        podium.setLocationRelativeTo(this);
        podium.setVisible(true);
    }

    public void afficherTour(int numTour) {
        statusLabel.setText("Tour " + numTour);
        mettreAJour(); 
    }

    public void afficherOffres(java.util.List<Joueur> joueurs) {
        mettreAJour(); 
    }

    public void afficherFinTour(int numTour) {
        mettreAJour(); 
    }

    // Parse options like "1-Option" or "1:Option"
    private String extractLabel(String question, int value) {
        String[] parts = question.split(",");
        for (String p : parts) {
            p = p.trim();
            // Check for "value-" or "value:" or "value -"
            // We search for the number followed by separator
            String valStr = String.valueOf(value);
            int valIdx = p.indexOf(valStr);
            
            while (valIdx != -1) {
                // Verify it's a whole word number (not 11 for 1)
                boolean startBound = (valIdx == 0) || !Character.isDigit(p.charAt(valIdx - 1));
                if (startBound) {
                    // Check what follows
                    int afterValIdx = valIdx + valStr.length();
                    // Skip spaces
                    while (afterValIdx < p.length() && p.charAt(afterValIdx) == ' ') afterValIdx++;
                    
                    if (afterValIdx < p.length()) {
                        char c = p.charAt(afterValIdx);
                        if (c == '-' || c == ':') {
                            // Found separator. extract potential label
                            String potential = p.substring(afterValIdx + 1).trim();
                            // Fix: if potential label starts with digit, it's likely a range (0-4), not a label
                            if (!potential.isEmpty() && !Character.isDigit(potential.charAt(0))) {
                                return potential;
                            }
                        }
                    }
                }
                // Search next occurrence
                valIdx = p.indexOf(valStr, valIdx + 1);
            }
        }
        return String.valueOf(value); // Fallback
    }

    public void demanderChoixInt(String question, int min, int max) {
        clearActionPanel();
        statusLabel.setText(question.split("[:]")[0]); // Titre court si possible
        
        // Parsing des labels si présents dans la question
        if (question.contains(min + "-") || question.contains(min + ":")) {
            for (int i = min; i <= max; i++) {
                final int val = i;
                JButton btn = new JButton(extractLabel(question, val));
                btn.setPreferredSize(new Dimension(150, 50));
                btn.addActionListener(e -> { clearActionPanel(); controleur.setInputInt(val); });
                actionPanel.add(btn);
            }
        } else {
            // Cas spécial Sauvegarde (0-1) Hardcodé si parsing echoue ou format specifique
             if (min == 0 && max == 1) {
                JButton btnContinuer = new JButton("Continuer");
                JButton btnSauvegarder = new JButton("Sauvegarder");
                btnContinuer.addActionListener(e -> { clearActionPanel(); controleur.setInputInt(0); });
                btnSauvegarder.addActionListener(e -> { clearActionPanel(); controleur.setInputInt(1); });
                actionPanel.add(btnContinuer);
                actionPanel.add(btnSauvegarder);
            } else {
                // Fallback number buttons
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

    public void demanderChoixOffre(Joueur j, Carte c1, Carte c2) {
        clearActionPanel();
        statusLabel.setText(j.getNom() + " : Cliquez sur la carte à garder VISIBLE");
        
        // Carte 1
        CarteGraphique cg1 = new CarteGraphique(c1, false);
        cg1.setSelectionnable(true, () -> {
            clearActionPanel();
            statusLabel.setText("En attente...");
            controleur.setInputInt(1);
        });
        
        // Carte 2
        CarteGraphique cg2 = new CarteGraphique(c2, false);
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

    public void demanderChoixPrise(Joueur j, projestJest.Offre o) {
        // SI on a une présélection (clic sur table), on l'utilise directement !
        if (choixCartePreselectionne != null) {
            int choix = choixCartePreselectionne;
            choixCartePreselectionne = null; // reset
            
            // On le fait dans un thread swing utilities invokeLater pour laisser le temps au redraw si besoin
            SwingUtilities.invokeLater(() -> controleur.setInputInt(choix));
            return; 
        }

        clearActionPanel();
        statusLabel.setText(j.getNom() + " : Cliquez sur la carte à PRENDRE");
        
        if (o.getFaceVisible() != null) {
            CarteGraphique cgVis = new CarteGraphique(o.getFaceVisible(), false);
            cgVis.setSelectionnable(true, () -> {
                clearActionPanel();
                controleur.setInputInt(1); // 1 = Visible
            });
            actionPanel.add(cgVis);
        }
        
        actionPanel.add(Box.createHorizontalStrut(50));
        
        // Carte Cachée
        CarteGraphique cgCache = new CarteGraphique(null, true);
        cgCache.setSelectionnable(true, () -> {
            clearActionPanel();
            controleur.setInputInt(2); // 2 = Cachée
        });
        actionPanel.add(cgCache);
        
        actionPanel.revalidate();
        actionPanel.repaint();
    }
    
    public void demanderChoixAdversaire(Joueur j, List<Joueur> adversaires) {
        clearActionPanel();
        statusLabel.setText(j.getNom() + " : Cliquez sur une carte adverse sur le tapis pour la voler !");
        choixCartePreselectionne = null;
        
        // On doit rendre les cartes du plateau cliquables.
        // Pour cela, on reconstruit le plateau avec mode interactif
        mettreAJourInteractif(adversaires);
    }
    
    // Version interactive de mettreAJour pour la phase de vol
    private void mettreAJourInteractif(List<Joueur> cibles) {
        centerPanel.removeAll();
        
        if (partie == null) return;
        List<Joueur> joueurs = partie.getJoueurs();
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
                
                // Si ce joueur est une cible valide, on rend ses cartes cliquables
                boolean estCible = cibles.contains(j);

                if (j.getOffre() != null) {
                    if (j.getOffre().getFaceVisible() != null) {
                        CarteGraphique cg = new CarteGraphique(j.getOffre().getFaceVisible(), false);
                        cg.setPreferredSize(new Dimension(60, 90));
                        if (estCible) {
                            cg.setSelectionnable(true, () -> {
                                choixCartePreselectionne = 1; // Visible
                                // Remettre le plateau en état normal (non cliquable) visuellement
                                mettreAJour(); 
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
                                choixCartePreselectionne = 2; // Cachée
                                mettreAJour(); 
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
