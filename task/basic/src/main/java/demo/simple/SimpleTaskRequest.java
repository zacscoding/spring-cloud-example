package demo.simple;

import java.util.List;

import lombok.Data;

@Data
public class SimpleTaskRequest {

    private List<String> names;
}
