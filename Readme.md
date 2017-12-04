# Field Manager

![Screenshot vom Startbildschrim der App]()
Screenshot vom Startbildschrim der App

Die App dient dazu Felder von Bauern zu erfassen und darin Schäden zu markieren. Dies geschiet mit einer Android App die ab Version 5.1 lauffähig ist. Sie soll Versicherungen und Bauern helfen Ihre Felder und vorallem die Schäden die in den Felder enstehen besser verwalten zu können als sie es im Moment tun.
Diese App löst vorallem das Problem das man als Versicherung/Bauer häufig nur den Schadensfall auf dem Papier hat und dies unter Umständen deutlich länger dauert ihn zu versenden und zu bearbeiten, als wenn es elektronisch erfasst ist.
Weiter ist es deutlich einfacher den Schaden wieder zu finden, weil man genau auf der Karte sieht wo der Schaden ist und welches Feld betroffen ist.


## Features


### Feature 1 Erstellen und Verwalten von Feldern

Es können Felder  auf der Karte mithilfe der Standortsensoren des Handys eingezeichnet werden und weitere Daten hinzugefügt werden (z.B. Namen des Versicheurngsnehemers, Lage des Feldes) und geändert werden.

![Nachdem klicken auf den add Button]()
Nachdem klicken auf den add Button

![Nachdem einfügen einiger Punkte]()
Nachdem einfügen einiger Punkte

![Nachdem kompletten umrunden des Feldes. Dateneintragung]()
Nachdem kompletten umrunden des Feldes. Dateneintragung

![Fertige Feld auf der Karte]()
Fertige Feld auf der Karte

### Feature 2 Erstellen und Veralten von Schadensfällen

Es können zu einem Feld Schadensfälle hinzugefügt oder enfernt werden. Das Hinzufügen läuft nahezu identisch ab wie bei Feature 1. Allerdings können hier noch Daten wie zum Beispiel das Enstehungs Datum des Schadens eigetragen werden.

![Hinzufügen eines Schadens in einem Feld]()
Hinzufügen eines Schadens in einem Feld

![Nachdem einfügen des Schadens]()
Nachdem einfügen des Schadens


### Feature 3 Anzeigen aller Scahdensfälle und Felder in einer Liste

Es wird ein List alle Schadensfälle und Felder sobald man auf den Button "list" drückt angezeigt. In dieser werden ein paar Informationen zu den Feldern und Schadensfällen angezeigt. Durch klicken auf einen Eintrag, werden alle Informationen die Verfügbar sind angezeigt und es wird zum jeweiligen Feld gezoomt. Es ist dann auch möglich den Eintrag zu berabeiten und einen Schadensfall hinzuzufügen.

![Anzeige der Liste mit den wichtigsten Informationen]()
Anzeige der Liste mit den wichtigsten Informationen

![Detailansicht einses FEldes/Schadensfall]()
Detailansicht einses FEldes/Schadensfall

![Ein Feld oder Schadensfall bearbeiten]()
Ein Feld oder Schadensfall bearbeiten


### Feature 4 Durchsuchen nach Versicherungsnehmer

Ein durchsuch der Felder und Schadensfälle nach Verischerungsnehmer ist im Suchfeld möglich. Wenn ein oder mehrer passende Felder gefunden worden sind, werden diese wie in Feature 3 beschrieben angezeigt.

![Das Suchfeld]()
Das Suchfeld

![Die gefundenen Treffer von der Suche]()
Die gefundenen Treffer von der Suche


### Feature 5 Klicken auf ein Feld/Schadenfall zeigt nähere Details an

Sobald man auf ein Polygon von einem Feld oder Schadensfall klickt, wird eine Detailansicht gezeigt. Diese enthält alle Daten des Feldes/Schadensfall und kann bei Bedarf dort direkt geändert werden.

![Ansicht eines Feldes]()
Ansicht eines Feldes

![Detailansicht des Feldes]()
Detailansicht des Feldes


### Feature 6 Die App ist eingeschränkt offline nutzbar

