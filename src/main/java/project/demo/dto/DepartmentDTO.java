package project.demo.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DepartmentDTO {
	private int id;
	@NotBlank
	private String name;
	private Date createdAt;
	private Date updateAt;

}
