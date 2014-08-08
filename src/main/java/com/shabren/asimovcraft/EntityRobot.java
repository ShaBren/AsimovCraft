package com.shabren.asimovcraft;

import java.util.concurrent.Semaphore;

import cpw.mods.fml.common.SidedProxy;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class EntityRobot extends EntityLiving
{
	public enum RobotEventType
	{
		NONE, SLEEP, MOVE_FORWARD, MOVE_BACKWARD, MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN
	};

	private JythonInterpreter interpreter;
	private final Semaphore available = new Semaphore( 1, true );
	private RobotEventType nextEvent = RobotEventType.NONE;
	private int currentTick = 0;
	private int lastTick = 0;
	private String owner;

	public EntityRobot( World par1 )
	{
		super( par1 );

		this.setSize( 1.0f, 1.0f );
		this.isImmuneToFire = true;
		this.noClip = true;
	}

	public void loadSource( String source, EntityPlayer player )
	{
		owner = player.getCommandSenderName();

		if ( !this.worldObj.isRemote )
		{
			interpreter = new JythonInterpreter();
			interpreter.api = new RobotAPI( this );
			interpreter.setSource( source );
			interpreter.setOStream( new RobotOutputStream().setRobot( this ) );
			interpreter.start();
		}
	}

	@Override
	protected void fall( float p_70069_1_ )
	{
	}

	@Override
	protected void updateFallState( double p_70064_1_, boolean p_70064_3_ )
	{
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if ( currentTick % 10 == 0 )
		{
			this.processPendingEvent();
		}

		currentTick++;
	}

	@Override
	public void onLivingUpdate()
	{
		if ( this.newPosRotationIncrements > 0 )
		{
			double d0 = this.posX + ( this.newPosX - this.posX ) / ( double )this.newPosRotationIncrements;
			double d1 = this.posY + ( this.newPosY - this.posY ) / ( double )this.newPosRotationIncrements;
			double d2 = this.posZ + ( this.newPosZ - this.posZ ) / ( double )this.newPosRotationIncrements;
			double d3 = MathHelper.wrapAngleTo180_double( this.newRotationYaw - ( double )this.rotationYaw );
			this.rotationYaw = ( float )( ( double )this.rotationYaw + d3 / ( double )this.newPosRotationIncrements );
			this.rotationPitch = ( float )( ( double )this.rotationPitch + ( this.newRotationPitch - ( double )this.rotationPitch ) / ( double )this.newPosRotationIncrements );
			--this.newPosRotationIncrements;
			this.setPosition( d0, d1, d2 );
			this.setRotation( this.rotationYaw, this.rotationPitch );
		}
		else if ( !this.isClientWorld() )
		{
			this.motionX *= 0.98D;
			this.motionY *= 0.98D;
			this.motionZ *= 0.98D;
		}

		if ( Math.abs( this.motionX ) < 0.005D )
		{
			this.motionX = 0.0D;
		}

		if ( Math.abs( this.motionY ) < 0.005D )
		{
			this.motionY = 0.0D;
		}

		if ( Math.abs( this.motionZ ) < 0.005D )
		{
			this.motionZ = 0.0D;
		}

		this.worldObj.theProfiler.startSection( "travel" );
		this.moveStrafing *= 0.98F;
		this.moveForward *= 0.98F;
		this.randomYawVelocity *= 0.9F;
		this.moveEntityWithHeading( this.moveStrafing, this.moveForward );
		this.worldObj.theProfiler.endSection();
	}

	@Override
	public void moveEntityWithHeading( float p_70612_1_, float p_70612_2_ )
	{
	}

	private void processPendingEvent()
	{
		this.worldObj.theProfiler.startSection( "robotTick" );

		switch ( nextEvent )
		{
		case NONE:
			break;

		case SLEEP:
			break;

		case MOVE_FORWARD:
			this.goTo( posX + 1, posY, posZ );
			break;

		case MOVE_BACKWARD:
			this.goTo( posX - 1, posY, posZ );
			break;

		case MOVE_LEFT:
			this.goTo( posX, posY, posZ - 1 );
			break;

		case MOVE_RIGHT:
			this.goTo( posX, posY, posZ + 1 );
			break;

		case MOVE_UP:
			this.goTo( posX, posY + 1, posZ );
			break;

		case MOVE_DOWN:
			this.goTo( posX, posY - 1, posZ );
			break;

		default:
			break;
		}

		if ( nextEvent != RobotEventType.NONE )
		{
			lastTick = currentTick;
		}

		nextEvent = RobotEventType.NONE;

		available.release();

		this.worldObj.theProfiler.endSection();
	}

	public void queueEvent( RobotEventType event )
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

	public void goTo( double pPosX, double pPosY, double pPosZ )
	{
		this.setPositionAndUpdate( pPosX, pPosY, pPosZ );
	}

	public void threadDied()
	{

	}

	public void sendToOwner( String string )
	{
		EntityPlayer player = this.worldObj.getPlayerEntityByName( owner );

		if ( player == null )
		{
			return;
		}

		player.addChatMessage( new ChatComponentText( string ) );
	}
}
