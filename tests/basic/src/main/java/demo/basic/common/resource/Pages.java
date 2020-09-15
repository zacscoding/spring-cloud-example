package demo.basic.common.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pages {

    @Default
    private String first = "";
    @Default
    private String prev = "";
    @Default
    private String last = "";
    @Default
    private String next = "";
}