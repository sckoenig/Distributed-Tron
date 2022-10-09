
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

Näheres siehe [Anforderungsdetails](#13.1-requirement-details).


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

# 13. Appendices
## 13.1 Requirement Details
