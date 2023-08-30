package project.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //tham so truyen vao
@NoArgsConstructor //mac dinh
public class SumScoreByCourse {
	private int courseId;
	private String courseName;
	private double avg;

}
