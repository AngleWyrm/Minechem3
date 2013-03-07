package ljdp.minechem.client.gui;

import java.util.ArrayList;

import ljdp.minechem.api.core.Chemical;
import ljdp.minechem.api.recipe.DecomposerRecipe;
import ljdp.minechem.api.recipe.DecomposerRecipeChance;
import ljdp.minechem.api.recipe.DecomposerRecipeSelect;
import ljdp.minechem.api.recipe.SynthesisRecipe;
import ljdp.minechem.api.util.Constants;
import ljdp.minechem.client.gui.tabs.TabHelp;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.containers.ContainerMicroscope;
import ljdp.minechem.common.recipe.DecomposerRecipeHandler;
import ljdp.minechem.common.recipe.SynthesisRecipeHandler;
import ljdp.minechem.common.tileentity.TileEntityMicroscope;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiMicroscope extends GuiContainerTabbed {
		
	int guiWidth = 176;
	int guiHeight = 217;
	int eyepieceX = 25;
	int eyepieceY = 26;
	int inputSlotX = 44;
	int inputSlotY = 45;
	int slideShowTimer = 0;
	int currentSlide = 0;
	public InventoryPlayer inventoryPlayer;
	protected TileEntityMicroscope microscope;
	GuiMicroscopeSwitch recipeSwitch;
	private boolean isShapedRecipe;
    
	public GuiMicroscope(InventoryPlayer inventoryPlayer, TileEntityMicroscope microscope) {
		super(new ContainerMicroscope(inventoryPlayer, microscope));
		this.inventoryPlayer = inventoryPlayer;
		this.microscope = microscope;
		this.xSize = guiWidth;
		this.ySize = guiHeight;
		this.itemRenderer = new RenderGUIItemMicroscope(this);
		this.recipeSwitch = new GuiMicroscopeSwitch(this);
		addTab(new TabHelp(this, MinechemHelper.getLocalString("help.microscope")));
	}
	
	public boolean isMouseInMicroscope() {
		int mouseX = getMouseX();
		int mouseY = getMouseY();
		int x = (width - guiWidth) / 2;
		int y = (height - guiHeight) / 2;
		x += eyepieceX; y += eyepieceY;
		int h = 54; int w = 54;
		return mouseX >= x && mouseX <= x+w && mouseY >= y && mouseY <= y+h;
	}
	
	private void drawMicroscopeOverlay() {
		zLevel = 401;
		drawTexturedModalRect(eyepieceX, eyepieceY, 176, 0, 54, 54);
	}
	
	private void drawUnshapedOverlay() {
		zLevel = 0;
		drawTexturedModalRect(97, 26, 176, 70, 54, 54);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		String info = MinechemHelper.getLocalString("gui.title.microscope");
		int infoWidth = fontRenderer.getStringWidth(info);
		fontRenderer.drawString(info, (guiWidth - infoWidth) / 2, 5, 0x000000);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2,
			int var3) {
		int texture = mc.renderEngine.getTexture(ModMinechem.proxy.MICROSCOPE_GUI_PNG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		int x = (width - guiWidth) / 2;
		int y = (height - guiHeight) / 2;
		zLevel = 0;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0.0F);
		drawTexturedModalRect(0, 0, 0, 0, guiWidth, guiHeight);
		drawMicroscopeOverlay();
		if(!isShapedRecipe)
			drawUnshapedOverlay();
		GL11.glPopMatrix();
		
		recipeSwitch.setPos(x + 153, y + 26);
		recipeSwitch.draw(mc.renderEngine);
		
		ItemStack itemstack = microscope.getStackInSlot(0);
		clearRecipeMatrix();
		if(itemstack != null) {
			if(recipeSwitch.getState() == 0) {
				drawSynthesisRecipe(itemstack, x, y);
			} else {
				isShapedRecipe = false;
				drawDecomposerRecipe(itemstack, x, y);
			}
		}
	}
	
	private void clearRecipeMatrix() {
		for(int slot = 2; slot < 2+9; slot++) {
			this.inventorySlots.putStackInSlot(slot, null);
		}
	}
	
	private void drawSynthesisRecipe(ItemStack inputstack, int x, int y) {
		SynthesisRecipe recipe = SynthesisRecipeHandler.instance.getRecipeFromOutput(inputstack);
		if(recipe != null) {
			drawSynthesisRecipeMatrix(recipe, x, y);
			drawSynthesisRecipeCost(recipe, x, y);	
		}
	}
	
	private void drawSynthesisRecipeMatrix(SynthesisRecipe recipe, int x, int y) {
		isShapedRecipe = recipe.isShaped();
		ItemStack[] shapedRecipe = MinechemHelper.convertChemicalArrayIntoItemStackArray(recipe.getShapedRecipe());
		int slot = 2;
		for(ItemStack itemstack : shapedRecipe) {
			this.inventorySlots.putStackInSlot(slot, itemstack);
			slot++;
		}
	}
	
	private void drawSynthesisRecipeCost(SynthesisRecipe recipe, int x, int y) {
		if(!recipeSwitch.isMoverOver()) {
			String cost = String.format("%d MJ", recipe.energyCost());
			fontRenderer.drawString(cost, x + 108, y + 85, 0x000000);
		}
	}

	private void drawDecomposerRecipe(ItemStack inputstack, int x, int y) {
		DecomposerRecipe recipe = DecomposerRecipeHandler.instance.getRecipe(inputstack);
		if(recipe != null) {
			ArrayList<ItemStack> output = MinechemHelper.convertChemicalsIntoItemStacks(recipe.getOutputRaw());
			if(recipe instanceof DecomposerRecipeSelect)
				drawDecomposerRecipeSelectMatrix(((DecomposerRecipeSelect)recipe).getAllPossibleRecipes(), x, y);
			else
				drawDecomposerRecipeMatrix(output, x, y);
			drawDecomposerChance(recipe, x, y);
		}
	}

	private void drawDecomposerRecipeMatrix(ArrayList<ItemStack> output, int x, int y) {
		int slot = 2;
		for(ItemStack itemstack : output) {
			this.inventorySlots.putStackInSlot(slot, itemstack);
			slot++;
		}
	}
	
	private void drawDecomposerRecipeSelectMatrix(ArrayList<DecomposerRecipe> recipes, int x, int y) {
		if(slideShowTimer == Constants.TICKS_PER_SECOND * 8) {
			slideShowTimer = 0;
			currentSlide++;
		}
		if(currentSlide == recipes.size())
			currentSlide = 0;
		slideShowTimer++;
		DecomposerRecipe recipe = recipes.get(currentSlide);
		ArrayList<ItemStack> output = MinechemHelper.convertChemicalsIntoItemStacks(recipe.getOutputRaw());
		drawDecomposerRecipeMatrix(output, x, y);
	}
	
	private void drawDecomposerChance(DecomposerRecipe recipe, int x, int y) {
		if(!recipeSwitch.isMoverOver() && recipe instanceof DecomposerRecipeChance) {
			DecomposerRecipeChance recipeChance = (DecomposerRecipeChance) recipe;
			int chance = (int) (recipeChance.getChance() * 100);
			String info = String.format("%d%%", chance);
			fontRenderer.drawString(info, x + 108, y + 85, 0x000000);
		}
	}

	@Override
	protected void drawTooltips(int mouseX, int mouseY) {
		
	}
	
	@Override
	protected void mouseClicked(int x, int y, int mouseButton) {
		super.mouseClicked(x, y, mouseButton);
		this.recipeSwitch.mouseClicked(x, y, mouseButton);
	}
	
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		this.recipeSwitch.handleMouseInput();
	}

}
