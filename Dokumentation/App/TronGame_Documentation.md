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
- Das Spiel kann entweder lokal (LOCAL) oder im Netzwerk (NETWORK) gespielt werden. Dies soll ebenfalls über die Konfigurationsdatei einstellbar sein.

Details siehe [Anforderungsdetails](#anforderungsdetails).


## 1.2 Qualitätsziele

| Ziel        | Beschreibung |
| ----------- | ----------- |
| Wohldefinierte Schnittstellen | Die Entwickler sollen sich gut um ihre Schnittstellen kümmern :) |
| Fehlertoleranz      | Ein Spiel soll ungestört durchspielbar sein. Auch wenn ein Teilnehmer abstürzt, läuft das Spiel weiter.       |
| Kompatibilität   | Mindestens 2 Teams müssen miteinander spielen können.        |
| Fairness | Das Spiel soll fair sein. Alle Spieler starten mit fairen Konditionen und folgen den gleichen Regeln.
| Bedienbarkeit | Spieler sollen das Spiel einfach bedienen können und Spaß haben. |

## 1.3 Stakeholders

| Rolle      | Kontakt | Erwartungen
| ----------- | ----------- | ----------- |
| Dozent / Kunde | Martin Becke: martin.becke@haw-hamburg.de |  Saubere Architektur mit Pattern und wohldefinierten Schnittstellen, Lernfortschritt der Entwickler |
| Entwickler | Sandra: sandra.koenig@haw-hamburg.de <br/> Inken: inken.dulige@haw-hamburg.de<br/> Majid: majid.moussaadoyi@haw-hamburg.de| Spaß an der Entwicklung, Architekturentwurf lernen, gutes Time handling, JavaFX Kenntnisse verbessern|
| Spieler   | Teilnehmer des Moduls VS WiSe22/23 | Stabile Anwendung, Spaß am Spielen |


# 2. Randbedingungen

| Technische Randbedingung        | Beschreibung |
| ----------- | ----------- |
| Java in der Version 17 | Zur Implementierung wird Java verwendet, da das ganze Team die Sprache beherrscht. <br/> Die Version muss zum Image der Rechner im Raum 7.65 passen. <br/> Es wird Java in der Version 17 verwendet, da es sich um die neueste LTS-Version handelt. |
| View Library | Es wird die zur Verfügung gestellte JavaFX View Library verwendet, um Zeit in der UI-Erstellung zu sparen. |

| Konventionen | Beschreibung |
| ----------- | ----------- |
| Dokumentation | Gliederung nach dem deutschen arc42-Template, um Struktur zu wahren. |
| Sprache | Die Dokumentation erfolgt auf deutsch, während die Diagramme auf Englisch gehalten werden, um die Umsetzung in (englischen) Code zu erleichern. |


# 3. Kontextabgrenzung

## 3.1 Business Kontext

![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/kontext/scope_business.png) \
Details siehe [Use Cases](#use-cases).

## 3.2 Technischer Kontext
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/kontext/scope_technical.png)

# 4. Lösungsstrategie

## 4.1 Allgemein

| Lösungsstrategie | Qualitätsmerkmale | Umsetzung |
| ----------- | ----------- |----------- |
| Fachliche Komponenten Trennung | Funktionalität, Wartbarkeit, Übertragbarkeit | Die Einführung vom MVC-Pattern soll die Bearbeitung an der Applikation vereinfachen und der Applikation eine verständliche Struktur geben.  |
| Grenzen der Konfiguration | Stabilität | Wir wollen vermeiden, dass Spieler die Konfigurationsdatei auf eine nicht vorgesehene Art und Weise manipulieren können. Um dies zu gewährleisten, überprüfen wir die Konfigurationsdatei bei jedem Start der Tron Anwendung und erstellen im Fall einer Beschädigung eine neu.  |
| Gleichmäßige Geschwindigkeit für alle Spieler | Zuverlässigkeit | Die generelle Spielgeschwindigkeit wird über die Konfigurationsdatei festgelegt. Jeder Spieler erhält in einem "Steuerungs-Intervall" die gleiche Anzahl an Bewegungen. |
| Faire konfigurierbare Steuerung | Benutzbarkeit | Jeder Spieler soll die Möglichkeit haben seine favoritisierte Steuerung in der Konfigurationsdatei zu hinterlegen. Vor jedem Match wird den Spieler die Steuerung nochmal angezeigt. Doppelte Tastenbelegungen werden nicht zugelassen. |
| Stabilität bei Absturz anderer Teilnehmer | Stabilität | Ein Spieler, welcher als nicht mehr erreichbar identifiziert wurde, wird aus dem Spiel entfernt. Dazu gehört sein Bike, sowie der Schatten, welchen er im Laufe des Spiels gelegt hat. |

