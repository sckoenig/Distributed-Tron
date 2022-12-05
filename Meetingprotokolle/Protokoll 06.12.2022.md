### Anmerkungen Sandra
- Activity Diagramme: 
    - Detect Collision nach Martin feedback bisschen angepasst -> mal drüber gucken, was euch besser passt / was mehr Sinn macht.
    - Activity Diagram für Gameloop erstellt -> ok? Was ist besser lesbar, Activity oder Sequenz?
- Labels: Prio Labels an den Git Issues erstellt. -> Prios ggf. anpassen, wenn ihr etwas anders seht.
- Implementierung: Extra Milestone für Implementierung der App angelegt. Due Date kurz vor Weihnachten! Jeder mal so nebenbei Issues schnappen. Feature Branches? -> ggf. über Teststrategie sprechen
- Martin zum Thema NameServer und toter Host: 
    - EGAL :'D Hautpsache *ein* Spiel läuft. Wir sollen uns das Leben nicht zu schwer machen!
    - NameResolver Cache: Normalerweise haben NameServices eine time to live. Wollen wir das auch machen? z.B. Counter an Requests, nach denen nochmal am Server gefragt wird.
- nochmal zum game.prepare ... :) Wir übergeben gerade den playerCount und den waitingTimer, NICHT arenaRows und arenaColumns
    - Remote: Arena wird vom Host bestimmt, waitingTimer aber vom ersten, der das Spiel eröffnet
    - Vorschlag: Vereinheitlichen. Der, der das Spiel eröffnet, bestimmt alles. Bitte abstimmen.
- zu den offenen Issues der Middleware schreibe ich noch ein paar Ideen an die Issues. 

