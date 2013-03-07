package ljdp.minechem.common;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import ljdp.minechem.api.core.EnumElement;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.items.ItemArmorRadiationShield;
import ljdp.minechem.common.items.ItemAtomicManipulator;
import ljdp.minechem.common.items.ItemBlockFusion;
import ljdp.minechem.common.items.ItemBlueprint;
import ljdp.minechem.common.items.ItemChemistJournal;
import ljdp.minechem.common.items.ItemElement;
import ljdp.minechem.common.items.ItemFusionStar;
import ljdp.minechem.common.items.ItemLens;
import ljdp.minechem.common.items.ItemMolecule;
import ljdp.minechem.common.items.ItemTestTube;
import ljdp.minechem.common.utils.MinechemHelper;

public class MinechemItems {
	public static ItemElement element;
	public static ItemMolecule molecule;
	public static ItemLens lens;
	public static ItemAtomicManipulator atomicManipulator;
	public static ItemFusionStar fusionStar;
	public static ItemBlueprint blueprint;
	public static ItemTestTube testTube;
	public static ItemChemistJournal journal;
	public static ItemArmorRadiationShield hazmatFeet;
	public static ItemArmorRadiationShield hazmatLegs;
	public static ItemArmorRadiationShield hazmatTorso;
	public static ItemArmorRadiationShield hazmatHead;
	
	public static ItemStack convexLens;
	public static ItemStack concaveLens;
	public static ItemStack projectorLens;
	public static ItemStack microscopeLens;
	
	private static int elementID;
	private static int moleculeID;
	private static int atomicManipulatorID;
	private static int lensID;
	private static int fusionStarID;
	private static int blockFusionID;
	private static int blueprintID;
	private static int testTubeID;
	private static int journalID;
	private static int hazmatFeetID;
	private static int hazmatLegsID;
	private static int hazmatTorsoID;
	private static int hazmatHeadID;
	
	public static void loadConfig(Configuration config) {
		int baseID  = 4736;
		elementID   = getItemConfig(config, "Element", baseID++);
	 	moleculeID  = getItemConfig(config, "Molecule", baseID++);
	 	lensID	 	= getItemConfig(config, "Lens", baseID++);
	 	atomicManipulatorID =getItemConfig(config, "AtomicManipulator", baseID++);
	 	fusionStarID = getItemConfig(config, "FusionStar", baseID++);
	 	blueprintID  = getItemConfig(config, "Blueprint", baseID++);
	 	testTubeID   = getItemConfig(config, "TestTube", baseID++);
	 	journalID    = getItemConfig(config, "ChemistJournal", baseID++);
	 	hazmatFeetID = getItemConfig(config, "HazmatFeet", baseID++);
	 	hazmatLegsID = getItemConfig(config, "HazmatLegs", baseID++);
	 	hazmatTorsoID = getItemConfig(config, "HazmatTorso", baseID++);
	 	hazmatHeadID = getItemConfig(config, "HazmatHead", baseID++);
	}
	
	private static int getItemConfig(Configuration config, String key, int defaultID) {
		return config.getItem(config.CATEGORY_ITEM, key, defaultID).getInt(defaultID);
	}
	
	public static void registerItems() {
		element  = new ItemElement(elementID);
		molecule = new ItemMolecule(moleculeID);
		lens	 = new ItemLens(lensID);
		atomicManipulator = new ItemAtomicManipulator(atomicManipulatorID);
		fusionStar  = new ItemFusionStar(fusionStarID);
		blueprint   = new ItemBlueprint(blueprintID);
		testTube    = new ItemTestTube(testTubeID);
		journal		= new ItemChemistJournal(journalID);
		hazmatFeet  = new ItemArmorRadiationShield(hazmatFeetID, 3, 0.1F);
		hazmatLegs  = new ItemArmorRadiationShield(hazmatLegsID, 2, 0.1F);
		hazmatTorso = new ItemArmorRadiationShield(hazmatTorsoID, 1, 0.5F);
		hazmatHead	= new ItemArmorRadiationShield(hazmatHeadID, 0, 0.2F);
		hazmatFeet.setIconCoord(0, 7);
		hazmatLegs.setIconCoord(0, 6);
		hazmatTorso.setIconCoord(0, 5);
		hazmatHead.setIconCoord(0, 4);
		
		LanguageRegistry.addName(atomicManipulator, MinechemHelper.getLocalString("item.name.atomicmanipulator"));
		LanguageRegistry.addName(fusionStar, MinechemHelper.getLocalString("item.name.fusionStar"));
		LanguageRegistry.addName(testTube, MinechemHelper.getLocalString("item.name.testtube"));
		LanguageRegistry.addName(journal, MinechemHelper.getLocalString("item.name.chemistJournal"));
		LanguageRegistry.addName(hazmatFeet, MinechemHelper.getLocalString("item.name.hazmatFeet"));
		LanguageRegistry.addName(hazmatLegs, MinechemHelper.getLocalString("item.name.hazmatLegs"));
		LanguageRegistry.addName(hazmatTorso, MinechemHelper.getLocalString("item.name.hazmatTorso"));
		LanguageRegistry.addName(hazmatHead, MinechemHelper.getLocalString("item.name.hazmatHead"));
		
		concaveLens = new ItemStack(lens, 1, 0);
		convexLens  = new ItemStack(lens, 1, 1);
		microscopeLens = new ItemStack(lens, 1, 2);
		projectorLens = new ItemStack(lens, 1, 3);
	}
	
	public static void registerToOreDictionary() {
		for(EnumElement element : EnumElement.values()) {
			OreDictionary.registerOre("element" + element.descriptiveName(), new ItemStack(MinechemItems.element, 1, element.ordinal()));
		}
	}
}
