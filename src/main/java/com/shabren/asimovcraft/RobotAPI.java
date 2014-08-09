package com.shabren.asimovcraft;

public class RobotAPI
{
	private EntityRobot robot;

	public RobotAPI( EntityRobot pRobot )
	{
		robot = pRobot;
	}

	public boolean goForward()
	{
		robot.queueEvent( new RobotEvent() { public void run( EntityRobot robot ) { robot.move( 1, 0, 0 ); } } );
		return true;
	}

	public boolean goBack()
	{
		robot.queueEvent( new RobotEvent() { public void run( EntityRobot robot ) { robot.move( -1, 0, 0 ); } } );
		return true;
	}

	public boolean goLeft()
	{
		robot.queueEvent( new RobotEvent() { public void run( EntityRobot robot ) { robot.move( 0, 0, -1 ); } } );
		return true;
	}

	public boolean goRight()
	{
		robot.queueEvent( new RobotEvent() { public void run( EntityRobot robot ) { robot.move( 0, 0, 1 ); } } );
		return true;
	}

	public boolean goUp()
	{
		robot.queueEvent( new RobotEvent() { public void run( EntityRobot robot ) { robot.move( 0, 1, 0 ); } } );
		return true;
	}

	public boolean goDown()
	{
		robot.queueEvent( new RobotEvent() { public void run( EntityRobot robot ) { robot.move( 0, -1, 0 ); } } );
		return true;
	}

	public boolean sleep()
	{
		robot.queueEvent( new RobotEvent() { public void run( EntityRobot robot ) {} } );
		return true;
	}

	public boolean turnRight()
	{
		robot.queueEvent( new RobotEvent() { public void run( EntityRobot robot ) { robot.turn( 90 );} } );
		return true;
	}

	public boolean turnLeft()
	{
		robot.queueEvent( new RobotEvent() { public void run( EntityRobot robot ) { robot.turn( -90 );} } );
		return true;
	}

	public boolean turnAround()
	{
		robot.queueEvent( new RobotEvent() { public void run( EntityRobot robot ) { robot.turn( 180 );} } );
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
