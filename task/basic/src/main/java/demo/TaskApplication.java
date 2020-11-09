package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableTask
public class TaskApplication {
    public static void main(String[] args) {
        System.setProperty("--spring.profiles.active", "mysql");
        ConfigurableApplicationContext ctx = SpringApplication.run(TaskApplication.class, args);
        for (String beanName : ctx.getBeanDefinitionNames()) {
            Object bean = ctx.getBean(beanName);
            System.out.printf("Bean:%s -> %s\n", beanName, bean.getClass().getName());
        }
    }
}
