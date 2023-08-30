package project.demo.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CourseDTO  {
	private int id;
	@NotBlank 
	private String name;
	
	@JsonFormat(pattern = "dd/mm/yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date createdAt;
	@JsonFormat(pattern = "dd/mm/yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date updateAt;

}
