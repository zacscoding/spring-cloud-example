package demo.basic.article;

import demo.basic.account.AccountProfile;

public final class ArticleAssembler {

    /**
     * Convert {@link ArticleEntity} and {@link AccountProfile} to {@link ArticleResource}
     */
    public static ArticleResource toResource(ArticleEntity entity, AccountProfile profile) {
        return ArticleResource.builder()
                              .slug(entity.getSlug())
                              .title(entity.getTitle())
                              .author(AuthorResource.builder()
                                                    .authorId(entity.getAuthorId())
                                                    .name(profile.getName())
                                                    .bio(profile.getBio())
                                                    .build())
                              .description(entity.getDescription())
                              .createdAt(entity.getCreatedAt())
                              .updatedAt(entity.getUpdatedAt())
                              .build();
    }

    /**
     * Convert {@link ArticleResource} and {@link AccountProfile} to {@link ArticleEntity}
     */
    public static ArticleEntity toEntity(ArticleResource resource, AccountProfile profile) {
        return ArticleEntity.createArticle(resource.getTitle(), resource.getDescription(), profile.getAccountId());
    }

    private ArticleAssembler() {
    }
}
