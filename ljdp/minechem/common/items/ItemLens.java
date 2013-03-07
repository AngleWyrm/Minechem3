package ljdp.minechem.common.items;

import java.util.List;

import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemLens extends Item {
	
	static String[] descriptiveNames = {"item.name.concavelens", "item.name.convexlens", "item.name.microscopelens", "item.name.projectorlens"};
	
	public ItemLens(int id) {
		super(id);
		setItemName("minechem.itemLens");
		setCreativeTab(ModMinechem.minechemTab);
		setHasSubtypes(true);
	}
	
	@Override
	public String getItemDisplayName(ItemStack itemStack) {
		int metadata = itemStack.getItemDamage();
		return MinechemHelper.getLocalString(descriptiveNames[metadata]);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(int id, CreativeTabs par2CreativeTabs,
			List par3List) {
		par3List.add(new ItemStack(id, 1, 0));
		par3List.add(new ItemStack(id, 1, 1));
		par3List.add(new ItemStack(id, 1, 2));
		par3List.add(new ItemStack(id, 1, 3));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int itemDamage) {
		switch(itemDamage) {
		case 0:
			return 9;
		case 1:
			return 8;
		case 2:
			return 11;
		case 3:
			return 12;
		}
		return 15;
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ITEMS_PNG;
	}
	
	
}
