package projestJest;

import java.util.List;
import projestJest.Carte.Carte;
import projestJest.Joueur.*;
import projestJest.Strategie.*;
import projestJest.Variante.*;
import javax.swing.SwingUtilities;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Le Controleur agit comme l'intermédiaire entre la Partie (Modèle) et les Vues (Console et Graphique).
 * Il synchronise les entrées venant de la Console et du GUI.
 */
public class Controleur implements InterfaceUtilisateur {

    private Partie partie;
    private VueConsole vueConsole;
    private VueGraphique vueGraphique;
    
    // Etat de la demande en cours
    private enum InputMode { NONE, INT, STRING, JOUEUR }
    private InputMode currentMode = InputMode.NONE;
    private int minInt, maxInt;
    private List<Joueur> validJoueurs; // Pour choix adversaire
    
    // Résultat
    private final Object lock = new Object();
    private Object result = null;

    public Controleur(Partie partie) {
        this.partie = partie;
        this.vueConsole = new VueConsole(); 
        this.vueGraphique = new VueGraphique(this, partie);
        
        // Console Input Thread
        Thread consoleThread = new Thread(this::consoleLoop);
        consoleThread.setDaemon(true);
        consoleThread.start();
        
        // Start GUI
        SwingUtilities.invokeLater(() -> {
            vueGraphique.setVisible(true);
        });
    }
    
    public void setPartie(Partie partie) {
        this.partie = partie;
        this.vueGraphique.setPartie(partie);
    }
    
    /**
     * Gère la configuration initiale de la partie via l'interface.
     */
    public void configurerPartie() {
        afficherMessage("=== JEST ===");
        
        int choixMenu = demanderChoixInt("Menu : 1-Nouvelle Partie, 2-Charger", 1, 2);

        if (choixMenu == 2) {
            Partie chargee = Partie.chargerPartie();
            if (chargee != null) {
                afficherMessage("Partie chargée avec succès !");
                this.setPartie(chargee);
                chargee.setVue(this); // Important : reconnecter la vue à la partie chargée
                return;
            } else {
                afficherMessage("Echec chargement, nouvelle partie.");
            }
        }
        
        // Nouvelle Partie
        int choixVariante = demanderChoixInt("Variante : 1-Classique, 2-As=5, 3-Coeurs+", 1, 3);
        switch (choixVariante) {
            case 2: partie.setVariante(new VarianteAsToujours5()); break;
            case 3: partie.setVariante(new VarianteCoeursJamaisNegatifs()); break;
            default: partie.setVariante(new VarianteClassique()); break;
        }
        
        int choixExt = demanderChoixInt("Extensions ? 1-Oui, 2-Non", 1, 2);
        if (choixExt == 1) {
            partie.activerExtensions();
        }

        int nbHumains = demanderChoixInt("Combien de joueurs humains ? (0-4)", 0, 4);
        // On limite a 4 total, donc calcul du reste pour les bots... ou on demande aussi.
        // Simplification : max 4 joueurs.
        
        for (int i = 1; i <= nbHumains; i++) {
            String nom = demanderChaine("Nom du joueur humain " + i);
            partie.ajouterJoueur(new JoueurHumain(nom));
        }
        
        int nbJoueursActuel = nbHumains;
        if (nbJoueursActuel < 4) {
            int maxBots = 4 - nbJoueursActuel;
            int minBots = (nbJoueursActuel < 3) ? (3 - nbJoueursActuel) : 0; // min 3 joueurs total
            
            int nbBots = demanderChoixInt("Combien de joueurs virtuels ? (" + minBots + "-" + maxBots + ")", minBots, maxBots);
            
            for (int i = 1; i <= nbBots; i++) {
                String nomBot = "Bot" + i;
                int strat = demanderChoixInt("Strategie " + nomBot + " : 1-Rand, 2-Off, 3-Def", 1, 3);
                Strategie strategie;
                switch(strat) {
                    case 2: strategie = new StrategieOffensive(); break;
                    case 3: strategie = new StrategieDefensive(); break;
                    default: strategie = new StrategieAleatoire(); break;
                }
                partie.ajouterJoueur(new JoueurVirtuel(nomBot, strategie));
            }
        }
    }

