package br.com.fc.video.adm.api;

import br.com.fc.video.adm.dto.request.CategoryCreateDTO;
import br.com.fc.video.adm.dto.response.CategoryResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/api/v1/categories")
public interface CategoryApi {

    @PostMapping
    Mono<ResponseEntity<CategoryResponseDTO>> createCategory(@Valid @RequestBody CategoryCreateDTO category);

    @GetMapping("/{id}")
    Mono<ResponseEntity<CategoryResponseDTO>> findOneCategory(@PathVariable Long id);
}