Es ist möglich auch ohne Internetverbindung möglich auf dem Gerät angelegte Felder und Schadensfälle anzusehen zu berabeiten und hinzuzufügen. Allerdings kann eventuell keine Karte dargestellt werden wenn diese nicht bei vorheriger Benutzung vom Gerät gespeichert worden ist.

### Feature 7 Aktuellen Standort erfassen

Die App kann sobald einmal auf den "Loc" Botton geklickt worden ist den Standort erfassen und diesen dann alle zwei Sekunden aktualisieren.
![Anzeige des akutellen Standorts]()
Anzeige des akutellen Standorts


## Installation

**TODO:** Beschreibung der durchzuführenden Schritte um die App zu installieren bzw. zum laufen zu bekommen.

1. Gehen sie in einen Ordner an dem sie die Daten der App laden wollen
2. Öffnen sie hier nun eine Konsole die mit Git-Befehlen arbeiten kann
3. Repository klonen: `git clone git@sopra.informatik.uni-stuttgart.de:sopra-ws1718/sopra-team-4.git`
4. geben sie nun den Benutzername und Passwort an das sie für den Zugriff bekommen haben
5. Nun schließen sie das Andorid Gerät an den PC und aktivieren an ihrem Handy eventeull noch die Dateiübertragung
6. Nun gehen sie in das gerade eben heruntergeladene Git-Repository und öffnen zusätzlich über den Explorer den Speicher des Smartphones und kopieren nun die .apk der App vom Repository an ihren Handyspeicher.
7. Nachdem das kopieren abgeschlossen ist, gehen sie in die Einstellungen und dort zu Speicher & USB und wählen das Speichermedium aus auf den sie es übertragen haben. Wenn es auf den internen Speicher übertragen worden ist müssen sie nach dem auswählen noch auf "Erkunden" klicken.
8. Nun suchen sie die .apk der App und klicken darauf. Eventuell müssen sie noch in die Sicheheitseinstellung ihres Smartphone und die Einstellung für das installieren aus unbekannten Quellen erlauben.
9. Nun sollte sich ein Fenster öffnen in dem sie nur noch auf "Installieren" klicken müssen
10. Somit ist die Installation abgeschlossen

## Verwendung der App

### Wichtiger Anwendungsfall 1: Erstellung und Übersicht von Feldern
Der Bauer oder Gutachter kann Felder erstellen in dem er sie abläuft/abfährt und die passeden Daten einträgt. Dadruch hat auch der Bauer nochmal eine Übersicht was er an Feldern hat, wie Groß diese sein oder was darauf angebaut wird. Diese Informationen können aber auch für die Versicherung interesant sein.

### Wichtiger Anwendungsfall 2: Erstellung von Schadensfälle durch einen Gutachter

Weiter kann ein Gutachter von einem geschädgiten Bauer angerufen wird, kann dieser über die App den Schaden schnell erfassen und alle notwendigen Daten eintragen. Dies ist genauer und schneller als wenn der Gutachter manuell die Positionsdaten des Schadens und alle anderen Daten per Papier erfassen müsste. Außerdem ist die Bearbeitung des Schadensfall einfacher, weil die Fälle sehr einfach weiterverschickbar sind und somit auch schnell an eine Sachbearbeiter weitergleitet werden können.


### Wichtiger Anwendungsfall 3: Übersicht von Schäden für die Versicherung und Bauer
Die Versicherung bekommt somit eine gute Übericht über die Felder die ein Bauer hat. Weiter sieht sie alle Schadensfälle die eingetragen sind und kann diese dann abarbeiten und dem Bauer schnellstmöglich das Geld überweisen.
Aber auch der Bauer weiß somit genau wo der Schaden im Feld liegt und wie schlimm dieser ist. Außerdem bekommt er durch die einfacherere Bearbeitung seine Entschädigung schneller.



## Changelog

Die Entwicklungsgeschichte befindet sich in [CHANGELOG.md](CHANGELOG.md).

## Verwendete Bibliotheken

- osmdroid

## Lizenz

**TODO Lizenz nennen**. Genaue Bedingungen der Lizenz können in [LICENSE](LICENSE) nachgelesen werden.