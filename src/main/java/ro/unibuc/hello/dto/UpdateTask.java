package ro.unibuc.hello.dto;

import lombok.Getter;
import lombok.Setter;
import ro.unibuc.hello.data.TaskPriority;
import ro.unibuc.hello.data.TaskStatus;
import ro.unibuc.hello.data.TaskType;
import ro.unibuc.hello.validation.ValueOfEnum;

@Getter
@Setter
public class UpdateTask {

	@ValueOfEnum(enumClass = TaskType.class, message = "The task cannot pe updated to this type!")
	String type;
	String summary;
	String description;
	String assigneeId;
	@ValueOfEnum(enumClass = TaskPriority.class, message = "The task cannot pe updated to this priority!")
	String priority;
	@ValueOfEnum(enumClass = TaskStatus.class, message = "The task cannot pe updated to this status!")
	String status;

}
