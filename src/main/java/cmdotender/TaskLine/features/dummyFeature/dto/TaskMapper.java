package cmdotender.TaskLine.features.dummyFeature.dto;


import cmdotender.TaskLine.features.dummyFeature.entity.Task;

public final class TaskMapper {
    private TaskMapper() {}

    public static Task toEntity(TaskCreateRequest r) {
        Task t = new Task();
        t.setTitle(r.title());
        t.setDescription(r.description());
        t.setStatus("OPEN");
        return t;
    }

    public static TaskDto toDto(Task t) {
        return new TaskDto(t.getId(), t.getTitle(), t.getDescription(), t.getStatus());
    }
}
