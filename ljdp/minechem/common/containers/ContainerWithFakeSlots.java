package ljdp.minechem.common.containers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerWithFakeSlots extends Container {
	
	private static int MOUSE_LEFT = 0;
	private static int MOUSE_RIGHT = 1;
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return false;
	}
	
	@Override
	public ItemStack slotClick(int slotNum, int mouseButton, int isShiftPressed, EntityPlayer entityPlayer) {
		Slot slot = null;
		if(slotNum >= 0 && slotNum < this.inventorySlots.size())
			slot = getSlot(slotNum);
		if(slot != null && slot instanceof SlotFake) {
			ItemStack stackOnMouse = entityPlayer.inventory.getItemStack();
			if(stackOnMouse != null && slot.isItemValid(stackOnMouse)) {
				if(mouseButton == MOUSE_LEFT)
					addStackToSlot(stackOnMouse, slot, stackOnMouse.stackSize);
				else
					addStackToSlot(stackOnMouse, slot, 1);
			} else {
				slot.putStack(null);
			}
			return null;
		} else
			return super.slotClick(slotNum, mouseButton, isShiftPressed, entityPlayer);
	}

	private void addStackToSlot(ItemStack stackOnMouse, Slot slot, int amount) {
		ItemStack stackInSlot = slot.inventory.getStackInSlot(slot.slotNumber);
		if(stackInSlot != null) {
			int newStackSize = Math.min(stackInSlot.stackSize + amount, slot.inventory.getInventoryStackLimit());
			stackInSlot.stackSize = newStackSize;
		} else {
			ItemStack copyStack = stackOnMouse.copy();
			copyStack.stackSize = amount;
			slot.putStack(copyStack);
		}
	}

}
