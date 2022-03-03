package ro.unibuc.hello.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {

	String id;
	String projectId;
	String type;
	String summary;
	String description;
	String reporterId;
	String assigneeId;
	String priority;
	String status;
	LocalDateTime createdAt;

}
