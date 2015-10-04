package vexatos.factumopus;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

/**
 * @author Vexatos
 */
public class CommonProxy {

	public void registerRenderers() {
		// NO-OP
	}

	public EntityPlayer getPlayerFromNetHandler(INetHandler handler) {
		if(handler instanceof NetHandlerPlayServer) {
			return ((NetHandlerPlayServer) handler).playerEntity;
		} else {
			return null;
		}
	}
}
