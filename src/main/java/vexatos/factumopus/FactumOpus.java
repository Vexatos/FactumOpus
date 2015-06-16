package vexatos.factumopus;

import buildcraft.api.transport.PipeManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vexatos.factumopus.block.BlockFluidVoidGoo;
import vexatos.factumopus.fluid.FluidContainerHandler;
import vexatos.factumopus.integration.botany.IntegrationBotany;
import vexatos.factumopus.integration.buildcraft.StripesHandlerVoidFumes;
import vexatos.factumopus.integration.extrabees.IntegrationExtraBees;
import vexatos.factumopus.item.ItemAcidBottles;
import vexatos.factumopus.item.ItemBucketFactumOpus;
import vexatos.factumopus.item.ItemLikeBonemeal;
import vexatos.factumopus.item.ItemMultiple;
import vexatos.factumopus.item.ItemSulfurTrioxide;
import vexatos.factumopus.item.block.ItemBlockFactumOpus;
import vexatos.factumopus.misc.CreativeTabFactumOpus;
import vexatos.factumopus.reference.Mods;

/**
 * @author Vexatos
 */
@Mod(modid = Mods.FactumOpus, name = Mods.FactumOpus_NAME, version = "@VERSION@",
	dependencies = "required-after:" + Mods.Factorization + "@[0.8.89,);after:"
		+ Mods.API.BuildCraftTransport + "@[4.0,);after:" + Mods.Botany
		+ "@[2.0,);after:" + Mods.ExtraBees + "@[2.0,);after:" + Mods.Forestry + "@[3.5.7,)")
public class FactumOpus {

	@Instance(Mods.FactumOpus)
	public static FactumOpus instance;

	public static Logger log;

	public static CreativeTabFactumOpus tab = new CreativeTabFactumOpus();

	public static FluidContainerHandler fluidContainerHandler;

	public static IntegrationExtraBees extraBees;
	public static IntegrationBotany botany;

	public static Item itemBowls;
	public static Item itemMaterial;
	public static Item itemAcidBottles;
	public static Item itemBucketVoidGoo;
	public static Item itemBottleVoidFumes;
	public static Item itemSulfurTrioxide;
	public static Item itemSalts;

	public static Block pondBase;
	public static Block blockVoidGoo;

	public static Fluid voidfumes;
	public static Fluid voidessence;
	public static Fluid voidgoo;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		log = LogManager.getLogger(Mods.FactumOpus);

		itemBowls = new ItemMultiple("dark_iron_bowl", "iron_bowl", "salt_bowl", "sulfur_trioxide_bottle")
			.setMaxStackSize(16);
		GameRegistry.registerItem(itemBowls, "factumopus.itemBowls");
		itemMaterial = new ItemMultiple("void_goo_glob", "salt_pile");
		GameRegistry.registerItem(itemMaterial, "factumopus.itemMaterial");

		itemAcidBottles = new ItemAcidBottles();
		GameRegistry.registerItem(itemAcidBottles, "factumopus.itemAcidBottles");

		itemSulfurTrioxide = new ItemSulfurTrioxide();
		GameRegistry.registerItem(itemSulfurTrioxide, "factumopus.itemSulfurTrioxide");

		itemSalts = new ItemLikeBonemeal(
			"potassium_bisulfate", "potassium_pyrosulfate",
			"potassium_sulfate", "potassium_bisulfate_powder", "potassium_sulfate_powder",
			"sodium_bisulfate", "sodium_pyrosulfate",
			"sodium_sulfate", "sodium_bisulfate_powder", "sodium_sulfate_powder").setBonemeal(2, 7);
		GameRegistry.registerItem(itemSalts, "factumopus.itemSalts");

		fluidContainerHandler = new FluidContainerHandler();

		itemBottleVoidFumes = new ItemMultiple("void_bottle")
			.setMaxStackSize(16).setContainerItem(Items.glass_bottle);
		GameRegistry.registerItem(itemBottleVoidFumes, "factumopus.itemBottle");

		voidfumes = new Fluid("voidfumes").setGaseous(true).setDensity(20).setViscosity(20);
		FluidRegistry.registerFluid(voidfumes);
		FluidContainerRegistry.registerFluidContainer(voidfumes, new ItemStack(itemBottleVoidFumes, 1, 0), new ItemStack(Items.glass_bottle));

		voidessence = new Fluid("voidessence").setGaseous(true);
		FluidRegistry.registerFluid(voidessence);

		// Void Goo
		voidgoo = new Fluid("voidgoo").setDensity(10000).setViscosity(10000);
		FluidRegistry.registerFluid(voidgoo);

		blockVoidGoo = new BlockFluidVoidGoo(voidgoo);
		GameRegistry.registerBlock(blockVoidGoo, ItemBlockFactumOpus.class, "factumopus.blockFluidVoidGoo");

		itemBucketVoidGoo = new ItemBucketFactumOpus(blockVoidGoo)
			.setUnlocalizedName("void_goo_bucket").setContainerItem(Items.bucket);
		GameRegistry.registerItem(itemBucketVoidGoo, "factumopus.itemBucket");

		fluidContainerHandler.buckets.put(blockVoidGoo, itemBucketVoidGoo);
		FluidContainerRegistry.registerFluidContainer(voidgoo,
			new ItemStack(itemBucketVoidGoo), new ItemStack(Items.bucket));

		// Compat
		if(Mods.isLoaded(Mods.Forestry) && Mods.isLoaded(Mods.ExtraBees)) {
			extraBees = new IntegrationExtraBees();
			extraBees.preInit();
		}

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(fluidContainerHandler);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		if(Mods.isLoaded(Mods.Forestry) && Mods.isLoaded(Mods.ExtraBees) && extraBees != null) {
			extraBees.init();
		}
		if(Mods.isLoaded(Mods.Botany)) {
			botany = new IntegrationBotany();
			botany.init();
		}
		if(Mods.API.hasAPI(Mods.API.BuildCraftTransport)) {
			registerStripesHandlers();
		}
	}

	@Optional.Method(modid = Mods.API.BuildCraftTransport)
	private void registerStripesHandlers() {
		log.info("Registering BuildCraft Stripes Pipe Handlers...");
		PipeManager.registerStripesHandler(new StripesHandlerVoidFumes(), 5);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {

	}

	@SideOnly(Side.CLIENT)
	public static void setFluidTextures(IIconRegister r) {
		if(voidfumes != null) {
			voidfumes.setIcons(r.registerIcon("factumopus:void_fumes"));
		}
		if(voidessence != null) {
			voidessence.setIcons(r.registerIcon("factumopus:void_essence"));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void assignTextures(TextureStitchEvent.Post event) {
		if(voidgoo != null) {
			voidgoo.setIcons(blockVoidGoo.getBlockTextureFromSide(1), blockVoidGoo.getBlockTextureFromSide(2));
		}
	}
}
