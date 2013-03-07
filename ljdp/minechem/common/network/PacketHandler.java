package ljdp.minechem.common.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import ljdp.easypacket.EasyPacketDispatcher;
import ljdp.easypacket.EasyPacketHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.FMLRelauncher;

public class PacketHandler implements IPacketHandler {
	
	public static final String MINECHEM_PACKET_CHANNEL = "MineChem2";
	private static PacketHandler instance;
	public static final PacketHandler getInstance() {
		return instance;
	}
	
	private EasyPacketDispatcher dispatcher;
	public EasyPacketHandler decomposerUpdateHandler;
	public EasyPacketHandler ghostBlockUpdateHandler;
	public EasyPacketHandler synthesisUpdateHandler;
	public EasyPacketHandler activeJournalItemHandler;
	
	public PacketHandler() {
		instance = this;
		dispatcher = new EasyPacketDispatcher(MINECHEM_PACKET_CHANNEL);
		decomposerUpdateHandler = EasyPacketHandler.registerEasyPacket(PacketDecomposerUpdate.class, dispatcher);
		ghostBlockUpdateHandler = EasyPacketHandler.registerEasyPacket(PacketGhostBlock.class, dispatcher);
		synthesisUpdateHandler  = EasyPacketHandler.registerEasyPacket(PacketSynthesisUpdate.class, dispatcher);
		activeJournalItemHandler = EasyPacketHandler.registerEasyPacket(PacketActiveJournalItem.class, dispatcher);
	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(packet.data != null)
			dispatcher.onPacketData(manager, packet, player);
	}

}
