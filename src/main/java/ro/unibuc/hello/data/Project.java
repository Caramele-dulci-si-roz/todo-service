package ro.unibuc.hello.data;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document
public class Project {


    String id;
    String name;
    String description;
    String projectLead;
    LocalDateTime createdAt;

}
