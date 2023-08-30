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
import org.springframework.util.StringUtils;

import project.demo.dto.CourseDTO;
import project.demo.dto.PageDTO;
import project.demo.dto.SearchDTO;
import project.demo.entity.Course;
import project.demo.repository.CourseRepo;

public interface CourseService {
	void create(CourseDTO courseDTO);

	void update(CourseDTO courseDTO);

	void delete(int id);

	CourseDTO getById(int id);
	
	PageDTO<List<CourseDTO>> search(SearchDTO searchDTO);
	
	List<CourseDTO> getAll();
}

@Service 
class CourseServiceImpl implements CourseService {
	@Autowired
	CourseRepo courseRepo; 
	
	@Override
	public void create(CourseDTO courseDTO) {
		Course course = new ModelMapper().map(courseDTO, Course.class);
		courseRepo.save(course);
	}

	@Override
	public void update(CourseDTO courseDTO) {
		Course course = courseRepo.findById(courseDTO.getId()).orElse(null);
		
		if (course != null) {
			course.setName(courseDTO.getName());
			courseRepo.save(course);
		}
	}

	@Override
	public void delete(int id) {
		courseRepo.deleteById(id);
		
	}

	@Override
	public CourseDTO getById(int id) {
		Course course = courseRepo.findById(id).orElseThrow(NoResultException::new);
		return convert(course);
	}
	
	private CourseDTO convert(Course course) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(course, CourseDTO.class);
	}

	@Override
	public PageDTO<List<CourseDTO>> search(SearchDTO searchDTO) {
		Sort sortBy = Sort.by("name").ascending(); //sep theo asc
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
		Page<Course> page = courseRepo.searchName("%" + searchDTO.getKeyword() + "%", pageRequest);
		PageDTO<List<CourseDTO>> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());


		List<CourseDTO> courseDTOs = page.get().map(u -> convert(u)).collect(Collectors.toList());
		pageDTO.setData(courseDTOs);
		return pageDTO;
	}

	@Override
	public List<CourseDTO> getAll() {
		List<Course> courseList = courseRepo.findAll();
		// java 8
		// chuyen tung phan tu userList sang userDTO xog collect ve list
		return courseList.stream().map(u -> convert(u)).collect(Collectors.toList());
	}
	
}
