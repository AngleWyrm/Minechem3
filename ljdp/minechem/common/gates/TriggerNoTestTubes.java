package ljdp.minechem.common.gates;

import ljdp.minechem.common.ModMinechem;
import net.minecraft.tileentity.TileEntity;
import buildcraft.api.gates.ITriggerParameter;
import buildcraft.api.gates.Trigger;

public class TriggerNoTestTubes extends Trigger {

	public TriggerNoTestTubes(int id) {
		super(id);
	}

	@Override
	public String getTextureFile() {
		return ModMinechem.proxy.ICONS_PNG;
	}
	
	@Override
	public int getIndexInTexture() {
		return 5;
	}
	
	@Override
	public String getDescription() {
		return "No Test Tubes";
	}
	
	@Override
	public boolean isTriggerActive(TileEntity tile, ITriggerParameter parameter) {
		if(tile instanceof IMinechemTriggerProvider) {
			return ((IMinechemTriggerProvider)tile).hasNoTestTubes();
		}
		return false;
	}

}
