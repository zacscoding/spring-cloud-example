package demo.basic.article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import demo.basic.configuration.JpaConfiguration;
import demo.basic.helper.LogLevelUtil;

/**
 *
 */

/**
 * Tests article repository layer
 *
 * Environment: jdbc database(docker) with test container
 * See configure: https://github.com/testcontainers/testcontainers-java/issues/2290
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
@Import(JpaConfiguration.class)
@ActiveProfiles({ "test", "mysql-docker" })
@Transactional
public class ArticleRepositoryTest2 {

    static {
        LogLevelUtil.setInfo();
    }

    @Container
    private static final MySQLContainer<?> mySqlContainer = new MySQLContainer<>("mysql:8.0.20");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySqlContainer::getJdbcUrl);
        registry.add("spring.datasource.hikari.jdbc-url", mySqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySqlContainer::getUsername);
        registry.add("spring.datasource.password", mySqlContainer::getPassword);
    }

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void testFindBySlug() {
        // given
        final ArticleEntity entity = articleRepository.save(createArticleEntity());

        // when
        Optional<ArticleEntity> findOptional = articleRepository.findBySlug(entity.getSlug());

        // then
        assertThat(findOptional.isPresent()).isTrue();
        ArticleEntity find = findOptional.get();
        assertThat(find.getSlug()).isNotEmpty();
        assertThat(find.getTitle()).isEqualTo(entity.getTitle());
        assertThat(find.getDescription()).isEqualTo(entity.getDescription());
        assertThat(find.getAuthorId()).isEqualTo(entity.getAuthorId());
        assertThat(find.getCreatedAt()).isNotNull();
        assertThat(find.getUpdatedAt()).isNotNull();
    }

    @Test
    public void testSaveArticleThrowExceptionIfSaveDuplicateTitle() {
        // given
        articleRepository.save(createArticleEntity());
        ArticleEntity entity2 = createArticleEntity();

        // when
        assertThatThrownBy(() -> articleRepository.save(entity2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testDeleteBySlug() {
        // given
        final ArticleEntity entity = articleRepository.save(createArticleEntity());

        // when
        int deleted = articleRepository.deleteBySlug(entity.getSlug());

        // then
        assertThat(deleted).isEqualTo(1);
    }

    @Test
    public void testDeleteBySlugShouldReturnZeroIfNotExist() {
        // when
        int deleted = articleRepository.deleteBySlug("title1");

        // then
        assertThat(deleted).isEqualTo(0);
    }

    private ArticleEntity createArticleEntity() {
        return ArticleEntity.createArticle("title1", "description1",
                                           UUID.randomUUID().toString());
    }
}
