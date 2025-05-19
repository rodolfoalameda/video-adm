package br.com.fc.video.adm.category.create;

import br.com.fc.video.adm.controller.CategoryController;
import br.com.fc.video.adm.dto.request.CategoryCreateDTO;
import br.com.fc.video.adm.dto.response.CategoryResponseDTO;
import br.com.fc.video.adm.exception.CustomException;
import br.com.fc.video.adm.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCategoryControllerUnitTest {

    @Mock
    private CategoryServiceImpl categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void createCategory_ShouldReturnCreatedResponseWithCategory() {

        CategoryCreateDTO request = new CategoryCreateDTO("Electronics", "Electronic devices", true);

        CategoryResponseDTO response = new CategoryResponseDTO(1L, "Electronics", "Electronic devices",
                true, LocalDateTime.now(), null, null);

        when(categoryService.createCategory(any(CategoryCreateDTO.class)))
                .thenReturn(Mono.just(response));

        Mono<ResponseEntity<CategoryResponseDTO>> result = categoryController.createCategory(request);

        StepVerifier.create(result)
                .expectNextMatches(responseEntity ->
                        responseEntity.getStatusCode() == HttpStatus.CREATED &&
                                responseEntity.getHeaders().getLocation().toString().equals("/api/v1/categories/1") &&
                                responseEntity.getBody().equals(response))
                .verifyComplete();
    }

    @Test
    void createCategory_WhenServiceError_ShouldPropagateError() {

        CategoryCreateDTO request = new CategoryCreateDTO("Electronics", "Electronic devices", true);

        when(categoryService.createCategory(any(CategoryCreateDTO.class)))
                .thenReturn(Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating category")));

        Mono<ResponseEntity<CategoryResponseDTO>> result = categoryController.createCategory(request);

        StepVerifier.create(result)
                .expectErrorMatches(ex ->
                        ex instanceof CustomException &&
                                ((CustomException) ex).getStatus() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verify();
    }
}
