package project.demo.entity; // data tranform object

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Table(name = "user") //map to table SQL
@Entity  
public class User {
	//id khoa chinh
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
//	@OneToMany(mappedBy = "user")
//	private List<UserRole> roles;
	
	// chi ap dung voi 2 cot, 1 cot pk
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_role",
		joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	private List<String> roles;
	
	@ManyToOne
	private Department department;
	
	private int age;
	private String name;
	//luu ten file path
	private String avatarURL;

	@Column (unique = true)
	private String username;
	
	private String password;
	
	private String homeAddress;
	
	@Temporal(TemporalType.DATE)
	private Date birthdate;
	
	private String email;
}
