# Admin Match API Documentation

## Overview
Bu dokümantasyon, admin kullanıcılarının maç detaylarını görüntülemesi, maç skorunu ve durumunu güncellemesi, ve maç sırasında ses çalma/durdurma işlemlerini yapabilmesi için gereken API'leri açıklar.

## Security
Tüm endpoint'ler JWT token ile kimlik doğrulaması gerektirir ve kullanıcının ADMIN rolüne sahip olması gerekir.

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Önemli**: Admin kullanıcıları yalnızca kendi takımlarına ait maçları görüntüleyebilir ve yönetebilir.

## Match Detail Endpoints

### Get Match Detail
Admin kullanıcısının takımına ait bir maçın detaylarını ve takımın tüm seslerini getirir.

**URL**: `/api/admin/match-detail/{matchId}`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "match": {
    "id": 1,
    "name": "Fenerbahçe vs Galatasaray",
    "teamId": 1,
    "teamName": "Fenerbahçe",
    "opponentTeamId": 2,
    "opponentTeamName": "Galatasaray",
    "location": "Şükrü Saracoğlu Stadium",
    "matchDate": "2025-05-20T19:00:00",
    "homeScore": 2,
    "awayScore": 1,
    "status": "LIVE",
    "activeSoundId": 5,
    "activeSoundTitle": "Fenerbahçe Anthem",
    "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
    "activeSoundLyrics": "[{\"lyric\": \"Fenerbahçe marşı sözleri\", \"second\": 0}, {\"lyric\": \"Devam eden sözler\", \"second\": 5}]",
    "soundStartTime": "2025-05-20T19:15:00",
    "elapsedTimeOnPause": null,
    "currentMillisecond": 12500
  },
  "sounds": [
    {
      "id": 5,
      "title": "Fenerbahçe Anthem",
      "description": "Official Fenerbahçe anthem",
      "imageUrl": "https://example.com/images/fenerbahce-anthem.jpg",
      "creatorId": 2,
      "creatorUsername": "fenerbahce_admin",
      "soundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
      "teamId": 1,
      "teamName": "Fenerbahçe",
      "playlistOrder": 1,
      "status": "PLAYING"
    },
    {
      "id": 6,
      "title": "Fenerbahçe Chant",
      "description": "Popular fan chant",
      "imageUrl": "https://example.com/images/fenerbahce-chant.jpg",
      "creatorId": 2,
      "creatorUsername": "fenerbahce_admin",
      "soundUrl": "https://example.com/sounds/fenerbahce-chant.mp3",
      "teamId": 1,
      "teamName": "Fenerbahçe",
      "playlistOrder": 2,
      "status": "STOPPED"
    }
  ]
}
```

### Update Match Score
Maçın skorunu günceller ve WebSocket üzerinden tüm bağlı istemcilere bildirim gönderir.

**URL**: `/api/admin/match-detail/{matchId}/score`  
**Method**: `PUT`  
**Auth required**: Yes (ADMIN)  
**Query Parameters**:
- `homeScore` - Ev sahibi takımın skoru
- `awayScore` - Deplasman takımının skoru

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe vs Galatasaray",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Galatasaray",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-05-20T19:00:00",
  "homeScore": 3,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null
}
```

### Update Match Status
Maçın durumunu günceller (PLANNED, LIVE, COMPLETED, CANCELLED) ve WebSocket üzerinden tüm bağlı istemcilere bildirim gönderir.

**URL**: `/api/admin/match-detail/{matchId}/status`  
**Method**: `PUT`  
**Auth required**: Yes (ADMIN)  
**Query Parameters**:
- `status` - Maçın yeni durumu (PLANNED, LIVE, COMPLETED, CANCELLED)

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe vs Galatasaray",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Galatasaray",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-05-20T19:00:00",
  "homeScore": 3,
  "awayScore": 1,
  "status": "COMPLETED",
  "activeSoundId": null,
  "activeSoundTitle": null,
  "activeSoundUrl": null,
  "soundStartTime": null,
  "elapsedTimeOnPause": null
}
```

### Control Match Sound
Maçta ses çalma, duraklatma veya durdurma işlemlerini gerçekleştirir ve WebSocket üzerinden tüm bağlı istemcilere bildirim gönderir.

**URL**: `/api/admin/match-detail/{matchId}/sound`  
**Method**: `POST`  
**Auth required**: Yes (ADMIN)  

#### Request Body
```json
{
  "soundId": 5,
  "action": "PLAYING",
  "startTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null,
  "currentMillisecond": 0
}
```

Diğer örnek request body'ler:

Sesi duraklatmak için:
```json
{
  "soundId": 5,
  "action": "PAUSED",
  "elapsedTimeOnPause": "30000",
  "currentMillisecond": 30000
}
```

Sesi durdurmak için:
```json
{
  "soundId": 5,
  "action": "STOPPED"
}
```

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe vs Galatasaray",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Galatasaray",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-05-20T19:00:00",
  "homeScore": 3,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null
}
```

