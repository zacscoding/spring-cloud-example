package demo.basic.helper;

import static org.assertj.core.api.Assertions.assertThat;

import demo.basic.account.AccountProfile;
import demo.basic.article.ArticleEntity;
import demo.basic.article.ArticleResource;

public class ArticleAssertHelper {

    /**
     * Assert expected {@link ArticleEntity} and {@link AccountProfile} are equal to {@link ArticleResource}
     */
    public static void assertArticle(ArticleEntity entity, AccountProfile profile, ArticleResource actual) {
        if (entity == null && profile == null) {
            assertThat(actual).isNull();
            return;
        }
        assertThat(actual).isNotNull();

        if (entity != null) {
            assertThat(actual.getSlug()).isEqualTo(entity.getSlug());
            assertThat(actual.getTitle()).isEqualTo(entity.getTitle());
            assertThat(actual.getDescription()).isEqualTo(entity.getDescription());
            assertThat(actual.getCreatedAt()).isEqualTo(entity.getCreatedAt());
            assertThat(actual.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());
        }

        if (profile != null) {
            assertThat(actual.getAuthor().getAuthorId()).isEqualTo(profile.getAccountId());
            assertThat(actual.getAuthor().getName()).isEqualTo(profile.getName());
            assertThat(actual.getAuthor().getBio()).isEqualTo(profile.getBio());
        }
    }

    private ArticleAssertHelper() {
    }
}
