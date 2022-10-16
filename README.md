[[_TOC_]]

# 1. Einführung und Ziele

Dieses Projekt entsteht im Rahmen des Moduls “Verteilte Systeme” und hat zum Ziel, das Multiplayer-Spiel “Tron” als Standalone Applikation zu entwickeln.

## 1.1 Aufgabenstellung

Allgemeine Spielprinzipien:

- Das Spiel wird mit mehreren Spielern gespielt, die jeweils ein Motorrad in einer Arena (das Spielfeld) steuern.
- Die Motorräder bewegen sich stetig vorwärts, in einer konstanten Geschwindigkeit.
- Der Spieler kann das Motorrad nach links oder rechts steuern und sich so über das Spielfeld nach oben, unten, links, rechts bewegen.
- Die Motorräder ziehen farbige “Schatten” hinter sich auf, die für die Lebenszeit des Spielers auf dem Spielfeld bleiben und durch die Vorwärtsbewegung der Motorräder länger werden.
- Ein Spieler stirbt, wenn er gegen die Wand der Arena oder den Schatten eines anderen Spielers fährt. Treffen zwei Spieler aufeinander, sterben beide.
- Wenn ein Spieler stirbt, verschwindet sein Schatten aus der Arena und er kann nicht weiterspielen.
- Alle Spieler spielen gegeneinander. Gewonnen hat der, der am längsten überlebt. Sterben die letzten beiden Spieler gleichzeitig, ist es unentschieden.

Details siehe [Anforderungsdetails](#anforderungsdetails).


## 1.2 Qualitätsziele

| Ziel        | Beschreibung |
| ----------- | ----------- |
| Wohldefinierte Schnittstellen | Die Entwickler sollen sich gut um ihre Schnittstellen kümmern :) |
| Fehlertoleranz      | Ein Spiel soll ungestört durchspielbar sein. Auch wenn ein Teilnehmer abstürzt, läuft das Spiel weiter.       |
| Bedienbarkeit | Spieler sollen das Spiel einfach bedienen können und Spaß haben |
| Kompatibilität   | Mindestens 2 Teams müssen miteinander spielen können.        |
| Fairness | Das Spiel soll fair sein. Alle Spieler starten mit fairen Konditionen und folgen den gleichen Regeln
| Spielbarkeit | Damit das Spiel flüssig läuft, brauchen wir mindestens 18 Frames

## 1.3 Stakeholders

| Rolle      | Kontakt | Erwartungen
| ----------- | ----------- | ----------- |
| Dozent / Kunde | Martin Becke: martin.becke@haw-hamburg.de |  Saubere Architektur mit Pattern und wohldefinierten Schnittstellen, Lernfortschritt der Entwickler | 
| Entwickler | Sandra: sandra.koenig@haw-hamburg.de <br/> Inken: inken.dulige@haw-hamburg.de<br/> Majid: majid.moussaadoyi@haw-hamburg.de| Spaß an der Entwicklung, Architekturentwurf lernen, gutes Time handling, JavaFX Kenntnisse verbessern|
| Spieler   | Teilnehmer des Moduls VS WiSe22/23 | Stabile Anwendung, Spaß am Spielen |


# 2. Randbedingungen

| Technische Randbedingung        | Beschreibung |
| ----------- | ----------- |
| Java in der Version ... | Zur Implementierung wird Java verwendet, da das ganze Team die Sprache beherrscht. <br/> Die Version muss zum Image der Rechner im Raum 7.85 passen. | 
| View Library | Es wird die zur Verfügung gestellte JavaFX View Library verwendet, um Zeit in der UI-Erstellung zu sparen. |

| Konventionen | Beschreibung |
| ----------- | ----------- |
| Dokumentation | Gliederung nach dem deutschen arc42-Template, um Struktur zu wahren. |
| Sprache | Die Dokumentation erfolgt auf deutsch, während die Diagramme auf Englisch gehalten werden, um die Umsetzung in (englischen) Code zu erleichern. |


# 3. Kontextabgrenzung 

## 3.1 Business Kontext

