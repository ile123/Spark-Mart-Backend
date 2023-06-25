package com.ilario.sparkmart.security.misc;

import java.util.UUID;

public record UserInformation(UUID userId, String firstName, String role) {}
