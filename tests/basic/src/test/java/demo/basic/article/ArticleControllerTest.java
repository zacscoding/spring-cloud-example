package demo.basic.article;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ArticleService articleService;

    List<ArticleResource> articles;

    @BeforeEach
    public void setUp() {
        articles = IntStream.rangeClosed(1, 15)
                            .boxed()
                            .sorted(Comparator.reverseOrder())
                            .map(i -> ArticleResource.builder()
                                                     .slug("title" + i)
                                                     .title("title" + i)
                                                     .description("description" + i)
                                                     .author(AuthorResource.builder()
                                                                           .authorId(UUID.randomUUID().toString())
                                                                           .name("user" + i)
                                                                           .bio("user bio" + i)
                                                                           .build())
                                                     .build())
                            .collect(Collectors.toList());
    }

    // test GET /v1/articles
    @Test
    public void testGetArticles() throws Exception {
        // given
        final Page<ArticleResource> page = new PageImpl<>(articles.subList(5, 10),
                                                          PageRequest.of(1, 5, Direction.DESC, "id"), articles.size());
        given(articleService.getArticles(getPageableMatcher(1, 5))).willReturn(page);

        // when then
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/articles")
                                              .queryParam("page", "1")
                                              .queryParam("size", "5")
                                              .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content").hasJsonPath())
               .andExpect(jsonPath("$.content[*].slug").hasJsonPath())
               .andExpect(jsonPath("$.content[*].title").hasJsonPath())
               .andExpect(jsonPath("$.content[*].description").hasJsonPath())
               .andExpect(jsonPath("$.content[*].author").hasJsonPath())
               .andExpect(jsonPath("$.content[*].author.authorId").hasJsonPath())
               .andExpect(jsonPath("$.content[*].author.name").hasJsonPath())
               .andExpect(jsonPath("$.content[*].author.bio").hasJsonPath())
               .andExpect(jsonPath("$.pages").hasJsonPath())
               .andExpect(jsonPath("$.pages.first").value("/v1/articles?page=0&size=5&sort=id,DESC"))
               .andExpect(jsonPath("$.pages.prev").value("/v1/articles?page=0&size=5&sort=id,DESC"))
               .andExpect(jsonPath("$.pages.next").value("/v1/articles?page=2&size=5&sort=id,DESC"))
               .andExpect(jsonPath("$.pages.last").value("/v1/articles?page=2&size=5&sort=id,DESC"))
        ;
    }

    static Pageable getPageableMatcher(final int pageNumber, final int pageSize) {
        return argThat(p -> {
            if (p.getPageNumber() != pageNumber) {
                return false;
            }
            if (p.getPageSize() != pageSize) {
                return false;
            }
            return true;
        });
    }
}
