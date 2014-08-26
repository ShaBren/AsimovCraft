package com.shabren.asimovcraft;

import java.io.IOException;
import java.io.OutputStream;

public class RobotOutputStream extends OutputStream
{
	private StringBuilder buffer = new StringBuilder();
	private Robot robot;

	public RobotOutputStream setRobot( Robot entity )
	{
		robot = entity;
		return this;
	}

	@Override
	public void write( int arg0 ) throws IOException
	{
		if ( arg0 == '\n' )
		{
			robot.sendToOwner( buffer.toString() );
			buffer.setLength( 0 );
		}
		else
		{
			buffer.append( ( char )arg0 );
		}
	}

}
