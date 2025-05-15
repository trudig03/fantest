# Admin API Documentation

## Overview
This document provides detailed information about the Admin endpoints for the Fanla application. These APIs allow team admins to manage matches, sounds, lyrics, and update their team information.

**Important**: Admin users can only access data related to their own team. All endpoints automatically filter data based on the authenticated admin's team association.

## Security
All endpoints in this document require authentication with a JWT token and the user must have the ADMIN role.

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Match Management

### Get My Team's Matches
Retrieves a list of matches for the admin's team.

**URL**: `/api/admin/matches`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
[
  {
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
    "status": "LIVE"
  },
  {
    "id": 2,
    "name": "Fenerbahçe vs Beşiktaş",
    "teamId": 1,
    "teamName": "Fenerbahçe",
    "opponentTeamId": 3,
    "opponentTeamName": "Beşiktaş",
    "location": "Şükrü Saracoğlu Stadium",
    "matchDate": "2025-05-27T19:00:00",
    "homeScore": 0,
    "awayScore": 0,
    "status": "PLANNED"
  }
]
```

### Get Match by ID
Retrieves a specific match by its ID.

**URL**: `/api/admin/matches/{id}`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

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
  "homeScore": 2,
  "awayScore": 1,
  "status": "LIVE",
  "activeSoundId": 5,
  "activeSoundTitle": "Fenerbahçe Anthem",
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null
}
```



### Create Match
Creates a new match.

**URL**: `/api/admin/matches`  
**Method**: `POST`  
**Auth required**: Yes (ADMIN)  

#### Request Body
```json
{
  "name": "Fenerbahçe vs Trabzonspor",
  "teamId": 1,
  "opponentTeamId": 4,
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-06-05T19:00:00"
}
```

Note: The following fields are optional and will be set to default values if not provided:
- `homeScore` (default: 0)
- `awayScore` (default: 0)
- `status` (default: PLANNED)
- `activeSoundId`
- `soundStartTime`
- `elapsedTimeOnPause`

#### Success Response
**Code**: `201 CREATED`  
**Content example**:
```json
{
  "id": 3,
  "name": "Fenerbahçe vs Trabzonspor",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "opponentTeamId": 4,
  "opponentTeamName": "Trabzonspor",
  "location": "Şükrü Saracoğlu Stadium",
  "matchDate": "2025-06-05T19:00:00",
  "homeScore": 0,
  "awayScore": 0,
  "status": "PLANNED"
}
```

### Update Match
Updates an existing match.

**URL**: `/api/admin/matches/{id}`  
**Method**: `PUT`  
**Auth required**: Yes (ADMIN)  

#### Request Body
```json
{
  "homeScore": 3,
  "awayScore": 1,
  "status": "COMPLETED",
  "activeSoundId": null
}
```

Note: Only include the fields you want to update. Any fields not included will remain unchanged.

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
  "soundStartTime": "2025-05-20T19:15:00",
  "elapsedTimeOnPause": null
}
```

### Delete Match
Deletes a match.

**URL**: `/api/admin/matches/{id}`  
**Method**: `DELETE`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `204 NO CONTENT`  

## Sound Management

### Get My Team's Sounds
Retrieves a list of sounds for the admin's team.

**URL**: `/api/admin/sounds`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
[
  {
    "id": 1,
    "title": "Fenerbahçe Anthem",
    "description": "Official Fenerbahçe anthem",
    "imageUrl": "https://example.com/images/fenerbahce-anthem.jpg",
    "creatorId": 2,
    "creatorUsername": "fenerbahce_admin",
    "soundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
    "teamId": 1,
    "teamName": "Fenerbahçe",
    "playlistOrder": 1,
    "status": "STOPPED"
  },
  {
    "id": 2,
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
```

### Get Sound by ID
Retrieves a specific sound by its ID.

**URL**: `/api/admin/sounds/{id}`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "title": "Fenerbahçe Anthem",
  "description": "Official Fenerbahçe anthem",
  "imageUrl": "https://example.com/images/fenerbahce-anthem.jpg",
  "creatorId": 2,
  "creatorUsername": "fenerbahce_admin",
  "soundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "playlistOrder": 1,
  "status": "STOPPED"
}
```



### Create Sound
Creates a new sound.

**URL**: `/api/admin/sounds`  
**Method**: `POST`  
**Auth required**: Yes (ADMIN)  

#### Request Body
```json
{
  "title": "New Fenerbahçe Chant",
  "description": "New chant for the 2025 season",
  "imageUrl": "https://example.com/images/new-chant.jpg",
  "soundUrl": "https://example.com/sounds/new-chant.mp3",
  "teamId": 1,
  "playlistOrder": 3
}
```

Note: The following fields are optional:
- `description`
- `imageUrl`
- `playlistOrder`
- `status` (default: STOPPED)

#### Success Response
**Code**: `201 CREATED`  
**Content example**:
```json
{
  "id": 3,
  "title": "New Fenerbahçe Chant",
  "description": "New chant for the 2025 season",
  "imageUrl": "https://example.com/images/new-chant.jpg",
  "creatorId": 2,
  "creatorUsername": "fenerbahce_admin",
  "soundUrl": "https://example.com/sounds/new-chant.mp3",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "playlistOrder": 3,
  "status": "STOPPED"
}
```

### Update Sound
Updates an existing sound.

**URL**: `/api/admin/sounds/{id}`  
**Method**: `PUT`  
**Auth required**: Yes (ADMIN)  

#### Request Body
```json
{
  "title": "Updated Fenerbahçe Chant",
  "description": "Updated description",
  "status": "PLAYING"
}
```

Note: Only include the fields you want to update. Any fields not included will remain unchanged.

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 2,
  "title": "Updated Fenerbahçe Chant",
  "description": "Updated description",
  "imageUrl": "https://example.com/images/fenerbahce-chant.jpg",
  "creatorId": 2,
  "creatorUsername": "fenerbahce_admin",
  "soundUrl": "https://example.com/sounds/fenerbahce-chant.mp3",
  "teamId": 1,
  "teamName": "Fenerbahçe",
  "playlistOrder": 2,
  "status": "PLAYING"
}
```

