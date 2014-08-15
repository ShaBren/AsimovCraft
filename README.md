# AsimovCraft

## What is AsimovCraft?
AsimovCraft is a Minecraft mod that provides in-game robots. The robots are programmable using Python.

## Where can I download it?
AsimovCraft is still in development, and is not yet ready for widespread usage. However, if you'd like to play around with it, you can clone this repository and run a `gradle build`.

## Usage
Currently, you can create a robot using the command `/rc set <hash>`, where `<hash>` is the ID of a Python script uploaded to http://haste.shabren.com. For example, `/rc set gexucuboru` will load a simple script that makes the robot move in circles.

### Fuel
Robots require fuel to operate. Different fuels allow the robot to operate at different speeds. The fuel types, consumption, and speed are:

Fuel | Actions per item | Actions per second
-----|-----------------:|-----:
Netherrack | 4 | 1
Coal | 8 | 2
Lava | 50 | 2
Blaze Powder | 16 | 3
Blaze Rod | 32 | 3
Nether Star | ∞ | 4

## Robot API
The api is a work in progress, but the currently available functions are:

|Function | Parameters | Return Type | Description|
|-|-|-|-|
|`general.sleep()`| N/A | N/A | Sleep for one cycle.|
|`general.getOwner()` | N/A | `String` | Get the name of the user who created the robot.|
|`general.getName()` | N/A | `String` | Returns the name of the robot if set, otherwise returns the unique id.|
|`general.setName( String name )` | `name` - the desired robot name | N/A | Sets the name of the robot.|
|`movement.move( Direction dir )` | `dir` - the desired direction (see `Direction`) | `Boolean` | Moves the robot one block in the specified direction. Returns true if the movement succeeded, otherwise false.|
|`movement.turn( Direction dir )` | `dir` - the desired direction | `Boolean` | Turns the robot in the specified direction. Returns true if the turn succeeded, otherwise false.|

|Direction|
|---------|
|`RobotAPI.Direction.FORWARD`|
|`RobotAPI.Direction.BACKWARD`|
|`RobotAPI.Direction.LEFT`|
|`RobotAPI.Direction.RIGHT`|
|`RobotAPI.Direction.UP`|
|`RobotAPI.Direction.DOWN`|

## Security Warning
While AsimovCraft attempts to sandbox user code, there is no guarantee that a skilled hacker can't break out of it. Therefore, it is recommended that you only give trusted users the ability to run code.

## License
AsimovCraft is available under the terms of the zlib license. See LICENSE.md for details.

## Author
Stephen Bryant, also known on the internet as Sha`Bren.

