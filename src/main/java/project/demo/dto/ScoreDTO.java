package project.demo.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import project.demo.entity.Course;
import project.demo.entity.Student;

@Data
public class ScoreDTO {
	private int id;
	private double score;
	
	private Student student;
	private Course course;
	
	@JsonFormat(pattern = "dd/mm/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date createdAt;
	@JsonFormat(pattern = "dd/mm/yyyy HH:mm", timezone = "Asia/Ho_Chi_Minh")
	private Date updateAt;
}
