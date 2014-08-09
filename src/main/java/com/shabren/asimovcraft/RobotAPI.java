package com.shabren.asimovcraft;

import com.shabren.asimovcraft.EntityRobot.RobotEventType;

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

}
