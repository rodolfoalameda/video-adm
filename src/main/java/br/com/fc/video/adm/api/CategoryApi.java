package br.com.fc.video.adm.api;

import br.com.fc.video.adm.dto.request.CategoryCreateDTO;
import br.com.fc.video.adm.dto.response.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@Tag(name = "Categories", description = "API para gerenciamento de categorias")
@RequestMapping("/api/v1/categories")
public interface CategoryApi {

    @Operation(summary = "Create a new category")
    @PostMapping
    Mono<ResponseEntity<CategoryResponseDTO>> createCategory(@Valid @RequestBody CategoryCreateDTO category);


}
