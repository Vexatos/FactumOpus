package vexatos.factumopus;

import buildcraft.api.transport.PipeManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import factorization.shared.Core;
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
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vexatos.factumopus.block.BlockBrine;
import vexatos.factumopus.block.BlockBrineSaturated;
import vexatos.factumopus.block.BlockClaySand;
import vexatos.factumopus.block.BlockFluidVoidGoo;
import vexatos.factumopus.block.BlockPondBase;
import vexatos.factumopus.block.BlockSaltLayer;
import vexatos.factumopus.block.BlockSolidVoidGoo;
import vexatos.factumopus.block.compressor.BlockCompressorValve;
import vexatos.factumopus.block.compressor.BlockCompressorWall;
import vexatos.factumopus.block.compressor.BlockFumeCompressor;
import vexatos.factumopus.fluid.ContainerHandler;
import vexatos.factumopus.integration.buildcraft.StripesHandlerSaltBowl;
import vexatos.factumopus.integration.buildcraft.StripesHandlerVoidFumes;
import vexatos.factumopus.integration.extrabees.IntegrationExtraBees;
import vexatos.factumopus.integration.recipes.ModRecipes;
import vexatos.factumopus.item.ItemAcidBottles;
import vexatos.factumopus.item.ItemBucketFactumOpus;
import vexatos.factumopus.item.ItemLikeBonemeal;
import vexatos.factumopus.item.ItemMultiple;
import vexatos.factumopus.item.ItemSulfurTrioxide;
import vexatos.factumopus.item.block.ItemBlockFactumOpus;
import vexatos.factumopus.misc.CreativeTabFactumOpus;
import vexatos.factumopus.reference.Mods;
import vexatos.factumopus.tile.TileClaySand;
import vexatos.factumopus.tile.TileFluidVoidGoo;
import vexatos.factumopus.tile.TilePondBase;
import vexatos.factumopus.tile.compressor.TileCompressorValve;
import vexatos.factumopus.tile.compressor.TileCompressorWall;
import vexatos.factumopus.tile.compressor.TileFumeCompressor;

/**
 * @author Vexatos
 */
@Mod(modid = Mods.FactumOpus, name = Mods.FactumOpus_NAME, version = "@VERSION@",
	dependencies = "required-after:" + Mods.Factorization + "@[0.8.89,);after:"
		+ Mods.API.BuildCraftTransport + "@[4.1,);after:" + Mods.Botany
		+ "@[2.0-pre14,);after:" + Mods.ExtraBees + "@[2.0,);after:" + Mods.Forestry + "@[3.6.6,4)")
public class FactumOpus {

	@Instance(Mods.FactumOpus)
	public static FactumOpus instance;

	@SidedProxy(
		clientSide = "vexatos.factumopus.ClientProxy",
		serverSide = "vexatos.factumopus.CommonProxy"
	)
	public static CommonProxy proxy;

	public static Logger log;

	public static CreativeTabFactumOpus tab = new CreativeTabFactumOpus();

	public static ContainerHandler containerHandler;

	public static IntegrationExtraBees extraBees;

	public static Item itemBowls;
	public static Item itemMaterial;
	public static Item itemAcidBottles;
	public static Item itemBucketVoidGoo;
	public static Item itemBucketBrine;
	public static Item itemBucketBrineSaturated;
	public static Item itemBottles;
	public static Item itemSulfurTrioxide;
	public static Item itemSalts;

	public static Block clayeySand;
	public static Block pondBase;
	public static BlockBrine blockBrine;
	public static BlockBrine blockBrineSaturated;
	public static Block saltLayer;
	public static BlockFluidVoidGoo blockVoidGooFluid;
	public static Block blockVoidGooSolid;

	public static Block blockCompressorWall;
	public static Block blockCompressorValve;
	public static BlockFumeCompressor blockFumeCompressor;

	public static Fluid brine;
	public static Fluid brineSaturated;
	public static Fluid voidfumes;
	public static Fluid voidessence;
	public static Fluid voidgoo;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		log = LogManager.getLogger(Mods.FactumOpus);

		itemBowls = new ItemMultiple("dark_iron_bowl", "iron_bowl", "salt_bowl")
			.setMaxStackSize(16);
		GameRegistry.registerItem(itemBowls, "factumopus.itemBowls");
		itemMaterial = new ItemMultiple("void_goo_glob", "salt_pile");
		GameRegistry.registerItem(itemMaterial, "factumopus.itemMaterial");

		itemAcidBottles = new ItemAcidBottles();
		GameRegistry.registerItem(itemAcidBottles, "factumopus.itemAcidBottles");

		itemSalts = new ItemLikeBonemeal(
			"potassium_bisulfate", "potassium_pyrosulfate",
			"potassium_sulfate", "potassium_bisulfate_powder", "potassium_sulfate_powder",
			"sodium_bisulfate", "sodium_pyrosulfate",
			"sodium_sulfate", "sodium_bisulfate_powder", "sodium_sulfate_powder").setBonemeal(2, 7);
		GameRegistry.registerItem(itemSalts, "factumopus.itemSalts");

