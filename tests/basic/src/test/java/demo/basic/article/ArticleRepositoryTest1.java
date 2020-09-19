package demo.basic.article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import demo.basic.configuration.JpaConfiguration;

/**
 * Tests article repository layer
 *
 * Environment: h2 database(in-memory) and {@link TestEntityManager}
 * Caution: check dialect (if use other dialect in application-XXX.yaml)
 *  - log -> org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.H2Dialect
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(JpaConfiguration.class)
@ActiveProfiles({ "test" })
@Transactional
public class ArticleRepositoryTest1 {

    @Autowired
    ApplicationContext ctx;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TestEntityManager em;

//    @BeforeEach
//    public void setUp() {
//        StringBuilder sb = new StringBuilder();
//        for (String beanName : ctx.getBeanDefinitionNames()) {
//            sb.append("name: ").append(beanName)
//              .append("class: ").append(ctx.getBean(beanName).getClass().getName())
//              .append('\n');
//        }
//        System.out.println(sb);
//    }

    @Test
    public void testFindBySlug() {
        // given
        final ArticleEntity entity = createArticleEntity();
        em.persist(entity);
        em.flush();

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
        final ArticleEntity entity1 = createArticleEntity();
        final ArticleEntity entity2 = createArticleEntity();

        em.persist(entity1);

        // when
        assertThatThrownBy(() -> articleRepository.save(entity2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void testDeleteBySlug() {
        // given
        final ArticleEntity entity = createArticleEntity();
        em.persist(entity);

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
