package com.shabren.asimovcraft;

public class RobotAPI
{
	public enum Direction
	{
		FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN
	};

	/* @formatter:off */
	protected static final RobotEvent eventSleep = new RobotEvent() { public void run( Robot robot ) { } public int fuelNeeded() { return 0; } };
	/* @formatter:on */

	protected Robot robot;

	public RobotAPI( Robot pRobot )
	{
		robot = pRobot;
	}

	public boolean sleep()
	{
		robot.queueEvent( eventSleep );
		return true;
	}

	public String getOwner()
	{
		return robot.getOwner();
	}

	public String getName()
	{
		return robot.getName();
	}

	public void setName( String name )
	{
		robot.setName( name );
	}
}
