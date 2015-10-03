package vexatos.factumopus;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import vexatos.factumopus.client.RenderBlockBrine;
import vexatos.factumopus.client.RenderFumeCompressor;
import vexatos.factumopus.tile.compressor.TileFumeCompressor;

/**
 * @author Vexatos
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		int id = RenderingRegistry.getNextAvailableRenderId();
		FactumOpus.blockBrine.setRenderType(id);
		FactumOpus.blockBrineSaturated.setRenderType(id);
		RenderingRegistry.registerBlockHandler(new RenderBlockBrine(id));
		//RenderFumeCompressor fumeCompressorRenderer = new RenderFumeCompressor(RenderingRegistry.getNextAvailableRenderId());
		//RenderingRegistry.registerBlockHandler(fumeCompressorRenderer);
		//ClientRegistry.bindTileEntitySpecialRenderer(TileFumeCompressor.class, fumeCompressorRenderer);
	}
}
