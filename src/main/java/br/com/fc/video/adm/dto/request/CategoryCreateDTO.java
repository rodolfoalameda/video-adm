package br.com.fc.video.adm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryCreateDTO(

        @NotBlank(message = "Name is required")
        @Size(min = 3, message = "Name must be at least 3 characters long")
        String name,

        String description,

        @NotNull(message = "IsActive is required")
        Boolean isActive
) {
}
