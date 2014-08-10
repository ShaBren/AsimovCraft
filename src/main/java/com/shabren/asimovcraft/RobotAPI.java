package com.shabren.asimovcraft;

public class RobotAPI
{
	private RoboBrain brain;

	public RobotAPI( RoboBrain pbrane )
	{
		brain = pbrane;
	}

	public boolean goForward()
	{
		brain.queueEvent( new RobotEvent() { public void run( RoboBrain brain ) { brain.move( 1, 0, 0 ); } } );
		return true;
	}

	public boolean goBack()
	{
		brain.queueEvent( new RobotEvent() { public void run( RoboBrain brain ) { brain.move( -1, 0, 0 ); } } );
		return true;
	}

	public boolean goLeft()
	{
		brain.queueEvent( new RobotEvent() { public void run( RoboBrain brain ) { brain.move( 0, 0, -1 ); } } );
		return true;
	}

	public boolean goRight()
	{
		brain.queueEvent( new RobotEvent() { public void run( RoboBrain brain ) { brain.move( 0, 0, 1 ); } } );
		return true;
	}

	public boolean goUp()
	{
		brain.queueEvent( new RobotEvent() { public void run( RoboBrain brain ) { brain.move( 0, 1, 0 ); } } );
		return true;
	}

	public boolean goDown()
	{
		brain.queueEvent( new RobotEvent() { public void run( RoboBrain brain ) { brain.move( 0, -1, 0 ); } } );
		return true;
	}

	public boolean sleep()
	{
		brain.queueEvent( new RobotEvent() { public void run( RoboBrain brain ) {} } );
		return true;
	}

	public boolean turnRight()
	{
		brain.queueEvent( new RobotEvent() { public void run( RoboBrain brain ) { brain.turn( 90 );} } );
		return true;
	}

	public boolean turnLeft()
	{
		brain.queueEvent( new RobotEvent() { public void run( RoboBrain brain ) { brain.turn( -90 );} } );
		return true;
	}

	public boolean turnAround()
	{
		brain.queueEvent( new RobotEvent() { public void run( RoboBrain brain ) { brain.turn( 180 );} } );
		return true;
	}

	public int getX()
	{
		return ( int )brain.getX();
	}

	public int getY()
	{
		return ( int )brain.getY();
	}

	public int getZ()
	{
		return ( int )brain.getZ();
	}

}
