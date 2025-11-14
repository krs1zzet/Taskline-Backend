//// TaskServiceTest.java
//package cmdotender.TaskLine;
//
//import cmdotender.TaskLine.features.dummyFeature.dto.TaskCreateRequest;
//import cmdotender.TaskLine.features.dummyFeature.dto.TaskDto;
//import cmdotender.TaskLine.features.dummyFeature.entity.Task;
//import cmdotender.TaskLine.features.dummyFeature.repository.TaskRepository;
//import cmdotender.TaskLine.features.dummyFeature.service.TaskService;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TaskServiceTest {
//
//    TaskRepository repo = mock(TaskRepository.class);
//    TaskService service = new TaskService(repo);
//
//    @Test
//    void getTask_found_returnsDto() {
//        Task t = new Task();
//        t.setId(42L);
//        t.setTitle("t");
//        t.setStatus("OPEN");
//        when(repo.findById(42L)).thenReturn(Optional.of(t));
//
//        TaskDto dto = service.getTask(42L);
//
//        assertThat(dto.id()).isEqualTo(42L);
//        assertThat(dto.title()).isEqualTo("t");
//        verify(repo).findById(42L);
//    }
//
//    @Test
//    void getTask_missing_throwsNotFound() {
//        when(repo.findById(99L)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> service.getTask(99L))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("id");
//
//    }
//
//    @Test
//    void createTask_maps_and_saves() {
//        TaskCreateRequest req = new TaskCreateRequest("A", "B");
//        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
//        when(repo.save(any(Task.class))).thenAnswer(inv -> {
//            Task in = inv.getArgument(0);
//            in.setId(10L);
//            return in;
//        });
//
//        TaskDto dto = service.createTask(req);
//
//        verify(repo).save(captor.capture());
//        Task saved = captor.getValue();
//        assertThat(saved.getTitle()).isEqualTo("A");
//        assertThat(dto.id()).isEqualTo(10L);
//    }
//}
