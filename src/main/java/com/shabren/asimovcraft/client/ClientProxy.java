package com.shabren.asimovcraft.client;

import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderMagmaCube;
import net.minecraftforge.client.MinecraftForgeClient;

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
