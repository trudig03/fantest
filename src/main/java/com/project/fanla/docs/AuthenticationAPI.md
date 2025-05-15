# Authentication API Documentation

## Overview
This document provides detailed information about the authentication endpoints for the Fanla application. The authentication system uses JWT (JSON Web Tokens) for secure user authentication and authorization.

## Base URL
```
/api/auth
```

## Endpoints

### Login
Authenticates a user and returns access and refresh tokens.

**URL**: `/api/auth/login`  
**Method**: `POST`  
**Auth required**: No  

#### Request Body
```json
{
  "username": "string",
  "password": "string"
}
```

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "username",
  "role": "SUPERADMIN",
  "userId": 1
}
```

#### Error Response
**Code**: `401 UNAUTHORIZED`  
**Content**:
```json
{
  "timestamp": "2025-05-13T09:58:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Bad credentials",
  "path": "/api/auth/login"
}
```

### Refresh Token
Generates a new access token using a valid refresh token.

**URL**: `/api/auth/refresh-token`  
**Method**: `POST`  
**Auth required**: No  

#### Request Body
```json
{
  "refreshToken": "string"
}
```

#### Success Response
**Code**: `200 OK`  
**Content example**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "username",
  "role": "SUPERADMIN",
  "userId": 1
}
```

#### Error Response
**Code**: `401 UNAUTHORIZED`  
**Content**:
```json
{
  "timestamp": "2025-05-13T09:58:00.000+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid refresh token",
  "path": "/api/auth/refresh-token"
}
```

## Authentication Flow

1. **Login**: Client sends username and password to `/api/auth/login`
2. **Server Authentication**: Server validates credentials and returns JWT tokens
3. **Using Access Token**: Client includes the access token in the Authorization header for subsequent requests
   ```
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```
4. **Token Expiration**: When the access token expires (after 24 hours), use the refresh token to get a new access token
5. **Refresh Process**: Send the refresh token to `/api/auth/refresh-token` to get a new access token

## Security Notes

- Access tokens are valid for 24 hours
- Refresh tokens are valid for 7 days
- All passwords are stored using BCrypt encryption
- Failed login attempts are logged with timestamp, username, and IP information
- Successful logins are also logged for audit purposes

## SUPERADMIN User

A SUPERADMIN user is automatically created at application startup with the following credentials:

- **Username**: superadmin
- **Password**: Fanla2025!
- **Email**: admin@fanla.com

This user has full administrative privileges in the system.
