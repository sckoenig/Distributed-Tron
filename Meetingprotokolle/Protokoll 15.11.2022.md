[[_TOC_]]

**Protokoll - 15.11.2022**

# 1. Recap verteilte Aufgaben  
- CollisionDetector -> eine Methode die private Methoden für wallCollision und headCollision
- countDown() -> einheitlich GameState COUNTDOWN
- CollisionDetector als lokale Varibale oder Instanzvariable
    - als Instanzvariable weil wir das Game immer wieder in run() als Thread starten und sonst mehrere CollisionDetector erstellen

# 2 Middleware Anpassung
- LocalGame anpassen das sich mehrere TronViews registrieren können -> brauchen das nachher für RemoteGame da mehrere TronViews gebraucht werden
    - übers Netzwerk selber callen wenn wir bei uns die View brauchen oder mit if == Datenhoheit dann nicht über Netzwerk 
    - **Abstimmung:** übers Netzwerk selber callen 
    - siehe Beispiel middleware ApplicationStub mit TronView als Beispiel
- mehrere Views registrieren -> Lösung mit register() in IGame in dem sich die ITronViews registrieren 
    - GameManager kennt nur die lokalen Views 
    - GameManager registriert seine View bei Game und sich selbst per register() -> hat zwei Listen einmal registrierte GameManager und registrierte Views 
    - (DummyObjekte in register() reinschmeißen und dann weiß der ApplicationStub) -> später klären, hat Game DummyObjekte oder eine Referenz auf mehrere TronViewCallerStubs
    - **Issue** erstellen
