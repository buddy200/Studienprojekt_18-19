# Product Backlog
 
Hier werden **alle** Anforderungen in Form von **User Stories** geordnet aufgelistet.
 
## Epic 1 Critical Features
 
Alle unbedingt notwendigen Anforderungen werden hier behandelt.
Die visuelle Darstellung der Schadensfälle muss in der App gewährleistet sein, um dem User bereits gekennzeichnete Felder zu zeigen und um direktes Feedback zu liefern. Dies wird durch die Implementierung einer Karte in der App und in ihr enthaltenen graphischen Objekten erreicht.
Desweiteren bekommt der jeweilige Nutzer die Möglichkeit Schadensfälle zu erstellen, bearbeiten und löschen.
 
 
### Feature 1.1 *Visuelle Darstellung der Schadensfälle*
 
> Als Benutzer möchte ich, dass Schadensfälle und ihre Form auf einer Karte dargestellt werden.
 
- Aufwandsschätzung: L
- Akzeptanztests:
    - [ ] Schadensfälle können in einer Kartenansicht dargestellt werden.
    - [ ] Die Kartenansicht des Schadens zeigt Polygone der versicherten Objekte.
    - [ ] Die Kartenansicht des Schadens zeigt den Schaden als Polygon/Fläche innerhalb der versicherten Objekte.
    - [ ] Die Kartenansicht inkl. der Schadensdarstellung ermöglicht mehrere Maßstäbe.
    - [ ] Die Ansicht der Polygone ist ohne Internetverbindung möglich.
 
#### Implementable Story 1.1.1 *Karten Darstellung*
> Als User möchte Ich eine interaktive Karte meiner Umgebung sehen.
 
- Aufwandsschätzung: 25 SP
- Akzeptanztests:
    - [ ] Es wird eine Karte dargestellt
    - [ ] Die Kartenansicht ermöglicht mehrere Maßstäbe
    - [ ] Die Ansicht der Karte lässt sich verschieben
 
##### Task 1.1.1.1 Darstellung einer Karte
 
- Aufwandsschätzung: 5 Stunden
 
##### Task 1.1.1.2 Die Kartenansicht lässt sich verschieben
 
- Aufwandsschätzung: 1 Stunde
 
##### Task 1.1.1.3 Es sind mehrere Maßstäbe möglich
 
- Aufwandsschätzung: 3 Stunden
 
#### Implementable Story 1.1.2 *Darstellung der versicherten Objekte*
> Als Benutzer möchte ich eine visuelle Darstellung der versicherten Objekte.
 
- Aufwandsschätzung: 75 SP
- Akzeptanztests:
    - [ ] Die Kartenansicht des Schadens zeig Polygone der versicherten Objekte.
    - [ ] Die Kartenansicht des Schadens zeigt den Schaden als Polygon/Fläche innerhalb der versicherten Objekte.
 
##### Task 1.1.2.1 *Darstellung der versicherten Objekte durch Polygone*
 
- Aufwandsschätzung: 4 Stunden
 
##### Task 1.1.2.2 *Berechnung der Fläche der Polygone*
 
- Aufwandsschätzung: 5 Stunden
 
##### Task 1.1.2.3 *Fläche der versicherten Objekte stellt Schaden dar*
 
- Aufwandsschätzung: 2 Stunden
 
#### Implementable Story 1.1.3 *Ansicht der Polygone ist ohne Internetverbindung möglich*
 
> Als Benutzer möchte ich meine versicherten Objekte auf der Karte auch ohne Internetverbindung ansehen können.
 
- Aufwandsschätzung: 40 SP
- Akzeptanztests:
    - [ ] Die Ansicht der Polygone ist ohne Internetverbindung möglich.
    - [ ] Die Ansicht der Karte der nähren Umgebung ist ohne Internetverbindung möglich
 
##### Task 1.1.3.1 *Offline Speicherung der Polygone*
 
- Aufwandsschätzung: 5 Stunden
 
 
##### Task 1.1.3.2 *Offline Speicherung der Karte*
 
- Aufwandsschätzung: 5 Stunden
 
### Feature 1.2 *Verwaltung von Feldern*
> Als Gutachter möchte ich Schadensfälle und Felder verwalten (erfassen/bearbeiten/löschen) können.
 
