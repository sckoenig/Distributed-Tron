Agenda:
- offene Fragen klären
    - Nicht erreichbare Dienste im NameServer -> ggf. Heartbeat?
    - WaitingTimer -> Wenn GameManager abgelehnt wird, schickt Game INIT (cancel)
- Issues besprechen +
- cancel / reset / prepare definieren +
- RPC-Protokoll anfangen 
    - vermutlich JSON -> JSON.parser gibt es schon, weniger selbst Parsen!
    - CallerID nötig (remoteID des Senders, nötig ggf. für Sortierung der Nachrichten)
    - CalleeID (remoteID des Empfängers) vermutlich nicht nötig
    - NachrichtenTyp -> Ein Typ pro Methode, die übers Netzwerk fliegt (Ordinal des Enums)
    - Length -> abhängig vom Nachrichtentyp, z.B. draw: erstes int = color, length beschreibt, wie viele Coordinaten nach color folgen.
    - Parameter = Payload
    - Counter (Clock? reihenfogle?)
    - Nachrichtentypen
        - PREPARE
            - Payload: 
                waitingTimer, playerCount
            - Length: TESTEN
        - REGISTER
            - Payload: 
                remoteID(view), remoteID (gm), int managedPlayers
        - HANDLE_GAME_STATE
            - Payload: 
                ein int, was auf Game State Enum mappt (macht der GameManagerCallee).
        - SET_MANAGED_PLAYERS
            - Payload: 
                ein int PlayerID, int x int y //remote NUR EIN SPIELER 
        - SET_ARENA
            - payload:
                int row, int column
        - COUNTDOWN
            - payload: keiner
        - HANDLE_STEER
            - payload:
                playerID int, 0 oder 1 (für ENUM LEFT RIGHT)
        - UPDATE_VIEW
            - payload:
                map{color => [coords], color => [coords], ... }
        - SET_GAME_RESULT
            - payload:
                int color (ENUM), int status (ENUM)
        
## Festlegung
- Für Impl feature branches, die auf dev gehen. 
- ARENA CONFIG kommt vom "Host"
- eine wrappende Draw-Methode, die alle player annimmt (an die VIEW)

## TO DO
- Lösungsstrategie mit Draw.io vereinheitlichen 
- dev auf master pushen
- game braucht handleTimeOut, die vom Timer aufgerufen werden kann
- GameManager methoden zusammenfassen für Winner: setGameResult(status : String, winner : color)  -> passt dann winnerColor und gameResult String an
- ITronView anpassen: updateView, setArenaSize(rows, columns)
- JSON parser: Müssen wir eine Länge mitschicken? z.B. wie viele Coordinaten geschickt werden oder macht Json Parser da eine Liste draus?
- ENUMS hinzufügen: COLOR, GAME_RESULT(WINNER, DRAW)
