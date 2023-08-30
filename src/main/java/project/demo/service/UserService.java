package project.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import project.demo.dto.PageDTO;
import project.demo.dto.SearchDTO;
import project.demo.dto.UserDTO;
import project.demo.entity.Department;
import project.demo.entity.User;
import project.demo.repository.UserRepo;

//@Component
@Service // tao bean: new Uservice, qly SpringContaine
public class UserService implements UserDetailsService{

	@Autowired
	UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepo.findByUsername(username);
		if (userEntity == null) {
			throw new UsernameNotFoundException("Not Found");
		}
		
		//convert userentity -> userdetails
		List<SimpleGrantedAuthority> authorities
			= new ArrayList<>();
		//chuyen vai tro ve quyen (authorities = roles)
		for(String role : userEntity.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return new org.springframework.security.core.userdetails.User(username,
				userEntity.getPassword(), authorities);
	}

	@Transactional
	public void create(UserDTO userDTO) {
		// convert userDTO --> user
		User user = new ModelMapper().map(userDTO, User.class);
		
		// conver password to brcrypt
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		userRepo.save(user);
	}

	@Transactional
	public void update(UserDTO userDTO) {
		// check
		User currentUser = userRepo.findById(userDTO.getId()).orElse(null);

		if (currentUser != null) {
			currentUser.setName(userDTO.getName());
			currentUser.setAge(userDTO.getAge());
			currentUser.setHomeAddress(userDTO.getHomeAddress());
			Department department = new Department();
			department.setId(userDTO.getDepartment().getId());
			
			currentUser.setDepartment(department);
			userRepo.save(currentUser);
		}
	}

	@Transactional
	public void updatePassword(UserDTO userDTO) {
		// check
		User currentUser = userRepo.findById(userDTO.getId()).orElse(null);
		if (currentUser != null) {
			currentUser.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
			
			userRepo.save(currentUser);
		}
	}

	@Transactional
	public void delete(int id) {
		userRepo.deleteById(id);

	}

	public UserDTO getById(int id) {
		User user = userRepo.findById(id).orElse(null);
		if (user != null) {
			return convert(user);
		}

		return null;
	}

	private UserDTO convert(User user) {
		return new ModelMapper().map(user, UserDTO.class);
	}

	public List<UserDTO> getAll() {
		List<User> userList = userRepo.findAll();

//		List<UserDTO> userDTOs = new ArrayList<>();
//		for (User user : userList) {
//			userDTOs.add(convert(user));
//		}
//		return userDTOs;

		// java 8
		// chuyen tung phan tu userList sang userDTO xog collect ve list
		return userList.stream().map(u -> convert(u)).collect(Collectors.toList());
	}

	public PageDTO<List<UserDTO>> searchName(SearchDTO searchDTO) {

		Sort sortBy = Sort.by("name").ascending().and(Sort.by("age").descending());
		// check null
		if (StringUtils.hasText(searchDTO.getSortedField())) {
			sortBy = Sort.by(searchDTO.getSortedField()).ascending();
		}

		if (searchDTO.getCurrentPage() == null) {
			searchDTO.setCurrentPage(0);
		}

		if (searchDTO.getSize() == null) {
			searchDTO.setSize(5);
		}

		PageRequest pageRequest = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getSize(), sortBy);
		Page<User> page = userRepo.searchByName("%" + searchDTO.getKeyword() + "%", pageRequest);
		// T: List<UserDTO>
		PageDTO<List<UserDTO>> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());

//		List<User> users = page.getContent(); 

		List<UserDTO> userDTOs = page.get().map(u -> convert(u)).collect(Collectors.toList());
		// T: List<UserDTO>
		pageDTO.setData(userDTOs);
		return pageDTO;
	}
}
