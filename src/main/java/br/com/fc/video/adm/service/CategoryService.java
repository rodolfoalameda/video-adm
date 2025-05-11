package br.com.fc.video.adm.service;

import br.com.fc.video.adm.dto.request.CategoryCreateDTO;
import br.com.fc.video.adm.dto.response.CategoryResponseDTO;
import reactor.core.publisher.Mono;


public interface CategoryService {

    Mono<CategoryResponseDTO> createCategory(CategoryCreateDTO dto);

    Mono<CategoryResponseDTO> findById(Long id);
}
