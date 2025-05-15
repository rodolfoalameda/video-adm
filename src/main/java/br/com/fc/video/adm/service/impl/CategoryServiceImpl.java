package br.com.fc.video.adm.service.impl;

import br.com.fc.video.adm.dto.request.CategoryCreateDTO;
import br.com.fc.video.adm.dto.request.CategoryUpdateDTO;
import br.com.fc.video.adm.dto.response.CategoryResponseDTO;
import br.com.fc.video.adm.exception.CustomException;
import br.com.fc.video.adm.mapper.CategoryMapper;
import br.com.fc.video.adm.model.Category;
import br.com.fc.video.adm.repository.CategoryRepository;
import br.com.fc.video.adm.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    public static final String CATEGORIA_NÃO_ENCONTRADA = "Category not founded";
    public static final String CATEGORY_ALREADY_DEACTIVATED = "Category already deactivated";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Mono<CategoryResponseDTO> createCategory(CategoryCreateDTO dto) {
        Category entity = categoryMapper.toEntity(dto);
        entity.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return categoryRepository.save(entity)
                .doOnSuccess(saved -> log.info("Category successfully created"))
                .map(categoryMapper::toResponseDTO)
                .onErrorResume(Exception.class, error -> {
                    log.error("Error creating category: {}", error.getMessage(), error);
                    return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating category"));
                });
    }

    @Override
    public Mono<CategoryResponseDTO> findById(Long id) {
        return categoryRepository.findById(id)
                .doOnSuccess(category -> log.info("Successfully accessed the database"))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, CATEGORIA_NÃO_ENCONTRADA)))
                .map(categoryMapper::toResponseDTO);
    }

    @Override
    public Mono<CategoryResponseDTO> updateCategory(Long id, CategoryUpdateDTO dto) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, CATEGORIA_NÃO_ENCONTRADA)))
                .flatMap(category -> {
                    categoryMapper.updateCategoryFromDto(dto, category);
                    category.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
                    return categoryRepository.save(category);
                })
                .map(categoryMapper::toResponseDTO)
                .onErrorResume(Exception.class, error -> {
                    log.error("Error updating category: {}", error.getMessage(), error);
                    return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating category"));
                });
    }

    @Override
    public Mono<Void> deactivateCategory(Long id) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, CATEGORIA_NÃO_ENCONTRADA)))
                .flatMap(category -> {
                    if (Boolean.FALSE.equals(category.getIsActive())) {
                        return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, CATEGORY_ALREADY_DEACTIVATED));
                    }
                    category.setIsActive(false);
                    category.setDeletedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
                    return categoryRepository.save(category);
                })
                .doOnSuccess(saved -> log.info("Category successfully deactivated"))
                .onErrorResume(Exception.class, error -> {
                    log.error("Error creating category: {}", error.getMessage(), error);
                    return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deactivating category"));
                })
                .then();
    }

    @Override
    public Mono<Void> reactivateCategory(Long id) {
        return categoryRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, CATEGORIA_NÃO_ENCONTRADA)))
                .flatMap(category -> {
                    if (Boolean.TRUE.equals(category.getIsActive())) {
                        return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Category already activated"));
                    }
                    category.setIsActive(true);
                    category.setDeletedAt(null);
                    return categoryRepository.save(category);
                })
                .doOnSuccess(saved -> log.info("Category successfully reactivated"))
                .onErrorResume(Exception.class, error -> {
                    log.error("Error creating category: {}", error.getMessage(), error);
                    return Mono.error(new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reactivating category"));
                })
                .then();
    }

    @Override
    public Mono<Page<CategoryResponseDTO>> findAllCategory(Pageable pageable) {
        return categoryRepository.findAllBy(pageable)
                .map(categoryMapper::toResponseDTO)
                .collectList()
                .zipWith(categoryRepository.countAll())
                .map(tuple -> new PageImpl<>(
                        tuple.getT1(),
                        pageable,
                        tuple.getT2()
                ));
    }
}
