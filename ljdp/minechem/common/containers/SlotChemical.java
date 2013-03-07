package ljdp.minechem.common.containers;

import ljdp.minechem.common.MinechemItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotChemical extends Slot {

	public SlotChemical(IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return itemStack.itemID == MinechemItems.element.itemID || itemStack.itemID == MinechemItems.molecule.itemID;
	}

}
