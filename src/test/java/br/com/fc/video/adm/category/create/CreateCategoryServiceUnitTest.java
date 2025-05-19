package br.com.fc.video.adm.category.create;

import br.com.fc.video.adm.dto.request.CategoryCreateDTO;
import br.com.fc.video.adm.dto.response.CategoryResponseDTO;
import br.com.fc.video.adm.exception.CustomException;
import br.com.fc.video.adm.mapper.CategoryMapper;
import br.com.fc.video.adm.model.Category;
import br.com.fc.video.adm.repository.CategoryRepository;
import br.com.fc.video.adm.service.impl.CategoryServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCategoryServiceUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Set<ConstraintViolation<CategoryCreateDTO>> validateDTO(CategoryCreateDTO dto) {
        return validator.validate(dto);
    }

    @Test
    void createCategory_ShouldReturnResponseWithCorrectName() {

        CategoryCreateDTO input = new CategoryCreateDTO("Livros", "Descrição", true);

        Category entity = new Category();
        entity.setName("Livros");
        entity.setDescription("Descrição");
        entity.setIsActive(true);

        Category savedEntity = new Category();
        savedEntity.setId(1L);
        savedEntity.setName("Livros");
        savedEntity.setDescription("Descrição");
        savedEntity.setIsActive(true);
        savedEntity.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        when(categoryMapper.toEntity(input)).thenReturn(entity);
        when(categoryRepository.save(any(Category.class))).thenReturn(Mono.just(savedEntity));
        when(categoryMapper.toResponseDTO(savedEntity))
                .thenReturn(new CategoryResponseDTO(1L, "Livros", "Descrição", true,
                        savedEntity.getCreatedAt(), null, null));

        StepVerifier.create(categoryService.createCategory(input))
                .assertNext(response -> {
                    assertEquals("Livros", response.name());
                    assertEquals(1L, response.id());
                    assertNotNull(response.createdAt());
                })
                .verifyComplete();
    }

    @Test
    void createCategory_WhenRepositoryFails_ShouldReturnCustomException() {

        CategoryCreateDTO validDTO = new CategoryCreateDTO("Livros", "Descrição válida", true);
        Category validEntity = new Category();
        validEntity.setName("Livros");

        RuntimeException simulatedError = new RuntimeException("Database connection failed");

        when(categoryMapper.toEntity(validDTO)).thenReturn(validEntity);
        when(categoryRepository.save(validEntity)).thenReturn(Mono.error(simulatedError));

        StepVerifier.create(categoryService.createCategory(validDTO))
                .expectErrorSatisfies(error -> {

                    assertTrue(error instanceof CustomException);

                    CustomException customEx = (CustomException) error;

                    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, customEx.getStatus());
                    assertEquals("Error creating category", customEx.getMessage());
                })
                .verify();
    }
}
