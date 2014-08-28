package com.shabren.asimovcraft;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class Robot
{
	public static class Facing
	{
		public static final int NORTH = 0;
		public static final int EAST = 1;
		public static final int SOUTH = 2;
		public static final int WEST = 3;
	};

	protected EntityRobot entity;
	protected String owner;
	protected String lastSource;
	protected String name;
	protected String id;
	protected int currentTick = 0;
	protected int lastTick = 0;
	protected RobotEvent nextEvent;
	protected JythonInterpreter interpreter;
	protected int facing = Facing.NORTH;
	protected final Semaphore available = new Semaphore( 1, true );
	protected int fuelLevel;
	protected int fuelTier;
	protected int ticksPerAction;
	protected boolean outOfFuel;

	public Robot( EntityRobot entity, String player )
	{
		this.entity = entity;
		this.owner = player;
		this.fuelLevel = 0;
		this.setFuelTier( 1 );

		this.id = Long.toHexString( this.getWorld().getTotalWorldTime() );
	}

	public void loadSource( String source )
	{
		this.lastSource = source;

		if ( source.length() < 1 )
		{
			return;
		}

		if ( !this.getWorld().isRemote )
		{
			this.interpreter = new JythonInterpreter( this );
			this.interpreter.setSource( source );
			this.interpreter.setOStream( new RobotOutputStream().setRobot( this ) );
			this.interpreter.setPriority( Thread.MIN_PRIORITY );
			this.interpreter.start();
		}
	}

	public void doTick()
	{
		if ( this.currentTick % this.ticksPerAction == 0 )
		{
			this.processPendingEvent();
		}

		this.currentTick++;
	}

	private void processPendingEvent()
	{
		if ( this.nextEvent != null )
		{
			if ( AsimovCraft.fuelRequired )
			{
				if ( this.nextEvent.fuelNeeded() > this.fuelLevel )
				{
					if ( !this.outOfFuel )
					{
						this.sendToOwner( "Out of fuel!" );
						this.outOfFuel = true;
					}

					return;
				}
				else
				{
					this.fuelLevel -= this.nextEvent.fuelNeeded();
				}
			}

			this.nextEvent.run( this );
			this.lastTick = this.currentTick;
		}

		this.nextEvent = null;

		this.available.release();
	}

	public void queueEvent( RobotEvent event )
	{
		try
		{
			this.available.acquire();
		}
		catch ( Exception e )
		{
			this.threadDied();
		}

		this.nextEvent = event;
	}

	public void threadDied()
	{
		this.sendToOwner( "I ded :(" );
	}

	public void sendToOwner( String string )
	{
		EntityPlayer player = this.getWorld().getPlayerEntityByName( owner );

		if ( player == null )
		{
			return;
		}

		player.addChatMessage( new ChatComponentText( "[" + this.getName() + "] " + string ) );
	}

	public void move( double offsetX, double offsetY, double offsetZ )
	{
		switch ( this.facing )
		{
		case Facing.NORTH:
			this.goTo( this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ );
			break;
		case Facing.EAST:
			this.goTo( this.getX() - offsetZ, this.getY() + offsetY, this.getZ() + offsetX );
			break;
		case Facing.SOUTH:
			this.goTo( this.getX() - offsetX, this.getY() + offsetY, this.getZ() - offsetZ );
			break;
		case Facing.WEST:
			this.goTo( this.getX() + offsetZ, this.getY() + offsetY, this.getZ() - offsetX );
			break;
		}
	}

	// direction = 1 for right, -1 for left
	public void turn( int direction )
	{
		this.facing = ( this.facing + direction ) % 4;

		if ( this.facing < 0 )
		{
			this.facing += 4;
		}

		this.goTo( this.getX(), this.getY(), this.getZ() );
	}

	public void setFacing( int facing )
	{
		this.facing = facing;
	}

	public void goTo( double posX, double posY, double posZ )
	{
		entity.setPositionAndRotation( posX, posY, posZ, this.facing * 90 + 45, 0 );
	}

	public synchronized String getOwner()
	{
		return this.owner;
	}

	public void breakBlock( int x, int y, int z )
	{
		Block b = this.getWorld().getBlock( x, y, z );

		ArrayList< ItemStack > drops = b.getDrops( this.getWorld(), x, y, z, this.getWorld().getBlockMetadata( x, y, z ), 0 );
		// b.dropBlockAsItem( this.getWorld(), x, y, z,
		// this.getWorld().getBlockMetadata( x, y, z ), 0 );
		this.getWorld().removeTileEntity( x, y, z );
		this.getWorld().setBlockToAir( x, y, z );
	}

	public synchronized String getName()
	{
		if ( this.name.length() > 0 )
		{
			return this.name;
		}
		else
		{
			return this.id;
		}
	}

	public synchronized void setName( String name )
	{
		this.name = name;
	}

	public synchronized String getLastSource()
	{
		return this.lastSource;
	}

	public synchronized int getFacing()
	{
		return this.facing;
	}

	public int getX()
	{
		return this.entity.getX();
	}

	public int getY()
	{
		return this.entity.getY();
	}

	public int getZ()
	{
		return this.entity.getX();
	}

	public World getWorld()
	{
		return this.entity.worldObj;
	}

	public int getFuelLevel()
	{
		return fuelLevel;
	}

	public void setFuelLevel( int fuelLevel )
	{
		this.fuelLevel = fuelLevel;

		if ( this.fuelLevel > 0 )
		{
			this.outOfFuel = false;
		}
	}

	public int getFuelTier()
	{
		return fuelTier;
	}

	public void setFuelTier( int fuelTier )
	{
		this.fuelTier = fuelTier;

		switch ( this.fuelTier )
		{
		case 1:
			this.ticksPerAction = AsimovCraft.fuelTicks1;
			break;

		case 2:
			this.ticksPerAction = AsimovCraft.fuelTicks2;
			break;

		case 3:
			this.ticksPerAction = AsimovCraft.fuelTicks3;
			break;

		case 4:
			this.ticksPerAction = AsimovCraft.fuelTicks4;
			break;

		default:
			this.ticksPerAction = 10;
			break;
		}
	}
}
