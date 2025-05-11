package br.com.fc.video.adm.dto.response;

import java.time.LocalDateTime;

public record CategoryResponseDTO(
        Long id,
        String name,
        String description,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt) {
}
