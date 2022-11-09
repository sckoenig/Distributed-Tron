[[_TOC_]]

# 1. Einführung und Ziele

In diesem Dokument wird eine Middleware für die Applikation Tron beschrieben.

## 1.1 Aufgabenstellung

Die Middleware bildet eine Zwischenschicht zwischen der Applikation und dem Betriebssystem. Sie soll den Aufruf von Funktionen zwischen voneinander unabhängigen Nodes ermöglichen.


## 1.2 Qualitätsziele

| Ziel        | Beschreibung |
| ----------- | ----------- |
| Offenheit | Die Entwickler sollen sich gut um ihre Schnittstellen kümmern :) |
| Skalierbarkeit | Größenskalierbarkeit: Es müssen sich 2-6 Nodes beteiligen können.
Geographische Skalierbarkeit: Die Anwendung läuft in einem LAN beim Kunden (Raum ... )
Administrative Skalierbarkeit: Es gibt eine administrative Domäne. |
| Transparenz |  <ul><li>Access</li><li>Location</li><li>Relocation</li><li>Migration</li><li>Replication</li><li>Concurrency</li><li>Failure</li></ul> |

## 1.3 Stakeholders

| Rolle      | Kontakt | Erwartungen
| ----------- | ----------- | ----------- |
| Dozent / Kunde | Martin Becke: martin.becke@haw-hamburg.de | Wohldefinierten Schnittstellen, Lernfortschritt der Entwickler |
| Entwickler | Stabile Anwendung, Anforderungen an Middleware verstehen und umsetzen |
| Spieler   | Teilnehmer des Moduls VS WiSe22/23 | Kriegt nicht mit, dass es eine Middleware gibt. |



# 2. Randbedingungen

| Technische Randbedingung        | Beschreibung |
| ----------- | ----------- |
| Java in der Version ... | Zur Implementierung wird Java verwendet, da das ganze Team die Sprache beherrscht. <br/> Die Version muss zum Image der Rechner im Raum 7.85 passen. |
| Kommunikation | Die Kommunikation der Middleware erfolgt über RPC- und/oder ReST-Schnittstelle. |

| Konventionen | Beschreibung |
| ----------- | ----------- |
| Dokumentation | Gliederung nach dem deutschen arc42-Template, um Struktur zu wahren. |
| Sprache | Die Dokumentation erfolgt auf deutsch, während die Diagramme auf Englisch gehalten werden, um die Umsetzung in (englischen) Code zu erleichern. |

# 3. Kontextabgrenzung

## 3.1 Business Kontext
![image info](./diagrams/scope_business.png)

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
