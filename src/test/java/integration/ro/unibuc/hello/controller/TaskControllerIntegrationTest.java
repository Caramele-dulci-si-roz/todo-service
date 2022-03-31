package integration.ro.unibuc.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ro.unibuc.hello.data.Task;
import ro.unibuc.hello.data.TaskRepository;
import ro.unibuc.hello.data.User;
import ro.unibuc.hello.data.UserRepository;
import ro.unibuc.hello.dto.AssignUser;
import ro.unibuc.hello.dto.CreateTask;
import ro.unibuc.hello.dto.UpdateTask;

import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Disabled
class TaskControllerIntegrationTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ObjectMapper objectMapper;

	@AfterEach
	void setup() {
		taskRepository.deleteAllById(taskRepository.findAllByIdStartingWith("-").stream().map(Task::getId).collect(Collectors.toList()));
		userRepository.deleteAllById(userRepository.findAllByIdStartingWith("-").stream().map(User::getId).collect(Collectors.toList()));
	}

	@Test
	@WithMockUser
	void testGetById() throws Exception {
		taskRepository.save(new Task().setId("-1"));
		mockMvc.perform(get("/task/{id}", "-1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is(not(empty()))))
				.andExpect(jsonPath("$.id", is("-1")));
	}

	@Test
	@WithMockUser
	void testGetByIdBadRequest() throws Exception {
		mockMvc.perform(get("/task/{id}", "-1"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("There is no task with this id!")));
	}

	@Test
	void testGetByIdUnauthorized() throws Exception {
		mockMvc.perform(get("/task/{id}", "-1"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void testGet() throws Exception {
		taskRepository.save(new Task().setId("-1"));
		taskRepository.save(new Task().setId("-2"));
		mockMvc.perform(get("/task"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));
	}

	@Test
	@WithMockUser
	void testGetByProjectId() throws Exception {
		taskRepository.save(new Task().setId("-1").setProjectId("-1"));
		taskRepository.save(new Task().setId("-2"));
		mockMvc.perform(get("/task")
						.param("projectId", "-1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	@WithMockUser
	void testCreate() throws Exception {
		CreateTask createTask = new CreateTask().setProjectId("-1");
		mockMvc.perform(post("/task")
						.content(objectMapper.writeValueAsString(createTask))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is("The task was successfully created.")));
		Assertions.assertEquals(1, taskRepository.findAllByProjectId("-1").size());
		taskRepository.deleteById(taskRepository.findAllByProjectId("-1").get(0).getId());
	}

	@Test
	@WithMockUser
	void testDelete() throws Exception {
		taskRepository.save(new Task().setId("-1").setProjectId("-1"));
		mockMvc.perform(delete("/task/{id}", "-1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is("The task was successfully removed.")));
		Assertions.assertTrue(taskRepository.findAllByProjectId("-1").isEmpty());
	}

	@Test
	@WithMockUser
	void testUpdate() throws Exception {
		taskRepository.save(new Task().setId("-1").setPriority("HIGH"));
		UpdateTask updateTask = new UpdateTask().setPriority("LOW");
		mockMvc.perform(put("/task/{id}", "-1")
						.content(objectMapper.writeValueAsString(updateTask))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is("The status was successfully updated.")));
		Assertions.assertEquals("LOW", taskRepository.findById("-1").get().getPriority());
	}

	@Test
	@WithMockUser
	void testUpdateValidationFail() throws Exception {
		taskRepository.save(new Task().setId("-1").setPriority("HIGH"));
		UpdateTask updateTask = new UpdateTask().setPriority("blabla");
		mockMvc.perform(put("/task/{id}", "-1")
						.content(objectMapper.writeValueAsString(updateTask))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$", is("The task cannot pe updated to this priority!")));
	}

	@Test
	@WithMockUser
	void testAssign() throws Exception {
		taskRepository.save(new Task().setId("-1").setAssigneeId("-1"));
		userRepository.save(new User().setId("-3"));
		AssignUser assignUser = new AssignUser().setAssigneeId("-3");
		mockMvc.perform(post("/task/{id}/assignment", "-1")
						.content(objectMapper.writeValueAsString(assignUser))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is("The task has been assigned successfully!")));
		Assertions.assertEquals("-3", taskRepository.findById("-1").get().getAssigneeId());
	}

}
