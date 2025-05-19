package br.com.fc.video.adm.category.get;

import br.com.fc.video.adm.dto.response.CategoryResponseDTO;
import br.com.fc.video.adm.exception.CustomException;
import br.com.fc.video.adm.mapper.CategoryMapper;
import br.com.fc.video.adm.model.Category;
import br.com.fc.video.adm.repository.CategoryRepository;
import br.com.fc.video.adm.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCategoryServiceUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void findById_ShouldReturnCategoryWithAllFields_WhenCategoryExists() {

        Long categoryId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Category entity = new Category();
        entity.setId(categoryId);
        entity.setName("Electronics");
        entity.setDescription("Electronic devices");
        entity.setIsActive(true);
        entity.setCreatedAt(now.minusDays(1));
        entity.setUpdatedAt(now);
        entity.setDeletedAt(null);

        CategoryResponseDTO expectedDto = new CategoryResponseDTO(categoryId, "Electronics", "Electronic devices", true,
                now.minusDays(1), now, null);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Mono.just(entity));

        when(categoryMapper.toResponseDTO(entity))
                .thenReturn(expectedDto);

        Mono<CategoryResponseDTO> result = categoryService.findById(categoryId);

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        dto.id().equals(categoryId) &&
                                dto.name().equals(expectedDto.name()) &&
                                dto.description().equals(expectedDto.description()) &&
                                dto.isActive() &&
                                dto.createdAt().equals(now.minusDays(1)) &&
                                dto.updatedAt().equals(now) &&
                                dto.deletedAt() == null
                )
                .verifyComplete();
    }

    @Test
    void findById_ShouldThrowNotFoundException_WhenCategoryDoesNotExist() {

        Long nonExistentId = 999L;

        when(categoryRepository.findById(nonExistentId))
                .thenReturn(Mono.empty());

        Mono<CategoryResponseDTO> result = categoryService.findById(nonExistentId);

        StepVerifier.create(result)
                .expectErrorMatches(ex ->
                        ex instanceof CustomException &&
                                ((CustomException) ex).getStatus() == HttpStatus.NOT_FOUND &&
                                ex.getMessage().equals("Category not founded")
                )
                .verify();
    }

    @Test
    void findById_ShouldIncludeAuditableFields_WhenCategoryIsInactive() {

        Long categoryId = 2L;
        LocalDateTime now = LocalDateTime.now();

        Category entity = new Category();
        entity.setId(categoryId);
        entity.setName("Old Products");
        entity.setDescription("Discontinued items");
        entity.setIsActive(false);
        entity.setCreatedAt(now.minusMonths(1));
        entity.setUpdatedAt(now.minusDays(1));
        entity.setDeletedAt(now);

        CategoryResponseDTO expectedDto = new CategoryResponseDTO(categoryId, "Old Products", "Discontinued items", false, now.minusMonths(1),
                now.minusDays(1), now
        );

        when(categoryRepository.findById(categoryId))
                .thenReturn(Mono.just(entity));

        when(categoryMapper.toResponseDTO(entity))
                .thenReturn(expectedDto);

        Mono<CategoryResponseDTO> result = categoryService.findById(categoryId);

        StepVerifier.create(result)
                .expectNextMatches(dto ->
                        !dto.isActive() &&
                                dto.deletedAt() != null &&
                                dto.deletedAt().isAfter(dto.updatedAt())
                )
                .verifyComplete();
    }
}
