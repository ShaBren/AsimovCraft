package com.shabren.asimovcraft;

import java.util.concurrent.Semaphore;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

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
	protected int currentTick = 0;
	protected int lastTick = 0;
	protected RobotEvent nextEvent;
	protected JythonInterpreter interpreter;
	protected int facing = Facing.NORTH;
	protected final Semaphore available = new Semaphore( 1, true );

	public Robot( EntityRobot entity, String player )
	{
		this.entity = entity;

		this.owner = player;
		this.name = Long.toHexString( entity.worldObj.getTotalWorldTime() );
	}

	public void loadSource( String source )
	{
		lastSource = source;

		if ( source.length() < 1 )
		{
			return;
		}

		if ( !entity.worldObj.isRemote )
		{
			interpreter = new JythonInterpreter( this );
			interpreter.setSource( source );
			interpreter.setOStream( new RobotOutputStream().setRobot( this ) );
			interpreter.setPriority( Thread.MIN_PRIORITY );
			interpreter.start();
		}
	}

	public void doTick()
	{

		if ( currentTick % 10 == 0 )
		{
			this.processPendingEvent();
		}

		currentTick++;
	}

	private void processPendingEvent()
	{
		if ( nextEvent != null )
		{
			nextEvent.run( this );
			lastTick = currentTick;
		}

		nextEvent = null;

		available.release();
	}

	public void queueEvent( RobotEvent event )
	{
		try
		{
			available.acquire();
		}
		catch ( Exception e )
		{
			this.threadDied();
		}

		nextEvent = event;
	}

	public void threadDied()
	{
		this.sendToOwner( "I ded :(" );
	}

	public void sendToOwner( String string )
	{
		EntityPlayer player = entity.worldObj.getPlayerEntityByName( owner );

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

	public synchronized String getName()
	{
		return this.name;
	}

	public synchronized void setName( String name )
	{
		if ( name.length() < 1 )
		{
			return; // Can't have blank name
		}

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
		return entity.getX();
	}

	public int getY()
	{
		return entity.getY();
	}

	public int getZ()
	{
		return entity.getX();
	}
}
