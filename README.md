# Quizzz!

**IMPORTANT:**

This is a repository containing a little project I did with 4 other computer science students.
It's a game which was developed using Java Spring, in addition to this, it also features a RESTful backend (REST API).


## Description of project
Quizzz is an intense, high-paced, quiz-esque game aimed to educate people about energy usages of different activities. 
It supports singleplayer and multiplayer.

## Group members

[REDACTED]

## Installation & Launch
The following are installation & launch instructions for Quizzz!. Pre-requisite knowledge about the utilisation
of git and bash is assumed. 
- All referenced commands are for a windows based environment. Append `./` to the start of any gradle command(s) if you are using git bash or a linux distribution.
- You must have JDK16+ installed

### Clone this Repository
To run or contribute to this project, you must first clone this repository
#### SSH
```
[REDACTED]
```
- **Warning:** You must have a valid SSH key. The set-up instructions & most common errors can be found [here](https://docs.gitlab.com/ee/user/ssh.html)

#### HTTP (Not Recommended)
```
[REDACTED]
```

 
### Verifying the Build Integrity
Before proceeding to the next step, ensure that you have a stable build of the product, 
by typing the following into your console (cmd/bash):
```
gradlew build
```
If the build is stable, `BUILD SUCCESSFUL` should be seen on the console, if this is not the case,
wait for a stable release, or go back to a previous stable release.
### Loading the Activities in from the Main Activity Repository

This is a very crucial step, as the game will not serve it's function
properly without any activities.

#### Downloading the Activities
The compressed activity archive can be downloaded from  [here](https://gitlab.ewi.tudelft.nl/cse1105/2021-2022/activity-bank/-/jobs/2444739/artifacts/raw/20220311-oopp-activity-bank.zip)

Unzip the archive, rename the directory as "activitybank" and put it in the root directory of the project.


#### Loading the Activities from the JSON file
Within the `ActivityRepositoryLoader` class, (which can be found at `server/src/main/java/server/database/ActivityRepositoryLoader.java`)
- set `LOAD_ACTIVITIES` to `true`

Start the server one time through your **DEVELOPMENT ENVIRONMENT** (important as the filepath is not recognised if you use `gradlew bootRun`), and wait for the activities to load. 
Set it back to `false` right afterwards to ensure that activities don't get reloaded/overwritten.

### Starting the Application
#### Starting the Server

To start the server do the following:

- Through your IDE, Go to the `Main` class found at `server/src/main/java/server` and run `Main.jar`



#### Starting the Client
To start the client, type the following into your console:

- Through your IDE, Go to the `Client.Main` class found at `client/src/main/java/client` and run `Main.jar`



You should be all set to play the game! Have Fun!



