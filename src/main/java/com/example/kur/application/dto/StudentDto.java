package com.example.kur.application.dto;

import java.util.UUID;

public record StudentDto(UUID id, String fullName, String email, boolean hasAccess) {}
