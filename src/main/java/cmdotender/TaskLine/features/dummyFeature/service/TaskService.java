package cmdotender.TaskLine.features.dummyFeature.service;

import cmdotender.TaskLine.features.dummyFeature.dto.TaskCreateRequest;
import cmdotender.TaskLine.features.dummyFeature.dto.TaskDto;
import cmdotender.TaskLine.features.dummyFeature.dto.TaskMapper;
import cmdotender.TaskLine.features.dummyFeature.entity.Task;
import cmdotender.TaskLine.features.dummyFeature.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskRepository repo;

    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    public TaskDto getTask(Long id) {
        Task t = repo.findById(id).orElseThrow(() -> new RuntimeException("id"));
        return TaskMapper.toDto(t);
    }

    public TaskDto createTask(TaskCreateRequest req) {
        Task t = TaskMapper.toEntity(req);
        t = repo.save(t);
        return TaskMapper.toDto(t);
    }
}
