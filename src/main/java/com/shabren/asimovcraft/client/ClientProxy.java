package com.shabren.asimovcraft.client;

import com.shabren.asimovcraft.CommonProxy;
import com.shabren.asimovcraft.EntityRobot;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderers()
	{
		RenderingRegistry.registerEntityRenderingHandler( EntityRobot.class, new RenderRobot() );
	}
}
