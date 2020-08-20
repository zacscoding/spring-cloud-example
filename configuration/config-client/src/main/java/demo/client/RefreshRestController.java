package demo.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RefreshScope
public class RefreshRestController {

    private final String message;

    @Autowired
    public RefreshRestController(@Value("${application.message}") String message) {
        logger.warn("## RefreshController() is called. message : {}", message);
        this.message = message;
    }

    @GetMapping("/message")
    public String getMessage() {
        return message;
    }
}
