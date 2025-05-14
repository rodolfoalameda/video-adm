package br.com.fc.video.adm.controller;

import br.com.fc.video.adm.api.CategoryApi;
import br.com.fc.video.adm.dto.request.CategoryCreateDTO;
import br.com.fc.video.adm.dto.request.CategoryUpdateDTO;
import br.com.fc.video.adm.dto.response.CategoryResponseDTO;
import br.com.fc.video.adm.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public Mono<ResponseEntity<CategoryResponseDTO>> updateCategory(Long id, CategoryUpdateDTO category) {
        log.info("Updating category id {} ", id);
        return categoryService.updateCategory(id, category)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> deactivateCategory(Long id) {
        log.info("Deactivating category id {}", id);
        return categoryService.deactivateCategory(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Override
    public Mono<ResponseEntity<Void>> reactivateCategory(Long id) {
        log.info("Reactivating category id {}", id);
        return categoryService.reactivateCategory(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    @Override
    public Mono<ResponseEntity<Page<CategoryResponseDTO>>> findAllCategories(int page,
                                                                             int size,
                                                                             String sort,
                                                                             String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        log.info("Reading all categories");
        return categoryService.findAllCategory(pageable)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }
}
