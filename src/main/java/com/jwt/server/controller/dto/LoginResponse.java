package com.jwt.server.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
