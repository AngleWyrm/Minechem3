package ljdp.minechem.client.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import ljdp.minechem.api.core.Chemical;
import ljdp.minechem.api.recipe.DecomposerRecipe;
import ljdp.minechem.api.recipe.DecomposerRecipeChance;
import ljdp.minechem.api.recipe.DecomposerRecipeSelect;
import ljdp.minechem.api.recipe.SynthesisRecipe;
import ljdp.minechem.api.util.Constants;
import ljdp.minechem.client.gui.tabs.TabHelp;
import ljdp.minechem.common.MinechemItems;
import ljdp.minechem.common.ModMinechem;
import ljdp.minechem.common.containers.ContainerChemistJournal;
import ljdp.minechem.common.network.PacketActiveJournalItem;
import ljdp.minechem.common.network.PacketHandler;
import ljdp.minechem.common.recipe.DecomposerRecipeHandler;
import ljdp.minechem.common.recipe.SynthesisRecipeHandler;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class GuiChemistJournal extends GuiContainerTabbed implements IVerticalScrollContainer {
	
	private static final int SYNTHESIS_X = 198;
	private static final int SYNTHESIS_Y = 122;
	private static final int DECOMPOSER_X = 198;
	private static final int DECOMPOSER_Y = 42;
	private static final int GUI_WIDTH = 302;
	private static final int GUI_HEIGHT = 191;
	
	GuiVerticalScrollBar scrollBar;
	List<GuiFakeSlot> itemListSlots = new ArrayList();
	int listHeight;
	GuiFakeSlot[] synthesisSlots = new GuiFakeSlot[9];
	GuiFakeSlot[] decomposerSlots = new GuiFakeSlot[9];
	EntityPlayer player;
	private int slideShowTimer = 0;
	private int currentSlide = 0;
	public ItemStack currentItemStack;
	SynthesisRecipe currentSynthesisRecipe;
	DecomposerRecipe currentDecomposerRecipe;
	ItemStack journalStack;
	
	public GuiChemistJournal(EntityPlayer entityPlayer) {
		super(new ContainerChemistJournal(entityPlayer.inventory));
		this.player = entityPlayer;
		this.journalStack = entityPlayer.inventory.getCurrentItem();
		this.currentItemStack = MinechemItems.journal.getActiveStack(journalStack);
		if(this.currentItemStack != null) {
			showRecipesForStack(currentItemStack);
		}
		this.xSize = GUI_WIDTH;
		this.ySize = GUI_HEIGHT;
		scrollBar = new GuiVerticalScrollBar(this, 128, 14, 157, this.xSize, this.ySize);
		
		List<ItemStack> itemList = MinechemItems.journal.getItemList(this.journalStack);
		if(itemList != null) {
			populateItemList(itemList, entityPlayer);
		}
	}
	
	public void populateItemList(List<ItemStack> items, EntityPlayer player) {
		int i = 0;
		int j = 0;
		for(ItemStack itemstack : items) {
			if(itemstack == null)
				continue;
			int xpos = (i * 18) + 18;
			int ypos = (j * 18) + 10;
			GuiFakeSlot slot = new GuiFakeSlot(this, this.player);
			slot.setXPos(xpos);
			slot.setYPos(ypos);
			slot.setItemStack(itemstack);
			itemListSlots.add(slot);
			if(++i == 6) {
				i = 0;
				j++;
			}
		}
		listHeight = j * 18;
	}
	
	@Override
	protected void mouseClicked(int x, int y, int mouseButton) {
		super.mouseClicked(x, y, mouseButton);
		GuiFakeSlot clickedSlot = null;
		for(GuiFakeSlot slot : itemListSlots) {
			if(slot.getMouseIsOver()) {
				clickedSlot = slot;
				break;
			}
		}
		if(clickedSlot != null) {
			onSlotClick(clickedSlot);
		}
	}
	
	public void onSlotClick(GuiFakeSlot slot) {
		ItemStack itemstack = slot.getItemStack();
		showRecipesForStack(itemstack);
	}
	
	public void showRecipesForStack(ItemStack itemstack) {
		currentItemStack = itemstack;
		MinechemItems.journal.setActiveStack(itemstack, journalStack);
		PacketActiveJournalItem packet = new PacketActiveJournalItem(itemstack, player);
		PacketHandler.getInstance().activeJournalItemHandler.sendToServer(packet);
		
		SynthesisRecipe synthesisRecipe = SynthesisRecipeHandler.instance.getRecipeFromOutput(itemstack);
		DecomposerRecipe decomposerRecipe = DecomposerRecipeHandler.instance.getRecipe(itemstack);
		synthesisSlots = new GuiFakeSlot[9];
		decomposerSlots = new GuiFakeSlot[9];
		currentSynthesisRecipe = null;
		currentDecomposerRecipe = null;
		if(synthesisRecipe != null) {
			showSynthesisRecipe(synthesisRecipe);
			currentSynthesisRecipe = synthesisRecipe;
		}
		if(decomposerRecipe != null) {
			showDecomposerRecipe(decomposerRecipe);
			currentDecomposerRecipe = decomposerRecipe;
		}
	}
	
	public void showSynthesisRecipe(SynthesisRecipe recipe) {
		ItemStack[] ingredients = MinechemHelper.convertChemicalArrayIntoItemStackArray(recipe.getShapedRecipe());
		showIngredients(ingredients, synthesisSlots, SYNTHESIS_X, SYNTHESIS_Y);
	}
	
	public void showDecomposerRecipe(DecomposerRecipe recipe) {
		if(recipe instanceof DecomposerRecipeSelect) {
			slideShowTimer = 0;
			currentSlide = 0;
			return;
		}
		
		List<ItemStack> ingredients = MinechemHelper.convertChemicalsIntoItemStacks(recipe.getOutputRaw());
		ItemStack[] ingredientArray = ingredients.toArray(new ItemStack[9]);
		showIngredients(ingredientArray, decomposerSlots, DECOMPOSER_X, DECOMPOSER_Y);
	}

	private void showDecomposerRecipeSelect(DecomposerRecipeSelect recipe) {
		List<DecomposerRecipe> recipes = recipe.getAllPossibleRecipes();
		if(slideShowTimer >= Constants.TICKS_PER_SECOND * 8) {
			slideShowTimer = 0;
			currentSlide++;
			if(currentSlide >= recipes.size())
				currentSlide = 0;
		}
		if(slideShowTimer == 0) {
			ArrayList<Chemical> chemicals = recipes.get(currentSlide).getOutputRaw();
			List<ItemStack> ingredients = MinechemHelper.convertChemicalsIntoItemStacks(chemicals);
			ItemStack[] ingredientArray = ingredients.toArray(new ItemStack[9]);
			showIngredients(ingredientArray, decomposerSlots, DECOMPOSER_X, DECOMPOSER_Y);
		}
		slideShowTimer++;
	}
	
	private void showIngredients(ItemStack[] ingredients, GuiFakeSlot[] slotArray, int xOffset, int yOffset) {
		int pos = 0;
		int i = 0;
		int j = 0;
		int x, y;
		for(ItemStack ingredient : ingredients) {
			if(pos >= 9)
				break;
			slotArray[pos] = null;
			if(ingredient != null) {
				x = (i * 18) + xOffset;
				y = (j * 18) + yOffset
						;
				GuiFakeSlot fakeSlot = new GuiFakeSlot(this, this.player);
				fakeSlot.setItemStack(ingredient);
				fakeSlot.setXPos(x);
				fakeSlot.setYPos(y);
				slotArray[pos] = fakeSlot;
			}
			pos++;
			if(++i == 3) {
				i = 0;
				j++;
			}
		}
	}

	@Override
	protected void drawTooltips(int mouseX, int mouseY) {
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int x = (width - this.xSize) / 2;
		int y = (height - this.ySize) / 2;
		
		GL11.glPushMatrix();
			GL11.glTranslatef(x, y, 0);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			GL11.glPushMatrix();
				GL11.glScalef(2.0F, 2.0F, 2.0F);
				int textureID = mc.renderEngine.getTexture(ModMinechem.proxy.JOURNAL_GUI_PNG);
				mc.renderEngine.bindTexture(textureID);
				drawTexturedModalRect(0, 0, 0, 0, this.xSize/2, this.ySize/2);
			GL11.glPopMatrix();
			
			if(currentItemStack != null) {
				drawRecipeGrid();
				drawRecipeGrid();
				drawText();
				drawRecipeSlots(x, y);
			} else {
				drawHelp();
			}
			
			scrollBar.draw();
			drawSlots(x, y);
			drawSlotTooltips();
			
			// Draw page corner overlay.
			GL11.glPushMatrix();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glScalef(2.0F, 2.0F, 2.0F);
				mc.renderEngine.bindTexture(textureID);
				drawTexturedModalRect(8/2, 164/2, 161/2, 192/2, 20/2, 20/2);
			GL11.glPopMatrix();
		GL11.glPopMatrix();
		
		if(currentDecomposerRecipe instanceof DecomposerRecipeSelect) {
			showDecomposerRecipeSelect((DecomposerRecipeSelect)currentDecomposerRecipe);
		}
	}
	
	private void drawRecipeGrid() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPushMatrix();
		
		GL11.glScalef(2.0F, 2.0F, 1.0F);
		int textureID = mc.renderEngine.getTexture(ModMinechem.proxy.JOURNAL_GUI_PNG);
		mc.renderEngine.bindTexture(textureID);
		
		drawTexturedModalRect(197/2, 41/2, 51/2, 192/2, 54/2, 54/2);
		if(currentSynthesisRecipe != null && currentSynthesisRecipe.isShaped()) {
			drawTexturedModalRect(197/2, 121/2, 104/2, 192/2, 54/2, 54/2);
		} else {
			drawTexturedModalRect(197/2, 121/2, 51/2, 192/2, 54/2, 54/2);
		}
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private void drawText() {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		String itemname = String.format("%sl%s", Constants.TEXT_MODIFIER, currentItemStack.getDisplayName());
		if(itemname.length() > 18)
			itemname = itemname.substring(0, 18).trim() + "...";
		fontRenderer.drawString(itemname, 175, 10, 0x0000FF);
		fontRenderer.drawString(MinechemHelper.getLocalString("gui.journal.decomposer"), 175, 20, 0x884400);
		
		float chance = 100;
		if(currentDecomposerRecipe != null && currentDecomposerRecipe instanceof DecomposerRecipeChance) {
			chance = ((DecomposerRecipeChance)currentDecomposerRecipe).getChance();
			chance *= 100.0F;
		}
		if(currentDecomposerRecipe != null)
			fontRenderer.drawString(String.format("%.1f%% chance", chance), 175, 30, 0x555555);
		
		fontRenderer.drawString(MinechemHelper.getLocalString("gui.journal.synthesis"), 175, 100, 0x884400);
		if(currentSynthesisRecipe != null) {
			int energyCost = currentSynthesisRecipe.energyCost();
			fontRenderer.drawString(String.format("%d MJ", energyCost), 175, 110, 0x555555);
		}
	}
	
	private void drawHelp() {
		fontRenderer.drawString(MinechemHelper.getLocalString("item.name.chemistJournal"), 180, 18, 0xFF000000);
		String help = MinechemHelper.getLocalString("help.journal");
		GL11.glPushMatrix();
		float scale = 0.5F;
		GL11.glScalef(scale, scale, 1);
		fontRenderer.drawSplitString(help, 345, 70, 200, 0xAA000000);
		GL11.glPopMatrix();
	}
	
	private void drawSlots(int x, int y) {
		GL11.glPushMatrix();
		ScissorHelper.startScissor(mc, x + 9, y + 7, 140, 176);
		int ypos = (int) ((listHeight-150) * scrollBar.getScrollValue());
		GL11.glTranslatef(0, -ypos, 0);
		for(GuiFakeSlot slot : itemListSlots) {
			slot.setYOffset(-ypos);
			slot.draw();
		}
		ScissorHelper.endScissor();
		GL11.glPopMatrix();
	}
	
	private void drawSlotTooltips() {
		for(GuiFakeSlot slot : itemListSlots) {
			slot.drawTooltip(mouseX + 10, mouseY);
		}
	}
	
	private void drawRecipeSlots(int x, int y) {
		for(GuiFakeSlot slot : synthesisSlots) {
			if(slot != null) {
				slot.draw();
			}
		}
		for(GuiFakeSlot slot : decomposerSlots) {
			if(slot != null) {
				slot.draw();
			}
		}
		for(GuiFakeSlot slot : synthesisSlots) {
			if(slot != null)
				slot.drawTooltip(mouseX + 10, mouseY);
		}
		for(GuiFakeSlot slot : decomposerSlots) {
			if(slot != null)
				slot.drawTooltip(mouseX + 10, mouseY);
		}
	}
	
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
		scrollBar.handleMouseInput();
	}

	@Override
	public boolean isScrollBarActive() {
		return listHeight > 130;
	}

	@Override
	public int getScreenWidth() {
		return width;
	}

	@Override
	public int getScreenHeight() {
		return height;
	}

	@Override
	public int getGuiWidth() {
		return xSize;
	}

	@Override
	public int getGuiHeight() {
		return ySize;
	}

	@Override
	public int getScrollAmount() {
		return 5;
	}

}
