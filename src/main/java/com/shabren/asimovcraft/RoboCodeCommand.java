package com.shabren.asimovcraft;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class RoboCodeCommand implements ICommand
{
	private List aliases;

	public RoboCodeCommand()
	{
		aliases = new ArrayList();
		aliases.add( "robocode" );
		aliases.add( "rc" );
	}

	@Override
	public int compareTo( Object arg0 )
	{
		return 0;
	}

	@Override
	public String getCommandName()
	{
		return "robocode";
	}

	@Override
	public String getCommandUsage( ICommandSender p_71518_1_ )
	{
		return "robocode set <hastebin hash>";
	}

	@Override
	public List getCommandAliases()
	{
		return aliases;
	}

	@Override
	public void processCommand( ICommandSender sender, String[] command )
	{
		if ( !( sender instanceof EntityPlayer ) )
		{
			sender.addChatMessage( new ChatComponentText( "This command can only be run by a player." ) );
			return;
		}

		EntityPlayer player = ( EntityPlayer )sender;

		if ( command.length == 0 )
		{
			sender.addChatMessage( new ChatComponentText( "Invalid arguments" ) );
			return;
		}

		if ( command[ 0 ].equals( "set" ) )
		{
			if ( command.length < 2 )
			{
				sender.addChatMessage( new ChatComponentText( "Invalid arguments" ) );
				return;
			}

			World world = player.worldObj;

			EntityRobot entity;
			entity = new EntityRobot( world );
			entity.setPosition( player.getPlayerCoordinates().posX + 2, player.getPlayerCoordinates().posY, player.getPlayerCoordinates().posZ );

			world.spawnEntityInWorld( entity );
			entity.loadSource( command[1], player );
		}
	}

	@Override
	public boolean canCommandSenderUseCommand( ICommandSender p_71519_1_ )
	{
		return true;
	}

	@Override
	public List addTabCompletionOptions( ICommandSender p_71516_1_, String[] p_71516_2_ )
	{
		return null;
	}

	@Override
	public boolean isUsernameIndex( String[] p_82358_1_, int p_82358_2_ )
	{
		return false;
	}

}