## WebSocket Integration

### WebSocket Connection
Admin ve fan kullanıcıları maç güncellemelerini gerçek zamanlı olarak almak için WebSocket bağlantısı kurabilirler.

**Endpoint**: `/ws`  
**STOMP Destination**: `/topic/match/{matchId}`  

### WebSocket Message Format
WebSocket üzerinden gelen mesajlar aşağıdaki formatta olacaktır:

```json
{
  "matchId": 1,
  "homeScore": 3,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "activeSoundLyrics": "[{\"lyric\": \"Fenerbahçe marşı sözleri\", \"second\": 0}, {\"lyric\": \"Devam eden sözler\", \"second\": 5}]",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null,
  "currentMillisecond": 12500
}
```



## Error Responses

Tüm endpoint'ler aşağıdaki hata yanıtlarını döndürebilir:

### Unauthorized
**Code**: `401 UNAUTHORIZED`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/admin/match-detail/1"
}
```

### Forbidden
**Code**: `403 FORBIDDEN`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/admin/match-detail/1"
}
```

### Not Found
**Code**: `404 NOT FOUND`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Match not found with id: 999",
  "path": "/api/admin/match-detail/999"
}
```

### Bad Request
**Code**: `400 BAD REQUEST`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid status value",
  "path": "/api/admin/match-detail/1/status"
}
```


## Overview
Bu dokümantasyon, admin kullanıcılarının maç detaylarını görüntülemesi, maç skorunu ve durumunu güncellemesi, ve maç sırasında ses çalma/durdurma işlemlerini yapabilmesi için gereken API'leri açıklar.

## Security
Tüm endpoint'ler JWT token ile kimlik doğrulaması gerektirir ve kullanıcının ADMIN rolüne sahip olması gerekir.

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Önemli**: Admin kullanıcıları yalnızca kendi takımlarına ait maçları görüntüleyebilir ve yönetebilir.

## Match Detail Endpoints

### Get Match Detail
Admin kullanıcısının takımına ait bir maçın detaylarını ve **takımın tüm seslerini** getirir. Bu sayede admin, maç detay sayfasında kendi takımına ait tüm sesleri görebilir ve hızlıca ses çalma, duraklatma ve durdurma işlemlerini yapabilir. Ses listesi, ses ID'lerini içerdiği için admin kolayca istediği sesi seçip çalabilir.

**URL**: `/api/admin/match-detail/{matchId}`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "match": {
    "id": 1,
    "name": "Fenerbahçe vs Galatasaray",
    "teamId": 1,
    "teamName": "Fenerbahçe",
    "opponentTeamId": 2,
    "opponentTeamName": "Galatasaray",
    "location": "Şükrü Saracoğlu Stadium",
    "matchDate": "2025-05-20T19:00:00",
    "homeScore": 2,
    "awayScore": 1,
    "status": "LIVE",
    "activeSoundId": 5,
    "activeSoundTitle": "Fenerbahçe Anthem",
    "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
    "activeSoundLyrics": "[{\"lyric\": \"Fenerbahçe marşı sözleri\", \"second\": 0}, {\"lyric\": \"Devam eden sözler\", \"second\": 5}]",
    "soundStartTime": "2025-05-20T19:15:00",
    "elapsedTimeOnPause": null,
    "currentMillisecond": 12500
  },
  "sounds": [
    {
      "id": 5,
      "title": "Fenerbahçe Anthem",
      "description": "Official Fenerbahçe anthem",
      "imageUrl": "https://example.com/images/fenerbahce-anthem.jpg",
      "creatorId": 2,
      "creatorUsername": "fenerbahce_admin",
      "soundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
      "teamId": 1,
      "teamName": "Fenerbahçe",
      "playlistOrder": 1,
      "status": "PLAYING"
    },
    {
      "id": 6,
      "title": "Fenerbahçe Chant",
      "description": "Popular fan chant",
      "imageUrl": "https://example.com/images/fenerbahce-chant.jpg",
      "creatorId": 2,
      "creatorUsername": "fenerbahce_admin",
      "soundUrl": "https://example.com/sounds/fenerbahce-chant.mp3",
      "teamId": 1,
      "teamName": "Fenerbahçe",
      "playlistOrder": 2,
      "status": "STOPPED"
    }
  ]
}
```

### Update Match Score
Maçın skorunu günceller ve WebSocket üzerinden tüm bağlı istemcilere bildirim gönderir.

**URL**: `/api/admin/match-detail/{matchId}/score`  
**Method**: `PUT`  
**Auth required**: Yes (ADMIN)  
**Query Parameters**:
- `homeScore` - Ev sahibi takımın skoru
- `awayScore` - Deplasman takımının skoru

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe vs Galatasaray",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Galatasaray",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-05-20T19:00:00",
  "homeScore": 3,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null
}
```

