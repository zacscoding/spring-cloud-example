package demo.basic.article;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {

    /**
     * Returns a optional of {@link ArticleEntity} given slug value
     */
    Optional<ArticleEntity> findBySlug(String slug);

    /**
     * Delete a article with given slug
     */
    @Modifying
    @Query("DELETE FROM Article a WHERE a.slug=:slug")
    Integer deleteBySlug(@Param("slug") String slug);
}