### Delete Sound
Deletes a sound.

**URL**: `/api/admin/sounds/{id}`  
**Method**: `DELETE`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `204 NO CONTENT`  

## Lyrics Management

### Get My Team's Lyrics
Retrieves a list of lyrics for the admin's team's sounds.

**URL**: `/api/admin/lyrics`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
[
  {
    "id": 1,
    "soundId": 1,
    "soundTitle": "Fenerbahçe Anthem",
    "lyricsText": "Yaşa Fenerbahçe, adın efsane olsun..."
  },
  {
    "id": 2,
    "soundId": 2,
    "soundTitle": "Fenerbahçe Chant",
    "lyricsText": "Fenerbahçe, sen çok yaşa..."
  }
]
```

### Get Lyrics by ID
Retrieves specific lyrics by ID.

**URL**: `/api/admin/lyrics/{id}`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "soundId": 1,
  "soundTitle": "Fenerbahçe Anthem",
  "lyricsText": "Yaşa Fenerbahçe, adın efsane olsun..."
}
```

### Get Lyrics by Sound ID
Retrieves lyrics for a specific sound.

**URL**: `/api/admin/lyrics/sound/{soundId}`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "soundId": 1,
  "soundTitle": "Fenerbahçe Anthem",
  "lyricsText": "Yaşa Fenerbahçe, adın efsane olsun..."
}
```

#### Error Response
**Code**: `404 NOT FOUND`  
If no lyrics exist for the specified sound.

### Create Lyrics
Creates new lyrics for a sound.

**URL**: `/api/admin/lyrics`  
**Method**: `POST`  
**Auth required**: Yes (ADMIN)  

#### Request Body
```json
{
  "soundId": 3,
  "lyricsText": "New chant lyrics go here..."
}
```

#### Success Response
**Code**: `201 CREATED`  
**Content example**:
```json
{
  "id": 3,
  "soundId": 3,
  "soundTitle": "New Fenerbahçe Chant",
  "lyricsText": "New chant lyrics go here..."
}
```

### Update Lyrics
Updates existing lyrics.

**URL**: `/api/admin/lyrics/{id}`  
**Method**: `PUT`  
**Auth required**: Yes (ADMIN)  

#### Request Body
```json
{
  "lyricsText": "Updated lyrics text goes here..."
}
```

Note: Only include the fields you want to update. Any fields not included will remain unchanged.

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "soundId": 1,
  "soundTitle": "Fenerbahçe Anthem",
  "lyricsText": "Updated lyrics text goes here..."
}
```

### Delete Lyrics
Deletes lyrics.

**URL**: `/api/admin/lyrics/{id}`  
**Method**: `DELETE`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `204 NO CONTENT`  

## Team Management

### Get My Team
Retrieves the team associated with the authenticated admin.

**URL**: `/api/admin/team/my-team`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe",
  "logo": "https://example.com/logos/fenerbahce.png",
  "description": "Fenerbahçe Sports Club",
  "countryId": 1,
  "countryName": "Turkey"
}
```

### Get Team by ID
Retrieves a specific team by its ID.

**URL**: `/api/admin/team/{id}`  
**Method**: `GET`  
**Auth required**: Yes (ADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe",
  "logo": "https://example.com/logos/fenerbahce.png",
  "description": "Fenerbahçe Sports Club",
  "countryId": 1,
  "countryName": "Turkey"
}
```

### Update Team
Updates the team associated with the authenticated admin.

**URL**: `/api/admin/team/{id}`  
**Method**: `PUT`  
**Auth required**: Yes (ADMIN)  

#### Request Body
```json
{
  "logo": "https://example.com/logos/fenerbahce-updated.png",
  "description": "Fenerbahçe Sports Club - Founded in 1907"
}
```

Note: Only include the fields you want to update. Any fields not included will remain unchanged.
Admin users can only update their own team.

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Fenerbahçe",
  "logo": "https://example.com/logos/fenerbahce-updated.png",
  "description": "Fenerbahçe Sports Club - Founded in 1907",
  "countryId": 1,
  "countryName": "Turkey"
}
```

## Error Responses

All endpoints may return the following error responses:

### Unauthorized
**Code**: `401 UNAUTHORIZED`  
**Content**:
```json
{
  "timestamp": "2025-05-13T16:30:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/admin/matches"
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
  "path": "/api/admin/matches"
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
  "path": "/api/admin/matches/999"
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
  "message": "Validation failed",
  "errors": [
    "Team ID is required"
  ],
  "path": "/api/admin/matches"
}
```
