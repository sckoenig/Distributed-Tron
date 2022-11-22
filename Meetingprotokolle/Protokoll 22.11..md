## Offene Fragen
- [ ] Absturz anderer Teilnehmer -> Nicht erreichbarer Spieler, wird er aus dem Spiel entfernt, oder wird er einfach weiter gefahren (vmtl. einfacher), aber per Socket nicht mehr informiert? 
    - **Abstimmung** nicht erreichbarer Spieler fährt immer weiter geradeaus
- [ ] Countdown Anzeige? Was wollen wir anzeigen (Player? Color? ID?) & Wie? & Wer soll das modellieren?
    - **Abstimmung** Tasten und Farbe anzeigen, Wie? -> vertagen und dann nochmal besprechen. (Evtl. direkt an den Koordinaten anzeigen von der richtigen Farbe, mit Labels)
- [ ] Wie Anforderungen aufschreiben? User Stories / Issues?
    - **Abstimmung** Als Issue erstellen und dann in User Stories kopieren 
- [ ] Was genau beim Naming Service registrieren? 
    - Host und Player
- [ ] KeyEvents näher betrachen, gerade wird jeder bearbeitet ..?
    - Player hat action die immer überschrieben wird und in einem Takt abgearbeitet -> Aufgabe von der GameLoop 

## Recap von Wiederholung gerade 
- Services aufteilen? -> wir machen es mit Host und Player, schreiben das ins Arc42 -> das das dann zukünftige Skalierung schwieriger macht 
- NamingService als extra Anwendung starten oder bei uns in der Anwendung mit Flag 
    - **Abstimmung:** Frage an Martin an Donnerstag, erstmal für das Erste: einer ist NamingService mit Flag in der Config
