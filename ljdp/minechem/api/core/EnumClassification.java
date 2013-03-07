package ljdp.minechem.api.core;

public enum EnumClassification {
	nonmetal	("Non-metal"),
	inertGas	("Inert gas"),
	halogen		("Halogen"),
	alkaliMetal	("Alkali metal"), 
	alkalineEarthMetal	("Alkaline earth metal"),
	semimetallic		("Semimetallic"),
	otherMetal			("Other metal"),
	transitionMetal		("Transition metal"),
	lanthanide			("Lanthanide"),
	actinide	("Actinide"),
	solid		("Solid"),
	gas			("Gaseous"),
	liquid		("Liquid");
	
	
	private final String descriptiveName;
	EnumClassification(String descriptiveName) {
		this.descriptiveName = descriptiveName;
	}
	
	public String descriptiveName() {
		return descriptiveName;
	}
}
