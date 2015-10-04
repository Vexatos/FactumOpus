package vexatos.factumopus;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import vexatos.factumopus.client.RenderBlockBrine;
import vexatos.factumopus.client.RenderFumeCompressor;
import vexatos.factumopus.tile.compressor.TileFumeCompressor;

/**
 * @author Vexatos
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		int brine_id = RenderingRegistry.getNextAvailableRenderId();
		FactumOpus.blockBrine.setRenderType(brine_id);
		FactumOpus.blockBrineSaturated.setRenderType(brine_id);
		RenderingRegistry.registerBlockHandler(new RenderBlockBrine(brine_id));
		int compressor_id = RenderingRegistry.getNextAvailableRenderId();
		FactumOpus.blockFumeCompressor.setRenderType(compressor_id);
		RenderFumeCompressor fumeCompressorRenderer = new RenderFumeCompressor(compressor_id);
		RenderingRegistry.registerBlockHandler(fumeCompressorRenderer);
		ClientRegistry.bindTileEntitySpecialRenderer(TileFumeCompressor.class, fumeCompressorRenderer);
	}

	@Override
	public EntityPlayer getPlayerFromNetHandler(INetHandler handler) {
		if(handler instanceof NetHandlerPlayServer) {
			return ((NetHandlerPlayServer) handler).playerEntity;
		} else {
			return Minecraft.getMinecraft().thePlayer;
		}
	}
}
