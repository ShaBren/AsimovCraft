package com.shabren.asimovcraft.client;

import org.lwjgl.opengl.GL11;

import com.shabren.asimovcraft.AsimovCraft;
import com.shabren.asimovcraft.EntityRobot;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelRobot extends ModelBase
{
	public ModelRenderer box;

	public ModelRobot()
	{
		box = new ModelRenderer( this, 0, 0 );
		box.addBox( 0F, 0F, 0F, 16, 16, 16, 1.0f );
		box.setRotationPoint( -8.0f, 8.0f, -8.0f );
	}

	public void render( Entity entity, float f, float f1, float f2, float f3, float f4, float f5 )
	{
		this.setRotationAngles( f, f1, f2, f3, f4, f5, entity );
		
		box.render( f5 );
	}
}