		blockVoidGooSolid = new BlockSolidVoidGoo();
		GameRegistry.registerBlock(blockVoidGooSolid, ItemBlockFactumOpus.class, "factumopus.blockSolidVoidGoo");

		containerHandler = new ContainerHandler();

		itemBottles = new ItemMultiple("void_fume_bottle", "void_essence_bottle", "sulfur_trioxide_bottle")
			.setMaxStackSize(16).setContainerItem(Items.glass_bottle);
		GameRegistry.registerItem(itemBottles, "factumopus.itemBottle");

		itemSulfurTrioxide = new ItemSulfurTrioxide();
		GameRegistry.registerItem(itemSulfurTrioxide, "factumopus.itemSulfurTrioxide");

		// Salt production
		clayeySand = new BlockClaySand();
		GameRegistry.registerBlock(clayeySand, ItemBlockFactumOpus.class, "factumopus.blockClayeySand");
		GameRegistry.registerTileEntity(TileClaySand.class, "factumopus.blockClayeySand");
		pondBase = new BlockPondBase();
		GameRegistry.registerBlock(pondBase, ItemBlockFactumOpus.class, "factumopus.blockPondBase");
		GameRegistry.registerTileEntity(TilePondBase.class, "factumopus.blockPondBase");
		saltLayer = new BlockSaltLayer();
		GameRegistry.registerBlock(saltLayer, ItemBlockFactumOpus.class, "factumopus.blockSaltLayer");

		brine = new Fluid("factumopus.brine").setDensity(1200);
		FluidRegistry.registerFluid(brine);
		blockBrine = new BlockBrine(brine);
		GameRegistry.registerBlock(blockBrine, ItemBlockFactumOpus.class, "factumopus.blockBrine");
		itemBucketBrine = new ItemBucketFactumOpus(blockBrine)
			.setUnlocalizedName("brine_bucket").setContainerItem(Items.bucket);
		GameRegistry.registerItem(itemBucketBrine, "factumopus.itemBucketBrine");
		blacklistFluid(brine);

		containerHandler.buckets.put(blockBrine, itemBucketBrine);
		FluidContainerRegistry.registerFluidContainer(brine,
			new ItemStack(itemBucketBrine), new ItemStack(Items.bucket));

		brineSaturated = new Fluid("factumopus.saturated_brine").setDensity(1400);
		FluidRegistry.registerFluid(brineSaturated);
		blockBrineSaturated = new BlockBrineSaturated(brineSaturated);
		GameRegistry.registerBlock(blockBrineSaturated, ItemBlockFactumOpus.class, "factumopus.blockBrineSaturated");
		itemBucketBrineSaturated = new ItemBucketFactumOpus(blockBrineSaturated)
			.setUnlocalizedName("brine_bucket_saturated").setContainerItem(Items.bucket);
		GameRegistry.registerItem(itemBucketBrineSaturated, "factumopus.itemBucketBrineSaturated");
		blacklistFluid(brineSaturated);

		containerHandler.buckets.put(blockBrineSaturated, itemBucketBrineSaturated);
		FluidContainerRegistry.registerFluidContainer(brineSaturated,
			new ItemStack(itemBucketBrineSaturated), new ItemStack(Items.bucket));

		// Void fumes
		voidfumes = new Fluid("factumopus.voidfumes").setGaseous(true).setDensity(20).setViscosity(20);
		FluidRegistry.registerFluid(voidfumes);
		FluidContainerRegistry.registerFluidContainer(voidfumes, new ItemStack(itemBottles, 1, 0), new ItemStack(Items.glass_bottle));
		blacklistFluid(voidfumes);

		// Void Essence
		voidessence = new Fluid("factumopus.voidessence").setGaseous(true);
		FluidRegistry.registerFluid(voidessence);
		FluidContainerRegistry.registerFluidContainer(voidessence, new ItemStack(itemBottles, 1, 1), new ItemStack(Items.glass_bottle));
		blacklistFluid(voidessence);

		// Void Goo
		voidgoo = new Fluid("factumopus.voidgoo").setDensity(10000).setViscosity(10000);
		FluidRegistry.registerFluid(voidgoo);
		FMLInterModComms.sendMessage(Mods.RFTools, "dimlet_preventworldgen", "Liquid." + voidgoo.getName());
		FMLInterModComms.sendMessage(Mods.RFTools, "dimlet_preventloot", "Liquid." + voidgoo.getName());
		FMLInterModComms.sendMessage(Mods.RFTools, "dimlet_configure", "Liquid." + voidgoo.getName() + "=100000,50000,144000,6");
		FMLInterModComms.sendMessage(Mods.Mystcraft, "blacklistfluid", voidgoo.getName());

