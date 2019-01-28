# Java Unterzee
Java Unterzee is a submarine simulation game written in Java using the Swing toolkit with a custom game engine. Download the latest release [here](https://github.com/SebLavK/java-unterzee/releases) and check the [Wiki](https://github.com/SebLavK/java-unterzee/wiki) for instructions on how to play.

## Planned and possible features
* ~~Orders to your submarine given by a command line~~
* Ship visibility and detection based on target solution
* Enemy and civilian AI
* Views for ~~strategy map~~ and periscope
* "Realistic" torpedo stores and loading times
* Plotting on map
* Panning and ~~zooming~~ map

## Used assets
Asset: [Sea Warfare set, ships and more](https://opengameart.org/content/sea-warfare-set-ships-and-more)  
Author: **Lowder2** at opengameart.org  
License: Free use license  

Asset: [The Battle for Wesnoth water animation](https://opengameart.org/content/the-battle-for-wesnoth-water-animation)  
Author: **Zabin** and **zookeper** at opengameart.org  
License:  Free use license

Asset: [Joystix Monospace](http://typodermicfonts.com/proportional-joystix/)  
Author: **Typodermic Fonts**  
Licenses:  
The Fontspring Desktop Font EULA Version 1.7.0 - February 26, 2017 ([link](https://www.fontspring.com/lic/jcefupvyrh))  
and  
The Fontspring Application Font EULA Version 1.7.2 - May 14, 2018 ([link](https://www.fontspring.com/lic/avhl0opgxj)) (with permission from the author)

Asset: [USS Columbus (SSN 762) performing an emergency ballast blow](https://commons.wikimedia.org/wiki/File:980604-N-7726D-002_Submarine_Emergency_Surfacing_Drill.jpg)  
Author: U.S. Navy photo by Photographer's Mate 2nd Class David C. Duncan  
License: Public domain

## Changelog

### v0.1
* Reached v0.1!

### v0.0.12
* Submarine can now launch torpedoes
* Added intro screen with fading in and out animation

### v0.0.11
* Added more ships to starting scenario
* Added targeting command
* Information of targeted ship now appears on side panel
* Started weapon and projectile structure
* Added Sonar class for the submarine

### v0.0.10
* Added camera panning, now you can choose between following the sub or free roaming
* Smoothed zooming
* When zooming out the map changes background into a "strategy" view
* Transition between close zoom and far zoom, dynamic change of vessel display mode
* Added a grid to the strategy map
* Added some transparent images for beautiness, both to map screen and side panel

### v0.0.9
* Added antialiasing
* Started work on the data screen
* Vessels can now dive!
* Submarine sprite changes to a bluer color the deeper it is
* Added depth related commands

### v0.0.8
* Various bugfixes and a few changes to crew messages

### v0.0.7
* Added side panel with data and command sections
* Commands are now given through the game's command license
* Crew messages now appear in game loading
* Map is now zoomable by scroll wheel

### v0.0.6
* Perfected ship turning dynamics
* Added camera class, all vessels are displayed in the map relative to it. Can free roam or follow the player submarine
* Added scenario class for game object creation and handling
* Added classes for each kind of ship

### v0.0.5
* Map zoom now uses floating point variable
* All sprites scale accordingly when zooming the map
* Vessel speeds are now more realistic
* Better control of sleep time in between frames
* Player gets notified after certain events (e.g. reached intended speed)

### v0.0.4
* Added wavy sea background
* Added image assets
* Map is now zommable

### v0.0.3
* Parametrized orders for safety in interactions between parser and interpreter

### v0.0.2
* Added a Parser for commands interpreted by the Executive Officer

### v0.0.1
* A working prototype!
