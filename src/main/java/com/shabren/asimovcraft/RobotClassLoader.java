package com.shabren.asimovcraft;

import java.util.ArrayList;

public class RobotClassLoader extends ClassLoader
{
	public RobotClassLoader()
	{
	}

	@Override
	public Class findClass( String name ) throws ClassNotFoundException
	{
		if ( name.startsWith( "com.shabren.asimovcraft.RobotAPI" ) )
		{
			return super.findClass( name );
		}

		throw new ClassNotFoundException( name + " not permitted." );
	}

	@Override
	protected Class< ? > loadClass( String name, boolean resolve ) throws ClassNotFoundException
	{
		if ( name.startsWith( "com.shabren.asimovcraft.RobotAPI" ) )
		{
			return super.loadClass( name, resolve );
		}

		throw new ClassNotFoundException( name + " not permitted." );
	}
}