### Update Match Status
Maçın durumunu günceller (PLANNED, LIVE, COMPLETED, CANCELLED) ve WebSocket üzerinden tüm bağlı istemcilere bildirim gönderir.

**URL**: `/api/admin/match-detail/{matchId}/status`  
**Method**: `PUT`  
**Auth required**: Yes (ADMIN)  
**Query Parameters**:
- `status` - Maçın yeni durumu (PLANNED, LIVE, COMPLETED, CANCELLED)

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe vs Galatasaray",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Galatasaray",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-05-20T19:00:00",
  "homeScore": 3,
  "awayScore": 1,
  "status": "COMPLETED",
  "activeSoundId": null,
  "activeSoundTitle": null,
  "activeSoundUrl": null,
  "soundStartTime": null,
  "elapsedTimeOnPause": null
}
```

### Control Match Sound
Maçta ses çalma, duraklatma veya durdurma işlemlerini gerçekleştirir ve WebSocket üzerinden tüm bağlı istemcilere bildirim gönderir.

**URL**: `/api/admin/match-detail/{matchId}/sound`  
**Method**: `POST`  
**Auth required**: Yes (ADMIN)  

#### Request Body
```json
{
  "soundId": 5,
  "action": "PLAYING",
  "startTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null,
  "currentMillisecond": 0
}
```

Diğer örnek request body'ler:

Sesi duraklatmak için:
```json
{
  "soundId": 5,
  "action": "PAUSED",
  "elapsedTimeOnPause": "30000",
  "currentMillisecond": 30000
}
```

Sesi durdurmak için:
```json
{
  "soundId": 5,
  "action": "STOPPED"
}
```

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe vs Galatasaray",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Galatasaray",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-05-20T19:00:00",
  "homeScore": 3,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null
}
```

## WebSocket Integration

### WebSocket Connection
Admin ve fan kullanıcıları maç güncellemelerini gerçek zamanlı olarak almak için WebSocket bağlantısı kurabilirler.

**Endpoint**: `/ws`  
**STOMP Destination**: `/topic/match/{matchId}`  

### WebSocket Message Format
WebSocket üzerinden gelen mesajlar aşağıdaki formatta olacaktır:

```json
{
  "matchId": 1,
  "homeScore": 3,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "activeSoundLyrics": "[{\"lyric\": \"Fenerbahçe marşı sözleri\", \"second\": 0}, {\"lyric\": \"Devam eden sözler\", \"second\": 5}]",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null,
  "currentMillisecond": 12500
}
```



## Error Responses

Tüm endpoint'ler aşağıdaki hata yanıtlarını döndürebilir:

### Unauthorized
**Code**: `401 UNAUTHORIZED`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/admin/match-detail/1"
}
```

### Forbidden
**Code**: `403 FORBIDDEN`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/admin/match-detail/1"
}
```

### Not Found
**Code**: `404 NOT FOUND`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Match not found with id: 999",
  "path": "/api/admin/match-detail/999"
}
```

### Bad Request
**Code**: `400 BAD REQUEST`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid status value",
  "path": "/api/admin/match-detail/1/status"
}
```
# Admin Match API Documentation

