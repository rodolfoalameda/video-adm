package br.com.fc.video.adm.dto.request;

public record CategoryCreateDTO(
        String name,
        String description,
        Boolean isActive
) {
}
