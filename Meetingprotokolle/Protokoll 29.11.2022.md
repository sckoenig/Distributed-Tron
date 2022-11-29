## Ankündigungen
- Methodennamen angepasst: performDirectionChange() und setNextDirectionChange() in Player
- detectCollision entfernt zukünftig auch die Spieler aus der Arena. Somit kann der Rückgabewert von detectCollision() auf void gesetzt werden.
- Wir haben uns für die ID = IP+PORT entschieden.
- Wenn bei einem Invoke Aufruf das Gegenüber noch unbekannt ist, soll die Methode einen default-Wert akzeptieren.

## Nacharbeiten
- Alle zu UDP belesen und das verhalten von Datagram testen.
- Eigenes Protokoll für NameServer muss erstellt werden.
- Skript zum aufrufen von zwei .jar-Files (Skript, .EXE, etc.)?
- Copy & Paste der UC in die Lösungsstrategien.

## Hinweis
- NamingServer = NameServer + NameResolver
- Aufbau der Methode: MethodCall (id (Ordinal Enum) : int , Methodenparameter : int... (varargs)))

## Fragen
- Wenn die Adresse aus dem Chache nicht erreichbar ist und der NamingServer (wiederholt) den selben nicht erreichbaren Host zurückgibt. Was tun wir? - Ist es für uns überhaupt relevant, oder können wir damit leben. 
- Verteilungsdiagramm, wie soll dies aussehen?
