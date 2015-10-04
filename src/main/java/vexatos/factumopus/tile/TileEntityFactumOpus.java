package vexatos.factumopus.tile;

import buildcraft.BuildCraftCore;
import buildcraft.api.core.ISerializable;
import buildcraft.core.lib.network.PacketTileUpdate;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * @author Vexatos
 */
public abstract class TileEntityFactumOpus extends TileEntity implements ISerializable {
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

	public void sendNetworkUpdate() {
		if(worldObj != null && !worldObj.isRemote) {
			BuildCraftCore.instance.sendToPlayers(new PacketTileUpdate(this), worldObj,
				xCoord, yCoord, zCoord, 64);
			//FzNetDispatch.addPacketFrom(this.getDescriptionPacket(), new Coord(this));
			//worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public void readData(ByteBuf data) {

	}

	@Override
	public void writeData(ByteBuf data) {

	}
}
