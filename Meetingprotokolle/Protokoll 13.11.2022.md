[[_TOC_]]

**Protokoll - 13.11.2022**

# 1. Organisation 
- wir nehmen java 17 ist die neuste longterm supported Version -> zwar läuft auf den Rechnern java 1.7 ist aber nicht mehr im Support

# 2. Recap Donnerstag 
- Diskussion über Rest API und RPC
- 2. Meilenstein erstmal RPC (Middleware)
- was wir Donnerstag gelernt haben

# Fragen zur Middleware
## Fragen die gestellt werden sollen an Martin
- woher weiß der Masternode wen er informieren soll, darf Masternode subscriber (ClientStubs -> zu Serverstub, ClientStub hat Liste mit Serverstubs) sich merken?
- Stateless im zusammenhang mit Middleware, können Clientstubs Serverstubs gemerkt werden?
- wie kennen sich die Middlewares untereinander?

## Verständnisfragen 
- Namingdienst ist das wie RN?, mit Nachrichten  1 -> hello --> ist NamingService hat listensocket mit hashtabelle, andere können sich registrieren. Findet dort wer mit wem spielen kann. Andere Variante Broker. Geht auch mit Multicast, würfeln aus wer der Master ist?
- woher wissen die anderen Middlewares wo der andere Namingservice ist -> kann man statisch angeben. --> Ende vom Video von Wiederholung am Dienstag
- bei RPC asynchron/synchron -> asynchron beide Seiten benötigen eine Queue --> brauchen keine Queue meine MainThread macht einfach weiter brauchen einen Handler 
- parameter passing -> beinhaltet marshalling/unmarshalling  

# Abstimmung
- NamingService oder Multicast 
    - für NamingService -> nicht hundert Prozent statisch aber auch nicht zu kompliziert 
    - 1. Abstimmung: NamingService abwarten auf Donnerstag 
- Countdown nicht im GameManager sondern das Game zählt runter und informiert GameManager, braucht einen extra State START; Issue: App - Move Countdown to Game
    - jeder Tick des CountDowns soll über das Netz geschickt werden 3,2,1
    - Abstimmung: machen wir so
- ApplcationStub designen -> schneiden zwischen GameManager (auf einem Node) und Game -> wollen wir noch mehr schneiden? 
    - bei mehr als einmal schneiden, wird Netzwerkverkehr erhöht Nachteil
    - Abstimmung: nur einmal schneiden -> ein Node hat alle Spieler

# Überlegung
- was machen wir bei verschiedenen Configs
    - a. es wird ausgehandelt
    - b. master zwingt die Config auf 
    - c. nur Spieler mit gleicher Config dürfen zusammenspielen 
    - Abstimmung: Aufzwängen auf jeden Fall wie das Spiel aussieht und welche Anzahl, wenn zwei Spiele die gleiche Steuerung haben dann Player Id dann mitschicken 
- wann weiß die Middlewar wann sie keine Spieler mehr auufnimmt 
    - applicationStub flagt sein dienst wenn spieler schon voll sind, NamingService wird geflagt oder wird rausgenommen 