## 4.2 Funktionale Zerlegung anhand der Use Cases
Details siehe [Use Cases](#use-cases).

| Objekt | Erklärung |
| ----------- | ----------- |
| Config | Verwaltet die anpassenbaren Werte und stellt Defaultwerte bereit. |
| IGame | Stellt die Spielelogik und startet die Spiel schleife. |
| IGameManager | Managed die Spieler die mitspielen wollen sowie in welcher Phase sich das Spiel befindet.|
| IArena | Verwaltet die Spieler innerhalb der Arena, merkt sich die Positionen der Spieler, sowie die Schatten der Spieler. |
| ICollisionDetector | Ist für die Überprüfung ob ein Spieler, mit einem anderen Spieler, dessen Schatten oder einer Arenawand zusammengestoßen ist. |
| IPlayer | Verwaltet einen einzelnen Spieler, mit den Coordinaten, der Richtung und der Farbe eines Spieler. |
| ITronModel | Nimmt KeyEvents von dem Controller an und stellt der View die Observables zur Verfügung. |
| ITronController | Verwaltet die Benutzereingaben. |
| Steer | Steer ist ein Objekt das eine PlayerId eine dazugehörende Richtung speichert. |
| StartingOverlay | Ist die Oberfläche wenn sich das Spiel im Zustand Menu befindet. |
| WaitingOverlay | Ist die Oberfläche, wenn sich das Spiel im Zustand Waiting befindet. |
| CountingOverlay | Ist die Oberfläche, wenn sich das Spiel im Zustand Countdown befindet. |
| EndingOverlay | Ist die Oberfläche, wenn sich das Spiel im Zustand Finished befinded. |

 
### 4.2.1 Model

### 4.2.1.1 Config
<!-- CONFIG -->
| UC | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
| ---- |----------------------------------------------------------------------------------| ----------- |----------- |----------- |----------- |----------- |
| UC-1 | `loadConfigFromFile() : void` | Config | Es existiert ein Config-Objekt. Es existiert eine TronConfig.properties File. | Properties-Objekt ist erzeugt. |Die Konfigurationsdatei ist ein '.properties' File, in Form: 'Key', 'Value' und wird in ein Properties-Objekt eingelesen. Das Config-Objetzt hält eine Referenz darauf.| Bei fehlender .properties-Datei an der erwarteten Speicheradresse, wird neue .properties-Datei erstellt (`reloadConfig()`) |
| UC-1 | `isConfigValid() : boolean` | Config | Es existiert ein Properties-Objekt. | Das Ergebnis ist wahr oder falsch. | Der Inhalt des Properties-Objekt wird auf fehlende 'Keys' geprüft. Und ob die 'Values' sich im richtigen Wertebereich befinden. Sollte einer der beiden Fälle eintreffen, würde die Methode 'false' zurückgeben. | - |
| UC-1 | `reloadConfig() : Properties `   |Config | Schreibrechte. | Properties mit 'default'-Werten wurde erzeugt. | Es wird eine neues Properties-Objekt auf Basis vom im Programmcode festgelegten 'Key-Value-Paaren' in der Config erstellt. Das Properties-Objekt wird auch an der hinterlegten Speicheradresse lokal in Form einer .properties-Datei hinterlegt.  | Wenn keine Rechte bestehen, wird der Anwender darüber informiert. |
| UC-1| `setKeyMappings() : void`                                                          | Config | Es existiert ein Properties-Objekt mit validen Daten | In dem Config-Objekt existiert eine Map, welche als 'Key' alle Tasten enthält, welche zum lenken genutz werden können. Als Value erhält die Map ein Steer-Objekt welches die Player-ID, sowie die Direction hält. | Die Methode zieht sich aus dem properties-Objekt die Tastenbelegungen aller Spieler. | - |
| UC-1 | `getAttribute(key : String) : String`                                              | Config | Es existiert ein Properties-Objekt mit validen Daten. | Es wurde der passende 'Value' zum 'Key' zurückgegeben.| Die Methode greift auf ein Properties-Objekt zu und zieht sich den ersten 'Value' welcher zu dem Eingabeparameter String passt. | - |
| UC-X | `hasAllAtributes() : Steer`                                                  | Config | Es existiert ein Properties-Objekt. | Abhängig davon ob Einträge in der Properties fehlen wird ein boolean zurückgegeben.| Die Methode greift auf ein Properties-Objekt zu und vergleicht dieses mit der Default-Properties. Wenn Keys fehlen ist dies gleichzusetzen damit, das Attribute fehlen . | - |
| UC-X | `isNumber(strings : String...) : boolean`                                                  | Config | Es existiert ein Properties-Objekt. | Abhängig davon ob alle der Eingabestrings nummerisch sind oder nicht erhält man true oder false. | Es wird über die Eingabe-Strings iteriert und für jeden String geschaut ob er nummerischist oder nicht. Sollte auch nur einer nicht nummerischsein ist das gesamte Ergebniss false | - |
| UC-X | `isSteerValid(strings : String...) : boolean`                                                  | Config | Es existiert ein Properties-Objekt. |Abhängig davon ob die Eingabe-Strings zu einem Steer umgewandelt werden können erhält man true oder false | Anhand der Länge der Eingabe und des Inhalts wird überpfüft ob aus der Eingabe ein valides Steer-Objekt erstellt werden könnte. | - |
| UC-X | `isValidKey(tempStringArray : String[] , index : int) : boolean`                                                  | Config | Es existiert ein Properties-Objekt. |Abhängig davon ob die Player-Tasten valide sind erhält man true oder false |Es wird überpfüft ob es sich bei den Tasten für die Steuerung um valide Eingaben handelt. Als valide gelten alle Zahlen, Buchstaben mit Außnahme (ä,ö,ü) und alle Pfeiltasten. | - |
| UC-X | `isValidKey(tempStringArray : String[] , index : int) : boolean`                                                  | Config | Es existiert ein Properties-Objekt. |Abhängig davon ob die Player-Tasten valide sind erhält man true oder false |Es wird überpfüft ob es sich bei den Tasten für die Steuerung um valide Eingaben handelt. Als valide gelten alle Zahlen, Buchstaben mit Außnahme (ä,ö,ü) und alle Pfeiltasten. | - |
| UC-X | `getKeyMappingForPlayer(id : int) : String`                                                  | Config | Es existiert ein Properties-Objekt. |String mit Tasten für die Steuerung des Spielers in Tupel-Form Bsp.: "A,Z" |Die Eingabe-Id wird auf den Spieler in der Properties gemappt, sodass z.B. die ID = 1, den Value zum Eintrag P_EINS (Key) aus der Properties zieht. | - |



### 4.2.1.2 IGame & Game
<!-- GAME -->
| UC | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
| ---- |----------------------------------------------------------------------------------| ----------- |----------- |----------- |----------- |----------- |
| UC-2 | `prepare(waitingTimer : int, playerCount : int) : void` | IGame | Ein Game Objekt wurde erzeugt und im State INIT. PlayerCount ist zwischen 2 und 6. | Das Game Objekt ist bereit für den Spielstart. | Das Game wird für den Start vorbereitet: Es erstellt eine Arena und statet einen Timer, nach dem die Vorbereitung beendet wird (waitingTimer der Config-File). | - |
| UC-2 | `register(viewObserver: ITronView, stateObserver: IGameManager, managedPlayerCount : int) : void` | IGame | Ein Game Objekt wurde erzeugt und befindet sich im State "PREPARING" | Das Game speichert sich seine Observer und managedPlayerCount Player erstellt. | Das Game merkt sich seine Observer, die es über das Spielgeschehen informieren soll und erstellt so viele Spieler, wie übergeben wird. Es gibt die IDs der erstellten Spieler zurück. | - |
| UC-2 | `handleTimeOut() : void` | IGame | Game State == PREPARING | Game State == COUNTDOWN oder Game State == INIT | Ist der WaitingTimer abgelaufen und das Game befindet sich noch im PREPARING State, wird die Vorbereitung beendet. > 2 Spieler: Spiel wird gestartet, < 2 Spieler: Spiel kehrt ins Menü zurück. | - |
| UC-2 | `start() : void` | IGame | Preparation war erfolgreich | Game-Thread wurde gestartet. | Startet den Game-Thread, in dem `countdown` und `gameloop` ausgeführt wird. | - |
| UC-3 | `countDown() : void`                                                               | Game | Es muss ein Game-Objekt erstellt worden sein und das 'Game' wurde erfolgreich initialisiert. Der GameManager befindet sich im State 'COUNTDOWN' | Der GameManager ändert seinen State zu "PLAYING"| Ein CountDown welcher für drei Sekunden runter zählt. In jeder Sekunde wird der Counter des GameManagers um einen heruntergezählt | Der GameManager befindet sich im falschen State. Er ignoriert den CountDown. |
| UC-3 | `gameLoop() : void`                                                                | Game | Es muss ein Game-Objekt erstellt und das 'Game' erfolgreich vorbereitet worden sein && Der Countdown ist abgelaufen. | Es ist ein oder kein Spieler am Leben. | Die gameLoop() ist eine Schleife in der die primäre Spiellogik implementiert ist. Sie berechnet in jedem Takt die neue Koordinate der Spieler anhand deren Direction (`calculateNextCoordinate()`). Ebenfalls überprüft sie, ob Spieler kollidieren oder ob ein Spiel zuende ist (isGameOver()). | - |
| UC-2 | `calculateFairStartingCoordinates(int playerCount) : List<Coordinate>`             | Game | playerCount > 1 | Es existieren genau soviele faire Startpositionen wie es Player gibt. | Anhand der Spielerzahl wird eine faire Aufteilung der Startposition in der Arena berechnet. Jeder Spieler soll gleich viel Abstand zu den Rändern der Arena und zu den Anderen Spielern haben. | - |
| UC-2 | `calculateStartingDirection(coordinate : Coordinate ) : Direction`                | Game | Coordinate darf nicht NULL sein. |Es wurde eine Strartposition errechnet.| Es wird eine Direction zurückgegeben, welche Richtung Spielfeldmitte zeigt. | - |
| UC-3 | `calculateNextCoordinate(direction : Direction) : Coordinate`                    | Game |Direction darf nicht NULL sein. | Es wurde eine neue Coordinate berechnet | In Abhängigkeit von der Direction wird eine neue Coordinate berechnet. | |
| UC-3 | `isGameOver() : boolean`                                                           | Game | Das Game wurde gestartet. | Ergebnis ist wahr oder falsch | Wenn der Counter der aktiven Player < 2 dann gibt die Methode den Wert 'true' zurück andernfalls 'false' | - |
| UC-3.1 | `handleSteer(steer : Steer ) : void`                                              | IGame | Player muss noch am Leben sein. | Der Player hat eine neue Direction.| Anhand der ausgelesenen Player-ID und Direction eines Steer-Objekts, wird die Fahrtrichtung des Players angepasst. | - |
| UC-3 | `reset() : void `                                                                    | Game | Die Gameloop ist beendet oder PREPARING war nicht erfolgreich. | Der Zustand des Game-Objekts ist wieder im 'default'-Zustand.  | Setzt alle Werte des Games zurück und leert die Arena, wenn ein Spiel vorbei ist oder die Vorbereitung abgebrochen wurde.| - |
| UC-3 | `finish() : void `                                                                    | Game | Die Gameloop ist beendet. | Der GameManager wurde über das Spiel Resultat informiert.  | Informiert den GameManager über das Spiel Resultat (`setGameResult`) und ruft `reset()`auf, um das Spiel in den Initial-Zustand zurückzuversetzen. | - |

### 4.2.1.3 IGameManager & GameManager
<!-- GAMEMANAGER -->
| UC | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
| ---- |----------------------------------------------------------------------------------| ----------- |----------- |----------- |----------- |----------- |
| UC-2,3 | `setManagedPlayers(managedPlayers: Map<ID, Coordinate>) : void` | IGameManager | Der GameManager hat sich erfolgreich bei einem Spiel registriert | Der GameManager weiß, welche Spieler (ID - ggf Farbe? Steuerung?) er managed | Informiert den GameManager über die Spieler, die er in einem Game managed, nachdem er sich bei einem Game registriert hat. | - | 
| UC-2,3,4 | `handleGameState(gameState : GameState) : void`                                    | IGameManager | Der GameManager wurde über handleGameState(gameState:GameState) über eine Veränderung informiert. | Der ModelState des GameManagers hat sich gemäß des GameStates verändert. | Das Model wechselt vom aktuellen ModelState in den nächsten ModelState abhängig von der Nachricht. | Ist eine Nachricht nicht gültig im aktuellen ModelState, wird sie ignoriert. |
| UC-2,3,4 | `executeState() : void`                                                            | GameManager | Es gab einen Zustandsübergang. | Die 'do's des States wurden durchgeführt. | In Abhängigkeit vom ModelState zeigt der GameManager Overlays an, initialisiert ein Game etc. (Verweis auf das State-Diagramm) | - |
| UC-4 | `setGameResult(result : String, color : Color) : void`                                            | IGameManager | Es gibt einen Gewinner oder es ist unentschieden | Das Spielergebnis (Gewinner-Farbe, Ergebnis-Text) wurde gesetzt. | Setzt das Spielergebnis im Game Manager fest, sodass es dem Spieler angezeigt werden kann. | - |

### 4.2.1.4 Arena
<!-- ARENA -->
| UC | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
| ---- |----------------------------------------------------------------------------------| ----------- |----------- |----------- |----------- |----------- |
| UC-3 | `addPlayerPosition(playerId : int, coordinate : Coordinate) : void`               | IArena | Player muss noch am leben sein. Coordinate darf nicht NULL sein und die Coordinate muss sich innerhalb der Arena befinden. | Die Arena wurde aktualisiert | Die aktuell Head-Koordinate des übergebenen Players wird in die Arena eingetragen. | - |
| UC-3 | `deletePlayerPositions(playerIds : List<Integer>) : void`                          | IArena | Liste mit den ID´s darf nicht leer sein.| | Alle koordinaten der übergebenen ID´s werden aus der Arena entfernt. | Wenn die Liste der ID´s leer ist, wird die Methode abgebrochen.|
| UC-3 | detectWallCollision(coordinate : Coordinate) : boolean                           | IArena | Die Coordinate muss innerhalb des Arena-Arrays sein. Coordinate darf nicht null sein. | Variable ist wahr wenn der Spieler zusammengestoßen ist und falsch wenn keine Kollision entdeckt wurde. | Bei jeden Zug wird überprüft ob der Spieler in den Schatten eines weiteren Spieler, die Arenawand oder in seinen eigenen Schatten gefahren ist. | - |

### 4.2.1.5 ICollisionDetector
<!-- COLLISION -->
| UC | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
| ---- |----------------------------------------------------------------------------------| ----------- |----------- |----------- |----------- |----------- |
| UC-3 | `detectHeadColision(players: List<Player>) : boolean`                       | CollisionDetector | Anzahl aktiver Spieler > 1 | - | Es wird überprüft ob ein Player mit dem head eines anderen Players kolidiert. | - |
| UC-3 | `detectColision(players: List<Player>, arena : Arena) : void`                       | ICollisionDetector | Anzahl aktiver Spieler > 1 | - | Es wird überprüft ob ein Player mit dem head eines anderen Players kolidiert. | - |

### 4.2.1.6 IPlayer
<!-- PLAYER -->
| UC | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
| ---- |----------------------------------------------------------------------------------| ----------- |----------- |----------- |----------- |----------- |
| UC-3 | `getHeadPosition() : Coordinate`                                                   | IPlayer | Die Liste der Koordinaten eines Spielers darf nicht leer sein. | die Head-Position wird zurückgeben. | Die letzte Position in der Liste ist immer die headPosition, welche durch die Methode getHeadPosition abgefragt wird. | - |
| UC-3 | `addCoordinate(coordinate : Coordinate) : void `                                 | IPlayer | Die Coordinate ist nicht NULL | Die Koordinatenliste ist um +1 gestiegen. | Dem Spieler wird eine neue Koordinate in seine List<Coordinate> hinzugefügt. | - |
| UC-3 | `isAlive() : boolean`                                                              | IPlayer | - | - | Der Spieler kann entweder noch aktiv am Spiel beteiligt sein oder nicht. Dies wird mit der Funktion abgefragt. Ein wechsel dieses Status erfolgt durch eine Kollision mit Playern (inkl. sich selbst) oder der Arena-Wand.| |
| UC-3 | `crash() : void`                                                                   | IPlayer | Der Spieler ist am Leben und in der aktuellen Spielrunde gecrashed | Der Spieler kann nicht mehr mitspielen | Setzt den alive-Status eines Spielers auf "false" nach einem Crash. | - |
| UC-3.1 | `setNextDirectionChange(directionChange : DirectionChange) : void`                                                                   | IPlayer | Ein Spieler hat per Tastenanschlag gelenkt | Der Player merkt hat sich seine nächste Action gemerkt. | Wenn der Spieler eine Taste drückt, erhält das Player Objekt eine neue Action, die im nächsten Takt ausgeführt werden kann. Tritt ein ActionChange im selben Takt erneut auf, wird die alte Action überschrieben. | - |
| UC-2,3 | `performDirectionChange() : Direction`                                     | IPlayer | Neuer Takt hat begonnen. | Die Direction des Players wurde der Action entsprechend verändert. | Pro Takt wird die Richtung jedes Spielers entsprechend seiner Action verändert. Die Action wird danach auf NONE gesetzt. | - |

### 4.2.1.7 ITronModel
<!-- ITRONMODEL -->
| UC | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
| ---- |----------------------------------------------------------------------------------| ----------- |----------- |----------- |----------- |----------- |
| UC-4 | `getWinnerObservable() : StringProperty`                                           | ITronModel | Der GameManager wurde initialisiert. | Ein Observable StringProperty wurde zurückgegeben. | Andere Komponenten(View) erhalten eine Referenz auf das Observable-Winner-Objekt. | - |
| UC-3.1 | `handleSteerEvent( key : KeyCode ) : void`                                         | ITronModel | Der GameManager wurde initialisiert. | Das Model hat auf das Event reagiert. | Ein Tastenanschlag wird an das Model weitergegeben. |  |
| UC-4 | `getGameResultObservable() : StringProperty`                                       | ITronModel | Der GameManager wurde initialisiert. | Ein Observable StringProperty wurde zurückgegeben.  | Andere Komponenten(View) erhalten eine Referenz auf das Observable-GameResult-Objekt. | |
| UC-3 | `getCounterObservable() : IntegerProperty`                                         | ITronModel | Der GameManager wurde initialisiert.  | Ein Observable IntegerProperty wurde zurückgegeben. | Andere Komponenten(View) erhalten eine Referenz auf das Observable-Counter-Objekt.| |
| UC-2 | `getPlayerCountObservable(): IntegerProperty`                                      | ITronModel | Der GameManager wurde initialisiert. | Ein Observable IntegerProperty wurde zurückgegeben. | Andere Komponenten(View) erhalten eine Referenz auf das Observable-Counter-Objekt. | |
| UC-2 | `initializeGame(playerNumber : int) : void`                                        | ITronModel | Der GameManager wurde initialisiert. | Der GameManager befindet sich im State "WAITING" | Im Model wird ein Game vorbereitet, in dem dann auch die Arena und die Spieler initialisiert werden. | - |

### 4.2.2 Controller
| UC | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
| ---- | ----------- | ----------- |----------- |----------- |----------- |----------- |
| UC-2 | `btnStartGame(event : ActionEvent) : void` | ITronController | Click-Event in View ausgelöst | Es wird im Model ein Game gestartet. | Bei Klick auf den "Spiel starten" Button erhält der Controller ein Event, woraufhin er der Model-Komponente Bescheid sagt, ein Game zu starten. | |
| UC-3 | `initKeyEventHandler(scene : Scene) : void` | ITronController | Scene nicht null, TronController wurde erstellt | Der Controller handelt die KeyEvents auf der übergebenen Scene. | Bei Start der Applikation wird der Controller als EventHandler für die Scene der View initialisiert, damit er KeyEvents des Users erhält und verarbeitet. | - |
| UC-3.1 | `handleKeyEvent(event : KeyEvent) : void` | ITronController | KeyEvent in View ausgelöst | Das Model wurde über einen Tastenanschlag informiert | Tastenanschläge werden von der View an den Controller geleitet, der das Model darüber informiert.

### 4.2.3 View
Es wird die zur Verfügung gestellte view library verwendet. Das ITronView Interface wird um folgende Methoden ergänzt:

| UC | Funktion | Objekt |Vorbedingung | Nachbedingung |Ablaufsemantik|Fehlersemantik|
| ---- | ----------- | ----------- |----------- |----------- |----------- |----------- |
| UC-3 | `updateView(players : Map<Color, List<Coordinates>>) : void `                                                                    | ITronView | - | - | Die Positionen der Spieler werden in der übergebenen Farbe auf den Screen gezeichnet.  | - |
| UC-2 | `setArenaSize(rows : int, columns : int) : void` | ITronView | TrownView wurde initialisiert. | TronView kennt Arena Größen | Das Game informiert die View über die Größen der Arena. | - | 

# 5. Bausteinsicht
## 5.1 Ebene 1
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/baustein/bs_layer1.png)
## 5.2 Ebene 2 : Application
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/baustein/bs_layer2_view_controller.png)
## 5.3 Ebene 3 : Application
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/baustein/bs_layer3_model_interfaces.png)
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/baustein/bs_layer3_model.png)
## 5.2 Ebene 3 : ApplicationStub
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/baustein/bs_layer3_stub_model_interfaces.png)

