# Fan Match API Documentation

## Overview
Bu dokümantasyon, fan kullanıcılarının maç detaylarını, aktif ses bilgilerini ve şarkı sözlerini görüntüleyebilmesi için gereken API'leri açıklar. Fan API'leri herhangi bir kimlik doğrulaması gerektirmez (permitAll).

## Security
Fan API'leri herhangi bir kimlik doğrulaması gerektirmez. Tüm endpoint'ler herkese açıktır.

## Fan API Endpoints

### Get Match Stream
Fan kullanıcıları için maç detaylarını, aktif ses bilgilerini ve şarkı sözlerini getirir.

**URL**: `/api/fan/match-stream/{matchId}`  
**Method**: `GET`  
**Auth required**: No (permitAll)  

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
  },
  "activeSound": {
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
  "websocket": {
    "endpoint": "/ws",
    "topic": "/topic/match/1"
  }
}
```

## WebSocket Integration

### WebSocket Connection
Fan kullanıcıları maç güncellemelerini gerçek zamanlı olarak almak için WebSocket bağlantısı kurabilirler.

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

### Not Found
**Code**: `404 NOT FOUND`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Match not found with id: 999",
  "path": "/api/fan/match-stream/999"
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
  "message": "Invalid match ID",
  "path": "/api/fan/match-stream/invalid"
}
```
