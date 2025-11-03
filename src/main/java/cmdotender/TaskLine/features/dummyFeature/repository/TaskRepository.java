package cmdotender.TaskLine.features.dummyFeature.repository;

import cmdotender.TaskLine.features.dummyFeature.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {}
