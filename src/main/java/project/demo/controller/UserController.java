package project.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.demo.dto.PageDTO;
import project.demo.dto.ResponseDTO;
import project.demo.dto.SearchDTO;
import project.demo.dto.UserDTO;
import project.demo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired // DI
	UserService userService;

	@PostMapping("/")
	// @ModelAttribute map tat cac thuoc tinh input vao userDTO
	public ResponseDTO<Void> newUser(@ModelAttribute @Valid UserDTO userDTO) throws IllegalStateException, IOException {
		if (!userDTO.getFile().isEmpty()) {
			// ten file upload
			String filename = userDTO.getFile().getOriginalFilename();
			// luu lai file vao o cung may chu
			File saveFile = new File("D:\\" + filename);
			userDTO.getFile().transferTo(saveFile);
			// lay ten file luu xuong db
			userDTO.setAvatarURL(filename);
		}
		userService.create(userDTO);
		return ResponseDTO.<Void>builder().status(200).msg("Create Ok").build();
	}

	@GetMapping("/list")
	public ResponseDTO<List<UserDTO>> list() {
		List<UserDTO> UserDTOs = userService.getAll();
		return ResponseDTO.<List<UserDTO>>builder().status(200).data(UserDTOs).build();
	}

	@GetMapping("/download")
	public void download(@RequestParam("filename") String filename, HttpServletResponse resp) throws IOException {
		File file = new File("D:/" + filename);
		Files.copy(file.toPath(), resp.getOutputStream());
	}

	@DeleteMapping("/")
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		userService.delete(id);
		return ResponseDTO.<Void>builder().status(200).msg("Delete Ok").build();
	}

	@PostMapping("/search")
	public ResponseDTO<PageDTO<List<UserDTO>>> search(@ModelAttribute @Valid SearchDTO searchDTO) {
		PageDTO<List<UserDTO>> pageUser = userService.searchName(searchDTO);

		return ResponseDTO.<PageDTO<List<UserDTO>>>builder().status(200).data(pageUser).build();
	}

	@PutMapping("/edit")
	public ResponseDTO<UserDTO> edit(@ModelAttribute UserDTO userDTO) throws IllegalStateException, IOException {
		if (!userDTO.getFile().isEmpty()) {
			// ten file upload
			String filename = userDTO.getFile().getOriginalFilename();
			// luu lai file vao o cung may chu
			File saveFile = new File("D:\\" + filename);
			userDTO.getFile().transferTo(saveFile);
			// lay ten file luu xuong db
			userDTO.setAvatarURL(filename);
		}
		userService.update(userDTO);
		return ResponseDTO.<UserDTO>builder().status(200).data(userDTO).build();
	}
}
