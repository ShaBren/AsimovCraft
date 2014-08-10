package com.shabren.asimovcraft;

import java.util.Random;

import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod( modid = AsimovCraft.MODID, name = AsimovCraft.MODID, version = AsimovCraft.VERSION )
public class AsimovCraft
{
	public static final String MODID = "AsimovCraft";
	public static final String VERSION = "1.0";

	@Instance( value = "AsimovCraft" )
	public static AsimovCraft instance;

	public final static Block robotController = new RobotController().setBlockName( "robotController" ).setCreativeTab( CreativeTabs.tabBlock ).setBlockTextureName( "asimovcraft:robotController" );;

	@SidedProxy( clientSide = "com.shabren.asimovcraft.client.ClientProxy", serverSide = "com.shabren.asimovcraft.CommonProxy" )
	public static CommonProxy proxy;

	public static Logger logger;

	@EventHandler
	public void init( FMLInitializationEvent event )
	{
		proxy.registerRenderers();
		GameRegistry.registerBlock( robotController, "robotController" );
		EntityRegistry.registerModEntity( EntityRobot.class, "entityRobot", 0, instance, 64, 1, false );
	}

	@EventHandler
	public void preInit( FMLPreInitializationEvent event )
	{
		logger = event.getModLog();
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
