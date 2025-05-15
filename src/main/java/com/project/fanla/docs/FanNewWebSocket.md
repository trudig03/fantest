# Fanla WebSocket API Dokümantasyonu

## Genel Bakış

Fanla uygulaması, maç bilgilerini ve ses işlemlerini gerçek zamanlı olarak takip etmek için WebSocket API'sini kullanır. Bu API, maç detaylarını, skor değişikliklerini ve ses işlemlerini (çalma, duraklatma, durdurma) anlık olarak istemcilere iletir.

## WebSocket Endpoint'leri

Fanla uygulaması iki farklı WebSocket bağlantı yöntemi sunar:

### 1. STOMP Protokolü ile Bağlantı

**Endpoint:** `ws://[sunucu-adresi]/ws`

STOMP (Simple Text Oriented Messaging Protocol) protokolü üzerinden bağlantı kurulur ve topic'lere abone olunur.

#### Topic'ler

- `/topic/match/{matchId}`: Belirli bir maçın güncellemelerini almak için kullanılır.

#### Örnek Kullanım

```javascript
// STOMP bağlantısı kurma
const socket = new SockJS('http://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function(frame) {
    console.log('Bağlantı başarılı: ' + frame);
    
    // Maç güncellemelerine abone olma
    stompClient.subscribe('/topic/match/1', function(message) {
        const matchData = JSON.parse(message.body);
        console.log('Maç güncellemesi alındı:', matchData);
    });
});
```

### 2. Doğrudan WebSocket Bağlantısı

**Endpoint:** `ws://[sunucu-adresi]/match-socket/{matchId}`

JSR-356 WebSocket API kullanılarak doğrudan WebSocket bağlantısı kurulur. STOMP protokolüne gerek yoktur.

#### Örnek Kullanım

```javascript
// Doğrudan WebSocket bağlantısı kurma
const socket = new WebSocket('ws://localhost:8080/match-socket/1');

socket.onopen = function(event) {
    console.log('Bağlantı başarılı');
};

socket.onmessage = function(event) {
    const matchData = JSON.parse(event.data);
    console.log('Maç güncellemesi alındı:', matchData);
};

socket.onclose = function(event) {
    console.log('Bağlantı kapatıldı');
};

socket.onerror = function(error) {
    console.error('Bağlantı hatası:', error);
};
```

## Veri Formatı

WebSocket üzerinden gönderilen veriler JSON formatındadır ve aşağıdaki yapıya sahiptir:

```json
{
  "id": 1,
  "name": "Fenerbahçe - Beşiktaş",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 2,
  "opponentTeamName": "Beşiktaş",
  "location": "Arena",
  "matchDate": "2022-01-11T13:22:00",
  "homeScore": 3,
  "awayScore": 2,
  "status": "PLANNED",
  "activeSoundId": 1,
  "activeSoundTitle": "yeni",
  "activeSoundUrl": "https://files.catbox.moe/xj9b4k.mp3",
  "soundStartTime": "2025-05-14T14:57:36.963357",
  "currentMillisecond": 23666,
  "activeSoundLyrics": "[{\"lyric\":\"sadasdasd\", \"second\":0}, {\"lyric\":\"asdsadasdasd\", \"second\":9}, {\"lyric\":\"asdasdasd\", \"second\":23}]"
}
```

### Veri Alanları

| Alan | Tip | Açıklama |
|------|-----|----------|
| id | Long | Maç ID'si |
| name | String | Maç adı (örn. "Fenerbahçe - Beşiktaş") |
| teamId | Long | Ev sahibi takım ID'si |
| teamName | String | Ev sahibi takım adı |
| opponentTeamId | Long | Deplasman takımı ID'si |
| opponentTeamName | String | Deplasman takımı adı |
| location | String | Maç lokasyonu |
| matchDate | String (ISO-8601) | Maç tarihi ve saati |
| homeScore | Integer | Ev sahibi takım skoru |
| awayScore | Integer | Deplasman takımı skoru |
| status | String | Maç durumu (PLANNED, LIVE, FINISHED) |
| activeSoundId | Long | Aktif çalan ses ID'si (varsa) |
| activeSoundTitle | String | Aktif çalan ses başlığı (varsa) |
| activeSoundUrl | String | Aktif çalan ses URL'si (varsa) |
| soundStartTime | String (ISO-8601) | Ses çalmaya başlama zamanı (varsa) |
| currentMillisecond | Long | Şarkının mevcut konumu (milisaniye) |
| elapsedTimeOnPause | Long | Duraklatma anındaki geçen süre (milisaniye) |
| activeSoundLyrics | String (JSON) | Şarkı sözleri (JSON formatında) |