Für *name* ∈ {IGameManager, IGame, ITronView} gibt es im ApplicationStub eine Komponente der Form:
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/baustein/bs_layer3_stub_model.png)

# 6. Laufzeitsicht
## 6.1 Ebene 1
### 6.1.1 UC-2: Spiel starten
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/laufzeit/sd_mvc_startGame.png)

### 6.1.2 UC-3: Spiel spielen
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/laufzeit/sd_mvc_playGame.png)

### 6.1.3 UC-3.1: Lenken
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/laufzeit/sd_mvc_steerBike.png)

### 6.1.4 UC-4: Ergebnisse ansehen
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/laufzeit/sd_mvc_seeResults.png)

## 6.2 Ebene 2: Model
### 6.2.1 Model States
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/laufzeit/GameManager_states.png)

### 6.2.2 UC-1: Spiel konfigurieren
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/laufzeit/configure.png)

### 6.2.3 UC-2: Spiel starten
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/laufzeit/activity_start.png)

### 6.2.4 UC-3: Spiel spielen
![image info](./diagrams/activity_play.png)

### 6.2.5 UC-4: Spiel beenden & Ergebnisse ansehen
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/laufzeit/activity_end.png)

### 6.2.5 UC-3.1: Lenken
![image info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/laufzeit/sd_steerBike.png)

