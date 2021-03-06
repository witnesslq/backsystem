package operation.repo.course;


import java.util.List;

import operation.pojo.course.Knowledge;
import operation.pojo.course.Lesson;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LessonRepository extends MongoRepository<Lesson, String>  {

	List<Lesson> findByIdIn(List<String> ids);
	
	List<Lesson> findByIdIn(List<String> ids,Sort sort);

	List<Lesson> findByKnowledge(String id);
	
	Page<Lesson> findByIsUsed(int isUsed,Pageable pageable);
        
}
