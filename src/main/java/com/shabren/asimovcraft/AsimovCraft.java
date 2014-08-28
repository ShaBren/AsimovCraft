package com.shabren.asimovcraft;

import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod( modid = AsimovCraft.MODID, name = AsimovCraft.MODID, version = AsimovCraft.VERSION )
public class AsimovCraft
{
	public static final String MODID = "AsimovCraft";
	public static final String VERSION = "1.0";

	@Instance( value = "AsimovCraft" )
	public static AsimovCraft instance;

	public final static Block robotController = new BlockController().setBlockName( "robotController" ).setCreativeTab( CreativeTabs.tabBlock ).setBlockTextureName( "asimovcraft:robotController" );;

	@SidedProxy( clientSide = "com.shabren.asimovcraft.client.ClientProxy", serverSide = "com.shabren.asimovcraft.CommonProxy" )
	public static CommonProxy proxy;

	public static Logger logger;
	public static Configuration config;

	public static boolean fuelRequired;
	public static int fuelTicks1;
	public static int fuelTicks2;
	public static int fuelTicks3;
	public static int fuelTicks4;

	@EventHandler
	public void preInit( FMLPreInitializationEvent event )
	{
		logger = event.getModLog();
		config = new Configuration( event.getSuggestedConfigurationFile() );

		config.load();

		fuelRequired = config.get( "Fuel", "Fuel Required", true ).getBoolean();

		fuelTicks1 = 20 / config.get( "Fuel", "Actions per second (tier 1)", 1 ).getInt();
		fuelTicks2 = 20 / config.get( "Fuel", "Actions per second (tier 2)", 2 ).getInt();
		fuelTicks3 = 20 / config.get( "Fuel", "Actions per second (tier 3)", 3 ).getInt();
		fuelTicks4 = 20 / config.get( "Fuel", "Actions per second (tier 4)", 4 ).getInt();

		config.save();
	}

	@EventHandler
	public void init( FMLInitializationEvent event )
	{
		proxy.registerRenderers();
		GameRegistry.registerBlock( robotController, "robotController" );
		EntityRegistry.registerModEntity( EntityRobot.class, "entityRobot", 0, instance, 64, 1, false );
		NetworkRegistry.INSTANCE.registerGuiHandler( instance, proxy );
	}

	@EventHandler
	public void postInit( FMLPostInitializationEvent event )
	{
	}

	@EventHandler
	public void serverLoad( FMLServerStartingEvent event )
	{
		event.registerServerCommand( new RoboCodeCommand() );
	}
}
