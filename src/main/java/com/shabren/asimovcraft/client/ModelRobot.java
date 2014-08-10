package com.shabren.asimovcraft.client;

import com.shabren.asimovcraft.AsimovCraft;

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
		box.render( f5 );
	}
}