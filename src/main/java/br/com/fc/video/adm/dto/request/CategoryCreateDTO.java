package br.com.fc.video.adm.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record CategoryCreateDTO(@Schema(description = "Nome da categoria", example = "Filmes de Ação")
                                @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
                                String name,

                                @Schema(description = "Descrição detalhada da categoria", example = "Categoria contendo os melhores filmes de ação")
                                @Size(max = 4000, message = "A descrição deve ter no máximo 4000 caracteres")
                                String description,

                                @Schema(description = "Se a categoria está ativa", example = "True ou False")
                                Boolean isActive

) {
}