- Aufwandsschätzung: L
- Akzeptanztests:
    - [ ] Schadensfälle können mit der Angabe des Versicherungsobjekts (Name des Versicherungsnehmers, Fläche und Koordinaten des Objekts, Region (mind. Landkreis)), Schadensinformationen (Schadensfläche, Schadensposition, Schadens-Koordinaten/-Polygon, Datum) und Name des Gutachters erfasst werden.
    - [ ] Die Erfassung von Schadensfällen/-Koordinaten verwendet tatsächliche Sensorwerte eines Positionssensors im Gerät.
    - [ ] Schadensfälle sind nach dem vollständigen Schließen der App und Starten der App wieder im gleichen Zustand verfügbar.
    - [ ] Schadensfälle können während des Erfassens in der Kartenansicht (*siehe Visuelle Darstellung der Schadensfälle*) dargestellt werden.
    - [ ] Schadensfälle können während des Bearbeitung in der Kartenansicht (*siehe Visuelle Darstellung der Schadensfälle*) dargestellt werden.
    - [ ] Schadensfälle können nach Name des Versicherungsnehmers gesucht werden.
    - [ ] Schadensfälle können gelöscht werden.
    - [ ] Schadensfälle können bearbeitet werden.
    - [ ] Die Verwaltung ist ohne Internetverbindung möglich.
 
#### Implementable Story 1.2.1 *Neue Felder können angelegt werden*
 
> Als Gutachter möchte ich Felder erstellen die Angaben des Versicherungsobjekts, Schadensinformationen und meinen eigenen Namen enthalten.
 
- Aufwandsschätzung: 60 SP
- Akzeptanztests:
    - [ ] Neue Felder können angelegt werden
    - [ ] Sie enthalten Angaben des Versicherungsobjekts und den Namen des Gutachters
    - [ ] Es können weitere Infomationen zu eventuell vorhandenen Schäden hinzugefügt werden
    - [ ] Die Form und Größe des Felds kann über interne Sensordaten festgelegt werden
 
##### Task 1.2.1.1 *Erstellen der Felder*
 
- Aufwandsschätzung: 4 Stunden
 
##### Task 1.2.1.2 *Hinzufügen der Angaben des Versicherungsobjekts und des Gutachternamens*
 
- Aufwandsschätzung: 2 Stunden
 
##### Task 1.2.1.3 *Hinzufügen der Schadeninsinformationen*
 
- Aufwandsschätzung 3 Stunden
 
##### Task 1.2.1.4 *Erfassung der Koordinaten durch interne Sensoren*
 
- Aufwandsschätzung 3 Stunden
 
#### Implementable Story 1.2.2 *Speichern der Schadensfälle*
 
> Als Gutachter möchte ich zuvor hinzugefügte Schadensfälle auch nach einem Neustart und ohne Internetverbindung prüfen können.
 
- Aufwandsschätzung: 50 SP
- Akzeptanztests:
    - [ ] Felder mit ihren Schäden können in einer Datei gespeichert werden
    - [ ] Diese Datei kann nach einem Neustart geladen werden
    - [ ] Die Verwaltung ist ohne Internetverbindung möglich.
 
##### Task 1.2.2.1 *Speicherung der Schadensfälle*
 
- Aufwandsschätzung: 3 Stunden
 
##### Task 1.2.2.2 *Offline Verfügbarkeit der Schadensfälle*
 
- Aufwandsschätzung: 1 Stunde
 
##### Task 1.2.2.3 *Laden der Schadensfälle*
 
- Aufwandsschätzung: 3 Stunden
 
#### Implementable Story 1.2.3 *Ansicht der Schadensfälle*
> Als Gutachter möchte ich Schadensfälle in einer Kartenansicht einsehen, auch während der Bearbeitung dieser.
 
- Aufwandsschätzung: 65 SP
- Akzeptanztests:
    - [ ] Während der Festlegung der Feldgröße und Form können andere Felder und ihre Schadensfälle angesehen werden
    - [ ] Während der Bearbeitung des Felderdaten kann man das zu bearbeitende Feld sehen
 
##### Task 1.2.3.1 *Die Kartenfunkionen sind nicht eingeschränkt während des Hinzufügens eines neuen Feldes*
 
- Aufwandsschätzung: 5 Stunden
 
##### Task 1.2.3.2 *Eine Ansicht des Feldes ist während der Bearbeitung vorhanden*
 
