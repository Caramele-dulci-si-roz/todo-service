package ro.unibuc.hello.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import ro.unibuc.hello.controller.TaskController;
import ro.unibuc.hello.data.*;
import ro.unibuc.hello.dto.CreateTask;
import ro.unibuc.hello.dto.TaskDto;
import ro.unibuc.hello.dto.UpdateTask;
import ro.unibuc.hello.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

	@InjectMocks
    TaskController taskController;

	@Mock
	TaskRepository taskRepository;

	@Mock
	ObjectMapper objectMapper;

	@Test
	void getById() {
		Task task = new Task()
				.setId("-1")
				.setAssigneeId("-1")
				.setType("BUG")
				.setStatus("OPEN")
				.setPriority("HIGH")
				.setCreatedAt(LocalDateTime.now())
				.setDescription("test")
				.setProjectId("-1")
				.setReporterId("-1");

		when(taskRepository.findById(any())).thenReturn(Optional.of(task));
		TaskDto result = taskController.getById("-1");
		Task taskResult = new Task();
		BeanUtils.copyProperties(result, taskResult);
		Assertions.assertEquals("-1", result.getId());
		Assertions.assertTrue(EqualsBuilder.reflectionEquals(task, taskResult));
	}

	@Test
	void getByIdNotFound() {
		when(taskRepository.findById(any())).thenReturn(Optional.empty());
		Throwable exception = assertThrows(BadRequestException.class, () -> taskController.getById("-1"));
		assertEquals("There is no task with this id!", exception.getMessage());
	}

	@Test
	void get() {
		when(taskRepository.findAll()).thenReturn(List.of(new Task()));
		Assertions.assertEquals(1, taskController.get(null, null).size());
	}

	@Test
	void getByProject() {
		when(taskRepository.findAllByProjectId(any())).thenReturn(List.of(new Task().setProjectId("-1")));
		Assertions.assertEquals(1, taskController.get("-1", null).size());
	}

	@Test
	void getByUser() {
		when(taskRepository.findAllByAssigneeId(any())).thenReturn(List.of(new Task().setAssigneeId("-1")));
		Assertions.assertEquals(1, taskController.get(null, "-1").size());
	}

	@Test
	void getBYProjectAndUser() {
		when(taskRepository.findAllByAssigneeIdAndProjectId(any(),any())).thenReturn(List.of(new Task().setAssigneeId("-1").setProjectId("-1")));
		Assertions.assertEquals(1, taskController.get("-1", "-1").size());
	}

	@Test
	void create() {
		CreateTask createTask = new CreateTask()
				.setSummary("test");
		taskController.create(createTask);
		ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
		verify(taskRepository).insert(captor.capture());
		Task task = captor.getValue();
		assertEquals(TaskPriority.MEDIUM.toString(), task.getPriority());
		assertEquals(TaskStatus.OPEN.toString(), task.getStatus());
		assertEquals(TaskType.TASK.toString(), task.getType());
		assertEquals("test", task.getSummary());
	}

	@Test
	void delete() {
		when(taskRepository.findById(any())).thenReturn(Optional.of(new Task().setId("-1")));
		Assertions.assertEquals("The task was successfully removed.", taskController.delete("-1").getBody());
		Assertions.assertEquals(HttpStatus.OK.value(), taskController.delete("-1").getStatusCodeValue());
	}

	@Test
	void deleteNotFound() {
		when(taskRepository.findById(any())).thenReturn(Optional.empty());
		Assertions.assertEquals("There is no task with this id!", taskController.delete("-1").getBody());
		Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), taskController.delete("-1").getStatusCodeValue());
	}

	@Test
	void update() {
		UpdateTask updateTask = new UpdateTask()
				.setSummary("update");
		when(taskRepository.findById("-1")).thenReturn(Optional.of(new Task().setId("-1").setSummary("test")));
		taskController.update("-1", updateTask);
		ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
		verify(taskRepository).save(captor.capture());
		Task task = captor.getValue();
		assertEquals("update", task.getSummary());
	}
}