### 6.2.6 UC-3.1: Lenken-States
![image-info](Desktop/VS/wise22-23-vs-2582106-2279123-2576932/Dokumentation/App/diagrams/laufzeit/Steer_states.png)



# 7. Verteilungssicht

# 8. Querschnittliche Konzepte

# 9. Architekturentscheidungen

# 10. Qualitätsanforderungen

# 11. Risiken und technische Schulden

# 12. Glossar
# 13. Anhang
## Anforderungsdetails
![image info](./diagrams/storyboard.jpg )
**1: Starting Screen**
- Ermöglicht die Wahl der Spieleranzahl.
    - Es wird ein Defaultwert angezeigt.
    - Es kann zwischen 2-6 Spielern ausgewählt werden.
- Enthält einen “Spiel starten” Button.
- Wird der Button betätigt, erscheint Bildschirm 2.

**2: Waiting Screen**
- Wird solange angezeigt, bis die vorher eingestellte Spielerzahl erreicht ist.
    - unter waiting wird angezeigt wie viele Spieler bereits warten
- Timer: Wird nach Ablauf des Timers die Spielerzahl nicht erreicht, wird
    - das Spiel gestartet, wenn > 2 Spieler bereit sind, damit die Spieler nicht so lange warten müssen.
    - der Starting Screen wieder angezeigt, wenn < 2 Spieler bereit sind.
