package com.shabren.asimovcraft;

import com.shabren.asimovcraft.client.GuiController;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	public void registerRenderers()
	{
		// Nothing here as the server doesn't render graphics or entities!
	}

	@Override
	public Object getServerGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z )
	{
		return null;
	}

	@Override
	public Object getClientGuiElement( int ID, EntityPlayer player, World world, int x, int y, int z )
	{
		if ( ID == GuiController.GUI_ID )
		{
			return new GuiController();
		}

		return null;
	}
}
