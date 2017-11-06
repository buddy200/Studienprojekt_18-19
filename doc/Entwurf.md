# Einführung

*Dieser Entwurf legt die prinzipielle Lösungsstruktur fest und enthält alles, was man benötigt, um einem Außenstehenden den prinzipiellen Aufbau der App erklären zu können.* (**keep it simple**)

**TODO:** Beschreibung des grundlegenden Aufbaus.

**TODO:** Verweis auf Standards wie zum Beispiel verwendete Entwurfsmuster o.ä.

# Komponentendiagramm

![Gubaer at the German language Wikipedia [GFDL (http://www.gnu.org/copyleft/fdl.html) or CC-BY-SA-3.0 (http://creativecommons.org/licenses/by-sa/3.0/)], via Wikimedia Commons](images/Komponentendiagramm.png)

Gubaer at the German language Wikipedia [GFDL (http://www.gnu.org/copyleft/fdl.html) or CC-BY-SA-3.0 (http://creativecommons.org/licenses/by-sa/3.0/)], via Wikimedia Commons.

**TODO:** Komponentendiagramm der eigenen und externen Komponenten der App erstellen.

## Database

Speichert alle Daten wie Positionsdaten der Felder, Details zu den Schadensfällen usw.

## Datenmangment

Holt die Daten aus der Database und stellt sie der Berechnung und dem User Interface zur Verfügung. Nimmt Datensätze die vom User Interface oder von der Berechnung erstelt werden und pack sie in die passende Datenstrucktur.

## Berechnungen

Realisiert jegliche Berechnungen die für die Felder und Schadensfälle nötig sind 

## User Interface



## Lokales Dateisystem


## Map


## UI Kompnenten




# Klassendiagramm

![Gubaer at the German language Wikipedia [GFDL (http://www.gnu.org/copyleft/fdl.html) or CC-BY-SA-3.0 (http://creativecommons.org/licenses/by-sa/3.0/)], via Wikimedia Commons](images/Klassendiagramm.png)

Gubaer at the German language Wikipedia [GFDL (http://www.gnu.org/copyleft/fdl.html) or CC-BY-SA-3.0 (http://creativecommons.org/licenses/by-sa/3.0/)], via Wikimedia Commons


## Beschreibung der wichtigen Klassenhierarchie 

## Beschreibung der Klasse User
Diese Klasse ist entweder vom Typ Landwirt oder vom Typ Gutachter, durch sie erfährt die App
welcher Benutzertyp aktiv ist.

## Beschreibung der Klasse Landwirt
Ein Landwirt besitzt 0..* Felder und kann als User "Landwirt" die App verwenden.

## Beschreibung der Klasse Gutachter
Die Klasse Gutachter hat 0..* Landwirte und kann ebenfalls als Nutzer die App verwenden.

## Beschreibung der Klasse 
Ein Feld besitzt einen oder keinen Schadensfall und 3..* Punkte, damit ein Polygon erstellt werden kann. 

## Beschreibung der Klasse Schadensfall
Ein Schadensfall kann 0..* Fotos besitzen und ist ein Attribut in der Klasse Feld.

## Beschreibung der Klasse Foto
Diese Klasse beinhaltet ein Foto das in einem Schadensfall vorhanden sein kann.

## Beschreibung der Klasse DataController
Der DataController kümmert sich um die interen verwendeten Daten und leitet diese weiter.

## Beschreibung der Klasse DataReader 
Diese Klasse ließt Daten aus dem internen Speicher.

## Beschreibung der Klasse DataWriter
Diese Klasse schreibt Daten auf den internen Speicher 

## Beschreibung der Klasse MainActivity
Die Haupt Activity der Android App, sie gibt die im internen Speicher hinterlegten Daten an die UI weiter.

## Beschreibung der Klasse MapFragment
Eine Teil Activity die sich um die Kontrolle eines Teils der UI kümmert und Daten weiter reicht.

## Beschreibung der Klasse BottomSheetFragment
Eine UI Komponente die über ein sogennantes Bottom Sheet eine Liste von Feldern oder eine Detailansicht eines Felder darstellen kann.

## Beschreibung der Klasse BSList
Ein BottomSheetFragment vom Typ Liste.

## Beschreibung der Klasse BSDetail
Ein BottomSheetFragment vom Typ Detail.

## Beschreibung der Klasse MapController 
Diese Klasse kontrolliert den MapView und dessen Inhalte.

## Beschreibung der Klasse MapView
Kümmert sich um die grafische Darstellung der Karte und deren Inhalte.

## Beschreibung der Klasse MenuBarFragment
Diese Klasse stellt das Menu im unteren Bildschirmteil dar und kümmert sich um Events die durch das Menu ausgelöst werden.

## Beschreibung der Klasse AddFieldActivity
Eine eigene Activity die den User neue Felder hinzufügen lässt. Sie kommuniziert mit der Klasse DataController um die hinzugefügten Daten im Speicher abzulegen.




# GUI-Skizze

![GUI-Skizze von Jan-Peter Ostberg, CC-BY-SA 4.0](sketches/GUI-Skizze.png)

GUI-Skizze von Jan-Peter Ostberg, CC-BY-SA 4.0

**TODO:** Eigene möglichst handschriftliche GUI-Skizzen erstellen und beschreiben.