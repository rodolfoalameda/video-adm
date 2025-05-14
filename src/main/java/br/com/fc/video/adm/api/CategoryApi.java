package br.com.fc.video.adm.api;

import br.com.fc.video.adm.dto.request.CategoryCreateDTO;
import br.com.fc.video.adm.dto.request.CategoryUpdateDTO;
import br.com.fc.video.adm.dto.response.CategoryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/categories")
public interface CategoryApi {

    @PostMapping
    Mono<ResponseEntity<CategoryResponseDTO>> createCategory(@Valid @RequestBody CategoryCreateDTO category);

    @GetMapping("/{id}")
    Mono<ResponseEntity<CategoryResponseDTO>> findOneCategory(@PathVariable Long id);

    @PutMapping("/{id}")
    Mono<ResponseEntity<CategoryResponseDTO>> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryUpdateDTO category);

    @DeleteMapping("/{id}")
    Mono<ResponseEntity<Void>> deactivateCategory(@PathVariable Long id);

    @PutMapping("/{id}/reactivate")
    Mono<ResponseEntity<Void>> reactivateCategory(@PathVariable Long id);

    @GetMapping
    Mono<ResponseEntity<Page<CategoryResponseDTO>>> findAllCategories(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size,
                                                                      @RequestParam(defaultValue = "name") String sort,
                                                                      @RequestParam(defaultValue = "ASC") String direction);
}
