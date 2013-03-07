package ljdp.minechem.common;

import java.util.EnumSet;
import java.util.List;

import ljdp.minechem.api.core.EnumMolecule;
import ljdp.minechem.api.core.IRadiationShield;
import ljdp.minechem.api.util.Constants;
import ljdp.minechem.common.utils.MinechemHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;

public class ScheduledTickHandler implements IScheduledTickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer entityPlayer = (EntityPlayer)tickData[0];
		World world = entityPlayer.worldObj;
		RadiationHandler.getInstance().update(entityPlayer);
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer entityPlayer = (EntityPlayer)tickData[0];
		World world = entityPlayer.worldObj;
		checkForPoison(entityPlayer);
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "minechem.ScheduledTickHandler";
	}

	@Override
	public int nextTickSpacing() {
		return Constants.TICKS_PER_SECOND;
	}
	
	private void checkForPoison(EntityPlayer entityPlayer) {
		ItemStack currentItem = entityPlayer.inventory.getCurrentItem();
		if(isPlayerEating(entityPlayer) && currentItem != null && currentItem.getTagCompound() != null) {
			NBTTagCompound stackTag = currentItem.getTagCompound();
			boolean isPoisoned = stackTag.getBoolean("minechem.isPoisoned");
			int effectType     = stackTag.getInteger("minechem.effectType");
			EnumMolecule molecule = EnumMolecule.getById(effectType);
			if(isPoisoned) {
				MinechemHelper.triggerPlayerEffect(molecule, entityPlayer);
				entityPlayer.inventory.decrStackSize(entityPlayer.inventory.currentItem, 1);
			}
		}
	}
	
	private boolean isPlayerEating(EntityPlayer player) {
		return (player.getDataWatcher().getWatchableObjectByte(0) & 1 << 4) != 0;
	}

}