		blockVoidGooFluid = new BlockFluidVoidGoo(voidgoo);
		GameRegistry.registerBlock(blockVoidGooFluid, ItemBlockFactumOpus.class, "factumopus.blockFluidVoidGoo");
		GameRegistry.registerTileEntity(TileFluidVoidGoo.class, "factumopus.blockFluidVoidGoo");

		itemBucketVoidGoo = new ItemBucketFactumOpus(blockVoidGooFluid)
			.setUnlocalizedName("void_goo_bucket").setContainerItem(Items.bucket);
		GameRegistry.registerItem(itemBucketVoidGoo, "factumopus.itemBucketVoidGoo");

		containerHandler.buckets.put(blockVoidGooFluid, itemBucketVoidGoo);
		FluidContainerRegistry.registerFluidContainer(voidgoo,
			new ItemStack(itemBucketVoidGoo), new ItemStack(Items.bucket));

		blockCompressorWall = new BlockCompressorWall();
		GameRegistry.registerBlock(blockCompressorWall, ItemBlockFactumOpus.class, "factumopus.blockCompressorWall");
		GameRegistry.registerTileEntity(TileCompressorWall.class, "factumopus.blockCompressorWall");

		blockCompressorValve = new BlockCompressorValve();
		GameRegistry.registerBlock(blockCompressorValve, ItemBlockFactumOpus.class, "factumopus.blockCompressorValve");
		GameRegistry.registerTileEntity(TileCompressorValve.class, "factumopus.blockCompressorValve");

		blockFumeCompressor = new BlockFumeCompressor();
		GameRegistry.registerBlock(blockFumeCompressor, ItemBlockFactumOpus.class, "factumopus.blockFumeCompressor");
		GameRegistry.registerTileEntity(TileFumeCompressor.class, "factumopus.blockFumeCompressor");

		// Compat
		if(Mods.isLoaded(Mods.Forestry) && Mods.isLoaded(Mods.ExtraBees)) {
			extraBees = new IntegrationExtraBees();
			extraBees.preInit();
		}

		{
			String darkIronOreName = Core.registry.dark_iron_ore.getUnlocalizedName();
			FMLInterModComms.sendMessage(Mods.RFTools, "dimlet_configure", "Material." + darkIronOreName + "=10000,10000,72000,5");
			FMLInterModComms.sendMessage(Mods.RFTools, "dimlet_preventloot", "Material." + darkIronOreName);
		}

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(containerHandler);
	}

	private void blacklistFluid(Fluid fluid) {
		FMLInterModComms.sendMessage(Mods.RFTools, "dimlet_blacklist", "Liquid." + fluid.getName());
		FMLInterModComms.sendMessage(Mods.Mystcraft, "blacklistfluid", fluid.getName());
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {

		if(Mods.isLoaded(Mods.Forestry) && Mods.isLoaded(Mods.ExtraBees) && extraBees != null) {
			extraBees.init();
		}

		if(Mods.isLoaded(Mods.Botany)) {
			FactumOpus.log.info("Adding Botany integration.");
			FMLInterModComms.sendMessage(Mods.Botany, "add-acid-fertiliser-2", new ItemStack(itemSalts, 1, 3));
			FMLInterModComms.sendMessage(Mods.Botany, "add-nutrient-fertiliser-2", new ItemStack(itemSalts, 1, 4));
			FMLInterModComms.sendMessage(Mods.Botany, "add-acid-fertiliser-1", new ItemStack(itemSalts, 1, 8));
			FMLInterModComms.sendMessage(Mods.Botany, "add-nutrient-fertiliser-1", new ItemStack(itemSalts, 1, 9));
		}
		if(Mods.API.hasAPI(Mods.API.BuildCraftTransport)) {
			registerStripesHandlers();
		}

		proxy.registerRenderers();
	}

	@Optional.Method(modid = Mods.API.BuildCraftTransport)
	private void registerStripesHandlers() {
		log.info("Registering BuildCraft Stripes Pipe Handlers...");
		PipeManager.registerStripesHandler(new StripesHandlerVoidFumes(), 5);
		PipeManager.registerStripesHandler(new StripesHandlerSaltBowl(), 5);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		TileFumeCompressor.setupFluidStacks();

		Item pipeWaterproof = GameRegistry.findItem(Mods.BuildCraftTransport, "pipeWaterproof");
		if(pipeWaterproof != null) {
			OreDictionary.registerOre("fo:airSealant", pipeWaterproof);
		}

		new ModRecipes().registerRecipes();
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
			voidgoo.setIcons(blockVoidGooFluid.getBlockTextureFromSide(1), blockVoidGooFluid.getBlockTextureFromSide(2));
		}
		if(brine != null) {
			brine.setIcons(blockBrine.getBlockTextureFromSide(1), blockBrine.getBlockTextureFromSide(2));
		}
		if(brineSaturated != null) {
			brineSaturated.setIcons(blockBrineSaturated.getBlockTextureFromSide(1), blockBrineSaturated.getBlockTextureFromSide(2));
		}
	}
}
