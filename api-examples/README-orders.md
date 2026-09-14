# Order API - curl Beispiele

Beispielbefehle für die `/api/orders`-Endpunkte von `mymerch`, lauffähig gegen
`http://localhost:8080`. Getestet mit PowerShell auf Windows.

## Voraussetzungen

- Die Anwendung muss laufen (`MymerchApplication`).
- `curl.exe` muss verfuegbar sein. In PowerShell **immer `curl.exe` statt `curl`**
  verwenden - `curl` ist dort ein Alias fuer `Invoke-WebRequest` und verhaelt sich anders.
- Es muessen bereits Produkte existieren, da eine Bestellung auf vorhandene
  `productId`s verweist (siehe [README.md](README.md) fuer die Product-API).
  Die Beispiele hier gehen von den Produkten mit den IDs `1` und `2` aus.

## Wichtiger Hinweis zu JSON in PowerShell

Inline-JSON mit `-d '{"key":"value"}'` geht in PowerShell schnell kaputt, weil
Anfuehrungszeichen und Escaping anders funktionieren als in Bash. Deshalb liegen
hier fertige `.json`-Dateien, die mit `-d "@datei.json"` gesendet werden - das
ist zuverlaessig und einfach zu bearbeiten.

Alle Befehle unten werden aus diesem Ordner (`api-examples/`) ausgefuehrt.

## 1. Bestellung anlegen (POST)

```powershell
curl.exe -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" -d "@create-order.json"
```

Beispiel-Payload (`create-order.json`):

```json
{
  "customerName": "Max Mustermann",
  "customerEmail": "max.mustermann@example.com",
  "orderItems": [
    { "productId": 1, "quantity": 2 },
    { "productId": 2, "quantity": 1 }
  ]
}
```

Erwartete Antwort (`200 OK`, vereinfacht):

```json
{
  "id": 1,
  "customerName": "Max Mustermann",
  "customerEmail": "max.mustermann@example.com",
  "status": "CONFIRMED",
  "totalPrice": 49.48,
  "orderItems": [
    {
      "id": 1,
      "quantity": 2,
      "priceAtPurchase": 19.99,
      "product": { "id": 1, "name": "T-Shirt", "...": "..." }
    },
    {
      "id": 2,
      "quantity": 1,
      "priceAtPurchase": 9.50,
      "product": { "id": 2, "name": "Kaffeetasse", "...": "..." }
    }
  ],
  "createdAt": "2026-09-14T12:00:00"
}
```

Der Server berechnet `totalPrice` und `priceAtPurchase` serverseitig aus dem
aktuellen Produktpreis und reduziert den `stockQuantity` der betroffenen
Produkte entsprechend. `status` wird automatisch auf `CONFIRMED` gesetzt.

## 2. Alle Bestellungen abrufen (GET)

```powershell
curl.exe http://localhost:8080/api/orders
```

## 3. Eine Bestellung nach ID abrufen (GET)

```powershell
curl.exe http://localhost:8080/api/orders/1
```

(`1` durch die gewuenschte ID ersetzen. Existiert die ID nicht, wirft der
Server aktuell eine `RuntimeException` -> `500 Internal Server Error`.)

## 4. Fehlerfall: ungueltige Bestellung (Validierung)

```powershell
curl.exe -v -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" -d "@create-order-invalid.json"
```

Payload (`create-order-invalid.json`) verletzt mehrere Pflichtfelder:

```json
{
  "customerName": "",
  "customerEmail": "keine-email",
  "orderItems": []
}
```

Erwartet: `400 Bad Request` wegen leerem `customerName`, ungueltiger
`customerEmail` und leerer `orderItems`-Liste.

## 3. Fehlerfall: unbekannte Produkt-ID oder zu wenig Lagerbestand

Wird in `orderItems` eine `productId` verwendet, die es nicht gibt, oder
uebersteigt die bestellte `quantity` den verfuegbaren `stockQuantity`, bricht
die Bestellung ab (aktuell als `500 Internal Server Error`, da die Fehler in
`OrderService` als `RuntimeException` geworfen werden):

```powershell
curl.exe -v -X POST http://localhost:8080/api/orders -H "Content-Type: application/json" -d "@create-order.json"
```

(z. B. `create-order.json` mit einer `productId` anpassen, die nicht existiert,
oder eine sehr hohe `quantity` eintragen, um den Fehler zu provozieren)

## Fehler debuggen

Falls ein Request einen 400er oder anderen Fehler zurueckgibt, mit `-v` den
kompletten Request/Response-Verkehr anzeigen lassen:

```powershell
curl.exe -v -X POST http://localhost:8080/api/orders `
  -H "Content-Type: application/json" `
  -d "@create-order.json"
```

## Pflichtfelder (Validierung)

Laut `OrderRequest` und `OrderItemRequest` gelten folgende Regeln:

| Feld                        | Pflicht | Bedingung                              |
|-----------------------------|---------|------------------------------------------|
| `customerName`              | ja      | darf nicht leer/blank sein               |
| `customerEmail`              | ja      | darf nicht leer sein und muss eine gueltige E-Mail-Adresse sein |
| `orderItems`                | ja      | darf nicht leer sein (mind. ein Eintrag) |
| `orderItems[].productId`    | ja      | darf nicht null sein                     |
| `orderItems[].quantity`     | ja      | muss mindestens `1` sein                 |

Ein Verstoss gegen diese Regeln fuehrt zu `400 Bad Request`.
