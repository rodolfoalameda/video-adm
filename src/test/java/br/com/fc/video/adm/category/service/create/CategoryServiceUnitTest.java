package br.com.fc.video.adm.category.service.create;

import br.com.fc.video.adm.dto.request.CategoryCreateDTO;
import br.com.fc.video.adm.dto.response.CategoryResponseDTO;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceUnitTest {

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
    void name_ShouldFailWhenBlankOrEmpty() {

        String[] invalidNames = {"", "   "};

        for (String invalidName : invalidNames) {

            CategoryCreateDTO dto = new CategoryCreateDTO(invalidName, "Descrição válida", true);
            Set<ConstraintViolation<CategoryCreateDTO>> violations = validateDTO(dto);

            assertFalse(violations.isEmpty());

            boolean hasCorrectMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("Name is required"));

            assertTrue(hasCorrectMessage);
        }
    }

    @Test
    void name_ShouldFailWhenShorterThan3Characters() {

        String[] invalidNames = {"a", "ab", "1", "12"};

        for (String invalidName : invalidNames) {

            CategoryCreateDTO dto = new CategoryCreateDTO(invalidName, "Descrição válida", true);
            Set<ConstraintViolation<CategoryCreateDTO>> violations = validateDTO(dto);

            assertFalse(violations.isEmpty());

            boolean hasCorrectMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("Name must be at least 3 characters long"));

            assertTrue(hasCorrectMessage);
        }
    }
}
