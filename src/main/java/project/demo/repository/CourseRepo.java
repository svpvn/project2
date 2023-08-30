package project.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.demo.entity.Course;


public interface CourseRepo extends JpaRepository<Course, Integer> {
	@Query("SELECT d FROM Course d WHERE d.name LIKE :x ")
	Page<Course> searchName(@Param("x") String name,Pageable pageable);
}
