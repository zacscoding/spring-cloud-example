package demo.basic.article;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import demo.basic.common.resource.PagedResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    /**
     * Get articles given page request
     */
    @GetMapping("/v1/articles")
    public PagedResource<ArticleResource> getArticles(
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
        logger.info("## Request articles. pageable : {}", pageable);
        final Page<ArticleResource> page = articleService.getArticles(pageable);
        return PagedResource.toPagedResource(URI.create("/v1/articles"), page);
    }

    /**
     * Save a new article
     */
    @PostMapping("/v1/article")
    public ResponseEntity saveArticle(@Valid @RequestBody ArticleResource article, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        return ResponseEntity.ok(articleService.saveArticle(article));
    }

    /**
     * Get a article by slug
     */
    @GetMapping("/v1/article/{slug}")
    public ResponseEntity getArticle(@PathVariable("slug") String slug) {
        Optional<ArticleResource> articleOptional = articleService.getArticleBySlug(slug);
        if (!articleOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(articleOptional.get());
    }

    @DeleteMapping("/v1/article/{slug}")
    public ResponseEntity deleteArticle(@PathVariable("slug") String slug) {
        final Integer deleted = articleService.deleteArticleBySlug(slug);
        if (deleted == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
