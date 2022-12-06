### Anmerkungen Sandra
- Activity Diagramme: 
    - Detect Collision nach Martin feedback bisschen angepasst -> mal drüber gucken, was euch besser passt / was mehr Sinn macht.
    -> sieht gut aus, Kantenbeschreibung wäre nicht unbedingt notwendig -> ist besser lesbar 
    - Activity Diagram für Gameloop erstellt -> ok? Was ist besser lesbar, Activity oder Sequenz?
    -> pfeil beschriftung player not alive irritierend, ansonsten activitätdiagram besser lesbar
- Labels: Prio Labels an den Git Issues erstellt. -> Prios ggf. anpassen, wenn ihr etwas anders seht.
- Implementierung: Extra Milestone für Implementierung der App angelegt. Due Date kurz vor Weihnachten! Jeder mal so nebenbei Issues schnappen. Feature Branches? -> ggf. über Teststrategie sprechen
- Martin zum Thema NameServer und toter Host: 
    - EGAL :'D Hautpsache *ein* Spiel läuft. Wir sollen uns das Leben nicht zu schwer machen!
    - NameResolver Cache: Normalerweise haben NameServices eine time to live. Wollen wir das auch machen? z.B. Counter an Requests, nach denen nochmal am Server gefragt wird.
    -> Gute Idee mit dem Counter am Request, wieviele Request sind sinnvoll?
- nochmal zum game.prepare ... :) Wir übergeben gerade den playerCount und den waitingTimer, NICHT arenaRows und arenaColumns
    - Remote: Arena wird vom Host bestimmt, waitingTimer aber vom ersten, der das Spiel eröffnet
    - Vorschlag: Vereinheitlichen. Der, der das Spiel eröffnet, bestimmt alles. Bitte abstimmen.
    -> Der Host hat Datenhohheit und bestimmt die länge des Timers, der erste Spieler startet doch nur den Timer (also Signal wird von dem gesetzt zum starten) mit der länge vom Host. Damit wäre es ja schon vereinheitlicht 
- zu den offenen Issues der Middleware schreibe ich noch ein paar Ideen an die Issues. 

### Besprochenes:
- Über calculateFairStartingPosition gesprochen, ob man die seitenpositionen rauszulassen 
    -> ist eventuell doch nicht fair, berücksichtigen das Spieler zu Spieler gleicher Abstand und das Spieler zur Wand gleicher Abstand
