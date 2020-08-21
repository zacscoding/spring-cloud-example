package article;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class ArticleServerApplication {
    public static void main(String[] args) {
        logger.warn("## start article server with : {}", Arrays.toString(args));
        SpringApplication.run(ArticleServerApplication.class, args);
    }
}