    private void consoleLoop() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            if (sc.hasNextLine()) {
                String line = sc.nextLine();
                verifierConsoleInput(line);
            }
        }
    }
    
    private void verifierConsoleInput(String line) {
        synchronized(lock) {
            if (currentMode == InputMode.NONE) return; // Ignorer
            
            try {
                if (currentMode == InputMode.INT) {
                    try {
                        int val = Integer.parseInt(line.trim());
                        if (val >= minInt && val <= maxInt) {
                            result = val;
                            lock.notifyAll(); // Succès
                        } else {
                            System.out.println("Valeur hors limites (" + minInt + "-" + maxInt + "). Réessayez :");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Veuillez entrer un entier valide :");
                    }
                } else if (currentMode == InputMode.STRING) {
                    if (!line.trim().isEmpty()) {
                        result = line.trim();
                        lock.notifyAll();
                    }
                } else if (currentMode == InputMode.JOUEUR) {
                     try {
                        int index = Integer.parseInt(line.trim());
                        if (index >= 1 && index <= validJoueurs.size()) {
                            result = validJoueurs.get(index - 1);
                            lock.notifyAll();
                        } else {
                            System.out.println("Choix invalide (1-" + validJoueurs.size() + "). Réessayez :");
                        }
                     } catch(NumberFormatException e) {
                         System.out.println("Entrez le numéro du joueur :");
                     }
                }
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    // --- GUI Callbacks ---

    public void setInputInt(int val) {
        synchronized(lock) {
            if (currentMode == InputMode.INT) {
                result = val;
                lock.notifyAll();
            } else if (currentMode == InputMode.JOUEUR) {
                // Mapping index (1-based) vers Joueur
                if (validJoueurs != null && val >= 1 && val <= validJoueurs.size()) {
                    result = validJoueurs.get(val - 1);
                    lock.notifyAll();
                }
            }
        }
    }

    public void setInputString(String val) {
        synchronized(lock) {
            if (currentMode == InputMode.STRING) {
                result = val;
                lock.notifyAll();
            }
        }
    }
    
    public void setInputJoueur(Joueur j) {
        synchronized(lock) {
            if (currentMode == InputMode.JOUEUR) {
                result = j;
                lock.notifyAll();
            }
        }
    }

    // --- Output Broadcast ---

    @Override
    public void afficherMessage(String msg) {
        vueConsole.afficherMessage(msg);
        vueGraphique.afficherMessage(msg);
    }

    @Override
    public void afficherTrophees(List<Carte> trophees) {
        vueConsole.afficherTrophees(trophees);
        vueGraphique.afficherTrophees(trophees);
    }

    @Override
    public void afficherTour(int numTour) {
        vueConsole.afficherTour(numTour);
        vueGraphique.mettreAJour(); 
    }

    @Override
    public void afficherOffres(List<Joueur> joueurs) {
        vueConsole.afficherOffres(joueurs);
        vueGraphique.mettreAJour();
    }

    @Override
    public void afficherFinTour(int numTour) {
        vueConsole.afficherFinTour(numTour);
        vueGraphique.mettreAJour();
    }

    @Override
    public void afficherResultats(List<Joueur> joueurs, Joueur vainqueur, int scoreMax) {
        vueConsole.afficherResultats(joueurs, vainqueur, scoreMax);
        vueGraphique.afficherResultats(joueurs, vainqueur, scoreMax);
    }

    // --- Input Waiters ---
    
    private void prepareWait(InputMode mode) {
        synchronized(lock) {
            currentMode = mode;
            result = null;
        }
    }
    
    private void finishWait() {
        synchronized(lock) {
            currentMode = InputMode.NONE;
        }
    }

    @Override
    public int demanderChoixInt(String question, int min, int max) {
        prepareWait(InputMode.INT);
        this.minInt = min;
        this.maxInt = max;
        
        System.out.println(question + " [" + min + "-" + max + "] : "); // Affichage Console Prompt
        vueGraphique.demanderChoixInt(question, min, max); // Affichage GUI Prompt
        
        synchronized(lock) {
            while (result == null) {
                try { lock.wait(); } catch (InterruptedException e) {}
            }
            finishWait();
            return (Integer) result;
        }
    }

    @Override
    public String demanderChaine(String question) {
        prepareWait(InputMode.STRING);
        System.out.println(question);
        vueGraphique.demanderChaine(question);
        
        synchronized(lock) {
             while (result == null) {
                try { lock.wait(); } catch (InterruptedException e) {}
            }
            finishWait();
            return (String) result;
        }
    }

    @Override
    public int demanderChoixOffre(Joueur j, Carte c1, Carte c2) {
        prepareWait(InputMode.INT);
        this.minInt = 1;
        this.maxInt = 2;

        // Logique spécifique d'affichage "VueConsole" reproduite ici pour le prompt
        System.out.println("\n" + j.getNom() + ", voici vos cartes : ");
        System.out.println("1 : " + c1);
        System.out.println("2 : " + c2);
        
        // Appel spécifique GUI
        vueGraphique.demanderChoixOffre(j, c1, c2);

        synchronized(lock) {
            while (result == null) {
                try { lock.wait(); } catch (InterruptedException e) {}
            }
            finishWait();
            return (Integer) result;
        }
    }

    @Override
    public int demanderChoixPrise(Joueur j, projestJest.Offre o) {
        prepareWait(InputMode.INT);
        this.minInt = 1;
        this.maxInt = 2;
        
        System.out.println("\n" + j.getNom() + ", offre sélectionnée : " + o);
        System.out.println("1 : Prendre la carte visible");
        System.out.println("2 : Prendre la carte cachée");
        
        // Appel spécifique GUI
        vueGraphique.demanderChoixPrise(j, o);
        
        synchronized(lock) {
            while (result == null) {
                try { lock.wait(); } catch (InterruptedException e) {}
            }
            finishWait();
            return (Integer) result;
        }
    }

    @Override
    public Joueur demanderChoixAdversaire(Joueur j, List<Joueur> adversaires) {
        prepareWait(InputMode.JOUEUR);
        this.validJoueurs = adversaires;
        
        // Prompt Console (inspiré de VueConsole)
        System.out.println("\n" + j.getNom() + ", choisissez un joueur chez qui prendre une carte :");
        for (int i = 0; i < adversaires.size(); i++) {
            Joueur adv = adversaires.get(i);
            String visible = (adv.getOffre() != null && adv.getOffre().getFaceVisible() != null) 
                           ? adv.getOffre().getFaceVisible().toString() 
                           : "Aucune";
            System.out.println((i + 1) + " : " + adv.getNom() + " (Carte visible : " + visible + ")");
        }
        System.out.println("Numéro du joueur cible : ");
        
        // Prompt GUI
        vueGraphique.demanderChoixAdversaire(j, adversaires);
        
        synchronized(lock) {
             while (result == null) {
                try { lock.wait(); } catch (InterruptedException e) {}
            }
            finishWait();
            return (Joueur) result;
        }
    }
}
