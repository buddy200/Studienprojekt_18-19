# Field Manager

![Screenshot vom Startbildschrim der App](doc\images\Startscreen.png)
Screenshot vom Startbildschrim der App

Die App dient dazu Felder von Bauern zu erfassen und darin Schäden zu markieren. Dies geschieht mit einer Android App die ab Version 5.1 lauffähig ist. Sie soll Versicherungen und Bauern helfen Ihre Felder und vor allem die Schäden die in den Felder entstehen besser verwalten zu können als sie es im Moment tun.
Diese App löst vor allem das Problem das man als Versicherung/Bauer häufig nur den Schadensfall auf dem Papier hat und dies unter Umständen deutlich länger dauert ihn zu versenden und zu bearbeiten, als wenn es elektronisch erfasst ist.
Weiter ist es deutlich einfacher den Schaden wieder zu finden, weil man genau auf der Karte sieht wo der Schaden ist und welches Feld betroffen ist.


## Features


### Feature 1 Erstellen und Verwalten von Feldern

Es können Felder auf der Karte mithilfe der Standortsensoren des Handys eingezeichnet werden und weitere Daten hinzugefügt werden (z.B. Namen des Versicheurngsnehemers, Lage des Feldes) und geändert werden.

![Nachdem klicken auf den add Button](doc\images\BlankAddField.png)
Nachdem klicken auf den add Button

![Nachdem einfügen einiger Punkte](doc\images\AddedPoints.png)
Nachdem einfügen einiger Punkte

![Nachdem kompletten umrunden des Feldes. Dateneintragung](doc\images\addFileddata.png)
Nachdem kompletten umrunden des Feldes. Dateneintragung

![Fertiges Feld auf der Karte](doc\images\FieldAdded.png)
Fertiges Feld auf der Karte

### Feature 2 Erstellen und Verwalten von Schadensfällen

Es können zu einem Feld Schadensfälle hinzugefügt oder entfernt werden. Das Hinzufügen läuft nahezu identisch ab wie bei Feature 1. Allerdings können hier noch Daten wie zum Beispiel das Enstehungs Datum des Schadens eigetragen werden.

![Hinzufügen eines Schadens in einem Feld](doc\images\.png)
Hinzufügen eines Schadens in einem Feld

![Nachdem einfügen des Schadens](doc\images\.png)
Nachdem einfügen des Schadens


### Feature 3 Anzeigen aller Schadensfälle und Felder in einer Liste

Es wird ein Liste alle Schadensfälle und Felder sobald man auf den Button "list" drückt angezeigt. In dieser werden ein paar Informationen zu den Feldern und Schadensfällen angezeigt. Durch klicken auf einen Eintrag, werden alle Informationen die Verfügbar sind angezeigt und es wird zum jeweiligen Feld oder Schadensfall gezoomt. Es ist dann auch möglich den Eintrag zu bearbeiten, löschen und einen Schadensfall hinzuzufügen.

![Anzeige der Liste mit den wichtigsten Informationen](doc\images\list.png)
Anzeige der Liste mit den wichtigsten Informationen

![Detailansicht eines Feldes/Schadensfall](doc\images\Detailansicht.png)
Detailansicht eines Feldes/Schadensfall

![Ein Feld oder Schadensfall bearbeiten](doc\images\BeimBearbeite.png)
Ein Feld oder Schadensfall bearbeiten

![Nach der Bearbeitung](doc\images\NachBearbeiten.png)
Nach der Bearbeitung

### Feature 4 Durchsuchen nach Versicherungsnehmer

Ein durchsuchen der Felder und Schadensfälle nach Versicherungsnehmer ist im Suchfeld möglich. Wenn ein oder mehrere passende Felder gefunden worden sind, werden diese wie in Feature 3 beschrieben angezeigt.

![Das Suchfeld](doc\images\Search.png)
Suchfeld mit einer Eingabe

![Die gefundenen Treffer von der Suche](doc\images\Searchlist.png)
Die gefundenen Treffer von der Suche


### Feature 5 Klicken auf ein Feld/Schadenfall zeigt nähere Details an

Sobald man auf ein Polygon von einem Feld oder Schadensfall klickt, wird eine Detailansicht gezeigt. Diese enthält alle Daten des Feldes/Schadensfall und kann bei Bedarf dort direkt geändert werden oder auch gelöscht werden.

![Ansicht eines Feldes](doc\images\FieldOverview.png)
Ansicht eines Feldes

![Detailansicht des Feldes](doc\images\Detailansicht.png)
Detailansicht des Feldes


