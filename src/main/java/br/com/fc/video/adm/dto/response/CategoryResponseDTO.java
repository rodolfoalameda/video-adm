package br.com.fc.video.adm.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponseDTO(
        @Schema(description = "ID único da categoria", example = "550e8400-e29b-41d4-a716-446655440000")
        Long id,

        @Schema(description = "Nome da categoria", example = "Filmes de Ação")
        String name,

        @Schema(description = "Descrição detalhada da categoria", example = "Categoria contendo filmes de ação")
        String description,

        @Schema(description = "Status de ativação da categoria", example = "true")
        Boolean isActive,

        @Schema(description = "Data de criação da categoria", example = "2023-11-20T10:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Data da última atualização", example = "2023-11-20T10:30:00")
        LocalDateTime updatedAt,

        @Schema(description = "Data de desativação (soft delete)", example = "null")
        LocalDateTime deletedAt) {
}
