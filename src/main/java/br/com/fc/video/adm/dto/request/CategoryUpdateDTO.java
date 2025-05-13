package br.com.fc.video.adm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryUpdateDTO(

        @NotBlank(message = "Name is required")
        @Size(min = 3, message = "Name must be at least 3 characters long")
        String name,

        String description

) {
}
