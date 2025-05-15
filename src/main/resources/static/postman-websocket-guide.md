# Postman ile WebSocket Bağlantısı Test Etme Rehberi

## WebSocket Bağlantısı Kurma

1. Postman'ı açın
2. Sol üst köşedeki "New" butonuna tıklayın
3. "WebSocket Request" seçeneğini seçin
4. URL kısmına aşağıdaki URL'lerden birini girin:
   - STOMP protokolü için: `ws://localhost:8080/ws` 
   - Doğrudan WebSocket bağlantısı için: `ws://localhost:8080/match-socket/1` (1 yerine istediğiniz maç ID'sini yazabilirsiniz)

## STOMP Protokolü ile Bağlanma (ws://localhost:8080/ws)

STOMP protokolü ile bağlandığınızda, önce bir bağlantı kurmanız, sonra bir topic'e abone olmanız gerekir:

1. Bağlantı kurduktan sonra, "Messages" sekmesine geçin
2. "STOMP" sekmesini seçin
3. "Connect" butonuna tıklayın
4. Bağlantı başarılı olduktan sonra, "Subscribe" sekmesine geçin
5. "Destination" alanına `/topic/match/1` yazın (1 yerine istediğiniz maç ID'sini yazabilirsiniz)
6. "Subscribe" butonuna tıklayın

Artık `/topic/match/1` topic'ine abone oldunuz. Bu topic'e gelen tüm mesajları görebilirsiniz.

## Doğrudan WebSocket Bağlantısı (ws://localhost:8080/match-socket/1)

Doğrudan WebSocket bağlantısı için:

1. URL'yi `ws://localhost:8080/match-socket/1` olarak girin (1 yerine istediğiniz maç ID'sini yazabilirsiniz)
2. "Connect" butonuna tıklayın
3. Bağlantı başarılı olduktan sonra, sunucudan gelen tüm mesajları "Messages" sekmesinde görebilirsiniz

## Ses İşlemlerini Test Etme

Ses işlemlerini test etmek için, bir API isteği göndermeniz gerekir:

### Ses Çalma İşlemi

```http
POST /api/admin/sounds/play
Content-Type: application/json

{
  "matchId": 1,
  "soundId": 1,
  "action": "PLAYING"
}
```

### Ses Duraklatma İşlemi

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

### Ses Durdurma İşlemi

```http
POST /api/admin/sounds/stop
Content-Type: application/json

{
  "matchId": 1,
  "soundId": 1,
  "action": "STOPPED"
}
```

Bu API isteklerini gönderdikten sonra, WebSocket bağlantınız üzerinden güncellemeleri alacaksınız.

## Hata Ayıklama

Eğer bağlantı kurulamıyorsa:

1. Uygulamanın çalıştığından emin olun
2. URL'nin doğru olduğunu kontrol edin
3. Güvenlik duvarı veya proxy ayarlarınızı kontrol edin
4. Tarayıcıda `http://localhost:8080/websocket-test.html` veya `http://localhost:8080/direct-websocket-test.html` sayfalarını açarak WebSocket bağlantısını test edin
