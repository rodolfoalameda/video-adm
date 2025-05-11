package br.com.fc.video.adm.service.impl;

import br.com.fc.video.adm.dto.request.CategoryCreateDTO;
import br.com.fc.video.adm.dto.response.CategoryResponseDTO;
import br.com.fc.video.adm.exception.CustomException;
import br.com.fc.video.adm.mapper.CategoryMapper;
import br.com.fc.video.adm.model.Category;
import br.com.fc.video.adm.repository.CategoryRepository;
import br.com.fc.video.adm.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    public static final String CATEGORIA_NÃO_ENCONTRADA = "Categoria não encontrada";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Mono<CategoryResponseDTO> createCategory(CategoryCreateDTO dto) {
        Category entity = categoryMapper.toEntity(dto);
        entity.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return categoryRepository.save(entity)
                .doOnSuccess(saved -> log.info("Category successfully created: {}", saved))
                .doOnError(error -> log.error("Error creating category: {}", error.getMessage(), error))
                .map(categoryMapper::toResponseDTO);
    }

    @Override
    public Mono<CategoryResponseDTO> findById(Long id) {
        return categoryRepository.findById(id)
                .doOnSuccess(category -> log.info("Successfully accessed the database"))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, CATEGORIA_NÃO_ENCONTRADA)))
                .map(categoryMapper::toResponseDTO);
    }
}
