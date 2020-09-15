package demo.basic.article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import demo.basic.account.AccountProfile;
import demo.basic.account.AccountRemoteService;
import demo.basic.helper.ArticleAssertHelper;

/**
 * Tests article service layer
 *
 * ArticleService depends on [ArticleRepository, ArticleRemoteService, CacheManager]
 * -> Use @MockBean
 */
@ExtendWith(SpringExtension.class)
public class ArticleServiceTest {

    @MockBean
    ArticleRepository articleRepository;
    @MockBean
    AccountRemoteService accountRemoteService;
    @MockBean
    CacheManager cacheManager;

    ArticleService articleService;
    String accountId;
    AccountProfile accountProfile;
    ArticleResource articleResource;
    ArticleEntity articleEntity;

    @BeforeEach
    public void setUp() {
        articleService = new ArticleService(articleRepository, accountRemoteService, cacheManager);
        accountId = UUID.randomUUID().toString();
        accountProfile = AccountProfile.builder()
                                       .accountId(accountId)
                                       .name("user1")
                                       .bio("user1 bio")
                                       .build();
        articleResource = ArticleResource.builder()
                                         .title("title1")
                                         .description("description")
                                         .build();
        articleEntity = ArticleEntity.createArticle(articleResource.getTitle(),
                                                    articleResource.getDescription(),
                                                    accountId);
    }

    @Test
    public void testGetArticles() {
        // given
        final Pageable pageRequest = PageRequest.of(1, 5);
        final Page<ArticleEntity> results = new PageImpl<>(Collections.singletonList(articleEntity));

        given(articleRepository.findAll(pageRequest)).willReturn(results);
        given(accountRemoteService.getAccountProfileById(accountId)).willReturn(accountProfile);

        // when
        final Page<ArticleResource> find = articleService.getArticles(pageRequest);

        // then
        // 1) Assert mock bean method called
        then(articleRepository).should().findAll(pageRequest);
        then(accountRemoteService).should().getAccountProfileById(accountId);

        // 2) Assert returned value from ArticleService
        assertThat(find.hasContent()).isTrue();
        assertThat(find.getContent().size()).isEqualTo(1);
        final ArticleResource resource = find.getContent().get(0);
        ArticleAssertHelper.assertArticle(articleEntity, accountProfile, resource);
    }

    @Test
    public void testSaveArticle() {
        // given
        given(accountRemoteService.getAuthenticatedAccount()).willReturn(accountProfile);
        given(articleRepository.save(any())).willReturn(articleEntity);

        // when
        final ArticleResource saved = articleService.saveArticle(articleResource);

        // then
        // 1) Assert mock bean method called
        then(accountRemoteService).should().getAuthenticatedAccount();
        then(articleRepository).should().save(argThat(a -> articleEntity.getTitle().equals(a.getTitle()) &&
                                                           articleEntity.getDescription().equals(a.getDescription())));
        then(cacheManager).should().getCache("article");

        // 2) Assert returned value from ArticleService
        assertThat(saved).isNotNull();
        ArticleAssertHelper.assertArticle(articleEntity, accountProfile, saved);
    }

    @Test
    public void testGetArticleBySlug() {
        // given
        given(articleRepository.findBySlug(articleEntity.getSlug())).willReturn(Optional.of(articleEntity));
        given(accountRemoteService.getAccountProfileById(accountId)).willReturn(accountProfile);

        // when
        final Optional<ArticleResource> findOptional = articleService.getArticleBySlug(articleEntity.getSlug());

        // then
        // 1) Assert mock bean method called
        then(articleRepository).should().findBySlug(articleEntity.getSlug());
        then(accountRemoteService).should().getAccountProfileById(accountId);

        // 2) Assert returned value from ArticleService
        assertThat(findOptional).isNotEmpty();
        ArticleAssertHelper.assertArticle(articleEntity, accountProfile, findOptional.get());
    }

    @Test
    public void testGetArticleBySlugShouldReturnEmptyIfNotExist() {
        // given
        given(articleRepository.findBySlug(articleEntity.getSlug())).willReturn(Optional.empty());

        // when
        final Optional<ArticleResource> findOptional = articleService.getArticleBySlug(articleEntity.getSlug());

        // then
        // 1) Assert mock bean method called
        then(articleRepository).should().findBySlug(articleEntity.getSlug());
        verify(accountRemoteService, never()).getAccountProfileById(any());

        // 2) Assert returned value from ArticleService
        assertThat(findOptional).isEmpty();
    }
}
