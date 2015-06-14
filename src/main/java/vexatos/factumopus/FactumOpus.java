package vexatos.factumopus;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import vexatos.factumopus.reference.Mods;

/**
 * @author Vexatos
 */
@Mod(modid = Mods.FactumOpus, name = Mods.FactumOpus_NAME, version = "@VERSION@",
	dependencies = "required-after:" + Mods.Factorization + "@[0.8.89,)")
public class FactumOpus {

	@Instance(Mods.FactumOpus)
	public static FactumOpus instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {

	}

	@EventHandler
	public void init(FMLInitializationEvent e) {

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {

	}
}
