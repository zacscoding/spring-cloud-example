package demo.simple;

import org.springframework.cloud.task.repository.TaskExecution;
import org.springframework.cloud.task.repository.TaskExplorer;
import org.springframework.cloud.task.repository.TaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/task/simple")
@Slf4j
@RequiredArgsConstructor
public class SimpleTaskController {

    private final ThreadPoolTaskExecutor taskExecutor;
    private final TaskRepository taskRepository;
    private final TaskExplorer taskExplorer;

    @PostMapping("")
    public TaskExecution launchTask(@RequestBody SimpleTaskRequest taskRequest) {
        logger.info("## Request to create a new task. names: {}", taskRequest.getNames());

        final TaskExecution taskExecution = taskRepository.createTaskExecution(SimpleTask.SIMPLE_TASK_NAME);
        final SimpleTask task = new SimpleTask(taskRepository, taskExecution, taskRequest.getNames());

        taskExecutor.submit(task);

        return taskExecution;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskExecution> getTask(@PathVariable("id") long executionId) {
        logger.info("## Request to get a task. id: {}", executionId);

        TaskExecution task = taskExplorer.getTaskExecution(executionId);
        if (task == null) {
            logger.warn("not found task execution. id: {}", executionId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(task);
    }

}
