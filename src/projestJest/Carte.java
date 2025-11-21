package projestJest;

public class Carte {
	
	public ValeurCarte getValeur() {
		return valeur;
	}

	public void setValeur(ValeurCarte valeur) {
		this.valeur = valeur;
	}

	public SuiteCarte getSuite() {
		return suite;
	}

	public void setSuite(SuiteCarte suite) {
		this.suite = suite;
	}



	private ValeurCarte valeur;
	private SuiteCarte suite;
	
	/**
	 * Constructeur pour une carte normale
	 * @param valeur
	 * @param suite
	 */
	public Carte(ValeurCarte valeur, SuiteCarte suite) {
		this.valeur = valeur;
		this.suite = suite;
	}
	
	/**
	 * Constructeur pour une carte Joker
	 * @param valeur
	 */
	public Carte(ValeurCarte valeur) {
	    if (valeur != ValeurCarte.JOKER) {
	        throw new IllegalArgumentException("Ce constructeur est réservé aux Jokers");
	    }
	    this.valeur = valeur;
	    this.suite = null; // Joker n'a pas de couleur
	}
	
	
	@Override
    public String toString() {
        if (valeur == ValeurCarte.JOKER) {
            return "Joker";
        } else {
            return valeur + " de " + suite;
        }
    }

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Carte carte1 = new Carte(ValeurCarte.AS,SuiteCarte.PIQUE);
		Carte carte2 = new Carte(ValeurCarte.JOKER);
		
		System.out.println(carte1.getValeur() + " de " + carte1.getSuite());
		System.out.println(carte2.getValeur() + " de " + carte2.getSuite());

	}

}
