package ljdp.minechem.client.render.item;

import ljdp.minechem.client.ModelSynthesizer;
import ljdp.minechem.common.ModMinechem;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;

public class ItemSynthesisRenderer extends ItemMinechemRenderer {

	private ModelSynthesizer model;
	
	public ItemSynthesisRenderer() {
		model = new ModelSynthesizer();
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		String texture = ModMinechem.proxy.SYNTHESIS_MODEL;
		GL11.glPushMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, FMLClientHandler.instance().getClient().renderEngine.getTexture(texture));
		if(type == ItemRenderType.ENTITY) {
			GL11.glTranslatef(0.0F, 1.5F, 0.0F);
			GL11.glRotatef(180f, 0.1f, 0.0f, 1f);
		} else if(type == ItemRenderType.EQUIPPED) {
			GL11.glTranslatef(0.5F, 1.6F, 0.0F);
			GL11.glRotatef(180f, 0.1f, 0.0f, 1f);
		} else{ 
			GL11.glTranslatef(0.0F, 1.0F, 0.0F);
			GL11.glRotatef(180f, 0f, 0f, 1f);
		}
		model.render(0.0625F);
		GL11.glPopMatrix();
	}

}
