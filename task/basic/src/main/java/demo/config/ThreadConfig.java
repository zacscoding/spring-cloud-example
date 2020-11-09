package demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadConfig {

    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        return createThreadPoolExecutor(0, 50, 3, "sample");
    }

    private ThreadPoolTaskExecutor createThreadPoolExecutor(int corePoolSize, int maxPoolSize,
                                                            int awaitTerminationSeconds, String threadNamePrefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        if (corePoolSize <= 0) {
            corePoolSize = Runtime.getRuntime().availableProcessors();
        }

        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        executor.initialize();
        executor.setDaemon(true);

        return executor;
    }
}
