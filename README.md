# Quizzz!

This README will need to contain a description of your project, how to run it, how to set up the development environment, and who worked on it.
This information can be added throughout the course, except for the names of the group members.
Add your own name (do not add the names for others!) to the section below.

## Description of project
Quizzz is an intense, high-paced, quiz-esque game aimed to educate people about energy usages of different activities. 
It supports singleplayer and multiplayer.

## Group members

| Profile Picture                                                                                         | Name                  | Email                             |
|---------------------------------------------------------------------------------------------------------|-----------------------|-----------------------------------|
| ![](https://secure.gravatar.com/avatar/9568e7770ae1e2274f2f07854c8c16c3?s=50&d=identicon)               | Henry Page            | H.Page@student.tudelft.nl         |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Vlad Tudor Stefanescu | V.T.Stefanescu@student.tudelft.nl |
| ![](https://secure.gravatar.com/avatar/d7a06f5c69ccf4f9f1f782f91c982cc6?s=50&d=identicon)               | Matyas Kollert        | M.Kollert@student.tudelft.nl      |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Andrei Dascalu        | A.Dascalu@student.tudelft.nl      |
| ![](https://eu.ui-avatars.com/api/?name=OOPP&length=4&size=50&color=DDD&background=777&font-size=0.325) | Tom Huisman           | t.m.huisman@student.tudelft.nl    |

## Installation & Launch
The following are installation & launch instructions for Quizzz!. Pre-requisite knowledge about the utilisation
of git and bash is assumed. 
- All referenced commands are for a windows based environment. Append `./` to the start of any gradle command(s) if you are using git bash or a linux distribution.
- You must have JDK16+ installed

### Clone this Repository
To run or contribute to this project, you must first clone this repository
#### SSH
```
git clone git@gitlab.ewi.tudelft.nl:cse1105/2021-2022/team-repositories/oopp-group-63/repository-template.git
```
- **Warning:** You must have a valid SSH key. The set-up instructions & most common errors can be found [here](https://docs.gitlab.com/ee/user/ssh.html)

#### HTTP (Not Recommended)
```
git clone https://gitlab.ewi.tudelft.nl/cse1105/2021-2022/team-repositories/oopp-group-63/repository-template.git
```

 
### Verifying the Build Integrity
Before proceeding to the next step, ensure that you have a stable build of the product, 
by typing the folllowing into your console (cmd/bash):
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

Unzip the archive, rename the directory as `activitybank` and put it in the root directory of the project.
If there is already a directory called `activitybank`, replace the directory with the new one. 


#### Loading the Activities from the JSON file
Within the `ActivityRepositoryLoader` class, (which can be found at `server/src/main/java/server/database/ActivityRepositoryLoader.java`)
- set `LOAD_ACTIVITIES` to `true`

Start the server one time through your **DEVELOPMENT ENVIRONMENT** (important as the filepath is not recognised if you use `gradlew bootRun`), and wait for the activities to load. 
Set it back to `false` right afterwards to ensure that activities don't get reloaded/overwritten.

### Starting the Application
#### Starting the Server

To start the server do the following:

- Through your IDE, Go to the `Server.Main` class found at `server/src/main/java/server` and run `Main.jar`



#### Starting the Client
To start the client, type the following into your console:

- Through your IDE, Go to the `Client.Main` class found at `client/src/main/java/client` and run `Main.jar`



You should be all set to play the game! Have Fun!



## How to contribute to it
- Create an Issue on GitLab 
- Create a new branch/draft-MR
- Make/Develop a feature
- Open a Merge Request!