- Aufwandsschätzung: 4 Stunden
 
#### Implementable Story 1.2.4 *Schadensfälle können gesucht werden*
> Als Gutachter möchte ich nach allen Schadensfällen eines Versicherungsnehmers suchen können.
 
- Aufwandsschätzung: 60 SP
- Akzeptanztests:
    - [ ] Schadensfälle können nach Name des Versicherungsnehmers gesucht werden.
 
##### Task 1.2.4.1 *Suche nach Schadensfällen nach Namen des Versicherungsnehmers*
 
- Aufwandsschätzung: 4 Stunden
 
#### Implementable Story 1.2.5 *Schadensfälle können bearbeitet und gelöscht werden*
> Als Nutzer möchte ich vorhandene Schadensfälle löschen oder bearbeiten können.
 
- Aufwandsschätzung 30 SP
- Akteptanztests:
    - [ ] Schadensfälle können gelöscht werden
    - [ ] Schadensfälle können bearbeitet werden
    - [ ] Vor dem Löschen wird nochmals nachgefragt ob sich der Nutzer sicher ist
 
##### Task 1.2.5.1 *Felder können vom internen Speicher gelöscht werden*
 
- Aufwandsschätzung: 1 Stunde
 
##### Task 1.2.5.2 *Dialogbox ob wirklich gelöscht werden soll*
 
- Aufwandsschätzung: 1 Stunde
 
##### Task 1.2.5.3 *Vorhandene Felder können bearbeitet werden*
 
- Aufwandsschätzung: 3 Stunden
 
## Epic 2 Additional Features
Hier werden alle Features aufgeführt die zusätzlich zu den gegebenen Anforderungen implementiert werden sollen.
Es werden eventuell nicht alle Features die hier aufgeführt werden später implementiert werden.

### Feature 2.1 Felder suchen/sortieren

> Als Benutzer möchte ich nach einzelnen Feldern suchen bzw. mir meine Felder geordnet ansehen.

- Aufwandsschätzung: M
- Akzeptanztests:
    - [ ] Felder können durchsucht werden
	- [ ] Felder können nach verschiedenen Kriterien sortiert werden
	- [ ] Das Suchen und Sortieren der Felder funktioniert ohne Internetverbindung

#### Implementable Story 2.1.1 Suche nach Feldern
> Als Benutzer möchte ich nach einzelnen Feldern suchen.

- Aufwandsschätzung: 50 SP
- Akzeptanztests:
    - [ ] Nach einzelnen Feldern kann gesucht werden
	- [ ] Bei einer nicht erfolgreichen Suche wird der Benutzer darüber informiert

##### Task 2.1.1.1 Suche nach Feldern

- Aufwandsschätzung: 4 Stunden

##### Task 2.1.1.2 Information der Benutzer über eine nicht erfolgreiche Suche

- Aufwandsschätzung: 2 Stunden

#### Implementable Story 2.1.2 Sortieren von Feldern
> Als Benutzer möchte ich mir meine Felder sortiert anzeigen lassen.

- Aufwandsschätzung: 70 SP
- Akzeptanztests:
    - [ ] Felder können nach unterschiedlichen Kriterien sortiert werden
	- [ ] Sortieren sowohl aufwärts als auch abwärts möglich
	- [ ] Benutzer wird darüber informiert, wenn es keine Felder gibt, die seinen Kriterien entsprechen

##### Task 2.1.2.1 Sortierung der Felder nach Kriterien

- Aufwandsschätzung: 6 Stunden

##### Task 2.1.2.2 Sortierung in beide Richtungen

- Aufwandsschätzung: 2 Stunden

##### Task 2.1.2.3 Information des Benutzer über unpassende Kriterien

- Aufwandsschätzung: 2 Stunden

### Feature 2.2 Fotos zu Schadensfällen hinzufügen

> Als Benutzer möchte ich Fotos zu Schadensfällen hinzufügen, um den vorhandenen Schaden zu dokumentieren.

- Aufwandsschätzung: M
- Akzeptanztests:
    - [ ] Einem Schadensfall können Fotos hinzugefügt werden
	- [ ] Die Fotos werden beim Bearbeiten des Schadensfalls angezeigt
	- [ ] Die Fotos werden abgespeichert werden dem Schadensfall zugeordnet

