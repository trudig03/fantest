# Fan API Documentation

Bu dokümantasyon, Fan uygulamasının public API endpointlerini açıklamaktadır.

## Base URL
```
http://localhost:8080/api/fan
```

## Endpoints

### 1. Ülkeleri Listele
Tüm ülkelerin listesini getirir.

```http
GET /countries
```

#### Yanıt
```json
[
  {
    "id": 1,
    "name": "Türkiye",
    "flag": "https://example.com/flags/turkey.png"
  }
]
```

### 2. Ülkeye Göre Takımları Listele
Belirli bir ülkeye ait takımları getirir.

```http
GET /countries/{countryId}/teams
```

#### Parametreler
| Parametre | Tip | Açıklama |
|-----------|-----|-----------|
| countryId | Long | Ülke ID'si |

#### Yanıt
```json
[
  {
    "id": 1,
    "name": "Fenerbahçe",
    "logo": "https://example.com/images/fenerbahce-logo.png",
    "description": "Fenerbahçe Spor Kulübü",
    "countryId": 1,
    "countryName": "Türkiye"
  }
]
```

### 3. Takıma Göre Maçları Listele
Belirli bir takımın maçlarını getirir.

```http
GET /teams/{teamId}/matches
```

#### Parametreler
| Parametre | Tip | Açıklama |
|-----------|-----|-----------|
| teamId | Long | Takım ID'si |

#### Yanıt
```json
[
  {
    "id": 1,
    "homeTeam": {
      "id": 1,
      "name": "Fenerbahçe"
    },
    "awayTeam": {
      "id": 2,
      "name": "Galatasaray"
    },
    "date": "2025-05-15T19:00:00",
    "status": "SCHEDULED",
    "score": {
      "homeTeam": 0,
      "awayTeam": 0
    }
  }
]
```

### 4. Tüm Maçları Listele
Sistemdeki tüm maçları getirir.

```http
GET /matches
```

#### Yanıt
```json
[
  {
    "id": 1,
    "homeTeam": {
      "id": 1,
      "name": "Fenerbahçe"
    },
    "awayTeam": {
      "id": 2,
      "name": "Galatasaray"
    },
    "date": "2025-05-15T19:00:00",
    "status": "SCHEDULED",
    "score": {
      "homeTeam": 0,
      "awayTeam": 0
    }
  }
]
```

### 5. Maç Detayı Getir
Belirli bir maçın detaylarını getirir.

```http
GET /matches/{matchId}
```

#### Parametreler
| Parametre | Tip | Açıklama |
|-----------|-----|-----------|
| matchId | Long | Maç ID'si |

#### Yanıt
```json
{
  "id": 1,
  "homeTeam": {
    "id": 1,
    "name": "Fenerbahçe",
    "logo": "https://example.com/images/fenerbahce-logo.png"
  },
  "awayTeam": {
    "id": 2,
    "name": "Galatasaray",
    "logo": "https://example.com/images/galatasaray-logo.png"
  },
  "date": "2025-05-15T19:00:00",
  "status": "SCHEDULED",
  "score": {
    "homeTeam": 0,
    "awayTeam": 0
  }
}
```

## Önemli Notlar

1. Tüm endpointler public'tir, authentication gerektirmez.
2. Tüm istekler CORS enabled'dır (Cross-Origin Resource Sharing).
3. Tarihler ISO 8601 formatında döner (YYYY-MM-DDTHH:mm:ss).
4. Hata durumunda aşağıdaki formatta yanıt döner:

```json
{
  "status": 404,
  "message": "Maç bulunamadı",
  "timestamp": "2025-05-15T15:48:40+03:00"
}
```

## HTTP Status Kodları

| Kod | Açıklama |
|-----|-----------|
| 200 | Başarılı |
| 404 | Kaynak bulunamadı |
| 500 | Sunucu hatası |
