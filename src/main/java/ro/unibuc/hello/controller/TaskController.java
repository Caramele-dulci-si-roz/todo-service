package ro.unibuc.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.data.*;
import ro.unibuc.hello.dto.CreateTask;
import ro.unibuc.hello.dto.TaskDto;
import ro.unibuc.hello.exception.BadRequestException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/task")
@RequiredArgsConstructor
@RestController
public class TaskController {

	final TaskRepository taskRepository;
	final ObjectMapper objectMapper;

	@GetMapping("/{id}")
	public TaskDto getById (@PathVariable String id){
		Optional<Task> optionalTask = taskRepository.findById(id);
		if(optionalTask.isPresent()){
			TaskDto taskDto = new TaskDto();
			BeanUtils.copyProperties(optionalTask.get(), taskDto);
			return taskDto;
		} else {
			throw new BadRequestException("There is no task with this id!");
		}
	}

	@GetMapping()
	public List<TaskDto> getByProjectId (@RequestParam(required = false) String projectId){
		if(Objects.isNull(projectId)){
			return taskRepository.findAll().stream().map(task -> objectMapper.convertValue(task, TaskDto.class)).collect(Collectors.toList());
		} else {
			return taskRepository.findAllByProjectId(projectId).stream().map(task -> objectMapper.convertValue(task, TaskDto.class)).collect(Collectors.toList());
		}
	}

	@PostMapping()
	public ResponseEntity<String> create(@RequestBody CreateTask createTask) {
		Task task = new Task()
				.setCreatedAt(LocalDateTime.now())
				// TODO: Change with logged user id
				.setReporterId(null)
				.setPriority(TaskPriority.MEDIUM.toString())
				.setStatus(TaskStatus.OPEN.toString())
				.setType(TaskType.TASK.toString());
		BeanUtils.copyProperties(createTask, task);
		taskRepository.insert(task);
		return ResponseEntity.ok().body("The task was successfully created.");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable String id){
		Optional<Task> optionalTask = taskRepository.findById(id);
		if(optionalTask.isPresent()){
			taskRepository.delete(optionalTask.get());
			return ResponseEntity.ok().body("The task was successfully removed.");
		} else {
			return ResponseEntity.badRequest().body("There is no task with this id!");
		}
	}

}
