package project.demo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;


import lombok.Data;

@Entity
@Data
public class Student {
	@Id
	private int userId; //user_id
	// khi thay doi thay Student thi user cung thay doi

	@OneToOne(cascade = CascadeType.ALL, 
			fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	@MapsId //copy id user set cho id cua student
	private User user;//user_id
	
	private String studentCode;
	
	@OneToMany (mappedBy = "student")
	private List<Score> scores;
	
	// manytomany map bang ngan gon hon
//	@ManyToMany 
//	@JoinTable(name = "score",
//		joinColumns = @JoinColumn(name = "student_id"),
//		inverseJoinColumns = @JoinColumn(name = "course_id"))
//	private List<Course> courses;
}
