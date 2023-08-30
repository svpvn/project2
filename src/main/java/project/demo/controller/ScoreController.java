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

import project.demo.dto.AvgScoreByCourse;
import project.demo.dto.PageDTO;
import project.demo.dto.ResponseDTO;
import project.demo.dto.ScoreByIdStudent;
import project.demo.dto.ScoreDTO;
import project.demo.dto.SearchScoreDTO;
import project.demo.dto.SumScoreByCourse;
import project.demo.service.ScoreService;

@RestController
@RequestMapping("/score")
public class ScoreController {

	@Autowired // DI: dependency inject
	ScoreService scoreService;

	@PostMapping("/") // gia su: khong upload file
	public ResponseDTO<Void> newUser(@RequestBody @Valid ScoreDTO scoreDTO) {
		scoreService.create(scoreDTO);
		return ResponseDTO.<Void>builder().status(200).msg("Create Ok").build();
	}

	@PutMapping("/")
	public ResponseDTO<Void> edit(@RequestBody @Valid ScoreDTO scoreDTO) {
		scoreService.update(scoreDTO);

		return ResponseDTO.<Void>builder().status(200).msg("Update Ok").build();
	}

	@DeleteMapping("/")
	public ResponseDTO<Void> delete(@RequestParam("id") int id) {
		scoreService.delete(id);
		return ResponseDTO.<Void>builder().status(200).msg("Delete Ok").build();
	}

	@GetMapping("/")
	// @ResponseStatus(code = HttpStatus.OK)
	public ResponseDTO<ScoreDTO> get(@RequestParam("id") int id) {
		return ResponseDTO.<ScoreDTO>builder().status(200)
				.data(scoreService.getById(id)).build();
	}

	@PostMapping("/search") 
	public ResponseDTO<PageDTO<List<ScoreDTO>>> search(@RequestBody @Valid SearchScoreDTO searchScoreDTO) {
		PageDTO<List<ScoreDTO>> pageDTO = scoreService.search(searchScoreDTO);
		return ResponseDTO.<PageDTO<List<ScoreDTO>>>builder().status(200)
				.data(pageDTO).build();
	}

	@GetMapping("/avg-score-by-course")
	public ResponseDTO<List<AvgScoreByCourse>> avgScoreByCourse() {
		return ResponseDTO.<List<AvgScoreByCourse>>builder().status(200)
				.data(scoreService.avgScoreByCourse()).build();
	}
	
	@GetMapping("/score-by-id-student")
	public ResponseDTO<List<ScoreByIdStudent>> searchScoreById(@RequestParam("id") int id) {
		return ResponseDTO.<List<ScoreByIdStudent>>builder().status(200)
				.data(scoreService.searchScoreById(id)).build();
	}
	
	@GetMapping("/sum-score-by-course")
	public ResponseDTO<List<SumScoreByCourse>> sumScoreByCourse() {
		return ResponseDTO.<List<SumScoreByCourse>>builder().status(200)
				.data(scoreService.sumScoreByCourse()).build();
	}
	
}
