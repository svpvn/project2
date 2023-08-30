package project.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import project.demo.dto.DepartmentDTO;
import project.demo.dto.PageDTO;
import project.demo.dto.ResponseDTO;
import project.demo.dto.SearchDTO;
import project.demo.service.DepartmentService;

//ws : REST
@RestController
@RequestMapping("/department")
public class DepartmentController {
	@Autowired // DI : dependency inject
	DepartmentService departmentService;

	@PostMapping("/")
	// @ModelAttribute map tat cac thuoc tinh input vao userDTO
	public ResponseDTO<Void> create(
			@ModelAttribute @Valid DepartmentDTO departmentDTO) {
		departmentService.create(departmentDTO);
		return ResponseDTO.<Void>builder()
				.status(200).msg("Create Ok").build();
	}
	// neu day len dang JSON, dung dang RequestBody
	// Json ko upload dc file	
	@PostMapping("/json")
	public ResponseDTO<Void> createNewJSON(
			@RequestBody @Valid DepartmentDTO departmentDTO) {
		departmentService.create(departmentDTO);
		return ResponseDTO.<Void>builder()
				.status(200).msg("Create Ok").build();
	}
	
	@PutMapping("/")
	public ResponseDTO<Void> edit(
			@ModelAttribute DepartmentDTO departmentDTO) {
		departmentService.update(departmentDTO);

		return ResponseDTO.<Void>builder()
				.status(200).msg("Update Ok").build();
	}
	//HTTP STATUS CODE:
	@DeleteMapping("/")
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		departmentService.delete(id);
		return ResponseDTO.<Void>builder()
				.status(200).msg("Delete Ok").build();
	}
	
	@GetMapping("/")
	@ResponseStatus(code = HttpStatus.OK)
	//@Secured({"ROLE_ADMIN", "ROLE_SYSADMIN"}) //ROLE_
//	@RolesAllowed({"ROLE_ADMIN", "ROLE_SYSADMIN"})
	@PreAuthorize("isAuthenticated()")
//	@PostAuthorize
	public ResponseDTO<DepartmentDTO> get(@RequestParam("id") int id){
		return ResponseDTO.<DepartmentDTO>builder()
				.status(200)
				.data(departmentService.getById(id)).build();
	}

	@PostMapping("/search") 
	public ResponseDTO<PageDTO<List<DepartmentDTO>>> search(@ModelAttribute @Valid SearchDTO searchDTO) {
		PageDTO<List<DepartmentDTO>> pageDTO = departmentService.search(searchDTO);
		return ResponseDTO.<PageDTO<List<DepartmentDTO>>>builder()
				.status(200)
				.data(pageDTO).build();
	}
}
