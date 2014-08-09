package com.shabren.asimovcraft.client;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderRobot extends RenderLiving
{
    private static final ResourceLocation texture = new ResourceLocation("asimovcraft:textures/entity/robot.png");

	public RenderRobot()
	{
		super( new ModelRobot(), 0.25f );
	}

	@Override
	protected ResourceLocation getEntityTexture( Entity p_110775_1_ )
	{
		return texture;
	}

}
