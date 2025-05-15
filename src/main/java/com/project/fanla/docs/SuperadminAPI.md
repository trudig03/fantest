# SUPERADMIN API Documentation

## Overview
This document provides detailed information about the SUPERADMIN endpoints for the Fanla application. These APIs allow the SUPERADMIN to manage countries, teams, and admin users.

## Security
All endpoints in this document require authentication with a JWT token and the user must have the SUPERADMIN role.

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Country Management

### Get All Countries
Retrieves a list of all countries.

**URL**: `/api/superadmin/countries`  
**Method**: `GET`  
**Auth required**: Yes (SUPERADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
[
  {
    "id": 1,
    "name": "Turkey",
    "shortCode": "TR",
    "logoUrl": "https://example.com/flags/turkey.png"
  },
  {
    "id": 2,
    "name": "Spain",
    "shortCode": "ES",
    "logoUrl": "https://example.com/flags/spain.png"
  }
]
```

### Get Country by ID
Retrieves a specific country by its ID.

**URL**: `/api/superadmin/countries/{id}`  
**Method**: `GET`  
**Auth required**: Yes (SUPERADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 1,
  "name": "Turkey",
  "shortCode": "TR",
  "logoUrl": "https://example.com/flags/turkey.png"
}
```

### Create Country
Creates a new country.

**URL**: `/api/superadmin/countries`  
**Method**: `POST`  
**Auth required**: Yes (SUPERADMIN)  

#### Request Body
```json
{
  "name": "Germany",
  "shortCode": "DE",
  "logoUrl": "https://example.com/flags/germany.png"
}
```

#### Success Response
**Code**: `201 CREATED`  
**Content example**:
```json
{
  "id": 3,
  "name": "Germany",
  "shortCode": "DE",
  "logoUrl": "https://example.com/flags/germany.png"
}
```

### Update Country
Updates an existing country.

**URL**: `/api/superadmin/countries/{id}`  
**Method**: `PUT`  
**Auth required**: Yes (SUPERADMIN)  

#### Request Body
```json
{
  "name": "Germany",
  "shortCode": "DE",
  "logoUrl": "https://example.com/flags/germany-updated.png"
}
```

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 3,
  "name": "Germany",
  "shortCode": "DE",
  "logoUrl": "https://example.com/flags/germany-updated.png"
}
```

### Delete Country
Deletes a country.

**URL**: `/api/superadmin/countries/{id}`  
**Method**: `DELETE`  
**Auth required**: Yes (SUPERADMIN)  

#### Success Response
**Code**: `204 NO CONTENT`  

## Team Management

### Get All Teams
Retrieves a list of all teams.

**URL**: `/api/superadmin/teams`  
**Method**: `GET`  
**Auth required**: Yes (SUPERADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
[
  {
    "id": 1,
    "name": "Fenerbahçe",
    "logo": "https://example.com/logos/fenerbahce.png",
    "description": "Fenerbahçe Sports Club",
    "countryId": 1,
    "countryName": "Turkey"
  },
  {
    "id": 2,
    "name": "Barcelona",
    "logo": "https://example.com/logos/barcelona.png",
    "description": "FC Barcelona",
    "countryId": 2,
    "countryName": "Spain"
  }
]
```

### Get Team by ID
Retrieves a specific team by its ID.

**URL**: `/api/superadmin/teams/{id}`  
**Method**: `GET`  
**Auth required**: Yes (SUPERADMIN)  

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

### Get Teams by Country
Retrieves all teams for a specific country.

