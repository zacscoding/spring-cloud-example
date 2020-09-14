package demo.basic.article;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleResource {

    private String slug;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    private AuthorResource author;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @JsonProperty
    public String getSlug() {
        return slug;
    }

    @JsonIgnore
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @JsonIgnore
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @JsonIgnore
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}