# Product API - curl Beispiele

Beispielbefehle für die `/api/products`-Endpunkte von `mymerch`, lauffähig gegen
`http://localhost:8080`. Getestet mit PowerShell auf Windows.

## Voraussetzungen

- Die Anwendung muss laufen (`MymerchApplication`).
- `curl.exe` muss verfuegbar sein. In PowerShell **immer `curl.exe` statt `curl`**
  verwenden - `curl` ist dort ein Alias fuer `Invoke-WebRequest` und verhaelt sich anders.

## Wichtiger Hinweis zu JSON in PowerShell

Inline-JSON mit `-d '{"key":"value"}'` geht in PowerShell schnell kaputt, weil
Anfuehrungszeichen und Escaping anders funktionieren als in Bash. Deshalb liegen
hier fertige `.json`-Dateien, die mit `-d "@datei.json"` gesendet werden - das
ist zuverlaessig und einfach zu bearbeiten.

Alle Befehle unten werden aus diesem Ordner (`api-examples/`) ausgefuehrt.

## 1. Produkt anlegen (POST)

```powershell
curl.exe -X POST http://localhost:8080/api/products -H "Content-Type: application/json" -d "@create-product.json"
```

Zweites Beispiel:

```powershell
curl.exe -X POST http://localhost:8080/api/products -H "Content-Type: application/json" -d "@create-product-2.json"
```

## 2. Alle Produkte abrufen (GET)

```powershell
curl.exe http://localhost:8080/api/products
```

## 3. Ein Produkt nach ID abrufen (GET)

```powershell
curl.exe http://localhost:8080/api/products/1
```

(`1` durch die gewuenschte ID ersetzen)

## 4. Produkt aktualisieren (PUT)

```powershell
curl.exe -X PUT http://localhost:8080/api/products/1 -H "Content-Type: application/json" -d "@update-product.json"
```

## 5. Produkt loeschen (DELETE)

```powershell
curl.exe -X DELETE http://localhost:8080/api/products/1
```

## Fehler debuggen

Falls ein Request einen 400er oder anderen Fehler zurueckgibt, mit `-v` den
kompletten Request/Response-Verkehr anzeigen lassen:

```powershell
curl.exe -v -X POST http://localhost:8080/api/products `
  -H "Content-Type: application/json" `
  -d "@create-product.json"
```

## Pflichtfelder (Validierung)

Laut `Product`-Entity gelten folgende Regeln:

| Feld            | Pflicht | Bedingung                          |
|-----------------|---------|-------------------------------------|
| `name`          | ja      | darf nicht leer sein                |
| `price`         | ja      | muss groesser als 0 sein            |
| `stockQuantity` | ja      | darf nicht negativ sein (>= 0)      |
| `description`   | nein    | -                                    |
| `category`      | nein    | -                                    |

Ein Verstoss gegen diese Regeln fuehrt zu `400 Bad Request`.
