package project.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.demo.dto.PageDTO;
import project.demo.dto.ResponseDTO;
import project.demo.dto.SearchDTO;
import project.demo.dto.StudentDTO;
import project.demo.service.StudentService;

@RestController
@RequestMapping("/student")
public class StudentController {

	@Autowired // DI: dependency inject
	StudentService studentService;

	@PostMapping("/") // gia su: khong upload file
	public ResponseDTO<Void> newUser(@RequestBody @Valid StudentDTO studentDTO) {
		studentService.create(studentDTO);
		return ResponseDTO.<Void>builder().status(200).msg("Create Ok").build();
	}

	@PutMapping("/")
	public ResponseDTO<Void> edit(@RequestBody @Valid StudentDTO studentDTO) {
		studentService.update(studentDTO);

		return ResponseDTO.<Void>builder().status(200).msg("Update Ok").build();
	}

	@DeleteMapping("/")
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		studentService.delete(id);
		return ResponseDTO.<Void>builder()
				.status(200).msg("Delete Ok").build();
	}

	@GetMapping("/")
	// @ResponseStatus(code = HttpStatus.OK)
	public ResponseDTO<StudentDTO> get(@RequestParam("id") int id) {
		return ResponseDTO.<StudentDTO>builder().status(200)
				.data(studentService.getById(id)).build();
	}
	
	@PostMapping("/search") 
	public ResponseDTO<PageDTO<List<StudentDTO>>> search(@RequestBody @Valid SearchDTO searchDTO) {
		PageDTO<List<StudentDTO>> pageDTO = studentService.search(searchDTO);
		return ResponseDTO.<PageDTO<List<StudentDTO>>>builder().status(200)
				.data(pageDTO).build();
	}
	@GetMapping("/list")
	public ResponseDTO<List<StudentDTO>> list() {
		List<StudentDTO> studentDTOs = studentService.getAll();
		return ResponseDTO.<List<StudentDTO>>builder().status(200).data(studentDTOs).build();
	}
	

}
