package project.demo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchScoreDTO extends SearchDTO {
	private Integer studentId ;
	private Integer courseId ;
}
