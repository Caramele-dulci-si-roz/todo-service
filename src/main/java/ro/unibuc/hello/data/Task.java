package ro.unibuc.hello.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Getter
@Setter
public class Task {

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
