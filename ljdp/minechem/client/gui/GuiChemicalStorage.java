package ljdp.minechem.client.gui;

import org.lwjgl.opengl.GL11;

import ljdp.minechem.client.gui.tabs.TabHelp;
import ljdp.minechem.common.utils.MinechemHelper;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;

public class GuiChemicalStorage extends GuiContainerTabbed {

	private IInventory playerInventory;
	private int inventoryRows = 0;

	public GuiChemicalStorage(IInventory playerInventory, IInventory blockInventory) {
		super(new ContainerChest(playerInventory, blockInventory));
        this.playerInventory = playerInventory;
        this.inventoryRows = blockInventory.getSizeInventory() / 9;
        addTab(new TabHelp(this, MinechemHelper.getLocalString("help.chemicalStorage")));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		super.drawGuiContainerForegroundLayer(par1, par2);
		String chestName = MinechemHelper.getLocalString("block.name.chemicalStorage");
		String playerInventoryTitle = StatCollector.translateToLocal(this.playerInventory.getInvName());
		this.fontRenderer.drawString(chestName, 8, 6, 4210752);
		this.fontRenderer.drawString(playerInventoryTitle, 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		int var4 = this.mc.renderEngine.getTexture("/gui/container.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(var4);
        int var5 = (this.width - this.xSize) / 2;
        int var6 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(var5, var6 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
	}
	
	@Override
	protected void drawTooltips(int mouseX, int mouseY) {
		
	}

}