### Feature 6 Die App ist eingeschränkt offline nutzbar

Es ist möglich auch ohne Internetverbindung möglich auf dem Gerät angelegte Felder und Schadensfälle anzusehen und zu bearbeiten und hinzuzufügen. Allerdings kann eventuell keine Karte dargestellt werden wenn diese nicht bei vorheriger Benutzung vom Gerät gespeichert worden ist.

### Feature 7 Aktuellen Standort erfassen

Die App kann sobald einmal auf den "Loc" Botton geklickt worden ist den Standort erfassen und diesen dann alle zwei Sekunden aktualisieren. Um dieses Feature nutzen zu können muss die Standorterfassung des Gerätes eingeschalten sein. Außerdem kann es einige Sekunden dauern bis ein Standort gefunden worden ist. In Gebäuden oder in Gebieten mit schlechten GPS-Empfang kann die Ortung eventuell nicht erfolgen
![Anzeige des aktuellen Standorts](doc\images\currLoc.png)
Anzeige des aktuellen Standorts


## Installation

**TODO:** Beschreibung der durchzuführenden Schritte um die App zu installieren bzw. zum laufen zu bekommen.

1. Gehen sie in einen Ordner an dem sie die Daten der App laden wollen
2. Öffnen sie hier nun eine Konsole die mit Git-Befehlen arbeiten kann
3. Repository klonen: `git clone git@sopra.informatik.uni-stuttgart.de:sopra-ws1718/sopra-team-4.git`
4. geben sie nun den Benutzername und Passwort an das sie für den Zugriff bekommen haben
5. Nun schließen sie das Android Gerät an den PC und aktivieren an ihrem Handy eventuell noch die Dateiübertragung
6. Nun gehen sie in das gerade eben heruntergeladene Git-Repository und öffnen zusätzlich über den Explorer den Speicher des Smartphones und kopieren nun die .apk der App vom Repository in ihren Handyspeicher.
7. Nachdem das Kopieren abgeschlossen ist, gehen sie in die Einstellungen und dort zu Speicher & USB und wählen das Speichermedium aus auf den sie es übertragen haben. Wenn es auf den internen Speicher übertragen worden ist müssen sie nach dem auswählen noch auf "Erkunden" klicken.
8. Nun suchen sie die .apk der App und klicken darauf. Eventuell müssen sie noch in die Sicherheiteinstellung ihres Smartphone und die Einstellung für das installieren aus unbekannten Quellen erlauben.
9. Nun sollte sich ein Fenster öffnen in dem sie nur noch auf "Installieren" klicken müssen
10. Damit ist die Installation abgeschlossen

## Verwendung der App

### Wichtiger Anwendungsfall 1: Erstellung und Übersicht von Feldern
Der Bauer oder Gutachter kann Felder erstellen in dem er sie abläuft/abfährt und die passenden Daten einträgt. Dadurch hat auch der Bauer nochmal eine Übersicht was er an Feldern hat, wie groß diese sind oder was darauf angebaut wird. Diese Informationen können aber auch für die Versicherung interessant sein.

### Wichtiger Anwendungsfall 2: Erstellung von Schadensfälle durch einen Gutachter

Weiter kann ein Gutachter, der von einem geschädigten Bauer angerufen wird, über die App den Schaden schnell erfassen und alle notwendigen Daten eintragen. Dies ist genauer und schneller als wenn der Gutachter manuell die Positionsdaten des Schadens und alle anderen Daten per Papier erfassen müsste. Außerdem ist die Bearbeitung des Schadensfall einfacher, weil die Fälle sehr einfach weiter zu verschicken sind und somit auch schnell an eine Sachbearbeiter weitergleitet werden können.


### Wichtiger Anwendungsfall 3: Übersicht von Schäden für die Versicherung und Bauer
Die Versicherung bekommt somit eine gute Übersicht über die Felder mit Schadensfällen die ein Bauer hat. Weiter sieht sie alle Schadensfälle die eingetragen sind und kann diese dann abarbeiten und dem Bauer schnellstmöglich das Geld überweisen.
Aber auch der Bauer weiß somit genau wo der Schaden im Feld liegt und wie schlimm dieser ist. Außerdem bekommt er durch die einfacherer Bearbeitung seine Entschädigung schneller.



## Changelog

Die Entwicklungsgeschichte befindet sich in [CHANGELOG.md](CHANGELOG.md).

## Verwendete Bibliotheken

- osmdroid

## Lizenz

**TODO Lizenz nennen**. Genaue Bedingungen der Lizenz können in [LICENSE](LICENSE) nachgelesen werden.