## Ses İşlemleri API'leri

WebSocket güncellemelerini tetiklemek için kullanabileceğiniz HTTP API'leri:

### Ses Çalma

```http
POST /api/admin/sounds/play
Content-Type: application/json

{
  "matchId": 1,
  "soundId": 1,
  "action": "PLAYING"
}
```

### Ses Duraklatma

```http
POST /api/admin/sounds/pause
Content-Type: application/json

{
  "matchId": 1,
  "soundId": 1,
  "action": "PAUSED",
  "currentMillisecond": 5000
}
```

### Ses Durdurma

```http
POST /api/admin/sounds/stop
Content-Type: application/json

{
  "matchId": 1,
  "soundId": 1,
  "action": "STOPPED"
}
```

## Güvenlik

WebSocket bağlantıları için herhangi bir kimlik doğrulama veya yetkilendirme gerekmez. Tüm WebSocket endpoint'leri herkese açıktır.

## Hata Ayıklama

WebSocket bağlantısı ile ilgili sorunları tespit etmek için:

1. Tarayıcıda `http://localhost:8080/websocket-test.html` adresini ziyaret edin (STOMP protokolü ile test için).
2. Tarayıcıda `http://localhost:8080/direct-websocket-test.html` adresini ziyaret edin (doğrudan WebSocket bağlantısı için).
3. Postman'ı kullanarak WebSocket bağlantısını test edin.

### Postman ile Test

1. Postman'ı açın ve yeni bir WebSocket isteği oluşturun.
2. URL olarak `ws://localhost:8080/match-socket/1` adresini girin (1 yerine istediğiniz maç ID'sini yazabilirsiniz).
3. "Connect" butonuna tıklayın.
4. Bağlantı başarılı olduktan sonra, sunucudan gelen tüm mesajları "Messages" sekmesinde görebilirsiniz.

## Notlar

- WebSocket bağlantısı, ses işlemleri (çalma, duraklatma, durdurma) sırasında güncellemeler gönderir.
- Skor değişiklikleri ve diğer maç güncellemeleri de WebSocket üzerinden iletilir.
- WebSocket bağlantısı kesilirse, istemci otomatik olarak yeniden bağlanmayı denemelidir.
- Şarkı sözleri (lyrics) JSON formatında gönderilir ve `second` alanı, şarkının hangi saniyesinde gösterileceğini belirtir.

## Örnek İstemci Uygulaması

Aşağıda, doğrudan WebSocket bağlantısı kuran basit bir JavaScript örneği verilmiştir:

```javascript
// Maç ID'si
const matchId = 1;

// WebSocket bağlantısı kurma
const socket = new WebSocket(`ws://localhost:8080/match-socket/${matchId}`);

// Bağlantı açıldığında
socket.onopen = function(event) {
    console.log('WebSocket bağlantısı kuruldu');
};

// Mesaj alındığında
socket.onmessage = function(event) {
    try {
        const matchData = JSON.parse(event.data);
        console.log('Maç güncellemesi alındı:', matchData);
        
        // Aktif ses varsa
        if (matchData.activeSoundId) {
            console.log('Aktif ses:', matchData.activeSoundTitle);
            console.log('Ses URL:', matchData.activeSoundUrl);
            console.log('Mevcut konum (ms):', matchData.currentMillisecond);
            
            // Şarkı sözleri varsa
            if (matchData.activeSoundLyrics) {
                try {
                    const lyrics = JSON.parse(matchData.activeSoundLyrics);
                    console.log('Şarkı sözleri:', lyrics);
                } catch (e) {
                    console.error('Şarkı sözleri JSON formatında değil:', e);
                }
            }
        }
    } catch (e) {
        console.error('Alınan veri JSON formatında değil:', e);
    }
};

// Bağlantı kapandığında
socket.onclose = function(event) {
    if (event.wasClean) {
        console.log(`Bağlantı temiz bir şekilde kapatıldı, kod=${event.code}, neden=${event.reason}`);
    } else {
        console.log('Bağlantı beklenmedik şekilde kesildi');
    }
};

// Hata oluştuğunda
socket.onerror = function(error) {
    console.error('WebSocket hatası:', error);
};
```

Bu örnek, maç güncellemelerini almak ve işlemek için kullanılabilir. Gerçek bir uygulamada, alınan verileri kullanıcı arayüzünde göstermek için ek işlemler yapmanız gerekecektir.
