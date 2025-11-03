package cmdotender.TaskLine;


import cmdotender.TaskLine.features.dummyFeature.entity.Task;
import cmdotender.TaskLine.features.dummyFeature.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskRepositoryIT extends AbstractPostgresIT {

    @Autowired
    TaskRepository repo;

    @Test
    void create_and_read_task() {
        Task t = new Task();
        t.setTitle("First task");
        t.setDescription("created by IT");
        t.setStatus("OPEN");

        Task saved = repo.save(t);

        assertThat(saved.getId()).isNotNull();
        assertThat(repo.findById(saved.getId())).isPresent();
    }
}