#### Implementable Story 2.2.1 Fotos zu Schadensfall hinzufügen 
> Als Benutzer möchte ich Fotos zu Schadensfällen hinzufügen, um den vorhandenen Schaden zu dokumentieren.

- Aufwandsschätzung: 50 SP
- Akzeptanztests:
    - [ ] Ein Foto kann einem Schadensfall hinzugefügt werden
	- [ ] Ein Schadensfall kann mit mehreren Fotos dokumentiert werden
	- [ ] Die Fotos werden lokal zwischengespeichert

##### Task 2.2.1.1 Foto aus der App machen

- Aufwandsschätzung: 4 Stunden

##### Task 2.2.1.2 Fotos einem Schadensfall zuordnen

- Aufwandsschätzung: 2 Stunden

##### Task 2.2.1.3 Zwischenspeichern der Fotos

- Aufwandsschätzung: 2 Stunden

#### Implementable Story 2.2.2 Fotos beim Bearbeiten eines Schadensfalls anzeigen
> Als Benutzer möchte ich mir die Fotos beim Bearbeiten eines Schadensfalls anzeigen lassen.

- Aufwandsschätzung: 60 Story Points
- Akzeptanztests:
    - [ ] Beim Bearbeiten eines Schadensfalls werden die dazugehörigen Fotos angezeigt

##### Task 2.2.2.1 Einlesen der Fotos

- Aufwandsschätzung: 2 Stunden

##### Task 2.2.2.2 Anzeige der Fotos
- Aufwandsschätzung: 6 Stunden

### Feature 2.3 Eigenes Datenmodell

> Als Nutzer möchte ich ein eigenes Datenmodell verwenden, um dessen Vorteile zu nutzen.

- Aufwandsschätzung: L
- Akzeptanztests:
    - [ ] Eigenes Datenmodel vorhanden
	- [ ] Modell ist les- und schreibbar

#### Implementable Story 2.3.1 Eigenes Datenmodell
> Als Nutzer möchte ich, dass die App ein eigenes Datenmodell verwendet.

- Aufwandsschätzung: 80 SP
- Akzeptanztests:
    - [ ] Eigenes Datenmodell verwendet

##### Task 2.3.1.1 Datenmodell konzipieren

- Aufwandsschätzung: 8 Stunden

##### Task 2.3.1.2 Datenmodell implementieren 

- Aufwandsschätzung: 4 Stunden

#### Implementable Story 2.3.2 Das Datenmodell ist einlesbar und schreibbar
> Als Nutzer möchte ich, dass das Datenmodell einlesbar und schreibbar ist.

- Aufwandsschätzung: 60 Story Points
- Akzeptanztests:    
	- [ ] Das Datenmodell ist schreibbar
	- [ ] Das Datenmodell ist einlesbar

##### Task 2.3.2.1 Datenmodell scheibbar

- Aufwandsschätzung: 4 Stunden

##### Task 2.3.2.2 Datenmodell einlesbar
- Aufwandsschätzung: 4 Stunden

### Feature 2.4 *Export und Import von Felderdaten*
> Als Benutzer möchte ich Daten Importieren und Exportieren können
 
- Aufwandsschätzung: M
- Akzeptanztests:
    - [ ] Speicherung im internen Speicher ist möglich
    - [ ] Export in Drittapplikationen
    - [ ] Import von Drittapplikationen
    - [ ] Import aus internem Speicher
 
#### Implementable Story 2.4.1
> Als Nutzer möchte ich die erstellten Felder mit ihren Daten und Schadensfällen versenden
 
- Aufwandsschätzung 40 SP
- Akzeptanztests:
    - [ ] Versenden per E-Mail möglich
    - [ ] Speicherung der Daten außerhalb der App
    - [ ] Versenden der Daten an andere Drittapplikationen beispielsweise Dropbox
 
##### Task 2.4.1.1 *Exportierung der Daten*
 
- Aufwandsschätzung: 2 Stunden
 
##### Task 2.4.1.2 *Versenden per E-Mail möglich*
 
- Aufwandsschätzung: 4 Stunden
 
##### Task 2.4.1.3 *Versenden der Daten an Drittapplikationen*
 
- Aufwandsschätzung 5 Stunden
 
#### Implementable Story 2.4.2
> Als Nutzer möchte ich externe Daten der Felder und deren Schadensfällen importieren
 
