package br.com.fc.video.adm.category.entity;

import br.com.fc.video.adm.model.Category;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryUnitTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validCategory_shouldPassValidation() {

        Category category = Category.builder()
                .name("Books")
                .description("All about books")
                .isActive(true)
                .build();

        assertTrue(validator.validate(category).isEmpty());
    }

    @Test
    void emptyName_shouldFailValidation() {
        Category category = Category.builder()
                .name("")
                .description("All about books")
                .isActive(true)
                .build();

        assertFalse(validator.validate(category).isEmpty());
    }

    @Test
    void shortName_shouldFailValidation() {
        Category category = Category.builder()
                .name("A")
                .description("All about books")
                .isActive(true)
                .build();

        assertFalse(validator.validate(category).isEmpty());
    }

    @Test
    void nullName_shouldFailValidation() {
        Category category = Category.builder()
                .name(null)
                .description("All about books")
                .isActive(true)
                .build();

        assertFalse(validator.validate(category).isEmpty());
    }

    @Test
    void nullDescription_shouldPassValidation() {
        Category category = Category.builder()
                .name("Books")
                .description(null)
                .isActive(true)
                .build();

        assertTrue(validator.validate(category).isEmpty());
    }
}
