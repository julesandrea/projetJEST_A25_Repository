package projestJest.Joueur;

import projestJest.*;
import projestJest.Carte.*;
import java.util.List;

/**
 * Représente un joueur humain.
 * Ses décisions sont prises via des interactions avec l'interface utilisateur (Console ou Graphique).
 */
public class JoueurHumain extends Joueur {

    /**
     * Constructeur pour un joueur humain.
     * 
     * @param nom Le nom du joueur.
     */
    public JoueurHumain(String nom) {
        super(nom);
    }

    @Override
    public void faireOffre(Carte c1, Carte c2, InterfaceUtilisateur vue) {
        int choix = vue.demanderChoixOffre(this, c1, c2);

        if (choix == 1)
            offre = new Offre(c1, c2);
        else
            offre = new Offre(c2, c1);
            
        vue.afficherMessage(nom + " a fait son offre.");
    }

    @Override
    public Carte choisirCarte(Offre offre, InterfaceUtilisateur vue) {
        int choix = vue.demanderChoixPrise(this, offre);
        return (choix == 1 ? offre.prendreVisible() : offre.prendreCachee());
    }

    @Override
    public Joueur choisirJoueurCible(List<Joueur> joueursValides, InterfaceUtilisateur vue) {
        return vue.demanderChoixAdversaire(this, joueursValides);
    }
}