**URL**: `/api/superadmin/teams/country/{countryId}`  
**Method**: `GET`  
**Auth required**: Yes (SUPERADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
[
  {
    "id": 1,
    "name": "Fenerbahçe",
    "logo": "https://example.com/logos/fenerbahce.png",
    "description": "Fenerbahçe Sports Club",
    "countryId": 1,
    "countryName": "Turkey"
  },
  {
    "id": 3,
    "name": "Galatasaray",
    "logo": "https://example.com/logos/galatasaray.png",
    "description": "Galatasaray Sports Club",
    "countryId": 1,
    "countryName": "Turkey"
  }
]
```

### Create Team
Creates a new team.

**URL**: `/api/superadmin/teams`  
**Method**: `POST`  
**Auth required**: Yes (SUPERADMIN)  

#### Request Body
```json
{
  "name": "Bayern Munich",
  "logo": "https://example.com/logos/bayern.png",
  "description": "FC Bayern Munich",
  "countryId": 3
}
```

#### Success Response
**Code**: `201 CREATED`  
**Content example**:
```json
{
  "id": 4,
  "name": "Bayern Munich",
  "logo": "https://example.com/logos/bayern.png",
  "description": "FC Bayern Munich",
  "countryId": 3,
  "countryName": "Germany"
}
```

### Update Team
Updates an existing team.

**URL**: `/api/superadmin/teams/{id}`  
**Method**: `PUT`  
**Auth required**: Yes (SUPERADMIN)  

#### Request Body
```json
{
  "name": "Bayern Munich",
  "logo": "https://example.com/logos/bayern-updated.png",
  "description": "FC Bayern Munich - German Champions",
  "countryId": 3
}
```

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 4,
  "name": "Bayern Munich",
  "logo": "https://example.com/logos/bayern-updated.png",
  "description": "FC Bayern Munich - German Champions",
  "countryId": 3,
  "countryName": "Germany"
}
```

### Delete Team
Deletes a team.

**URL**: `/api/superadmin/teams/{id}`  
**Method**: `DELETE`  
**Auth required**: Yes (SUPERADMIN)  

#### Success Response
**Code**: `204 NO CONTENT`  

## Admin User Management

### Get All Admins
Retrieves a list of all admin users.

**URL**: `/api/superadmin/users`  
**Method**: `GET`  
**Auth required**: Yes (SUPERADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
[
  {
    "id": 2,
    "username": "fenerbahce_admin",
    "email": "admin@fenerbahce.com",
    "role": "ADMIN",
    "firstName": "Fenerbahçe",
    "lastName": "Admin",
    "teamId": 1,
    "teamName": "Fenerbahçe"
  },
  {
    "id": 3,
    "username": "barcelona_admin",
    "email": "admin@barcelona.com",
    "role": "ADMIN",
    "firstName": "Barcelona",
    "lastName": "Admin",
    "teamId": 2,
    "teamName": "Barcelona"
  }
]
```

### Get Admin by ID
Retrieves a specific admin user by ID.

**URL**: `/api/superadmin/users/{id}`  
**Method**: `GET`  
**Auth required**: Yes (SUPERADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 2,
  "username": "fenerbahce_admin",
  "email": "admin@fenerbahce.com",
  "role": "ADMIN",
  "firstName": "Fenerbahçe",
  "lastName": "Admin",
  "teamId": 1,
  "teamName": "Fenerbahçe"
}
```

### Get Admins by Team
Retrieves all admin users for a specific team.

**URL**: `/api/superadmin/users/team/{teamId}`  
**Method**: `GET`  
**Auth required**: Yes (SUPERADMIN)  

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
[
  {
    "id": 2,
    "username": "fenerbahce_admin",
    "email": "admin@fenerbahce.com",
    "role": "ADMIN",
    "firstName": "Fenerbahçe",
    "lastName": "Admin",
    "teamId": 1,
    "teamName": "Fenerbahçe"
  },
  {
    "id": 5,
    "username": "fenerbahce_admin2",
    "email": "admin2@fenerbahce.com",
    "role": "ADMIN",
    "firstName": "Fenerbahçe",
    "lastName": "Admin 2",
    "teamId": 1,
    "teamName": "Fenerbahçe"
  }
]
```

### Create Admin
Creates a new admin user.

**URL**: `/api/superadmin/users`  
**Method**: `POST`  
**Auth required**: Yes (SUPERADMIN)  

#### Request Body
```json
{
  "username": "bayern_admin",
  "password": "securePassword123",
  "email": "admin@bayern.com",
  "firstName": "Bayern",
  "lastName": "Admin",
  "teamId": 4
}
```

#### Success Response
**Code**: `201 CREATED`  
**Content example**:
```json
{
  "id": 6,
  "username": "bayern_admin",
  "email": "admin@bayern.com",
  "role": "ADMIN",
  "firstName": "Bayern",
  "lastName": "Admin",
  "teamId": 4,
  "teamName": "Bayern Munich"
}
```

### Update Admin
Updates an existing admin user.

**URL**: `/api/superadmin/users/{id}`  
**Method**: `PUT`  
**Auth required**: Yes (SUPERADMIN)  

#### Request Body
```json
{
  "username": "bayern_admin",
  "password": "newSecurePassword456", // Optional, only include if changing password
  "email": "admin@bayern.com",
  "firstName": "Bayern Munich",
  "lastName": "Administrator",
  "teamId": 4
}
```

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "id": 6,
  "username": "bayern_admin",
  "email": "admin@bayern.com",
  "role": "ADMIN",
  "firstName": "Bayern Munich",
  "lastName": "Administrator",
  "teamId": 4,
  "teamName": "Bayern Munich"
}
```

### Delete Admin
Deletes an admin user.

**URL**: `/api/superadmin/users/{id}`  
**Method**: `DELETE`  
**Auth required**: Yes (SUPERADMIN)  

#### Success Response
**Code**: `204 NO CONTENT`  

## Error Responses

All endpoints may return the following error responses:

### Unauthorized
**Code**: `401 UNAUTHORIZED`  
**Content**:
```json
{
  "timestamp": "2025-05-13T10:32:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/superadmin/countries"
}
```

### Forbidden
**Code**: `403 FORBIDDEN`  
**Content**:
```json
{
  "timestamp": "2025-05-13T10:32:00.000+00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/superadmin/countries"
}
```

### Not Found
**Code**: `404 NOT FOUND`  
**Content**:
```json
{
  "timestamp": "2025-05-13T10:32:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Country not found with id: 999",
  "path": "/api/superadmin/countries/999"
}
```

### Bad Request
**Code**: `400 BAD REQUEST`  
**Content**:
```json
{
  "timestamp": "2025-05-13T10:32:00.000+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    "Country name is required",
    "Short code must be between 2 and 3 characters"
  ],
  "path": "/api/superadmin/countries"
}
```
