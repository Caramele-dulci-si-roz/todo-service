package ro.unibuc.hello.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectDto {

	String id;
	String name;
	String description;
	String projectLead;
	LocalDateTime createdAt;
}
