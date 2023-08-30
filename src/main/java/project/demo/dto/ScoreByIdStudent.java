package project.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //tham so truyen vao
@NoArgsConstructor //mac dinh
public class ScoreByIdStudent {
	private int studentId;
	private String nameStudent;
	private String nameCourse;
	private double score;

}
