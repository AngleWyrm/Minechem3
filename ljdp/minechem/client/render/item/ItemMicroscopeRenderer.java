package ljdp.minechem.client.render.item;

import ljdp.minechem.client.ModelMicroscope;
import ljdp.minechem.common.ModMinechem;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemMicroscopeRenderer implements IItemRenderer {
	
	private ModelMicroscope modelMicroscope;

	public ItemMicroscopeRenderer() {
		modelMicroscope = new ModelMicroscope();
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		if(type == ItemRenderType.INVENTORY)
			return true;
		if(type == ItemRenderType.EQUIPPED)
			return true;
		if(type == ItemRenderType.ENTITY)
			return true;
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		if(helper == ItemRendererHelper.INVENTORY_BLOCK)
			return true;
		if(helper == ItemRendererHelper.ENTITY_BOBBING)
			return true;
		if(helper == ItemRendererHelper.ENTITY_ROTATION)
			return true;
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		String texture = ModMinechem.proxy.MICROSCOPE_MODEL;
		GL11.glPushMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture(texture));
		if(type == ItemRenderType.ENTITY)
			GL11.glTranslatef(0.0F, 0.5F, 0.0F);
		if(type == ItemRenderType.EQUIPPED) {
			GL11.glTranslatef(0.5F, 1.6F, 0.0F);
			GL11.glRotatef(180f, 1f, 0f, 1f);
		} else{ 
			GL11.glTranslatef(0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180f, 0f, 0f, 1f);
		}
		modelMicroscope.render(0.0625F);
		GL11.glPopMatrix();
	}

}
