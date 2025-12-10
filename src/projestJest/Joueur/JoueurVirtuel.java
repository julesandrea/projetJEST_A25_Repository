package projestJest.Joueur;

import projestJest.*;
import projestJest.Carte.*;
import projestJest.Strategie.*;
import java.util.List;

/**
 * Représente un joueur virtuel (IA).
 * Ses décisions sont déléguées à une stratégie (Pattern Strategy).
 */
public class JoueurVirtuel extends Joueur {

    /**
     * Constructeur pour un joueur virtuel.
     * @param nom Nom du bot.
     * @param strategie La stratégie utilisée par ce bot.
     */
    public JoueurVirtuel(String nom, Strategie strategie) {
        super(nom);
        this.strategie = strategie;
    }

    @Override
    public void faireOffre(Carte c1, Carte c2, InterfaceUtilisateur vue) {
        // Simple : on met la première visible pour l'instant (la stratégie d'offre n'est pas demandée complexe)
        this.offre = new Offre(c1, c2);
        vue.afficherMessage(nom + " (IA) a fait son offre.");
    }

    @Override
    public Carte choisirCarte(Offre offre, InterfaceUtilisateur vue) {
        boolean prendreVisible = strategie.choisirVisibleOuCachee(offre);
        Carte retiree = offre.prendre(prendreVisible);

        vue.afficherMessage(nom + " (IA) a pris : " + retiree);
        return retiree;
    }

    @Override
    public Joueur choisirJoueurCible(List<Joueur> joueursValides, InterfaceUtilisateur vue) {
        return strategie.choisirOffre(joueursValides);
    }
}
