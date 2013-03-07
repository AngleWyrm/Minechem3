package ljdp.minechem.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.tileentity.TileEntityFusion;
import ljdp.minechem.common.tileentity.TileEntityProxy;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFusion extends BlockMinechemContainer {

	public BlockFusion(int id) {
		super(id, Material.iron);
		setCreativeTab(ModMinechem.minechemTab);
		setBlockName("minechem.blockFusion");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y,
			int z, EntityPlayer entityPlayer, int side, float par7, float par8, float par9)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);;
		if(tileEntity == null || entityPlayer.isSneaking())
			return false;
		entityPlayer.openGui(ModMinechem.instance, 0, world, x, y, z);
		return true;
	}

	@Override
	public ArrayList<ItemStack> addStacksDroppedOnBlockBreak(TileEntity tileEntity, ArrayList itemStacks) {
		return itemStacks;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		if(metadata == 2) 
			return new TileEntityFusion();
		else
			return new TileEntityProxy();
	}
	
	@Override
	public int damageDropped(int metadata) {
		return metadata;
	}
	
	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.BLOCKS_PNG;
	}
	
	@Override
	public int getBlockTextureFromSideAndMetadata(int par1, int metadata) {
		switch(metadata) {
		case 0:
			return 16;
		case 1:
			return 17;
		default:
			return 0;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < 2; i++)
			par3List.add(new ItemStack(this.blockID, 1, i));
	}

}
