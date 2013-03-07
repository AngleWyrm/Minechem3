package ljdp.minechem.common.items;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.server.FMLServerHandler;
import ljdp.minechem.common.GuiHandler;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.network.PacketHandler;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class ItemChemistJournal extends Item {
	
	public static final String ITEMS_TAG_NAME = "discoveredItems";
	private static final String ACTIVE_ITEMSTACK_TAG = "activeItemStack";
	
	public ItemChemistJournal(int id) {
		super(id);
		setItemName("minechem.itemChemistJournal");
		setCreativeTab(null);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer entityPlayer) {
		if(!world.isRemote)
			entityPlayer.openGui(ModMinechem.instance, GuiHandler.GUI_ID_JOURNAL, world, 
				entityPlayer.chunkCoordX, entityPlayer.chunkCoordY, entityPlayer.chunkCoordY);
		return par1ItemStack;
	}
	
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		super.onCreated(itemStack, world, entityPlayer);
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if(tagCompound == null)
			tagCompound = new NBTTagCompound();
		tagCompound.setString("owner", entityPlayer.getEntityName());
		itemStack.setTagCompound(tagCompound);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack,
			EntityPlayer entityPlayer, List list, boolean par4)
	{
		NBTTagCompound stackTag = itemStack.getTagCompound();
		if(stackTag != null) {
			NBTTagCompound activeTag = (NBTTagCompound) stackTag.getTag(ACTIVE_ITEMSTACK_TAG);
			String owner = stackTag.getString("owner");
			if(activeTag != null) {
				ItemStack activeItemStack = ItemStack.loadItemStackFromNBT(activeTag);
				list.add(activeItemStack.getDisplayName());
			}
			list.add("Owned by " + owner);
		}
	}
	
	public void setActiveStack(ItemStack itemstack, ItemStack journalStack) {
		NBTTagCompound journalTag = journalStack.getTagCompound();
		if(journalTag == null)
			journalTag = new NBTTagCompound();
		NBTTagCompound stackTag = itemstack.writeToNBT(new NBTTagCompound());
		journalTag.setTag(ACTIVE_ITEMSTACK_TAG, stackTag);
		journalStack.setTagCompound(journalTag);
	}
	
	public ItemStack getActiveStack(ItemStack journalStack) {
		NBTTagCompound journalTag = journalStack.getTagCompound();
		if(journalTag != null) {
			NBTTagCompound stackTag = (NBTTagCompound) journalTag.getTag(ACTIVE_ITEMSTACK_TAG);
			if(stackTag != null)
				return ItemStack.loadItemStackFromNBT(stackTag);
		}
		return null;
	}
	
	public List<ItemStack> getItemList(ItemStack journal) {
		NBTTagCompound tag = journal.getTagCompound();
		if(tag != null) {
			NBTTagList taglist = tag.getTagList(ITEMS_TAG_NAME);
			if(taglist != null)
				return MinechemHelper.readTagListToItemStackList(taglist);
		}
		return null;
	}
	
	public void addItemStackToJournal(ItemStack itemstack, ItemStack journal, World world) {
		NBTTagCompound tagCompound = journal.getTagCompound();
		if(tagCompound == null)
			tagCompound = new NBTTagCompound();
		NBTTagList taglist = tagCompound.getTagList(ITEMS_TAG_NAME);
		if(taglist == null)
			taglist = new NBTTagList();
		ArrayList<ItemStack> itemArrayList = MinechemHelper.readTagListToItemStackList(taglist);
		if(!hasDiscovered(itemArrayList, itemstack)) {
			taglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
			tagCompound.setTag(ITEMS_TAG_NAME, taglist);
			journal.setTagCompound(tagCompound);
		}
	}
	
	private boolean hasDiscovered(ArrayList<ItemStack> list, ItemStack itemstack) {
		for(ItemStack itemstack2 : list) {
			if(itemstack.isItemEqual(itemstack2))
				return true;
		}
		return false;
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ITEMS_PNG;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int par1) {
		return 50;
	}
	
}