- Aufwandsschätzung: 35 SP
- Akzeptanztests:
    - [ ] Importieren der Daten aus einer E-Mail Applikation (bspw. Gmail)
    - [ ] Importieren der Daten aus einer Cloud Applikation (bspw. Dropbox)
    - [ ] Importieren der Daten vom internen Speicher
 
##### Task 2.4.2.1 *Importieren von internem Speicher*
 
- Aufwandsschätzung: 3 Stunden
 
##### Task 2.4.2.2 *Importieren aus einer Cloud Applikation*
 
- Aufwandsschätzung: 2 Stunden
 
##### Task 2.4.2.3 *Importieren aus einer E-Mail Applikation*
 
- Aufwandsschätzung: 2 Stundens
 
### Feature 2.5 *User Verwaltung*
> Als Nutzer möchte ich mich als Verwalter oder Versicherungsnehmer registrieren können
 
- Aufwandsschätzung: L
- Akzeptanztests:
    - [ ] Es existiert eine Login Seite mit Login Daten
    - [ ] Der Login unterscheidet Verwalter und Versicherungsnehmer
    - [ ] Verwalter und Versicherungsnehmer haben unterschiedliche Funktionen zur Verfügung
    - [ ] Es kann zwischen verschiedenen Verwaltern und Versicherungsnehmern unterschieden werden
    - [ ] Der Nutzer muss sich nur einmal einloggen, und behält seine Login Daten nach einem Neustart
 
#### Implementable Story 2.5.1 *Login Seite und Identifizierung*
> Als Nutzer möchte ich in meiner jeweiligen Rolle identifiziert werden.
 
- Aufwandsschätzung: 60 SP
- Akzeptanztests:
    - [ ] Es existiert eine Login Seite beim ersten öffnen der App
    - [ ] Mithilfe der eingegebenen Daten kann der Nutzer seiner Rolle zugeordnet werden
 
##### Task 2.5.1.1 *Login Seite*
 
- Aufwandsschätzung: 5 Stunden
 
##### Task 2.5.1.2 *Identifizierung*
 
- Aufwandsschätzung: 3 Stunden
 
#### Implementable Story 2.5.2 *Unterscheidung zwischen Verwalter und Versicherungsnehmer*
> Als Nutzer möchte ich Features nutzen die ich benötige.
 
- Aufwandsschätzung: 35 SP
- Akzeptanztests:
    - [ ] Versicherungsnehmer darf nur Felder hinzufügen
    - [ ] Verwalter hat Zugriff auf alle Funktionen
 
##### Task 2.5.2.1 *Versicherungsnehmer darf nur Felder hinzufügen*
 
- Aufwandsschätzung: 2 Stunden
 
##### Task 2.5.2.2 *Verwalter hat Zugriff auf alle Funtkionen*
 
- Aufwandsschätzung: 1 Stunde

#### Implementable Story 2.5.3 *Einmaliges Einloggen*
> Als Nutzer möchte ich mich nur einmalig einloggen müssen.
 
- Aufwandsschätzung: 50 SP
- Akzeptanztests:
    - [ ] Der Login muss einmalig vollzogen sein
    - [ ] Nach dem ersten Login muss die App automatisch den alten Login wieder herstellen können
 
##### Task 2.5.3.1 *Die Login Anzeige darf nur einmalig erscheinen*
 
- Aufwandsschätzung: 1 Stunde
 
##### Task 2.5.3.2 *Wiederherstellen des Logins*
 
- Aufwandsschätzung: 5 Stunden
 
### Feature 2.6 *Status der Schadensfälle*
> Als Nutzer möchte ich den Aktuellen Status vorhandener Schadensfälle sehen und editieren können
 
- Aufwandsschätzung: M
- Akzeptanztests:
    - [ ] Der aktuelle Status muss dargestellt werden
    - [ ] Der Status muss für Verwalter veränderbar sein
    - [ ] Für Felder ohne Schadensfall ist kein Status vorhanden
 
#### Implementable Story 2.6.1 *Darstellung des Status*
> Als Nutzer möchte ich den aktuellen Status über meinen Schadensfall einsehen können
 
- Aufwandsschätzung: 40 SP
- Akzeptanztests:
    - [ ] Mehrere verschiedene Status für Schadensfälle sind möglich
    - [ ] Es ist kein Status möglich
 