- Ist der Timer  mit > 2 Spieler oder die eingestellte Spielerzahl erreicht, erscheint Bildschirm 3.

**3: Arena**
- Zuerst wird ein Countdown (3-2-1-go) angezeigt.
- Die Arena besteht aus einem Raster, auf dem die Motorräder fahren.
- Hier gelten die oben genannten Spielregeln.
- Alle Motorräder sollen “faire” Startkondition haben. (”Fair” ist nicht näher definiert und den Entwicklern überlassen).
- Die Entwickler sollen sich eine “geeignete Logik” überlegen, durch die die Spieler wissen, welche Figur sie steuern.
- Wenn das Spiel zu Ende ist, wird Screen 4 angezeigt.
- Das Spiel ist beendet, wenn:
    - Es ist nur noch ein Spieler übrig und es gibt einen Gewinner.
    - Die letzten beiden Spieler sterben gleichzeitig. Das Spiel ist unentschieden

**4: End Screen**
- Anzeige “Spiel ist zu Ende”
- Anzeige des Gewinners oder “Unentschieden”. Identifikation des Gewinners ist den Entwicklern überlassen.
- Dieser Screen wird für eine gewisse Zeit angezeigt, danach geht es zurück zum Bildschirm 1.

**Konfiguration**

Folgende Aspekte sollen über eine Konfigurationsdatei einstellbar sein:
- Timer des Waiting Screens.
- Timer des Endscreens
- Defaultwert für Spieleranzahl
- Geschwindigkeit der Motorräder
    - Auf einer Skala von 1%-100%, gemessen in Bewegungen pro Sekunde b/s
    - 1% = 1 b/s, 100% = 500 b/s
