
[[_TOC_]]

# 1. Introduction and Goals

Dieses Projekt entsteht im Rahmen des Moduls “Verteilte Systeme” und hat zum Ziel, das Multiplayer-Spiel “Tron” als Standalone Applikation zu entwickeln.

## 1.1 Requirements Overview

Der Kunde wurde über das allgemeine Spielprinzip befragt. Daraus ergaben sich folgende Spielregeln:

- Das Spiel wird mit mehreren Spielern gespielt, die jeweils ein Motorrad in einer Arena (das Spielfeld) steuern.
- Die Motorräder bewegen sich stetig vorwärts, in einer konstanten Geschwindigkeit.
- Der Spieler kann das Motorrad nach links oder rechts steuern und sich so über das Spielfeld nach oben, unten, links, rechts bewegen.
- Die Motorräder ziehen farbige “Schatten” hinter sich auf, die für die Lebenszeit des Spielers auf dem Spielfeld bleiben und durch die Vorwärtsbewegung der Motorräder länger werden.
- Ein Spieler stirbt, wenn er gegen die Wand der Arena oder den Schatten eines anderen Spielers fährt. Treffen zwei Spieler aufeinander, sterben beide.
- Wenn ein Spieler stirbt, verschwindet sein Schatten aus der Arena und er kann nicht weiterspielen.
- Alle Spieler spielen gegeneinander. Gewonnen hat der, der am längsten überlebt. Sterben die letzten beiden Spieler gleichzeitig, ist es unentschieden.

Mit dem Kunden wurde über Storyboarding die Details der Anforderungen erarbeitet:

[ STORY BOARD ]

**1: Starting Screen**
- Ermöglicht die Wahl der Spieleranzahl.
    - Es wird ein Defaultwert angezeigt.
    - Es kann zwischen 2-6 Spielern ausgewählt werden.
- Enthält einen “Spiel starten” Button.
- Wird der Button betätigt, erscheint Bildschirm 2.

**2: Waiting Screen**
- Wird solange angezeigt, bis die vorher eingestellte Spielerzahl erreicht ist.
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
- Zum Schließen des Spiels reicht der Standard "x"-Button.

## 1.2 Quality Goals

| Ziel        | Beschreibung |
| ----------- | ----------- |
| Wohldefinierte Schnittstellen | Die Entwickler sollen sich gut um ihre Schnittstellen kümmern :) |
| Fehlertoleranz      | Ein Spiel soll ungestört durchspielbar sein. Auch wenn ein Teilnehmer abstürzt, läuft das Spiel weiter.       |
| Bedienbarkeit | Spieler sollen das Spiel einfach bedienen können und Spaß haben |
| Kompatibilität   | Mindestens 2 Teams müssen miteinander spielen können.        |
| Fairness | Das Spiel soll fair sein. Alle Spieler starten mit fairen Konditionen und folgen den gleichen Regeln

## 1.3 Stakeholders

| Rolle      | Kontakt | Erwartungen
| ----------- | ----------- | ----------- |
| Martin Becke      | ...       | ... |
| Entwickler | ... | ... |
| Spieler   | ...        | ... |


# 2. Architecture Constraints

- Es muss eine objektorientierte Programmiersprache verwendet werden (z.B. Java).
- Das Einsetzen von Architektur/Entwurdsmustern (MVC, Observer, State) ist erwünscht.


# 3. System Scope and Context

## Business Context 

![image info](./diagrams/scope_business.png)

## Technical Context
![image info](./diagrams/scope_technical.png)

# 4. Solution Strategy

# 5. Building Block View

# 6. Runtime View

# 7. Deployment View

# 8. Cross-cutting Concepts

# 9. Architecture Decisions

# 10. Quality Requirements

# 11. Risks and Technical Debts

# 12. Glossary
