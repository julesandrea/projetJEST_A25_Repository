package projestJest;

import java.util.List;
import projestJest.Carte.Carte;
import projestJest.Joueur.*;
import projestJest.Strategie.*;
import projestJest.Variante.*;
import javax.swing.SwingUtilities;
import java.util.Scanner;
import java.util.ArrayList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * La classe Controleur agit comme l'intermédiaire central entre le modèle (la Partie) et les vues (VueConsole et VueGraphique).
 * Elle implémente le patron de conception MVC (Modèle-Vue-Contrôleur) en assurant la synchronisation entre les données du jeu
 * et leur affichage.
 * 
 * En tant qu'implémentation de l'interface InterfaceUtilisateur, le Contrôleur gère toutes les demandes d'interaction
 * (choix de carte, d'adversaire, etc.) indifféremment de la source (console ou graphique).
 * 
 * En tant qu'implémentation de PropertyChangeListener, il écoute les notifications de changement d'état provenant du modèle
 * (Partie) et répercute ces changements vers les vues appropriées en leur transmettant les données nécessaires.
 */
public class Controleur implements InterfaceUtilisateur, PropertyChangeListener {

    /**
     * Référence vers le modèle du jeu.
     */
    private Partie partie;

    /**
     * Référence vers la vue textuelle (console).
     */
    private VueConsole vueConsole;

    /**
     * Référence vers la vue graphique (fenêtre Swing).
     */
    private VueGraphique vueGraphique;
    
    /**
     * Enumération définissant le type d'entrée attendue de l'utilisateur.
     * Utilisé pour synchroniser les threads d'attente lors des demandes interactives.
     */
    private enum InputMode { NONE, INT, STRING, JOUEUR }

    /**
     * Mode d'entrée actuel en attente.
     */
    private InputMode currentMode = InputMode.NONE;

    /**
     * Valeur entière minimale autorisée pour la saisie courante.
     */
    private int minInt;

    /**
     * Valeur entière maximale autorisée pour la saisie courante.
     */
    private int maxInt;

    /**
     * Liste des joueurs valides pour la sélection d'un adversaire.
     */
    private List<Joueur> validJoueurs;
    
    /**
     * Objet de verrouillage pour la synchronisation des threads d'entrée/sortie.
     */
    private final Object lock = new Object();

    /**
     * Variable stockant le résultat de la saisie utilisateur.
     */
    private Object result = null;

    /**
     * Constructeur du Contrôleur.
     * Initialise les vues, s'abonne aux événements du modèle et lance le thread d'écoute de la console.
     * 
     * @param partie L'instance du modèle de jeu à contrôler.
     */
    public Controleur(Partie partie) {
        this.partie = partie;
        this.vueConsole = new VueConsole(); 
        this.vueGraphique = new VueGraphique(this); 
        
        this.partie.addPropertyChangeListener(this);
        
        Thread consoleThread = new Thread(this::consoleLoop);
        consoleThread.setDaemon(true);
        consoleThread.start();
        
        SwingUtilities.invokeLater(() -> {
            vueGraphique.setVisible(true);
        });
    }
    
    /**
     * Met à jour la référence vers la partie et synchronise la vue graphique.
     * Utiliser principalement lors du chargement d'une partie sauvegardée.
     * 
     * @param partie La nouvelle instance de la partie.
     */
    public void setPartie(Partie partie) {
        this.partie = partie;
        this.vueGraphique.setPartie(partie);
    }
    
    /**
     * Gère la configuration initiale de la partie via l'interface utilisateur.
     * Permet de choisir entre nouvelle partie ou chargement, configurer les variantes,
     * les extensions et le nombre de joueurs (humains et virtuels).
     */
    public void configurerPartie() {
        afficherMessage("=== JEST ===");
        
        int choixMenu = demanderChoixInt("Menu : 1-Nouvelle Partie, 2-Charger", 1, 2);

        if (choixMenu == 2) {
            Partie chargee = Partie.chargerPartie();
            if (chargee != null) {
                afficherMessage("Partie chargée avec succès !");
                this.setPartie(chargee);
                chargee.setVue(this); 
                return;
            } else {
                afficherMessage("Echec chargement, nouvelle partie.");
            }
        }
        
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
        
        for (int i = 1; i <= nbHumains; i++) {
            String nom = demanderChaine("Nom du joueur humain " + i);
            partie.ajouterJoueur(new JoueurHumain(nom));
        }
        
        int nbJoueursActuel = nbHumains;
        if (nbJoueursActuel < 4) {
            int maxBots = 4 - nbJoueursActuel;
            int minBots = (nbJoueursActuel < 3) ? (3 - nbJoueursActuel) : 0; 
            
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

    /**
     * Boucle principale d'écoute de la console (System.in).
     * Lit les entrées ligne par ligne et les transmet pour vérification.
     */
    private void consoleLoop() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            if (sc.hasNextLine()) {
                String line = sc.nextLine();
                verifierConsoleInput(line);
            }
        }
    }
    
    /**
     * Vérifie si l'entrée reçue de la console correspond à la demande en cours (Entier, Chaîne ou Joueur).
     * Si l'entrée est valide, elle débloque le thread en attente.
     * 
     * @param line La ligne de texte saisie dans la console.
     */
    private void verifierConsoleInput(String line) {
        synchronized(lock) {
            if (currentMode == InputMode.NONE) return; 
            
            try {
                if (currentMode == InputMode.INT) {
                    try {
                        int val = Integer.parseInt(line.trim());
                        if (val >= minInt && val <= maxInt) {
                            result = val;
                            lock.notifyAll(); 
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
                
            }
        }
    }

    /**
     * Définit le résultat de l'entrée utilisateur sous forme d'entier (depuis l'interface graphique).
     * Réveille le thread en attente si le mode correspond.
     * 
     * @param val La valeur entière saisie.
     */
    public void setInputInt(int val) {
        synchronized(lock) {
            if (currentMode == InputMode.INT) {
                result = val;
                lock.notifyAll();
            } else if (currentMode == InputMode.JOUEUR) {
                if (validJoueurs != null && val >= 1 && val <= validJoueurs.size()) {
                    result = validJoueurs.get(val - 1);
                    lock.notifyAll();
                }
            }
        }
    }

    /**
     * Définit le résultat de l'entrée utilisateur sous forme de chaîne (depuis l'interface graphique).
     * Réveille le thread en attente si le mode correspond.
     * 
     * @param val La chaîne de caractères saisie.
     */
    public void setInputString(String val) {
        synchronized(lock) {
            if (currentMode == InputMode.STRING) {
                result = val;
                lock.notifyAll();
            }
        }
    }
    
    /**
     * Définit le résultat de l'entrée utilisateur sous forme de Joueur (depuis l'interface graphique).
     * Réveille le thread en attente si le mode correspond.
     * 
     * @param j Le joueur sélectionné.
     */
    public void setInputJoueur(Joueur j) {
        synchronized(lock) {
            if (currentMode == InputMode.JOUEUR) {
                result = j;
                lock.notifyAll();
            }
        }
    }

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
        vueGraphique.afficherTour(numTour, partie.getJoueurs()); 
    }

    @Override
    public void afficherOffres(List<Joueur> joueurs) {
        vueConsole.afficherOffres(joueurs);
        vueGraphique.afficherOffres(joueurs);
    }

    @Override
    public void afficherFinTour(int numTour) {
        vueConsole.afficherFinTour(numTour);
        vueGraphique.afficherFinTour(numTour, partie.getJoueurs());
    }

    @Override
    public void afficherResultats(List<Joueur> joueurs, Joueur vainqueur, int scoreMax) {
        vueConsole.afficherResultats(joueurs, vainqueur, scoreMax);
        vueGraphique.afficherResultats(joueurs, partie.getTrophees());
    }
    
    /**
     * Prépare le contrôleur à attendre une entrée utilisateur d'un certain type.
     * Réinitialise le résultat précédent.
     * 
     * @param mode Le mode d'entrée attendu (INT, STRING, JOUEUR).
     */
    private void prepareWait(InputMode mode) {
        synchronized(lock) {
            currentMode = mode;
            result = null;
        }
    }
    
    /**
     * Termine la phase d'attente d'entrée utilisateur.
     * Réinitialise le mode à NONE.
     */
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
        
        System.out.println(question + " [" + min + "-" + max + "] : "); 
        vueGraphique.demanderChoixInt(question, min, max); 
        
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

        System.out.println("\n" + j.getNom() + ", voici vos cartes : ");
        System.out.println("1 : " + c1);
        System.out.println("2 : " + c2);
        
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
        
        System.out.println("\n" + j.getNom() + ", choisissez un joueur chez qui prendre une carte :");
        for (int i = 0; i < adversaires.size(); i++) {
            Joueur adv = adversaires.get(i);
            String visible = (adv.getOffre() != null && adv.getOffre().getFaceVisible() != null) 
                           ? adv.getOffre().getFaceVisible().toString() 
                           : "Aucune";
            System.out.println((i + 1) + " : " + adv.getNom() + " (Carte visible : " + visible + ")");
        }
        System.out.println("Numéro du joueur cible : ");
        
        vueGraphique.demanderChoixAdversaire(j, adversaires, partie.getJoueurs());
        
        synchronized(lock) {
             while (result == null) {
                try { lock.wait(); } catch (InterruptedException e) {}
            }
            finishWait();
            return (Joueur) result;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        switch (prop) {
            case Partie.PROP_MESSAGE:
                afficherMessage((String) evt.getNewValue());
                break;
            case Partie.PROP_TROPHEES:
                List<Carte> t = (List<Carte>) evt.getNewValue();
                vueConsole.afficherTrophees(t);
                vueGraphique.afficherTrophees(t);
                break;
            case Partie.PROP_TOUR:
                int tour = (Integer) evt.getNewValue();
                vueConsole.afficherTour(tour);
                vueGraphique.afficherTour(tour, partie.getJoueurs());
                break;
            case Partie.PROP_OFFRES:
                 List<Joueur> joueurs = (List<Joueur>) evt.getNewValue();
                 vueConsole.afficherOffres(joueurs);
                 vueGraphique.afficherOffres(joueurs);
                 break;
            case Partie.PROP_FIN_TOUR:
                int ft = (Integer) evt.getNewValue();
                vueConsole.afficherFinTour(ft);
                vueGraphique.afficherFinTour(ft, partie.getJoueurs());
                break;
            case Partie.PROP_RESULTATS:
                List<Joueur> jList = (List<Joueur>) evt.getNewValue();
                
                Joueur winner = null;
                int maxScore = Integer.MIN_VALUE;
                for (Joueur j : jList) {
                    if (j.getScore() > maxScore) {
                        maxScore = j.getScore();
                        winner = j;
                    }
                }
                
                vueConsole.afficherResultats(jList, winner, maxScore); 
                vueGraphique.afficherResultats(jList, partie.getTropheesInitial());
                break;
        }
    }
}