- Größe der Arena anhand von 3 Werten:
    - Größe der Rasterpunkte/-zellen, z.B. in Pixel.
    - Anzahl der Rasterpunkte/-zellen in x-Richtung und y-Richtung.
- Tastenkombinationen für die Bewegung der Spieler

Die Konfiguration greift dabei nur bei Neustart der Applikation.

**Weitere Aspekte**
- Das Motorrad muss nicht konfigurierbar sein (Farbe einstellen oder ähnliches).
- Es gibt kein Punktesystem oder ähnliches. Jedes Spiel wird für sich gespielt.
- Die Multiplayer-Anzeige (Splitscreen mit mehreren Arenen oder eine Arena für alle) ist den Entwicklern überlassen.
- Spieler können ein Spiel weder pausieren noch beenden.
- Zum Schließen des Spiels reicht der Standard "x"-Button.

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
        speed (Ganzzahl zwischen 1-100)
        windowHeight (Ganzzahl)
        windowWidth (Ganzzahl)
        rasterX (Anzahl in X Richtung)
        rasterY (Anzahl in y Richtung)
        controllsPlayer1-6 (bsp: W,A,S,D)
        gameMode (LOCAL, REMOTE, REMOTE_HOST)
        nameServer (IP:Port)
    2. Der Benutzer speichert die Datei.
    3. Der Benutzer startet das System.
    4. Das System lädt die Daten aus der Konfigurationsdatei.
    5. Das System überprüft die Daten der Konfigurationsdatei auf Fehler.
    6. Das System zeigt den Starting Screen an.

