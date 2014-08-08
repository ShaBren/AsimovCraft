package com.shabren.asimovcraft;

import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class JythonInterpreter extends Thread
{
	private PythonInterpreter interp = null;
	public RobotAPI api = null;

	public JythonInterpreter()
	{
	}

	public void init()
	{
		interp = new PythonInterpreter();

	}

	@Override
	public void run()
	{
		// Set variable values within the PythonInterpreter instance
		//interp.set( "a", new PyInteger( 42 ) );
		//interp.exec( "print a" );
		//interp.exec( "x = 2+2" );

		// Obtain the value of an object from the PythonInterpreter and store it
		// into a PyObject.
		//PyObject x = interp.get( "x" );
		//System.out.println( "x: " + x );

		//try
		//{
			interp.exec( "import com.shabren.asimovcraft.RobotAPI" );
			interp.set( "robot", api );
			//interp.exec( "while True: robot.goUp(); robot.goForward(); robot.goLeft(); robot.goDown(); robot.goBack(); robot.goRight()" );
			interp.exec( "while True: robot.goForward()" );
		//}
		//catch ( InterruptedException e )
		//{
			//// TODO Auto-generated catch block
			//e.printStackTrace();
		//}

	}
}
