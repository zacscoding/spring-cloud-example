package demo.simple;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.cloud.task.repository.TaskRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SimpleTask implements Runnable {

    public static final String SIMPLE_TASK_NAME = "SIMPLE-TASK";

    private final TaskRepository taskRepository;
    private final TaskExecution taskExecution;
    private final List<String> names;

    @Override
    public void run() {
        try {
            logger.info("Start to simple task. id: {}", taskExecution.getExecutionId());
            taskRepository.startTaskExecution(taskExecution.getExecutionId(), SIMPLE_TASK_NAME, new Date(),
                                              names, taskExecution.getExternalExecutionId());

            long sleep = new Random().nextInt(5);
            boolean throwEx = new Random().nextInt(100) % 3 == 0;

            logger.info("Task[{}] will sleep {} seconds with throw exception?{}", taskExecution.getExecutionId(),
                        sleep, throwEx);

            TimeUnit.SECONDS.sleep(sleep);

            if (throwEx) {
                throw new Exception("force exception");
            }

            String exitMessage = names.stream().map(n -> n.toUpperCase()).collect(Collectors.joining(","));
            taskRepository.completeTaskExecution(taskExecution.getExecutionId(), 0, new Date(), exitMessage);
        } catch (Exception e) {
            taskRepository.completeTaskExecution(taskExecution.getExecutionId(), 1, new Date(), "failed to execute..",
                                                 e.getMessage());
        }
    }
}
