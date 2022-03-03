package ro.unibuc.hello.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateTask {

	String projectId;
	String type;
	String summary;
	String description;
	String reporterId;
	String assigneeId;
	String priority;

}
