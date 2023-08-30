package project.demo.dto; // data tranform object

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
//ORM framework: object - Record Table
//JPA - Hibernate
//JDBC - MySQL

@Data
public class UserDTO {
	private int id;
	@Min(value = 0,message = "{min.msg}")
	private int age;
	@NotBlank(message = "{not.blank}")
	private String name;
	private String avatarURL;
	private String username;
	private String password;
	private String homeAddress;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonFormat(pattern = "dd/mm/yyyy", timezone = "Asia/Ho_Chi_Minh")
	private Date birthdate;
	//manytone
	private DepartmentDTO department;
	private List<String> roles;
	
	@JsonIgnore // bo qua file
	private MultipartFile file;
}
