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

import project.demo.dto.CourseDTO;
import project.demo.dto.PageDTO;
import project.demo.dto.ResponseDTO;
import project.demo.dto.SearchDTO;
import project.demo.service.CourseService;

@RestController
@RequestMapping("/course")
public class CourseController {

	@Autowired // DI: dependency inject
	CourseService courseService;

	@PostMapping("/") // gia su: khong upload file
	public ResponseDTO<Void> create(@RequestBody @Valid CourseDTO courseDTO) {
		courseService.create(courseDTO);
		return ResponseDTO.<Void>builder().status(200).msg("Create Ok").build();
	}

	@PutMapping("/")
	public ResponseDTO<CourseDTO> edit(@RequestBody @Valid CourseDTO courseDTO) {
		courseService.update(courseDTO);

		return ResponseDTO.<CourseDTO>builder().status(200).msg("Update Ok").build();
	}

	@DeleteMapping("/")
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		courseService.delete(id);
		return ResponseDTO.<Void>builder().status(200).msg("Delete Ok").build();
	}

	@GetMapping("/")
	// @ResponseStatus(code = HttpStatus.OK)
	public ResponseDTO<CourseDTO> get(@RequestParam("id") int id) {
		return ResponseDTO.<CourseDTO>builder().status(200)
				.data(courseService.getById(id)).build();
	}

	@PostMapping("/search")
	public ResponseDTO<PageDTO<List<CourseDTO>>> search(@RequestBody @Valid SearchDTO searchDTO) {
		PageDTO<List<CourseDTO>> pageDTO = courseService.search(searchDTO);
		return ResponseDTO.<PageDTO<List<CourseDTO>>>builder().status(200)
				.data(pageDTO).build();

	}

	@GetMapping("/list")
	public ResponseDTO<List<CourseDTO>> list() {
		List<CourseDTO> studentDTOs = courseService.getAll();
		return ResponseDTO.<List<CourseDTO>>builder().status(200)
				.data(studentDTOs).build();
	}
}