## Overview
Bu dokümantasyon, admin kullanıcılarının maç detaylarını görüntülemesi, maç skorunu ve durumunu güncellemesi, ve maç sırasında ses çalma/durdurma işlemlerini yapabilmesi için gereken API'leri açıklar.

## Security
Tüm endpoint'ler JWT token ile kimlik doğrulaması gerektirir ve kullanıcının ADMIN rolüne sahip olması gerekir.

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Önemli**: Admin kullanıcıları yalnızca kendi takımlarına ait maçları görüntüleyebilir ve yönetebilir.

## Match Detail Endpoints

### Get Match Detail
Admin kullanıcısının takımına ait bir maçın detaylarını ve takımın tüm seslerini getirir.

**URL**: `/api/admin/match-detail/{matchId}`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "match": {
    "id": 1,
    "name": "Fenerbahçe vs Galatasaray",
    "teamId": 1,
    "teamName": "Fenerbahçe",
    "opponentTeamId": 2,
    "opponentTeamName": "Galatasaray",
    "location": "Şükrü Saracoğlu Stadium",
    "matchDate": "2025-05-20T19:00:00",
    "homeScore": 2,
    "awayScore": 1,
    "status": "LIVE",
    "activeSoundId": 5,
    "activeSoundTitle": "Fenerbahçe Anthem",
    "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
    "activeSoundLyrics": "[{\"lyric\": \"Fenerbahçe marşı sözleri\", \"second\": 0}, {\"lyric\": \"Devam eden sözler\", \"second\": 5}]",
    "soundStartTime": "2025-05-20T19:15:00",
    "elapsedTimeOnPause": null,
    "currentMillisecond": 12500
  },
  "sounds": [
    {
      "id": 5,
      "title": "Fenerbahçe Anthem",
      "description": "Official Fenerbahçe anthem",
      "imageUrl": "https://example.com/images/fenerbahce-anthem.jpg",
      "creatorId": 2,
      "creatorUsername": "fenerbahce_admin",
      "soundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
      "teamId": 1,
      "teamName": "Fenerbahçe",
      "playlistOrder": 1,
      "status": "PLAYING"
    },
    {
      "id": 6,
      "title": "Fenerbahçe Chant",
      "description": "Popular fan chant",
      "imageUrl": "https://example.com/images/fenerbahce-chant.jpg",
      "creatorId": 2,
      "creatorUsername": "fenerbahce_admin",
      "soundUrl": "https://example.com/sounds/fenerbahce-chant.mp3",
      "teamId": 1,
      "teamName": "Fenerbahçe",
      "playlistOrder": 2,
      "status": "STOPPED"
    }
  ]
}
```

### Update Match Score
Maçın skorunu günceller ve WebSocket üzerinden tüm bağlı istemcilere bildirim gönderir.

**URL**: `/api/admin/match-detail/{matchId}/score`  
**Method**: `PUT`  
**Auth required**: Yes (ADMIN)  
**Query Parameters**:
- `homeScore` - Ev sahibi takımın skoru
- `awayScore` - Deplasman takımının skoru

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe vs Galatasaray",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Galatasaray",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-05-20T19:00:00",
  "homeScore": 3,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null
}
```

### Update Match Status
Maçın durumunu günceller (PLANNED, LIVE, COMPLETED, CANCELLED) ve WebSocket üzerinden tüm bağlı istemcilere bildirim gönderir.

**URL**: `/api/admin/match-detail/{matchId}/status`  
**Method**: `PUT`  
**Auth required**: Yes (ADMIN)  
**Query Parameters**:
- `status` - Maçın yeni durumu (PLANNED, LIVE, COMPLETED, CANCELLED)

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe vs Galatasaray",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Galatasaray",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-05-20T19:00:00",
  "homeScore": 3,
  "awayScore": 1,
  "status": "COMPLETED",
  "activeSoundId": null,
  "activeSoundTitle": null,
  "activeSoundUrl": null,
  "soundStartTime": null,
  "elapsedTimeOnPause": null
}
```

### Control Match Sound
Maçta ses çalma, duraklatma veya durdurma işlemlerini gerçekleştirir ve WebSocket üzerinden tüm bağlı istemcilere bildirim gönderir.

**URL**: `/api/admin/match-detail/{matchId}/sound`  
**Method**: `POST`  
**Auth required**: Yes (ADMIN)  

#### Request Body
```json
{
  "soundId": 5,
  "action": "PLAYING",
  "startTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null,
  "currentMillisecond": 0
}
```

Diğer örnek request body'ler:

Sesi duraklatmak için:
```json
{
  "soundId": 5,
  "action": "PAUSED",
  "elapsedTimeOnPause": "30000",
  "currentMillisecond": 30000
}
```

Sesi durdurmak için:
```json
{
  "soundId": 5,
  "action": "STOPPED"
}
```

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe vs Galatasaray",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Galatasaray",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-05-20T19:00:00",
  "homeScore": 3,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null
}
```

