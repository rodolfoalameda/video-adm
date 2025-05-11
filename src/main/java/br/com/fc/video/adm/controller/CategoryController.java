package br.com.fc.video.adm.controller;

import br.com.fc.video.adm.api.CategoryApi;
import br.com.fc.video.adm.dto.request.CategoryCreateDTO;
import br.com.fc.video.adm.dto.response.CategoryResponseDTO;
import br.com.fc.video.adm.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController implements CategoryApi {

    private final CategoryServiceImpl categoryService;

    @Override
    public Mono<ResponseEntity<CategoryResponseDTO>> createCategory(CategoryCreateDTO category) {
        log.info("Creating category: {}", category);
        return categoryService.createCategory(category)
                .map(savedCategory -> ResponseEntity
                        .created(URI.create("/api/v1/categories/" + savedCategory.id()))
                        .body(savedCategory)
                );
    }

    @Override
    public Mono<ResponseEntity<CategoryResponseDTO>> findOneCategory(Long id) {
        log.info("Searching for category id {}", id);
        return categoryService.findById(id)
                .map(ResponseEntity::ok);

    }
}
