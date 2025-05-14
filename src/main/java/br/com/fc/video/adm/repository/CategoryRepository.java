package br.com.fc.video.adm.repository;

import br.com.fc.video.adm.model.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {

    Flux<Category> findAllBy(Pageable pageable);

    @Query("SELECT COUNT(*) FROM category")
    Mono<Long> countAll();

}
