package demo.basic.common.resource;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import demo.basic.common.resource.Pages.PagesBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResource<T> {

    private List<T> content;
    private Pages pages;

    public static <T> PagedResource<T> toPagedResource(URI uri, Page<T> page) {
        Assert.notNull(uri, "uri");
        Assert.notNull(page, "page");

        if (!page.hasContent()) {
            return new PagedResource<>(Collections.emptyList(), new Pages());
        }

        final PagesBuilder builder = Pages.builder();
        final Pageable pageable = page.getPageable();
        final Sort sort = pageable.getSort();
        final Optional<String> sortOptional = Optional.ofNullable(sort.isUnsorted() ? null :
                                                                  sort.get()
                                                                      .map(o -> o.getProperty() + "," + o.getDirection()
                                                                                                         .toString())
                                                                      .collect(Collectors.joining()));
        final int pageSize = pageable.getPageSize();

        if (!page.isFirst()) {
            builder.first(buildUri(uri, 0, pageSize, sortOptional));
        }
        if (page.hasPrevious()) {
            builder.prev(buildUri(uri, page.getNumber() - 1, pageSize, sortOptional));
        }
        if (page.hasNext()) {
            builder.next(buildUri(uri, page.getNumber() + 1, pageSize, sortOptional));
        }
        if (!page.isLast()) {
            builder.last(buildUri(uri, page.getTotalPages() - 1, pageSize, sortOptional));
        }

        return new PagedResource<>(page.getContent(), builder.build());
    }

    private static String buildUri(URI uri, int page, int size, Optional<String> sortOptional) {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(uri)
                                                                    .queryParam("page", page)
                                                                    .queryParam("size", size);
        sortOptional.ifPresent(s -> uriBuilder.queryParam("sort", s));
        return uriBuilder.toUriString();
    }
}
