package ljdp.minechem.api.core;

public class Chemical {
	
	public int amount;
	
	public Chemical(int amount) {
		this.amount = amount;
	}
	
	public boolean sameAs(Chemical chemical) {
		return false;
	}
	
}
