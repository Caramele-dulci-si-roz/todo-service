package ro.unibuc.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.unibuc.hello.data.*;
import ro.unibuc.hello.dto.CreateProject;
import ro.unibuc.hello.dto.ProjectDto;
import ro.unibuc.hello.dto.TaskDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project")
@SecurityRequirement(name = "security")
public class ProjectController {

	final ProjectRepository projectRepository;
	final ObjectMapper objectMapper;

	@PostMapping
	public ResponseEntity<String> create(@RequestBody CreateProject createProject) {
		Project project = new Project()
				.setCreatedAt(LocalDateTime.now());
		BeanUtils.copyProperties(createProject, project);
		projectRepository.insert(project);
		return ResponseEntity.ok().body("The project has been successfully created.");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable String id) {
		Optional<Project> project = projectRepository.findById(id);
		if(project.isPresent()){
			projectRepository.deleteById(id);
			return ResponseEntity.ok().body("The project has been successfully removed.");
		} else {
			return ResponseEntity.badRequest().body("We couldn't find any project with this id.");
		}
	}

	@GetMapping
	public List<ProjectDto> get(){
		return projectRepository.findAll().stream().map(project -> objectMapper.convertValue(project, ProjectDto.class)).collect(Collectors.toList());
	}

}
