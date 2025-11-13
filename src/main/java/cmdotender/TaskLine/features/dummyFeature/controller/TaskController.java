package cmdotender.TaskLine.features.dummyFeature.controller;


import cmdotender.TaskLine.features.dummyFeature.dto.TaskCreateRequest;
import cmdotender.TaskLine.features.dummyFeature.dto.TaskDto;
import cmdotender.TaskLine.features.dummyFeature.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public TaskDto get(@PathVariable Long id) {
        return service.getTask(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto create(@Valid @RequestBody TaskCreateRequest req) {
        return service.createTask(req);
    }


}
