package article;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final RestTemplate restTemplate;

    @GetMapping("/articles")
    public List<Article> getArticles() {
        final List<Article> articles = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            final Article article = new Article();
            article.setTitle("title-" + i);

            // fetch author
            final String uri = UriComponentsBuilder.fromUriString("//account-service")
                                                   .pathSegment("accounts", String.valueOf(i))
                                                   .build().toUriString();

            final ResponseEntity<JsonNode> response = restTemplate.getForEntity(uri, JsonNode.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                logger.error("failed to fetch author. {}", response);
                throw new RuntimeException("internal error");
            }
            if (!response.getBody().has("name")) {
                logger.error("not exist in account response. body: {}", response.getBody());
                throw new RuntimeException("internal error");
            }
            article.setAuthor(Author.builder()
                                    .id((long) i)
                                    .name(response.getBody().get("name").asText())
                                    .build());
            articles.add(article);
        }
        return articles;
    }

    @Getter
    @Setter
    public static class Article {
        private String title;
        private Author author;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Author {
        private Long id;
        private String name;
    }
}
