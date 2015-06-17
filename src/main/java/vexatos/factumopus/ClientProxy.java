package vexatos.factumopus;

import cpw.mods.fml.client.registry.RenderingRegistry;
import vexatos.factumopus.client.RenderBlockBrine;

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
	}
}
