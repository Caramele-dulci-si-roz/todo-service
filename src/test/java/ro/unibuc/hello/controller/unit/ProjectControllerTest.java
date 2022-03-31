package ro.unibuc.hello.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ro.unibuc.hello.controller.ProjectController;
import ro.unibuc.hello.data.ProjectRepository;
import org.junit.jupiter.api.Assertions;
import ro.unibuc.hello.data.*;
import ro.unibuc.hello.dto.CreateProject;
import ro.unibuc.hello.dto.ProjectDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @InjectMocks
    ProjectController projectController;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    ObjectMapper objectMapper;



    @Test
    void get() {
        when(projectRepository.findAll()).thenReturn(List.of(new Project()));
        Assertions.assertEquals(1, projectController.get().size());
    }

    @Test
    void create() {
        CreateProject createProject = new CreateProject()
                .setName("test");
        projectController.create(createProject);
        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).insert(captor.capture());
        Project project = captor.getValue();
        assertEquals("test", project.getName());
        Assertions.assertEquals("The project has been successfully created.",projectController.create(createProject).getBody());

    }

    @Test
    void delete() {
        when(projectRepository.findById(any())).thenReturn(Optional.of(new Project().setId("0")));
        Assertions.assertEquals("The project has been successfully removed.", projectController.delete("0").getBody());
        Assertions.assertEquals(HttpStatus.OK.value(),projectController.delete("0").getStatusCodeValue());
    }

    @Test
    void deleteBadRequest() {
        when(projectRepository.findById(any())).thenReturn(Optional.empty());
        Assertions.assertEquals("We couldn't find any project with this id.", projectController.delete("0").getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),projectController.delete("0").getStatusCodeValue());
    }


}