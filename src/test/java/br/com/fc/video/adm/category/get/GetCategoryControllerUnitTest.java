package br.com.fc.video.adm.category.get;

import br.com.fc.video.adm.controller.CategoryController;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCategoryControllerUnitTest {

    @Mock
    private CategoryServiceImpl categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void findOneCategory_WhenCategoryExists_ShouldReturnOkWithCategory() {

        Long categoryId = 1L;
        LocalDateTime now = LocalDateTime.now();

        CategoryResponseDTO responseDTO = new CategoryResponseDTO(categoryId, "Electronics", "Electronic devices",
                true, now.minusDays(1), now, null
        );

        when(categoryService.findById(categoryId))
                .thenReturn(Mono.just(responseDTO));

        Mono<ResponseEntity<CategoryResponseDTO>> result = categoryController.findOneCategory(categoryId);

        StepVerifier.create(result)
                .expectNextMatches(responseEntity ->
                        responseEntity.getStatusCode() == HttpStatus.OK &&
                                responseEntity.getBody().equals(responseDTO) &&
                                responseEntity.getBody().id().equals(categoryId) &&
                                responseEntity.getBody().isActive() &&
                                responseEntity.getBody().deletedAt() == null
                )
                .verifyComplete();
    }

    @Test
    void findOneCategory_WhenCategoryIsInactive_ShouldReturnOkWithInactiveCategory() {

        Long categoryId = 2L;
        LocalDateTime now = LocalDateTime.now();

        CategoryResponseDTO responseDTO = new CategoryResponseDTO(categoryId, "Old Products", "Discontinued items",
                false, now.minusMonths(1), now.minusDays(1), now
        );

        when(categoryService.findById(categoryId))
                .thenReturn(Mono.just(responseDTO));

        Mono<ResponseEntity<CategoryResponseDTO>> result = categoryController.findOneCategory(categoryId);

        StepVerifier.create(result)
                .expectNextMatches(responseEntity ->
                        responseEntity.getStatusCode() == HttpStatus.OK &&
                                !responseEntity.getBody().isActive() &&
                                responseEntity.getBody().deletedAt() != null
                )
                .verifyComplete();
    }

    @Test
    void findOneCategory_WhenCategoryNotFound_ShouldReturnNotFound() {

        Long nonExistentId = 999L;

        when(categoryService.findById(nonExistentId))
                .thenReturn(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Category not founded")));

        Mono<ResponseEntity<CategoryResponseDTO>> result = categoryController.findOneCategory(nonExistentId);

        StepVerifier.create(result)
                .expectErrorMatches(ex ->
                        ex instanceof CustomException &&
                                ((CustomException) ex).getStatus() == HttpStatus.NOT_FOUND
                )
                .verify();
    }
}
