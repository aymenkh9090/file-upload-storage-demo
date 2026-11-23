package com.aymen.doc.dto;

import java.time.Instant;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        String originalName,
        String contentType,
        long size,
        Instant createdAt,
        String downloadUrl

) {
}
