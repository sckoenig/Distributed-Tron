[[_TOC_]]

# 1. Einführung und Ziele

Dieses Projekt entsteht im Rahmen des Moduls “Verteilte Systeme” und hat zum Ziel, das Multiplayer-Spiel “Tron” zu entwickeln.
Das Spiel soll lokal an einem Computer oder verteilt auf mehrere Computer gespielt werden können.

## 1.1 Aufgabenstellung

Allgemeine Spielprinzipien:

- Das Spiel wird mit mehreren Spielern gespielt, die jeweils ein Motorrad in einer Arena (das Spielfeld) steuern.
- Die Motorräder bewegen sich stetig vorwärts, in einer konstanten Geschwindigkeit.
- Der Spieler kann das Motorrad nach links oder rechts steuern und sich so über das Spielfeld nach oben, unten, links, rechts bewegen.
- Die Motorräder ziehen farbige “Schatten” hinter sich auf, die für die Lebenszeit des Spielers auf dem Spielfeld bleiben und durch die Vorwärtsbewegung der Motorräder länger werden.
- Ein Spieler stirbt, wenn er gegen die Wand der Arena oder den Schatten eines anderen Spielers fährt. Treffen zwei Spieler aufeinander, sterben beide.
- Wenn ein Spieler stirbt, verschwindet sein Schatten aus der Arena und er kann nicht weiterspielen.
- Alle Spieler spielen gegeneinander. Gewonnen hat der, der am längsten überlebt. Sterben die letzten beiden Spieler gleichzeitig, ist es unentschieden.

Weitere Anforderungen:
- Das Spiel soll über eine Konfigurationsdatei konfiguriert werden können.
- Das Spiel kann entweder lokal oder im Netzwerk gespielt werden. Dies soll ebenfalls über die Konfigurationsdatei einstellbar sein.

## 1.2 Qualitätsziele

| Ziel                          | Beschreibung                                                                                            |
|-------------------------------|---------------------------------------------------------------------------------------------------------|
| Wohldefinierte Schnittstellen | Die Entwickler sollen sich gut um ihre Schnittstellen kümmern :)                                        |
| Kompatibilität                | Mindestens 2 Teams müssen miteinander spielen können.                                                   |
| Fehlertoleranz                | Ein Spiel soll ungestört durchspielbar sein. Auch wenn ein Teilnehmer abstürzt, läuft das Spiel weiter. |
| Fairness                      | Das Spiel soll fair sein. Alle Spieler starten mit fairen Konditionen und folgen den gleichen Regeln.   |
| Bedienbarkeit                 | Spieler sollen das Spiel einfach bedienen können und Spaß haben.                                        |

## 1.3 Stakeholders

| Rolle      | Kontakt | Erwartungen                                                                                          |
| ----------- | ----------- |------------------------------------------------------------------------------------------------------|
| Dozent / Kunde | Martin Becke: martin.becke@haw-hamburg.de | Saubere Architektur mit Pattern und wohldefinierten Schnittstellen, Lernfortschritt der Entwickler   |
| Entwickler | Sandra: sandra.koenig@haw-hamburg.de <br/> Inken: inken.dulige@haw-hamburg.de<br/> Majid: majid.moussaadoyi@haw-hamburg.de| Spaß an der Entwicklung, Architekturentwurf üben, Verteilte Systeme besser verstehen, Zeitmanagement |
| Spieler   | Teilnehmer des Moduls VS WiSe22/23 | Stabile Anwendung, Spaß am Spielen                                                                   |


# 2. Randbedingungen

