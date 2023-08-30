package project.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.demo.dto.AvgScoreByCourse;
import project.demo.dto.ScoreByIdStudent;
import project.demo.dto.SumScoreByCourse;
import project.demo.entity.Score;


public interface ScoreRepo 
	extends JpaRepository<Score, Integer> {
	@Query("SELECT s FROM Score s WHERE s.student.userId = :sid ")
	Page<Score> searchByStudent(@Param("sid") int studentId, Pageable pageable);
	@Query("SELECT s FROM Score s WHERE s.course.id = :cid ")
	Page<Score> searchByCourse(@Param("cid") int courseId, Pageable pageable);
	//trung binh score theo course
	@Query("SELECT new project.demo.dto.AvgScoreByCourse("
			+ "c.id, c.name, AVG(s.score) "
			+ ")"
			+ "FROM Score s JOIN s.course c GROUP BY c.id,c.name ")
	List<AvgScoreByCourse> avgScoreByCourse();
	// cach 2
	@Query("SELECT c.id, c.name, AVG(s.score) "
			+ "FROM Score s JOIN s.course c GROUP BY c.id,c.name ")
	List<Object[]> avgScoreByCourse2();
	
	// in diem theo id sinh vien   s.student.user.name, s.course.name, s.score
	@Query("SELECT new project.demo.dto.ScoreByIdStudent("
			+ "s.student.id, s.student.user.name, s.course.name, s.score) "
			+ "FROM Score s JOIN s.student st JOIN s.course c WHERE st.id = :sid ")
	List<ScoreByIdStudent> searchScoreById(@Param("sid") int studentId);
	
	//tong score theo course
	@Query("SELECT new project.demo.dto.SumScoreByCourse("
			+ "c.id, c.name, SUM(s.score)"
			+ ")"
			+ "FROM Score s JOIN s.course c GROUP BY c.id,c.name ")
	List<SumScoreByCourse> sumScoreByCourse();
}
