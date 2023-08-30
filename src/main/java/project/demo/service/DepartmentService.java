package project.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import project.demo.dto.DepartmentDTO;
import project.demo.dto.PageDTO;
import project.demo.dto.SearchDTO;
import project.demo.entity.Department;
import project.demo.repository.DepartmentRepo;

public interface DepartmentService {
	void create(DepartmentDTO departmentDTO);

	DepartmentDTO update(DepartmentDTO departmentDTO);

	void delete(int id);

	DepartmentDTO getById(int id);

	PageDTO<List<DepartmentDTO>> search(SearchDTO searchDTO);
}

@Service
class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	private DepartmentRepo departmentRepo;
	
	@Autowired
	CacheManager cacheManager;

	@Override
	@Transactional
	@CacheEvict(cacheNames = "department-search", allEntries = true)
	public void create(DepartmentDTO departmentDTO) {
		// convert departmentDTO --> department
		Department department = new ModelMapper().map(departmentDTO, Department.class);
		departmentRepo.save(department);
		
		Cache cache = 
				cacheManager.getCache("department-search");
		cache.invalidate();
	}

	@Override
	@Transactional
	@Caching(evict = {
		@CacheEvict(cacheNames = "department-search", allEntries = true)
	},
	put = {
			@CachePut(cacheNames = "department", key = "#departmentDTO.id")	
	})
	public DepartmentDTO update(DepartmentDTO departmentDTO) {
		// check
		Department department = departmentRepo.findById(departmentDTO.getId())
				.orElse(null);

		if (department != null) {
			department.setName(departmentDTO.getName());
			departmentRepo.save(department);
		}
		return convert(department);
	}

	@Override
	@Transactional
	@Caching(evict = {
			@CacheEvict(cacheNames = "department", key = "#id"),
			@CacheEvict(cacheNames = "department-search", allEntries = true)
	})
	public void delete(int id) {
		departmentRepo.deleteById(id);
		
	}

	@Override
	@Cacheable(cacheNames = "department", key = "#id", unless = "#result == null")
	public DepartmentDTO getById(int id) {
		System.out.println("CHUA CO CACHE");
		//Optional
		Department department = departmentRepo.findById(id)
				.orElseThrow(NoResultException::new);
//		List<User> users = department.getUsers();
//		System.out.println(users.size());
		return convert(department);
	}
	
	private DepartmentDTO convert(Department department) {
		return new ModelMapper().map(department, DepartmentDTO.class);
	}

	@Override
	@Cacheable(cacheNames = "department-search")
	public PageDTO<List<DepartmentDTO>> search(SearchDTO searchDTO) {
		Sort sortBy = Sort.by("id").ascending(); //sep theo asc
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
		Page<Department> page = departmentRepo.searchName("%" + searchDTO.getKeyword() + "%", pageRequest);
		PageDTO<List<DepartmentDTO>> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());


		List<DepartmentDTO> departmentDTOs = page.get().map(u -> convert(u)).collect(Collectors.toList());
		pageDTO.setData(departmentDTOs);
		return pageDTO;
		}

}