| Technische Randbedingung     | Beschreibung                                                                                                                                                                                                                                                                                      |
|------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Java in der Version 17       | Zur Implementierung wird Java verwendet, da das ganze Team die Sprache beherrscht. <br/> Die Version muss zum Image der Rechner im Raum 7.65 passen. <br/> Es wird Java in der Version 17 verwendet, da es sich um die neueste LTS-Version handelt.                                               |
| View Library                 | Es wird die zur Verfügung gestellte JavaFX View Library verwendet, um Zeit in der UI-Erstellung zu sparen.                                                                                                                                                                                        |
| Versionsverwaltung in GitLab | Alle Dateien dieses Projektes werden über Git verwaltet. Das zentrale Remote Repository befindet sich im GitLab der HAW. Wegen technischer Störung der HAW wurde am 30.12.2022 auf gitlab.com gewechselt (zu finden unter: https://gitlab.com/noaelx/wise22-23-vs-2582106-2279123-2576932_extern) |

| Konventionen | Beschreibung                                                                                                                                    |
| ----------- |-------------------------------------------------------------------------------------------------------------------------------------------------|
| Dokumentation | Gliederung erfolgt nach dem deutschen arc42-Template, um Struktur zu wahren.                                                                    |
| Sprache | Die Dokumentation erfolgt auf deutsch, während die Diagramme auf Englisch gehalten werden, um die Umsetzung in (englischen) Code zu erleichern. |


# 3. Kontextabgrenzung

## 3.1 Business Kontext

![image info](./diagrams/kontext/scope_business.png) \
Details siehe [Use Cases](#use-cases).

## 3.2 Technischer Kontext
![image info](./diagrams/kontext/scope_technical.png)

# 4. Lösungsstrategie

## 4.1 Funktionale Zerlegung anhand der Use Cases
Aus den [Use Cases](#3.1-business-kontext) ergeben sich folgende benötigte Objekte, denen in folgenden Abschnitten Funktionen zugeordnet werden.

| Objekt             | Erklärung                                                                                                                         |
|--------------------|-----------------------------------------------------------------------------------------------------------------------------------|
| Configuration      | Verwaltet die anpassenbaren Werte und stellt Defaultwerte bereit.                                                                 |
| ITronView          | Stellt die Hauptkomponente der UI dar.                                                                                            |
| ITronModel         | Verwaltet die gesamte Spielelogik.                                                                                                |
| ITronController    | Verwaltet die Benutzereingaben.                                                                                                   |
| IGame              | Stellt die Spielelogik und startet die Spielschleife.                                                                             |
| IGameManager       | Managed die Spieler die, mitspielen wollen sowie in welcher Phase sich das Spiel befindet.                                        |
| IArena             | Verwaltet die Spieler innerhalb der Arena, merkt sich die Positionen der Spieler, sowie die Schatten der Spieler.                 |
| ICollisionDetector | Ist für die Überprüfung ob ein Spieler, mit einem anderen Spieler, dessen Schatten oder einer Arenawand zusammengestoßen ist.     |
| IPlayer            | Verwaltet einen einzelnen Spieler, mit den Coordinaten, der Richtung und der Farbe eines Spieler.                                 |
| IUpdateListener    | Bekommt von dem Model Aktualisierungen und aktualisiert die View dementsprechend.                                                 |
| Overlay            | Eine Oberfläche, die in der UI angezeigt werden kann. Für jede Scene(Menu, Waiting, Countdown, Ending) wird ein Overlay benötigt. |
| TronViewBuilder    | Builder-Objekt, das den Zusammenbau der View (aus ITronView, Overlays und IUpdateListener) übernimmt.                             |

### 4.1.1 Configuration
<!-- CONFIG -->
| UC | Funktion                                                                          | Objekt          | Vorbedingung                                                                  | Nachbedingung |Ablaufsemantik|Fehlersemantik|
| ---- |-----------------------------------------------------------------------------------|-----------------|-------------------------------------------------------------------------------|----------- |----------- |----------- |
| 1 | `loadConfigFromFile() : void`                                                     | Configuration   | Es existiert ein Config-Objekt. Es existiert eine TronConfig.properties File. | Properties-Objekt ist erzeugt. |Die Konfigurationsdatei ist ein '.properties' File, in Form: 'Key', 'Value' und wird in ein Properties-Objekt eingelesen. Das Config-Objetzt hält eine Referenz darauf.| Bei fehlender .properties-Datei an der erwarteten Speicheradresse, wird neue .properties-Datei erstellt (`reloadConfig()`) |
| 1 | `isConfigValid() : boolean`                                                       | Configuration   | Es existiert ein Properties-Objekt.                                           | Das Ergebnis ist wahr oder falsch. | Der Inhalt des Properties-Objekt wird auf fehlende 'Keys' geprüft. Und ob die 'Values' sich im richtigen Wertebereich befinden. Sollte einer der beiden Fälle eintreffen, würde die Methode 'false' zurückgeben. | - |
| 1 | `reloadConfig() : Properties `                                                    | Configuration   | Schreibrechte.                                                                | Properties mit 'default'-Werten wurde erzeugt. | Es wird eine neues Properties-Objekt auf Basis vom im Programmcode festgelegten 'Key-Value-Paaren' in der Config erstellt. Das Properties-Objekt wird auch an der hinterlegten Speicheradresse lokal in Form einer .properties-Datei hinterlegt.  | Wenn keine Rechte bestehen, wird der Anwender darüber informiert. |
| 1| `setKeyMappings() : void`                                                         | Configuration   | Es existiert ein Properties-Objekt mit validen Daten                          | In dem Config-Objekt existiert eine Map, welche als 'Key' alle Tasten enthält, welche zum lenken genutz werden können. Als Value erhält die Map ein Steer-Objekt welches die Player-ID, sowie die Direction hält. | Die Methode zieht sich aus dem properties-Objekt die Tastenbelegungen aller Spieler. | - |
| 1 | `getAttribute(key : String) : String`                                             | Configuration   | Es existiert ein Properties-Objekt mit validen Daten.                         | Es wurde der passende 'Value' zum 'Key' zurückgegeben.| Die Methode greift auf ein Properties-Objekt zu und zieht sich den ersten 'Value' welcher zu dem Eingabeparameter String passt. | - |
| 3.1 | `getSteer(key : KeyCode) : Steer`                                                 | Configuration   | KeyMappings wurden erfolgreich erstellt.                                      | Steer-Objekt | Als 'Value' enthält die Map ein Steer-Objekt, welches die Player-ID und die Direction enthält. Die Methode gibt das zur Taste gehörende Steer-Objekts zurück. | Gibt null zurück, wenn es für die eingegebenen Taste kein Treffer gibt. |
| 1 | `hasAllAtributes() : Steer`                                                       | Config          | Es existiert ein Properties-Objekt.                                           | Abhängig davon ob Einträge in der Properties fehlen wird ein boolean zurückgegeben.| Die Methode greift auf ein Properties-Objekt zu und vergleicht dieses mit der Default-Properties. Wenn Keys fehlen ist dies gleichzusetzen damit, das Attribute fehlen . | - |
| 1 | `isNumber(strings : String...) : boolean`                                         | Config          | Es existiert ein Properties-Objekt.                                           | Abhängig davon ob alle der Eingabestrings nummerisch sind oder nicht erhält man true oder false. | Es wird über die Eingabe-Strings iteriert und für jeden String geschaut ob er nummerischist oder nicht. Sollte auch nur einer nicht nummerischsein ist das gesamte Ergebniss false | - |
| 1 | `isSteerValid(strings : String...) : boolean`                                     | Config          | Es existiert ein Properties-Objekt.                                           |Abhängig davon ob die Eingabe-Strings zu einem Steer umgewandelt werden können erhält man true oder false | Anhand der Länge der Eingabe und des Inhalts wird überpfüft ob aus der Eingabe ein valides Steer-Objekt erstellt werden könnte. | - |
| 1 | `isValidKey(tempStringArray : String[] , index : int) : boolean`                  | Config          | Es existiert ein Properties-Objekt.                                           |Abhängig davon ob die Player-Tasten valide sind erhält man true oder false |Es wird überpfüft ob es sich bei den Tasten für die Steuerung um valide Eingaben handelt. Als valide gelten alle Zahlen, Buchstaben mit Außnahme (ä,ö,ü) und alle Pfeiltasten. | - |
| 3.2 | `getKeyMappingForPlayer(id : int) : String`                                       | Config          | Es existiert ein Properties-Objekt.                                           |String mit Tasten für die Steuerung des Spielers in Tupel-Form Bsp.: "A,Z" |Die Eingabe-Id wird auf den Spieler in der Properties gemappt, sodass z.B. die ID = 1, den Value zum Eintrag P_EINS (Key) aus der Properties zieht. | - |

### 4.1.2. ITronView
Es wird die zur Verfügung gestellte view library verwendet. Die ITronView wird lediglich um Setter für ROWS und COLUMNS erweitert.

### 4.1.3 ITronModel
<!-- ITRONMODEL -->
| UC     | Funktion                                                                                                            | Objekt     | Vorbedingung                                                                  | Nachbedingung                         | Ablaufsemantik                                                                        | Fehlersemantik                                                                                        |
|--------|---------------------------------------------------------------------------------------------------------------------|------------|-------------------------------------------------------------------------------|---------------------------------------|---------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|
| 2   | `initialize(config : Configuration, modus : Modus, singleView : boolean, executorService : ExecutorService) : void` | ITronModel | Der GameManager-Objekt existiert wurde instaniziiert.                         | GameManager wurde initialisiert.                                  | Das Model wird mit den erforderlichen Abhängigkeiten gespeist.                        | -                                                                                                     |
| 2   | `playGame(listener : IUpdateListener, playerNumber : int) : void`                                                   | ITronModel | Der GameManager wurde instanziiert.                                           | GameManager beim Spiel registriert.                                  | Der Initiator des Spiels startet beim Model ein Spiel mit der gewünschten Spielerzahl | Wenn der ModelState nicht dem Status MENU oder WAITING entspricht, wird der Aufruf ignoriert.         |
| 3.1 | `handleSteerEvent(registrationId : int, key : String ) : void`                                                      | ITronModel | Der GameManager wurde instanziiert. Die registrationId ist dem Model bekannt. | Das Model hat auf das Event reagiert. | Ein Tastenanschlag wird an das Model weitergegeben.                                   | Falls Key nicht bekannt ist oder die registrationId unbekannt ist, wird der Tastenanschlag verworfen. |

### 4.1.4 ITronController
| UC  | Funktion                                                         | Objekt          | Vorbedingung              | Nachbedingung                                         | Ablaufsemantik                                                                                                            | Fehlersemantik  |
|-----|------------------------------------------------------------------|-----------------|---------------------------|-------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------|-----------------|
| 2,3 | `initialize(model : TronModel) : void`                           | ITronController | Model wurde instanziiert. | Der Controller ist über sein Modell informiert.       | Legt das Modell des Controllers fest, an das er die Eingaben leitet.                                                      | -               |
| 2   | `playGame(listener : IUpdateListener, playerCount : int) : void` | ITronController | -                         | Das Model reagiert auf den Aufruf.                    | Beauftragt das Model, ein Game zu starten.                                                                                | -               |
| 3.1 | `handleKeyEvent(registrationId : int, event : KeyEvent) : void`  | ITronController | -                         | Das Model wurde über einen Tastenanschlag informiert  | Übersetzt ein KeyEvent zu dem dazugehörigen String und leitet diesen ans Model weiter.                                    | -               |


### 4.1.5 IGame & Game
<!-- GAME -->
| UC | Funktion  | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
| -- | -- |-------|----------- |----------- |----------- |----------- |
| 2 | `initialize(modus : GameModus, speed : int, rows : int, columns : int, waitingTimer : int, endingTimer : int, executorService : ExecutorService` | IGame | - | Game-Objekt ist initialisert. | Initialisiert ein game nach Erstellung des Game-Objekts. Ähnliche Funktionalität wie Initialisierung mittels Konstruktors. - Durch Anwendung des Factory-Patterns aber in Methode verlagert.| - |
| 2 | `prepareForRegistration(playerCount : int) : void`                              | IGame | Ein Game Objekt wurde erzeugt und im State INIT. PlayerCount ist zwischen 2 und 6. | Das Game Objekt ist bereit für den Spielstart. | Das Game wird für den Start vorbereitet: Es erstellt eine Arena und statet einen Timer, nach dem die Vorbereitung beendet wird (waitingTimer der Config-File). | - |
| 2 | `register(gameManager : IGameManager, listener : IUpdateListener, listenerId : int, managedPlayerCount : int) : void` | IGame | Ein Game Objekt wurde erzeugt und befindet sich im GameState REGISTRATION. | Das Game speichert sich seine Observer und managedPlayerCount Player erstellt. | Das Game merkt sich seine Observer, die es über das Spielgeschehen informieren soll und erstellt so viele Spieler, wie übergeben wird. Es gibt die IDs der erstellten Spieler zurück. | register wird aufgerufen während der GameState in einem anderen Zusatnd als REGISTRATION ist. - Die Registrierung wird nicht vollzogen. Es gibt keinen Hinweis oder Fehler.  |
| 3 | `handleSteer(steer : Steer) : void` | IGame | Steer ist nicht NULL. | Player hat eine neue Direction. | Dem Player mit der id aus dem Steer Object wird als neue Direction, die Direction steer eingetragen. | - |
| 2,3,4 | `transitionState(newState : GameState) : void` | Game | GameState ist nicht NULL. | - | Der newState wird im Game gesetzt und der stateListener wird informiert (`executeState()`) | Wenn der newState dem currentState entspricht machen wir nichts. |
| 2,3,4 | `executeState() : void` | Game | - | - | Führt basierend auf dem currentState, die nächste Methode aus. | - |
| 2,4 | `startTimer(waitingTimer : int, startedAt, GameState) : void` | Game | - | - | Started ein Timer mit der Zeit des waiting Timers und ruft wenn der Timer um ist `handleState(startedAt)` auf. | Wenn Methodenaufruf in anderem GameState als REGISTRATION oder FINISHING aufgerufen wird, schläft der Thread zwar, der interne Methodenaufruf `handleTimeOut` bewirkt aber nichts.|
| 2,4 | `handleTimeOut(startedAt : GameState) : void` | Game | GameState == REGISTRATION oder GameState == FINISHING | Game State == STARTING oder Game State == INIT | Ist der WaitingTimer abgelaufen und das Game befindet sich noch im REGISTRATION State, wird die Vorbereitung beendet. > 2 Spieler: Spiel wird gestartet, < 2 Spieler: Spiel kehrt ins Menü zurück, mit Methodenaufruf `transitionState()`. Befindet sich das Game im State FINISHING und der WaiingTimer ist abgelaufen, kehrt das Spiel ins Menü zurück | - |
| 2 | `isGameReady() : boolean` | Game | - | - | Es wird überprüft ob genügend Player im Spiel sind. Falls nicht gibt die Methode false zurück. | - |
| 2 | `isGameFull() : boolean` | Game | - | - | Es wird überprüft ob die gewünschte Spielerzahl bereits erreicht wurde. Falls nicht gibt die Methode false zurück. | - |
| 2 | `isRegistrationAllowed(playerCountToRegister : int) : boolean` | Game | - | - | Wenn der GameState sich auf REGISTRATION befindet und noch Plätze frei sind gibt die Methode true zurück. - Andernfalls false. | - |
| 2 | `createPlayers(count : int) : List<Integer>` | Game | Count darf und kann nicht größer sein als 6. | neue Player-Objekte wurden erstellt. | Erstellt die übergebene Anzahl an Spieler und speichert diese im Game. Gib eine Liste der Ids zurück. | - |
| 2 | `startGame() : void` | Game | Es müssen mindestens zwei Player erstellt sein. | Positionen der Player sind ermittelt und an diese verteilt. | Die Vorbereitung zur Verteilung der Player auf der Arena wird vorgenommen, die Informationen werden an die UpdateListener weitergereicht und das Game wechselt in den Status RUNNING. | - |
| 3 | `countDown() : void` | Game  | Es muss ein Game-Objekt erstellt worden sein und das 'Game' wurde erfolgreich initialisiert. Der GameManager befindet sich im State 'COUNTDOWN' | Der GameManager ändert seinen State zu "PLAYING"| Ein CountDown welcher für drei Sekunden runter zählt. In jeder Sekunde wird der Counter des GameManagers um einen heruntergezählt | Der GameManager befindet sich im falschen State. - Er ignoriert den CountDown. | 
| 3 | `runGame() : void` | Game | - | - | Der `countDown()` und die `gameLoop()` werden gestartet. | Wenn der Thread der Gameloop interrupted wird, wechselt das Game in den Status FINISHING. |
| 3 | `gameLoop() : void` | Game  | Es muss ein Game-Objekt erstellt und das 'Game' erfolgreich vorbereitet worden sein && Der Countdown ist abgelaufen. | Es ist ein oder kein Spieler am Leben. | Die gameLoop() ist eine Schleife in der die primäre Spiellogik implementiert ist. Sie berechnet in jedem Takt die neue Koordinate der Spieler anhand deren Direction (`calculateNextCoordinate()`). Ebenfalls überprüft sie, ob Spieler kollidieren oder ob ein Spiel zuende ist (isGameOver()). | - |
| 3 | `movePlayers() : void` | Game | Player ist "alive". | Player hält ein neues Coordinate Tupel in seiner Liste an Coordinates.  | Wenn der Player noch am leben ist, wird für ihn seine nächste Position abhängig von seiner Direction berechnet. | - |
| 3 | `updateField() : void` | Game | - | Das Spielfeld wird aktuallisiert. | Alle Kollisionen auf der Arena, entweder Player mit Player oder Player mit Arena werden ermittelt und die verunfallten Player werden durch `detectCollision()` ermittelt und entfernt. Im Anschschluss werden die registrierten UpdateListener informiert. |  |
| 3 | `updatePlayerMap() : Map<Integer, List<Coordinate>>` | Game | - | Verunfallte Spieler sind aus der Player-Map entfernt. | Es wird für jeden Player überprüft ob dieser noch den Status "alive" hat. - Wnn nein, wird er aus der Player-Map gelöscht. | - |
| 3 | `calculateNextCoordinate(coordinate : Coordinate, direction : Direction) : Coordinate` | Game |Direction darf nicht NULL sein. | Es wurde eine neue Coordinate berechnet | In Abhängigkeit von der Direction wird eine neue Coordinate berechnet. | |
| 3 | `isGameOver() : boolean` | Game  | Das Game wurde gestartet. | Ergebnis ist wahr oder falsch | Wenn der Counter der aktiven Player < 2 dann gibt die Methode den Wert 'true' zurück andernfalls 'false' | - |
| 3 | `finishGame() : void` | Game | Die Gameloop ist beendet. | Der GameManager wurde über das Spiel Resultat informiert. | Informiert den GameManager über das Spiel Resultat (`setGameResult`) und ruft `resetGame()`auf, um das Spiel in den Initial-Zustand zurückzuversetzen. | - |
| 3 | `resetGame() : void ` | Game | Die Gameloop ist beendet oder PREPARING war nicht erfolgreich. | Der Zustand des Game-Objekts ist wieder im 'default'-Zustand. | Setzt alle Werte des Games zurück und leert die Arena, wenn ein Spiel vorbei ist oder die Vorbereitung abgebrochen wurde. | - |

### 4.1.6 IGameManager & GameManager
<!-- GAMEMANAGER -->
| UC    | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
|-------|----------------------------------------------------------------------------------| ----------- |----------- |----------- |----------- |----------- |
| 2,3,4 | `handleManagedPlayers(id: int, managedPlayers : List<Integer>) : void` | IGameManager | Es existier ein GameManager-Objekt | Der GameManager hält einen bzw. mehrere neue Player die er verwaltet.  | Der GameManager wird über die Player informiert, welchen er verwalten soll. Intern erfolgt ein Mapping der Steuerung auf die playerId. | - | 
| 2,3,4 | `handleGameState(gameState : GameState) : void`                                    | IGameManager | Der GameManager wurde über handleGameState(gameState:GameState) über eine Veränderung informiert. | Der ModelState des GameManagers hat sich gemäß des GameStates verändert. | Das Model wechselt vom aktuellen ModelState in den nächsten ModelState abhängig von der Nachricht. | Ist eine Nachricht nicht gültig im aktuellen ModelState, wird sie ignoriert. |
| 2,3,4 | `executeState() : void`                                                            | GameManager | Es gab einen Zustandsübergang. | Die 'do's des States wurden durchgeführt. | In Abhängigkeit vom ModelState zeigt der GameManager Overlays an, initialisiert ein Game etc. (Verweis auf das State-Diagramm) | - |
| 2,3,4 | `transition(newState : ModelState) : void`                                                            | GameManager | - | Der Ablauf zum wechsel des Zustands wird initialisert und der neue Zustand wird intern abgespeichert. | Intern wird der neue übergebene ModelState gesettzt und `executeState()` wird aufgerufen. | - |
| 2,3,4 | `updateListeners() : void`                                                            | GameManager | Es gab einen Zustandswechsel des ModelState. | Die UpdateListener wurden informiert. | Bei allen Zustandswechsel des ModelState´s außer bei PLAYING werden die listener über den neuen ModelState informiert.| - |
| 4     | `reset() : void`                                                            | GameManager | Es existiert ein GameManager-Objekt. | GameManager hält keine Spieler Informationen mehr. | Der GameManager wird in einen Status gebracht, in dem er alle informationnen über seine  gehaltenen managment Daten verliert.| - |

### 4.1.7 IArena & Arena
<!-- ARENA -->
| UC  | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
|-----|----------------------------------------------------------------------------------| ----------- |----------- |----------- |----------- |----------- |
| 3   | `addPlayerPosition(playerId : int, coordinate : Coordinate) : void` | IArena | Der Spieler mit der playerId muss noch am leben sein. coordinate darf nicht NULL sein und muss sich innerhalb der Arena befinden. | Die Arena wurde aktualisiert | Die aktuell Head-Koordinate des übergebenen Players wird in die Arena eingetragen. | - |
| 3   | `deletePlayerPositions(playerIds : List<Integer>) : void` | IArena | Die Liste playerIds darf nicht leer sein. | Die Arena wurde akualisiert und die übergebenen Spieler rausgelöscht. | Alle Koordinaten der übergebenen ID´s werden aus der Arena entfernt. | Wenn die Liste der ID´s leer ist, wird die Methode abgebrochen.|
| 3   | `detectCollision(coordinate : Coordinate) : boolean` | IArena | Die coordinate darf nicht NULL sein. |  | Es wird geschaut ob sich die Koordinate außerhalb der Arena befindet und ob sich an der Koordinate bereits der Schatten oder auch anderer Spieler befindet. Falls eine Kollision entdeckt wird, geben wir true zurück, ansonsten false. |  |
| 2   | `calculateFairStartingCoordinate(playerCount : int) : List<Coordinate>` | IArena | Der playerCount muss zwischen 2 und 6 liegen. | Es wurden playerCount viele Startpositionen berechnet. | Es werden je nach Spieleranzahl und Arenagröße die fairsten Startpositionen berechnet. |  |
| 2   | `calculateStartingDirection(coordinate : Coordinate) : Direction` | IArena | Die coordinate darf nicht NULL sein und muss sich innerhalb der Arena befinden. | Für die coordinate wurde eine Startrichtung berechnet. | Abhänging von der übergeben Koodinate wird die Startrichtung berechnet. |  |


### 4.1.8 ICollisionDetector & CollisionDetector
<!-- COLLISION -->
| UC | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
| ---- |----------------------------------------------------------------------------------| ----------- |----------- |----------- |----------- |----------- |
| 3 | `detectCollision(players: List<Player>, arena : Arena) : void` | ICollisionDetector | Anzahl aktiver Spieler > 1 | - | Es wird überprüft ob ein Player mit einem anderen Player, dem Schatten eines anderen Player oder der Arenawand kollidiert. | - |
| 3 | `detectHeadCollision(players: List<Player>) : boolean` | CollisionDetector | Anzahl aktiver Spieler > 1 | - | Es wird überprüft ob ein Player mit dem head eines anderen Players kollidiert. | - |


### 4.1.9 IPlayer 
<!-- PLAYER -->
| UC | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik| Fehlersemantik |
| -- |---------------------------------------------------------------------------------| ----------- |----------- |----------- |----------- |----------------|
| 3 | `addCoordinate(coordinate : Coordinate) : void` | IPlayer | Die Coordinate ist nicht NULL | Die Koordinatenliste ist um +1 gestiegen. | Dem Spieler wird eine neue Koordinate in seine List<Coordinate> hinzugefügt. | - |
| 3 | `isAlive() : boolean` | IPlayer | - | - | Der Spieler kann entweder noch aktiv am Spiel beteiligt sein oder nicht. Dies wird mit der Funktion abgefragt. Ein wechsel dieses Status erfolgt durch eine Kollision mit Playern (inkl. sich selbst) oder der Arena-Wand.| - |
| 3 | `crash() : void` | IPlayer | Der Spieler ist am Leben und in der aktuellen Spielrunde gecrashed | Der Spieler kann nicht mehr mitspielen | Setzt den alive-Status eines Spielers auf "false" nach einem Crash. | - |
| 3 | `performDirectionChange() : Direction` | IPlayer | Neuer Takt hat begonnen. | Die Direction des Players wurde der Action entsprechend verändert. | Pro Takt wird die Richtung jedes Spielers entsprechend seiner Action verändert. Die Action wird danach auf NONE gesetzt. | - |


### 4.1.10 IUpdateListener & UpdateListener 
| UC | Funktion | Objekt | Vorbedingung | Nachbedingung | Ablaufsemantik | Fehlersemantik |
| -- | -------- | ------ | ------------ | ------------- | -------------- | -------------- |
| 2,3 | `updateOnRegistration(id : int) : void` | IUpdateListener | Der UpdateListener wurde initialisiert. | UpdateListener kennt danach seine Id. | Der UpdateListener wird vom Model über die vergebene Id informiert. |  |
| 3.2 | `updateOnKeyMappings(mappings : Map<String, String>) : void` | IUpdateListener | Der UpdateListener wurde initialisert. |  | Der UpdateListener wird über die Keys die im CountdownOverlay angezeigt werden sollen informiert und setzt diese im CountdownOverlay. |  |
| 2 | `updateOnArena(rows : int, columns : int) : void` | IUpdateListener | Der UpdateListener wurde initialisiert. |  | Informiert den UpdateListener über die Größe der Arena, die entsprechende View wird angepasst. |  |
| 5 | `updateOnState(state : String): void` | IUpdateListener | Der UpdateListener wurde initialisiert. |  | Der UpdateListener wird über den State des Model informiert und setzt die View auf das zum State passende Overlay. |  |
| 2 | `updateOnGameStart() : void` | IUpdateListener | Der UpdateListener wurde initialisiert. |  | Infomiert den UpdateListener über den start des Spieles, alle Overlays werden versteckt, sodass kein Overlay mehr angezeigt wird. |  |
| 4 | `updateOnGameResult(color : String, result : String)` | IUpdateListener | Der UpdateListener wurde informiert. |  | Informiert den UpdateListener über das Ende des Spiels, der Sieger wird angezeigt, das CoundownOverlay wird zurückgesetzt und alle Koordinaten werden auf der View gelöscht. |  |
| 3 | `updateOnCountDown(value : int) : void` | IUpdateListener | Der UpdateListener wurde initialisert. |  | Der UpdateListener wird über den nächsten Schritt im Countdown informiert und setzt das CoundownOverlay auf den übergebenen Wert. |  |
| 3 | `updateOnField(field : Map<Color, List<Coordinate>>) : void` | IUpdateListener | Der UpdateListener wurde initialisiert. |  | Der UpdateListener wird über den aktuellen Zustand des Spielfeldes informiert und der aktuelle Zustand wird in die View übertragen. |  |
| 2-5 | `initialize(mainView : ITronView, countdownOverlay : CountdownOverlay, endingOverlay : EndingOverlay, mainController : ITronController) : void` | UpdateListener | Der UpdateListener wurde initialisiert. |  | Der UpdateListener wird initialisiert. |  |

### 4.1.11 TronViewBuilder
<!-- VIEWBUILDER -->
| UC  | Funktion                                                                                                                                                                | Objekt          | Vorbedingung                                     | Nachbedingung                                                           | Ablaufsemantik                                                                        | Fehlersemantik |
|-----|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------|--------------------------------------------------|-------------------------------------------------------------------------|---------------------------------------------------------------------------------------|----------------|
| 2-5 | `buildView(controller : ITronController, height : int, width : int, defaultPlayerCount : int, idOverlayMapping : Map<String, String>, stage : Stage) : IUpdateListener` | TronViewBuilder | ITronController wurde instanziiert. | Instanzen von ITronView, IUpdateListener sowie Overlays wurden erzeugt. | Beauftragt den Builder, die View aus ihren verschiedenen Komponenten zusammenzubauen. | -              |


# 5. Bausteinsicht
## 5.1 Ebene 1
![image info](./diagrams/baustein/bs_layer1.png)
## 5.2 Ebene 2 : Application
![image info](./diagrams/baustein/bs_layer2_view_controller.png)
## 5.3 Ebene 3 : Application
![image info](./diagrams/baustein/bs_layer3_model.png)
![image info](./diagrams/baustein/bs_layer3_view.png)
### 5.4 Ebene 3 : ApplicationStub:
### 5.4.1 Ebene 3 : ApplicationStub: Caller und Callee
![image info](./diagrams/baustein/bs_layer3_stub_model.png)
Für *name* ∈ {IGameManager, IGame, IUpdateListener} gibt es im ApplicationStub eine Komponente der oben beschriebenen Form.
Die Stubs verwenden darüber hinaus folgende Klassen:
- `RemoteId`: Stellt eine einzigartige ID für diesen Application Stub dar.
- `Service`: Enhtält die Services, die Remote Objekte dieses Application Stubs bereitstellen können.

Caller-Objekte können darüber hinaus das ICaller-Interface implementieren, das lediglich dazu dient, die RemoteId zu setzen.

### 5.4.2 Ebene 3 : ApplicationStub: REST
![image info](./diagrams/baustein/bs_layer3_stub_rest.png)

Für REST siehe Abschnitt 8.2.4.

# 6. Laufzeitsicht
## 6.1 Use Cases auf MVC-Ebene
![image info](./diagrams/laufzeit/UC2_startGame.png)
![image info](./diagrams/laufzeit/UC2.1_seeKeyMappings.png)
![image info](./diagrams/laufzeit/UC3_playGame.png)
![image info](./diagrams/laufzeit/UC3.1_steerBike.png)
![image info](./diagrams/laufzeit/UC4_seeResults.png)
![image info](./diagrams/laufzeit/UC5_changeScenes.png)

## 6.2 Model
### 6.2.1 States im Model
Die in den States beschriebenen Aktivitäten sind in Abschnitt [Activities im Model](#6.2.3-Activities-im-Model) näher beschrieben.
![image info](./diagrams/laufzeit/States.png)

### 6.2.2 Details der Use Cases im Model
![image info](./diagrams/laufzeit/UC1_configure.png)
![image info](./diagrams/laufzeit/model_UC3.1_steerBike.png)
![image info](./diagrams/laufzeit/model_UC3.2_seeKeyMappings.png)

### 6.2.3 Activities im Model
### 6.2.3.1 Start 
![image info](./diagrams/laufzeit/activity_start1.png)
![image info](./diagrams/laufzeit/activity_start2.png)
### 6.2.3.2 Play 
![image info](./diagrams/laufzeit/activity_runGame_loop.png)
![image info](./diagrams/laufzeit/activity_runGame_collision.png)
### 6.2.3.3 End 
![image info](./diagrams/laufzeit/activity_end.png)

## 6.3 ApplicationStub : REST
![image info](./diagrams/laufzeit/rest_states.png)
![image info](./diagrams/laufzeit/rest_activity_1.png)
![image info](./diagrams/laufzeit/rest_activity_2.png)
![image info](./diagrams/laufzeit/rest_activity_3.png)

# 7. Verteilungssicht
Es sind beliebige Verteilungen möglich.

# 8. Querschnittliche Konzepte
## 8.1 Application
### 8.1.1 Modus
Die Application kann in drei verschiedenen Modi gestartet werden: `LOCAL`, `RPC`, `REST`. Dies geschieht über die tronConfig.properties Datei.

Der Modus wird aus der Datei gelesen und ein entsprechendes Enum zum instanziieren von Objekten in Factories verwendet. 
Im RPC-Modus kommen die im ApplicationStub unter Abschnitt 5.4.1 beschriebenen Caller- und Callee-Stubs zum Einsatz.
Im REST-Modus wird der im ApplicationStub unter Abschnitt 5.4.2 beschriebene RESTStub sowie die in RPC verwendeten Stubs verwendet.

### 8.1.2 Registration-ID
Um zu ermöglichen, dass mehrere Views mit dem Model kommunizieren können, registriert sich die View als Listener beim Model
und erhält anschließend eine Registration-ID vom Model. Kommunikation, die über den Controller ans Model geht, muss
anschließend diese ID enthalten. Dadurch kann das Model speichern, welche Spieler zu welcher Registration-ID gehören, sodass
nur berechtigte Tastenanschläge vom Model verarbeitet werden.

## 8.2 ApplicationStub
### 8.2.1 RemoteId
Der ApplicationStub kann verschiedene Services anbieten. In manchen Fällen kann es notwendig sein, den Service eines konkreten ApplicationStubs anzufragen. Dazu erhält
jeder ApplicationStub eine `RemoteId`, über die er eindeutig identifiziert werden kann.

### 8.2.2 Namensraum
Zur Identifizierung der vom ApplicationStub angebotenen Services wird ein hierarchischer Namensraum verwendet.
Jeder Service kann eindeutig über die Kombination aus ServiceId und RemoteId des anbietenden ApplicationStubs bestimmt werden.

### 8.2.3 Service Call Protokoll
Die Anfrage, einen angebotenen Service auszuführen, besteht aus folgenden Bestandteilen:
- `serviceId`: die Id des Services
- `intParameters` : Parameter des Services vom Typ int
- `stringParameters`: Parameter des Servcies vom Typ String

Services können nach folgenden Regeln angefragt werden:

| Service                  | ServiceId | intParameters                                               | stringParameters                                                                      |
|--------------------------|-----------|-------------------------------------------------------------|---------------------------------------------------------------------------------------|
| `PREPARE               ` | 0         | playercount für das Spiel                                   | keine                                                                                 | 
| `REGISTER              ` | 1         | registrationId, managedPlayerCount                          | RemoteId des IGameManager Remote Objekts, RemoteId des IUpdateListener Remote Objekts | 
| `HANDLE_STEER          ` | 2         | playerId, Ordinal des DirectionChange                       | keine                                                                                 | 
| `HANDLE_MANAGED_PLAYERS` | 3         | regristationId, Liste mit managedPlayer ids                 | keine                                                                                 | 
| `HANDLE_GAME_STATE     ` | 4         | Ordinal des GameState                                       | keine                                                                                 | 
| `UPDATE_ARENA          ` | 5         | Rows, Columns                                               | keine                                                                                 | 
| `UPDATE_STATE          ` | 6         | Ordinal des GameState                                       | keine                                                                                 | 
| `UPDATE_START          ` | 7         | keine                                                       | keine                                                                                 | 
| `UPDATE_RESULT         ` | 8         | SpielerId des Gewinners, Ordinal des ResultTextes           | keine                                                                                 | 
| `UPDATE_COUNTDOWN      ` | 9         | CountDown value                                             | keine                                                                                 | 
| `UPDATE_FIELD          ` | 10        | PlayerCount, PlayerId, X, Y, ..., PlayerId, X, Y, ...       | keine                                                                                 | 


### 8.2.4 Spiel mit den Implementierungen anderer Teams
Für das Zusammenspiel mit anderen Teams wird eine Synchronisation des Spiels mittels REST gewählt.
Dazu wurde unter allen teilnehmenden Teams ein REST-Protokoll erarbeitet, das von jeder Implementierung umgesetzt werden muss.
Zentral in dem Protokoll ist, dass zunächst ein Coordinator bestimmt werden muss. Dies geschieht in Absprache mit den anderen
Teams über einen Name Server, bei dem sich ein Coordinator als `tron.coordinator` anmeldet.
Implementiert wird das Protokoll durch die rest-Komponente im Applicationstub, dargestellt in Abschnitt 5.4.2.

Das REST-Protokoll ist zu finden unter: https://gitlab.com/jessyvere/rest-protocol

Der verwendete Name Server: https://name-service.onrender.com/swagger/index.html

# 9. Architekturentscheidungen

| Entscheidung         | Qualitätsmerkmale                                                                           | Beschreibung                                                                                                                                                                                               |
|----------------------|---------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Komponenten nach MVC | Erweiterbarkeit, Wartbarkeit, Übertragbarkeit                                               | Die Einführung vom MVC-Pattern soll die Bearbeitung an der Applikation vereinfachen und der Applikation eine verständliche Struktur geben.                                                                 |
| Factory Method       | Lose Kopplung, Erweiterbarkeit, Wartbarkeit                                                 | An vielen Stellen werden Factories verwendet, um die Objekterzeugung vom Rest der Implementierung löszulösen und eine Implementierung gegen Schnittstellen anstatt gegen konkrete Objekte zu unterstützen  | 
| Singleton Pattern    | Lose Kopplung                                                                               | Einige Objekte werden als Singleton realisiert, da sie an unterschiedlichen Stellen gebraucht werden und dies das erfüllen von Dependencies erleichtert.                                                   | 

# 10. Qualitätsanforderungen
siehe Abschnitt 1.2.

# 11. Risiken und technische Schulden

| Entscheidung           | Beschreibung                                                                                                                                                                  |
|------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Dependencies           | Dependencies werden derzeit manuell über initialize-Methoden, Konstruktoren oder Singletons realisiert.                                                                       |
| Singleton Pattern      | Singletons werden an einigen Stellen verwendet, um das Realisieren von Dependencies zu erleichtern.                                                                           | 
| Manuelles Testen       | Das Definieren einer Teststrategie mit automatisierten Tests wurde versäumt und erschwert zukünftiges Refaktoring, Erweiterungen sowie die Sicherstellung der Funktionalität. | 


# 12. Glossar
# 13. Anhang
## ## Storyboard
![image info](./diagrams/storyboard.jpg )

## Use Cases

**UC-1: Configure Game**

Akteur: Spieler \
Ziel: Spiel nach seinen Wünschen konfigurieren \
Auslöser: Öffnen der Config-File \
Nachbedingungen: Neue Konfigurationsdaten sind gespeichert und werden bei Applikationsstart verwendet.

Standardfall:

    1. Der Benutzer bearbeitet die Daten der Config-Datei:
        waitingTimer (Sekunden)
        endingTimer (Sekunden)
        defaultPlayerNumber (Ganzzahl zwischen 2-6)
        speed (Ganzzahl zwischen 1-500)
        height (Ganzzahl)
        width (Ganzzahl)
        rows (Ganzzahl)
        columns (Ganzzahl)
        controlls p1-6 (bsp: W,A)
        gameMode (LOCAL, NETWORK)
        nameServer (IP:Port)
        nameServerHost (TRUE, FALSE)
    2. Der Benutzer speichert die Datei.
    3. Der Benutzer startet das System.
    4. Das System lädt die Daten aus der Konfigurationsdatei.
    5. Das System überprüft die Daten der Konfigurationsdatei auf Fehler.
    6. Das System zeigt den Menu Screen an.

Fehlerfälle:

    6.a. Das System findet einen Fehler in der Konfigurationsdatei oder findet die Konfigurationsdatei nicht.
        6.a.1 Das System erstellt eine neue .properties-Datei mit default Werten.

<br/>

**UC-2: Start Game**

Akteur: Spieler \
Ziel: Tron-Spiel spielen. \
Vorbedingungen: Das System befindet sich im Starting-Screen. \
Nachbedingungen: Es wird ein Tron-Game begonnen und angezeigt.

Standardfall:

    1. Das System zeigt den Starting Screen mit der defaultPlayerNumber und einen "Spiel starten" Button an.
    2. Der Spieler betätigt den Button.
    3. Das System wechselt in den Waiting-Screen.
    4. Das System initialisiert ein Game mit der defaultPlayerNumber.
    5. Das System startet den Waiting-Timer.
    6. Das System registriert die Spieler beim Game.
    7. Das System berechnet die Startpositionen und die Startrichtung der Spieler.
    8. Das System wechselt in den Arena-Screen.
    9. Es beginnt US-3: Play Game

Erweiterungsfälle:

    7.a Nach Ablauf des Waiting-Timers konnte die Spieleranzahl nicht erreicht werden mit >= 2 Spieler.
        7.a.1 weiter im Standardfall Punkt 7.
    2.a UC-2.1: Choose Player Number: Der Spieler wählt eine andere Spielerzahl.
        2.a.1 Der Spieler wählt eine Spielerzahl zwischen 2-6 aus einem Drop Down Menü.
        2.a.2 Das System verwendet den eingegebenen Wert anstelle des Defaultwertes.
        2.a.3 Weiter im Standardfall Punkt 2.

Fehlerfälle:

    7.b Nach Ablauf des Waiting-Timers konnte die Spieleranzahl nicht erreicht werden mit <2 Spieler.
        7.b.1 Das System setzt die Spieldaten zurück.
        7.b.2 Das System kehrt zum Starting-Screen zurück.

<br/>

**UC-3: Play Game**

Akteur: Spieler \
Ziel: Gewinnen \
Vorbedingungen: UC-2: Spiel Starten erfolgreich abgeschlossen. \
Nachbedingungen: Das Spiel ist mit "Gewinner" oder "unentschieden" geendet.

Standardfall:

    1. Das System zeigt das Spielfeld in der konfigurierten Größe an.
    2. Das System zeigt einen Countdown(3-2-1) an, der 3 Sekunden lang ist.
    3. Das System zeigt während des Countdowns die Farben der Spieler und die Steuerung an.
    4. Das System bewegt die Bikes stetig in die aktuelle Richtung in der konfigurierten Geschwindigkeit vorwärts.
    5. Das System vergrößert den Schatten des Bikes mit jeder Vorwärtsbewegung.
    6. Der / Die Mitspieler stirbt / sterben bei Kollision.
    7. Das System zeigt die Schatten der gestorbenen Spieler nicht mehr an.
    8. Das System beendet das Spiel, wenn nur noch einer oder kein Spieler mehr am Leben ist.
    9. Ausführung UC-4: See Results

Erweiterungsfälle:

    4.a UC-3.1: Steer Bike: Der Spieler steuert sein Bike durch Tasteneingaben
        4.a.1 Das System verarbeitet die Tasteneingabe abhängig von der Konfiguration des Spielers.
        4.a.2 Das System ändert die Richtung des Bikes des Spielers abhängig von der Taste.

<br>

**UC-4: See Results**

Akteur: Spieler \
Ziel: Ergebnisse des letzten Spiels ansehen \
Vorbedingungen: UC-3: Spiel Spielen erfolgreich abgeschlossen. \
Nachbedingungen: Die Applikation ist in den Starting-Screen zurückgekehrt.\

Standardfall:

    1. Das System startet den Ending-Timer.
    2. Das System zeigt den Ending-Screen an, in dem das Ergebnis des Spiels ("Gewinner ist ..." oder "Unentschieden!")
       und die Farbe des Gewinners, falls es einen gibt, angezeigt wird.
    3. Das System wechselt zurück zum Starting Screen, wenn der konfigurierte End-Timer angelaufen ist.
