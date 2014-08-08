# AsimovCraft

## What is AsimovCraft?
AsimovCraft is a Minecraft mod that provides in-game robots. The robots are programmable using Python.

## Where can I download it?
AsimovCraft is still in development, and is not yet ready for widespread usage. However, if you'd like to play around with it, you can clone this repository and run a `gradle build`.

## Usage
Currently, you can create a robot using the command `/rc set <hash>`, where `<hash>` is the ID of a Python script uploaded to http://haste.shabren.com. For example, `/rc set gexucuboru` will load a simple script that makes the robot move in circles.

## Robot API
The robot API is accessed via the `robot` variable in your Python script. There is currently no documentation, so take a look at http://haste.shabren.com/gexucuboru.py for a simple example.

## Security Warning
AsimovCraft currently provides no sandboxing for user code, so if you run it on a server, only allow users you trust to access it. They will effectively have full access to the user account running the server.

## License
AsimovCraft is available under the terms of the zlib license. See LICENSE.md for details.

## Author
Stephen Bryant, also known on the internet as Sha`Bren.
