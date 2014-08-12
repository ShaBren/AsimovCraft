package com.shabren.asimovcraft;

public class RobotAPIMovement extends RobotAPI
{
	protected static final RobotEvent eventMoveBackward = new RobotEvent() { public void run( EntityRobot robot ) { robot.move( -1, 0, 0 ); } }; 
	protected static final RobotEvent eventMoveDown = new RobotEvent() { public void run( EntityRobot robot ) { robot.move( 0, -1, 0 ); } }; 
	protected static final RobotEvent eventMoveForward = new RobotEvent() { public void run( EntityRobot robot ) { robot.move( 1, 0, 0 ); } };
	protected static final RobotEvent eventMoveLeft = new RobotEvent() { public void run( EntityRobot robot ) { robot.move( 0, 0, -1 ); } };
	protected static final RobotEvent eventMoveRight = new RobotEvent() { public void run( EntityRobot robot ) { robot.move( 0, 0, 1 ); } };
	protected static final RobotEvent eventMoveUp = new RobotEvent() { public void run( EntityRobot robot ) { robot.move( 0, 1, 0 ); } };
	protected static final RobotEvent eventTurnBackward = new RobotEvent() { public void run( EntityRobot robot ) { robot.turn( 2 );} };
	protected static final RobotEvent eventTurnLeft = new RobotEvent() { public void run( EntityRobot robot ) { robot.turn( -1 );} };
	protected static final RobotEvent eventTurnRight = new RobotEvent() { public void run( EntityRobot robot ) { robot.turn( 1 );} };

	public RobotAPIMovement( EntityRobot pRobot )
	{
		super( pRobot );
	}

	public boolean move( Direction dir )
	{
		switch ( dir )
		{
		case BACKWARD:
			robot.queueEvent( eventMoveBackward );
			break;

		case DOWN:
			robot.queueEvent( eventMoveDown );
			break;

		case FORWARD:
			robot.queueEvent( eventMoveForward );
			break;

		case LEFT:
			robot.queueEvent( eventMoveLeft );
			break;

		case RIGHT:
			robot.queueEvent( eventMoveRight );
			break;

		case UP:
			robot.queueEvent( eventMoveUp );
			break;
		}
		
		return true;
	}

	public boolean turn( Direction dir )
	{
		switch ( dir )
		{
		case BACKWARD:
			robot.queueEvent( eventTurnBackward );
			break;

		case LEFT:
			robot.queueEvent( eventTurnLeft );
			break;

		case RIGHT:
			robot.queueEvent( eventTurnRight );
			break;

		case DOWN:
		case FORWARD:
		case UP:
		default:
			robot.sendToOwner( "ERROR: RobotAPIMovement.turn: Invalid direction." );
			return false;
		}
		
		return true;
	}

	public int getX()
	{
		return ( int )robot.posX;
	}

	public int getY()
	{
		return ( int )robot.posY;
	}

	public int getZ()
	{
		return ( int )robot.posZ;
	}
}