##### Task 2.6.1.1 *Verschiedene Status*
 
- Aufwandsschätzung: 1 Stunde
 
##### Task 2.6.1.2 *Kein Status ist möglich*
 
- Aufwandsschätzung: 1 Stunde
 
#### Implementable Story 2.6.2 *Editierbarer Status*
> Als Verwalter möchte ich den Status aktualisieren können
 
- Aufwandsschätzung: 45 SP
- Aktzeptanztests:
    - [ ] Es soll ein Status hinzugefügt werden können
    - [ ] Dieser soll veränderbar sein
 
##### Task 2.6.2.1 *Hinzufügen eines Status*
 
- Aufwandsschätzung: 1 Stunde
 
##### Task 2.6.2.2 *Editieren des Status*
 
- Aufwandsschätzung: 3 Stunden

### Feature 2.7 *Unterstützung von verschiedener Sprachen*
> Ich möchte als Nutzer gerne in meiner bervorzugten Sprache die App nutzen

- Aufwandsschätzung: S
- Akzeptanztests:
    - [ ] Englisch wird unterstützt
    - [ ] Deutsch wird unterstützte
    - [ ] Der Code enthält keine Text-Literale

#### Implementable Story 2.7.1 *Sprachunabhängige GUI*

> Als Nutzer will ich eine komplett auf meine Sprache angepasste GUI

- Aufwandsschätzung: 30 SP
- Aktzeptanztests:
	- [ ] Buttonbeschriftung sind in korrekter Sprache
	- [ ] Pop-Up Meldungen sind in korrekter Sprache
	- [ ] Texte sind in korrekter Sprache

##### Task 2.7.1.1 *Alle GUI Elemente sind verallgemeinert*
- Aufwandsschätzung: 4 Stunden

#### Implementable Story 2.7.2 *Deutsche und Englische GUI*
- Aufwandsschätzung: 25 SP
- Aktzeptanztests:
	- [ ] Alle Texte, Buttons, und sonstige Elemente die Schrift enthalten sind auf Deutsch
	- [ ] Alle Texte, Buttons, und sonstige Elemente die Schrift enthalten sind auf Englisch

##### Task 2.7.2.1 *Deutsche Oberfläche*
- Aufwandsschätzung: 2 Stunden

##### Task 2.7.2.2 *Englische Oberfläche*
- Aufwandsschätzung: 2,5 Stunden

### Feature 2.8 *Navigation zu den Feldern via Google Maps*
> Als Nutzer will ich zu den Feldern die z.B einen Schaden haben navigieren können um die Felder finden. Gerade wenn ich die Felder/Schaden nicht selbst erstellt habe.

- Aufwandsschätzung: S
- Akzeptanztests:
    - [ ] Route zu Feld kann berechnet werden
    - [ ] Route zu Schaden kann berechent werden

##### Task 2.8.1 *Schnittstelle zwischen unserer App und Google Maps*
- Aufwandsschätzung: 5 Stunden


### Feature 2.9 *Einzeichnen von Eckpunkten*
> Als Nutzer möchte ich nur Eckpunkte einzeichnen oder ablaufen und dann die Feldgröße berechnen

- Aufwandsschätzung: M
- Akzeptanztests:
    - [ ] Punkte können manuell auf der Karte gesetzt werden
    - [ ] Einzlene Punkte können per GPS hinzugefügt werden
    - [ ] Fläche soll aus den Punkten berechnet werden (mit bestehenden Algorithmus)

#### Implementable Story 2.9.1 *einzelne Punkte erfassen*
>Ich will nicht immer das ganze Feld ablaufen sondern nur die Eckpunkte eintragen

- Aufwandsschätzung: 40 SP
- Aktzeptanztests:
	- [ ] Eckpunkte müssen über das GPS bestimmt sein
	- [ ] Eckpunkte können manuell hinzugefügt werden können
	- [ ] Eckpunkte können sowohl händisch als auch per GPS eingetragen werden (Von einem Feld)

##### Task 2.9.1.1 *Punkte mit GPS einzeichnen*

- Aufwandsschätzung: 5 Stunden

##### Task 2.9.1.2 *Punkte manuell einzeichnen*

- Aufwandsschätzung: 3,5 Stunden