## WebSocket Integration

### WebSocket Connection
Admin ve fan kullanıcıları maç güncellemelerini gerçek zamanlı olarak almak için WebSocket bağlantısı kurabilirler.

**Endpoint**: `/ws`  
**STOMP Destination**: `/topic/match/{matchId}`  

### WebSocket Message Format
WebSocket üzerinden gelen mesajlar aşağıdaki formatta olacaktır:

```json
{
  "matchId": 1,
  "homeScore": 3,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "activeSoundLyrics": "[{\"lyric\": \"Fenerbahçe marşı sözleri\", \"second\": 0}, {\"lyric\": \"Devam eden sözler\", \"second\": 5}]",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null,
  "currentMillisecond": 12500
}
```



## Error Responses

Tüm endpoint'ler aşağıdaki hata yanıtlarını döndürebilir:

### Unauthorized
**Code**: `401 UNAUTHORIZED`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/admin/match-detail/1"
}
```

### Forbidden
**Code**: `403 FORBIDDEN`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/admin/match-detail/1"
}
```

### Not Found
**Code**: `404 NOT FOUND`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Match not found with id: 999",
  "path": "/api/admin/match-detail/999"
}
```

### Bad Request
**Code**: `400 BAD REQUEST`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid status value",
  "path": "/api/admin/match-detail/1/status"
}
```


## Overview
Bu dokümantasyon, admin kullanıcılarının maç detaylarını görüntülemesi, maç skorunu ve durumunu güncellemesi, ve maç sırasında ses çalma/durdurma işlemlerini yapabilmesi için gereken API'leri açıklar.

## Security
Tüm endpoint'ler JWT token ile kimlik doğrulaması gerektirir ve kullanıcının ADMIN rolüne sahip olması gerekir.

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Önemli**: Admin kullanıcıları yalnızca kendi takımlarına ait maçları görüntüleyebilir ve yönetebilir.

## Match Detail Endpoints

### Get Match Detail
Admin kullanıcısının takımına ait bir maçın detaylarını ve **takımın tüm seslerini** getirir. Bu sayede admin, maç detay sayfasında kendi takımına ait tüm sesleri görebilir ve hızlıca ses çalma, duraklatma ve durdurma işlemlerini yapabilir. Ses listesi, ses ID'lerini içerdiği için admin kolayca istediği sesi seçip çalabilir.

**URL**: `/api/admin/match-detail/{matchId}`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "match": {
    "id": 1,
    "name": "Fenerbahçe vs Galatasaray",
    "teamId": 1,
    "teamName": "Fenerbahçe",
    "opponentTeamId": 2,
    "opponentTeamName": "Galatasaray",
    "location": "Şükrü Saracoğlu Stadium",
    "matchDate": "2025-05-20T19:00:00",
    "homeScore": 2,
    "awayScore": 1,
    "status": "LIVE",
    "activeSoundId": 5,
    "activeSoundTitle": "Fenerbahçe Anthem",
    "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
    "activeSoundLyrics": "[{\"lyric\": \"Fenerbahçe marşı sözleri\", \"second\": 0}, {\"lyric\": \"Devam eden sözler\", \"second\": 5}]",
    "soundStartTime": "2025-05-20T19:15:00",
    "elapsedTimeOnPause": null,
    "currentMillisecond": 12500
  },
  "sounds": [
    {
      "id": 5,
      "title": "Fenerbahçe Anthem",
      "description": "Official Fenerbahçe anthem",
      "imageUrl": "https://example.com/images/fenerbahce-anthem.jpg",
      "creatorId": 2,
      "creatorUsername": "fenerbahce_admin",
      "soundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
      "teamId": 1,
      "teamName": "Fenerbahçe",
      "playlistOrder": 1,
      "status": "PLAYING"
    },
    {
      "id": 6,
      "title": "Fenerbahçe Chant",
      "description": "Popular fan chant",
      "imageUrl": "https://example.com/images/fenerbahce-chant.jpg",
      "creatorId": 2,
      "creatorUsername": "fenerbahce_admin",
      "soundUrl": "https://example.com/sounds/fenerbahce-chant.mp3",
      "teamId": 1,
      "teamName": "Fenerbahçe",
      "playlistOrder": 2,
      "status": "STOPPED"
    }
  ]
}
```

### Update Match Score
Maçın skorunu günceller ve WebSocket üzerinden tüm bağlı istemcilere bildirim gönderir.

**URL**: `/api/admin/match-detail/{matchId}/score`  
**Method**: `PUT`  
**Auth required**: Yes (ADMIN)  
**Query Parameters**:
- `homeScore` - Ev sahibi takımın skoru
- `awayScore` - Deplasman takımının skoru

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe vs Galatasaray",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Galatasaray",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-05-20T19:00:00",
  "homeScore": 3,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null
}
```

