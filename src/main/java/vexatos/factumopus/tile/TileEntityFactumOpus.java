package vexatos.factumopus.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * @author Vexatos
 */
public abstract class TileEntityFactumOpus extends TileEntity {
	public void readFromRemoteNBT(NBTTagCompound tag) {
	}

	public void writeToRemoteNBT(NBTTagCompound tag) {
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToRemoteNBT(tag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound tag = pkt.func_148857_g();
		if(tag != null) {
			readFromRemoteNBT(tag);
		}
	}
}
