# JKUTutorTool
A tool for tutors at JKU (or other universities, if the setup is simmilar) to handle submitted eclipse projects, if they have been submitted over Moodle.

# Features
As of today (17.04.2018) the TutorTool can do the following:
* When downloading all exercises over moodle, the TT deletes exercises which are not part of a specified list of names, and only leaves those that have to be corrected by the tutor. There can be more than one folders (called assignments as of now).
* Unzip all eclipse projects to a specified path.
* Rename the Eclipse projects from "<projectname>" to "<projectname><studentname>" to make it easier to see which submission is being corrected (no more renaming by hand!)

**Note: 7zip has to be installed, to handle the extraction of the submissions**

# How To
## The Student List
The Student list has to be a textfile containint a list of names, one name per line, for example:

    Marco Antonovich
    Ingrid Bergmann
    Aron Aaronovsky
    Brad Pittly
    Susan Jenkins

Make sure, that there are no empty lines in the textfile, this will screw something up, I didn't fix that yet because I'm lazy.

## Using the TutorTool jar
I have exported an executable jar, so that makes it easier to use. As of yet there is no GUI, use the commandline to run the tool:
    java -jar tutortool.jar <path to student list> <targetpath> <path to assignment1> [<path to assignment 2> ... <path to assignment n>]
Be aware, that the targetpath has to exist beforehand. 
Download: <to be inserted>

# Building it yourself
If you want to build it yourself, pull the repo and make sure to include the pom. 
