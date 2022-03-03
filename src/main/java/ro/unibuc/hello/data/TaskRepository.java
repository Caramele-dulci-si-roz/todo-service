package ro.unibuc.hello.data;


import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {

	List<Task> findAllByProjectId(String projectId);

}
