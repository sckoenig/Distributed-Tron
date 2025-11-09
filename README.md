
## Distributed Tron

This repository contains the code for Distributed Tron - a game developed as a team project
in the Distributed Systems Course at HAW Hamburg.

## How To Use

You will need:
- Java 17
- Maven

You will also need the view-library:
`````bash
git clone git@gitlab.com:sckoenig/view-library.git
mvn clean install -DskipTests 
`````

To run the game: 
`````bash
mvn clean package
java -jar ./target/tronGame-1.0.jar
`````

To configure the game, take a look at the ````tronConfig.properties````,
where you can set the arena size etc. as well as the network to play in.

The game can be played in three modes:
- ```LOCAL```: The players play on the same device and keyboard
- ```RPC```: The players can play distributed in the same network using RPC.
**Note**: One of the players must mark ```nameServerHost=TRUE```, and
  the nameserver's address must be known by the players.

- ```REST```: The players can play distributed in the same network, with other team's implementations via Rest protocol.

For more info, take a look at the detailed information here: 

[Game](Dokumentation/App/TronGame_Documentation.md)  
[Middleware](Dokumentation/Middleware/Middleware_Documentation.md)