![image info](./diagrams/scope_business.png) \
Details siehe [Use Cases](#use-cases).

## 3.2 Technischer Kontext
![image info](./diagrams/scope_technical.png)

# 4. Lösungsstrategie 

# 5. Bausteinsicht

# 6. Laufzeitsicht 

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
        rasterSize (Pixel)
        rasterX (Anzahl in X Richtung)
        rasterY (Anzahl in y Richtung)
        controllsPlayer1-6 (bsp: W,A,S,D)
    2. Der Benutzer speichert die Datei.
    3. Der Benutzer startet das System.
    4. Das System lädt die Daten aus der Konfigurationsdatei.
    5. Das System überprüft die Daten der Konfigurationsdatei auf Fehler.
    6. Das System zeigt den Starting Screen an.

Fehlerfälle: 

    6.a. Das System findet einen Fehler in der Konfigurationsdatei oder findet die Konfigurationsdatei nicht.
        6.a.1 Das System zeigt eine Fehlermeldung "Konfigurationsdaten fehlerhaft".
        6.a.2 Das System erstellt eine neue .properties-Datei und ersetzt die alte.


| Methode      | Baustein | Erläuterung 
| ----------- | ----------- | ----------- | 
| loadConfig() : Properties; throws IOException  | Config | Die Konfigurationsdatei ist ein '.properties' File. Form: 'Key', 'Value' und wird in ein Properties-Objekt eingelesen.  |
|getAttribute(key : String) : String| Config | Lädt einzelnes Attribut aus Properties-Objekt.|
| isConfigValid() : boolean; ~~throws InvalidConfigException~~ | Config | Beim einlesen der '.properties' wird die Zulässigkeit der Values überprüft. Bei unzulässigen Werten, setzt das Spiel default Werte und informiert den Spieler darüber.|
|reloadConfig() : .properties| Game | Bei fehlender '.properties' am vermuteten Speicherort, wird eine neue default-Datei erstellt.|
| showAlert(message : String) : void | Controller | Zeigt Hinweis Pop-up. |
| getGameState(): GameState | Game | Gibt den State des Spieles zurück.|

<br/>

**UC-2: Start Game**

Akteur: Spieler \
Ziel: Tron-Spiel spielen. \
Vorbedingungen: Das System befindet sich im Starting-Screen. \
Nachbedingungen: Es wird ein Tron-Game begonnen und angezeigt.

Standardfall:

    1. Das System zeigt den Starting Screen mit der defaultPlayerNumber und einen "Spiel starten" Button an.
    2. Der Spieler betätigt den Button.
    3. Das System startet den Waiting-Timer.
    4. Das System wechselt in den Waiting-Screen.
    5. Das System initialisiert ein Tron-Game mit der defaultPlayerNumber und konfigurierten Arenagröße.
    6. Das System berechnet die Startpositionen der Spieler. 
    7. Das System wechselt in den Arena-Screen.
    8. Es beginnt US-3: Play Game

Erweiterungsfälle:

    6.a Nach Ablauf des Waiting-Timers konnte die Spieleranzahl nicht erreicht werden mit >= 2 Spieler.
        6.a.1 Das System initialisiert das Tron-Game mit der vorhandenen Spieleranzahl.
    2.a Der Spieler wählt eine andere Spielerzahl (Choose Player Number)
        2.a.1 Der Spieler wählt eine Spielerzahl zwischen 2-6 aus einem Drop Down MenÃ¼.
        2.a.2 Das System verwendet den eingegebenen Wert anstelle des Defaultwertes.
        2.a.3 Weiter im Standardfall Punkt 2.

Fehlerfälle:

    4.b Nach Ablauf des Waiting-Timers konnte die Spieleranzahl nicht erreicht werden mit <2 Spieler.
        4.b.1 Das System kehrt zum Starting-Screen zurück.

| Methode      | Baustein | Erläuterung 
| ----------- | ----------- | ----------- | 
| showWaitingScreen() : void| Controller | Wechselt in den Waiting Screen.|
| startTimer(seconds : int) : void | Game | Startet den Timer, wie lange der Waiting Screen angezeigt werden soll.|
| initializeGame(playerNumber : int) : void | Game | Ändert den State des Spiels.|
| startGame(playerCount: int) : void | Controller | Initialisiert das Spiel. |
| initializePlayers(playerCount : int) : void | Game | Initialisiert die Anzahl der Spieler.|
| initializeArena() : void | Game | Initialisiert die Arena, nach den vorgegebenen Parametern.|
| setPlayerCount(actualPlayerCount : int) : void | Game | Setzt die Anzahl der Spieler, auf die Anzahl der erstellten Spieler.|
| calculateFairStartingPositions() : void | Game | Berechnet die Startpositionen der Spieler in Abhängigkeit zur Spieleranzahl.|
| showArenaScreen() : void | Controller | Wechselt in den Arena Screen.|

<br/>

**UC-3: Play Game**

Akteur: Spieler \
Ziel: Gewinnen \
Vorbedingungen: UC-2: Spiel Starten erfolgreich abgeschlossen. \
Nachbedingungen: Das Spiel ist mit "Gewinner" oder "unentschieden" geendet.

Standardfall:

    1. Das System zeigt das Spielfeld in der konfigurierten Größe und die Spieler an. 
    2. Das System zeigt einen Countdown(3-2-1) an.
    3. Das System zeigt während des Countdowns die Farben und ID der Spieler an ihrer Startposition an.
    4. Das System bewegt die Bikes stetig in die aktuelle Richtung in der konfigurierten Geschwindigkeit vorwärts.
    5. Das System vergrößert den Schatten des Bikes mit jeder Vorwärtsbewegung.
    6. Der / Die Mitspieler stirbt / sterben bei Kollision. 
    7. Das System zeigt die Schatten der gestorbenen Spieler nicht mehr an. 
    8. Das System beendet das Spiel, wenn nur noch einer oder kein Spieler mehr am Leben ist.
    9. Das System startet den Ending-Timer.
    10. Das System zeigt den Ending-Screen an, in dem "Gewinner:" und die Farbe des Spielers angezeigt werden. 
    11. Das System wechselt zurück zum Starting Screen, wenn der konfigurierte End-Timer angelaufen ist.

Erweiterungsfälle:

    10.a Die letzten beiden Spieler sind gleichzeitig gestorben
        10.a.1 Das System zeigt den Ending Screen mit "Unentschieden" an.

    4.a Der Spieler steuert sein Bike durch Tasteneingaben (Steer Bike)
        4.a.1 Das System verarbeitet die Tasteneingabe abhängig von der Konfiguration des Spielers. 
        4.a.2 Das System ändert die Richtung des Bikes des Spielers abhängig von der Taste.

| Methode      | Baustein | Erläuterung 
| ----------- | ----------- | ----------- | 
| countDown() : void   | Game | Es wird ein Countdown 3-2-1 heruntergezählt, bevor das Spiel startet. | 
| calculateNextCoordinate() : void | Bike | Berechnet die nächste Koordinate eines Bikes. |
| addToBike(coordinate : Coordinate) : void | Bike | Eine Koordinate wird zum Schatten eines Bikes hinzugefügt, sodass er länger wird. |
| changeDirection(direction : Direction) : void | Bike |Verändert die Richtung eines Spielers. |
| detectCollision() : void | Game |Überprüft für alle Bikes eines Games, ob sie (1) gegen eine Wand, (2) gegen den Schatten eines anderen Spielers (3) ineinander gefahren sind. |
| crash() : void | Bike | Setzt den "alive"-Status eines Bikes auf false nach einer Kollision. | 
| isGameOver() : boolean | Game | Überprüft, ob nur noch ein Spieler oder kein Spieler am Leben ist. |
| calculateWinner(): Bike | Game | Prüft, welcher Spieler am Ende noch am Leben ist und gibt ihn zurück. Wurde das Spiel beendet, weil alle Spieler gestorben sind, wird null zurückgegeben. | 
| showEndingScreen() | Controller | Wechselt in den Ending-Screen. | 
