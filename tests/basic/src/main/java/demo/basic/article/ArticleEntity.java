package demo.basic.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.common.base.MoreObjects;

import demo.basic.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Article")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @Column(name = "slug", unique = true)
    private String slug;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "author_id")
    private String authorId;

    /**
     * Create a new {@link ArticleEntity} given args
     */
    public static ArticleEntity createArticle(String title, String description, String authorId) {
        final ArticleEntity entity = new ArticleEntity();

        entity.setSlug(toSlug(title));
        entity.setTitle(title);
        entity.setDescription(description);
        entity.setAuthorId(authorId);

        return entity;
    }

    private static String toSlug(String title) {
        return title.toLowerCase().replaceAll("[\\&|\\uFE30-\\uFFA0|\\’|\\”|\\s\\?\\,\\.]+", "-");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(ArticleEntity.class)
                          .add("id", id)
                          .add("slug", slug)
                          .add("title", title)
                          .add("description", description)
                          .add("authorId", authorId)
                          .toString();
    }
}