### Update Match Status
Maçın durumunu günceller (PLANNED, LIVE, COMPLETED, CANCELLED) ve WebSocket üzerinden tüm bağlı istemcilere bildirim gönderir.

**URL**: `/api/admin/match-detail/{matchId}/status`  
**Method**: `PUT`  
**Auth required**: Yes (ADMIN)  
**Query Parameters**:
- `status` - Maçın yeni durumu (PLANNED, LIVE, COMPLETED, CANCELLED)

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe vs Galatasaray",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Galatasaray",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-05-20T19:00:00",
  "homeScore": 3,
  "awayScore": 1,
  "status": "COMPLETED",
  "activeSoundId": null,
  "activeSoundTitle": null,
  "activeSoundUrl": null,
  "soundStartTime": null,
  "elapsedTimeOnPause": null
}
```

### Control Match Sound
Maçta ses çalma, duraklatma veya durdurma işlemlerini gerçekleştirir ve WebSocket üzerinden tüm bağlı istemcilere bildirim gönderir.

**URL**: `/api/admin/match-detail/{matchId}/sound`  
**Method**: `POST`  
**Auth required**: Yes (ADMIN)  

#### Request Body
```json
{
  "soundId": 5,
  "action": "PLAYING",
  "startTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null,
  "currentMillisecond": 0
}
```

Diğer örnek request body'ler:

Sesi duraklatmak için:
```json
{
  "soundId": 5,
  "action": "PAUSED",
  "elapsedTimeOnPause": "30000",
  "currentMillisecond": 30000
}
```

Sesi durdurmak için:
```json
{
  "soundId": 5,
  "action": "STOPPED"
}
```

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe vs Galatasaray",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Galatasaray",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-05-20T19:00:00",
  "homeScore": 3,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null
}
```

## WebSocket Integration

### WebSocket Connection
Admin ve fan kullanıcıları maç güncellemelerini gerçek zamanlı olarak almak için WebSocket bağlantısı kurabilirler.

**Endpoint**: `/ws`  
**STOMP Destination**: `/topic/match/{matchId}`  

### WebSocket Message Format
WebSocket üzerinden gelen mesajlar aşağıdaki formatta olacaktır:

```json
{
  "matchId": 1,
  "homeScore": 3,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "activeSoundLyrics": "[{\"lyric\": \"Fenerbahçe marşı sözleri\", \"second\": 0}, {\"lyric\": \"Devam eden sözler\", \"second\": 5}]",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null,
  "currentMillisecond": 12500
}
```



## Error Responses

Tüm endpoint'ler aşağıdaki hata yanıtlarını döndürebilir:

### Unauthorized
**Code**: `401 UNAUTHORIZED`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/admin/match-detail/1"
}
```

### Forbidden
**Code**: `403 FORBIDDEN`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/admin/match-detail/1"
}
```

### Not Found
**Code**: `404 NOT FOUND`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Match not found with id: 999",
  "path": "/api/admin/match-detail/999"
}
```

### Bad Request
**Code**: `400 BAD REQUEST`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid status value",
  "path": "/api/admin/match-detail/1/status"
}
```
