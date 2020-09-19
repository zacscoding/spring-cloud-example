package demo.basic.article;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import demo.basic.account.AccountProfile;
import demo.basic.account.AccountRemoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final AccountRemoteService accountService;
    private final CacheManager cacheManager;

    public Page<ArticleResource> getArticles(Pageable pageable) {
        Page<ArticleEntity> page = articleRepository.findAll(pageable);

        if (!page.hasContent()) {
            return Page.empty();
        }

        final List<ArticleResource> resources = page.getContent().stream().map(entity -> {
            final AccountProfile profile = getAccountProfileById(
                    entity.getAuthorId());
            return ArticleAssembler.toResource(entity, profile);
        }).collect(Collectors.toList());

        return new PageImpl<>(resources, pageable, page.getTotalElements());
    }

    @Transactional
    public ArticleResource saveArticle(ArticleResource articleResource) {
        final AccountProfile profile = accountService.getAuthenticatedAccount();
        final ArticleEntity saved = articleRepository.save(ArticleAssembler.toEntity(articleResource, profile));
        final ArticleResource resource = ArticleAssembler.toResource(saved, profile);

        try {
            final Cache cache = cacheManager.getCache("article");

            if (cache != null) {
                cache.put(resource.getSlug(), resource);
            }
        } catch (Exception ignored) {
            logger.warn("failed to put article. reason: {}", ignored.toString());
        }
        return resource;
    }

    @Cacheable(value = "article", key = "#slug")
    public Optional<ArticleResource> getArticleBySlug(String slug) {
        Optional<ArticleEntity> entityOptional = articleRepository.findBySlug(slug);
        if (!entityOptional.isPresent()) {
            return Optional.empty();
        }

        final ArticleEntity entity = entityOptional.get();
        final AccountProfile profile = getAccountProfileById(entity.getAuthorId());

        return Optional.of(ArticleAssembler.toResource(entity, profile));
    }

    @CacheEvict(value = "article", key = "#slug")
    @Transactional
    public Integer deleteArticleBySlug(String slug) {
        return articleRepository.deleteBySlug(slug);
    }

    private AccountProfile getAccountProfileById(String accountId) {
        return accountService.getAccountProfileById(accountId);
    }
}
