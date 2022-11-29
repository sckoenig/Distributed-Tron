Besprechen:
- Methodennamen angepasst: performDirectionChange() und setNextDirectionChange() in Player
- Activity Diagramme, prepare, reset, game states
- Issues
- To Dos / Plan 
- invoke ... -> IDs? Wer wie was wann?
- Receiver TCP vs UDP -> zwei unterschiedliche Workflows?!
	- TCP: Ein neuer Socket wird erstellt. Kann man einem Handlerthread übergeben, der Nachricht vom Socket liest?
	- UDP: Es entsteht KEIN neuer Socket. Der Receiver ließt X Bytes vom Socket und erstellt Datagram-Paket daraus?
	-> UDP Test durchführen?
- Wo brauchen wir Threads? ClientStub, ServerStub?
 
