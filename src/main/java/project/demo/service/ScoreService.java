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

import project.demo.dto.AvgScoreByCourse;
import project.demo.dto.PageDTO;
import project.demo.dto.ScoreByIdStudent;
import project.demo.dto.ScoreDTO;
import project.demo.dto.SearchScoreDTO;
import project.demo.dto.SumScoreByCourse;
import project.demo.entity.Course;
import project.demo.entity.Score;
import project.demo.entity.Student;
import project.demo.repository.CourseRepo;
import project.demo.repository.ScoreRepo;
import project.demo.repository.StudentRepo;

public interface ScoreService {
	void create(ScoreDTO scoreDTO);

	void update(ScoreDTO scoreDTO);

	void delete(int id);

	ScoreDTO getById(int id);

	PageDTO<List<ScoreDTO>> search(SearchScoreDTO searchScoreDTO);

	List<AvgScoreByCourse> avgScoreByCourse();
	
	List<ScoreByIdStudent> searchScoreById(int id);
	
	List<SumScoreByCourse> sumScoreByCourse();
	
	
}

@Service
class ScoreServiceImpl implements ScoreService {
	@Autowired
	ScoreRepo scoreRepo;

	@Autowired
	StudentRepo studentRepo;

	@Autowired
	CourseRepo courseRepo;

	@Override
	@Transactional
	public void create(ScoreDTO scoreDTO) {
		Score score = new ModelMapper().map(scoreDTO, Score.class);
		scoreRepo.save(score);
	}

	@Override
	@Transactional
	public void update(ScoreDTO scoreDTO) {
		Score score = scoreRepo.findById(scoreDTO.getId())
				.orElseThrow(NoResultException::new);
		score.setScore(scoreDTO.getScore());
		
		Student student = studentRepo.findById(scoreDTO
				.getStudent().getUser().getId()).orElseThrow(NoResultException::new);
		score.setStudent(student);

		Course course = courseRepo.findById(scoreDTO
				.getCourse().getId()).orElseThrow(NoResultException::new);
		score.setCourse(course);

		scoreRepo.save(score);
	}

	@Override
	@Transactional
	public void delete(int id) {
		scoreRepo.deleteById(id);
	}

	@Override
	public ScoreDTO getById(int id) {
		Score score = scoreRepo.findById(id).orElseThrow(NoResultException::new);
		return convert(score);
	}

	private ScoreDTO convert(Score score) {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return modelMapper.map(score, ScoreDTO.class);
	}

	@Override
	public PageDTO<List<ScoreDTO>> search(SearchScoreDTO searchScoreDTO) {
		Sort sortBy = Sort.by("id").ascending(); // sep theo asc
		// check null
		if (StringUtils.hasText(searchScoreDTO.getSortedField())) {
			sortBy = Sort.by(searchScoreDTO.getSortedField()).ascending();
		}

		if (searchScoreDTO.getCurrentPage() == null) {
			searchScoreDTO.setCurrentPage(0);
		}

		if (searchScoreDTO.getSize() == null) {
			searchScoreDTO.setSize(5);
		}

		if (searchScoreDTO.getKeyword() == null) {
			searchScoreDTO.setKeyword("");
		}

		PageRequest pageRequest = PageRequest.of(searchScoreDTO.getCurrentPage(), searchScoreDTO.getSize(), sortBy);
		Page<Score> page = null;
		if (searchScoreDTO.getCourseId() != null) {
			page = scoreRepo.searchByCourse(searchScoreDTO.getCourseId(), pageRequest);
		} else if (searchScoreDTO.getStudentId() != null) {
			page = scoreRepo.searchByStudent(searchScoreDTO.getStudentId(), pageRequest);
		} else {
			page = scoreRepo.findAll(pageRequest);
		}

		PageDTO<List<ScoreDTO>> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(page.getTotalPages());
		pageDTO.setTotalElements(page.getTotalElements());

		List<ScoreDTO> scoreDTOs = page.get().map(u -> convert(u)).collect(Collectors.toList());
		pageDTO.setData(scoreDTOs);
		return pageDTO;
	}

	@Override
	public List<AvgScoreByCourse> avgScoreByCourse() {
		return scoreRepo.avgScoreByCourse();
	}
	
	@Override
	public List<ScoreByIdStudent> searchScoreById(int id) {
		return scoreRepo.searchScoreById(id);
	}

	@Override
	public List<SumScoreByCourse> sumScoreByCourse() {
		return scoreRepo.sumScoreByCourse();
	}

}