Fehlerfälle:

    6.a. Das System findet einen Fehler in der Konfigurationsdatei oder findet die Konfigurationsdatei nicht.
        6.a.1 Das System zeigt eine Fehlermeldung "Konfigurationsdaten fehlerhaft".
        6.a.2 Das System erstellt eine neue .properties-Datei und ersetzt die alte.

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
    4. Das System initialisiert ein Tron-Game mit der defaultPlayerNumber und konfigurierten Arenagröße.
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
    3. Das System zeigt während des Countdowns die Farben und ID der Spieler an ihrer Startposition an.
    4. Das System bewegt die Bikes stetig in die aktuelle Richtung in der konfigurierten Geschwindigkeit vorwärts.
    5. Das System vergrößert den Schatten des Bikes mit jeder Vorwärtsbewegung.
    6. Der / Die Mitspieler stirbt / sterben bei Kollision.
    7. Das System zeigt die Schatten der gestorbenen Spieler nicht mehr an.
    8. Das System beendet das Spiel, wenn nur noch einer oder kein Spieler mehr am Leben ist.
    9. Das System aktualisiert die Ergebnisdaten des Spiels.

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
    2. Das System zeigt den Ending-Screen an, in dem das Ergebnis des Spiels ("Gewinner ist ..." oder "Unentschieden!") und die Farbe des Gewinners, falls es einen gibt.
    3. Das System wechselt zurück zum Starting Screen, wenn der konfigurierte End-Timer angelaufen ist.
