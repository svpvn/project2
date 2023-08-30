package project.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import project.demo.dto.PageDTO;
import project.demo.dto.SearchDTO;
import project.demo.dto.StudentDTO;
import project.demo.entity.Student;
import project.demo.repository.StudentRepo;

public interface StudentService {
	void create(StudentDTO studentDTO);

	void update(StudentDTO studentDTO);

	void delete(int id);

	StudentDTO getById(int id);

	PageDTO<List<StudentDTO>> search(SearchDTO searchDTO);

	List<StudentDTO> getAll();
}

@Service
class StudentServiceImpl implements StudentService {
	@Autowired
	StudentRepo studentRepo;

	@Override
	@Transactional
	public void create(StudentDTO studentDTO) {
//		User user = new ModelMapper().map(studentDTO.getUser(), User.class);
//		userRepo.save(user);

		// dung casecade
		// save user xog getid set vao student
//		student.setUser(user);
//		student.setStudentCode(studentDTO.getStudentCode());
		Student student = new ModelMapper().map(studentDTO, Student.class);
		studentRepo.save(student);
	}

	@Override
	@Transactional
	public void update(StudentDTO studentDTO) {
		Student student = studentRepo.findById(studentDTO.getUser().getId())
				.orElseThrow(NoResultException::new);

		student.setStudentCode(studentDTO.getStudentCode());
//		student.setUser(new ModelMapper().map(studentDTO.getUser(), User.class));
		studentRepo.save(student);
	}

	@Override
	@Transactional
	public void delete(int id) {
		studentRepo.deleteById(id);
	}

	@Override
	public StudentDTO getById(int id) {
		Student student = studentRepo.findById(id).orElseThrow(NoResultException::new);
		return convert(student);
	}

	private StudentDTO convert(Student student) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(student, StudentDTO.class);
	}

	@Override
	public PageDTO<List<StudentDTO>> search(SearchDTO searchDTO) {
		Sort sortBy = Sort.by("studentCode").ascending(); // sep theo asc
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

		if (searchDTO.getKeyword() == null) {
			searchDTO.setKeyword("");
		}

		PageRequest pageRequest = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getSize(), sortBy);
		Page<Student> page = studentRepo.searchByName("%" + searchDTO.getKeyword() + "%", pageRequest);
		PageDTO<List<StudentDTO>> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());

		List<StudentDTO> studentDTOs = page.get().map(u -> convert(u)).collect(Collectors.toList());
		pageDTO.setData(studentDTOs);
		return pageDTO;
	}

	@Override
	public List<StudentDTO> getAll() {
		List<Student> studentList = studentRepo.findAll();
		// java 8
		// chuyen tung phan tu userList sang userDTO xog collect ve list
		return studentList.stream().map(u -> convert(u)).collect(Collectors.toList());
	}

}