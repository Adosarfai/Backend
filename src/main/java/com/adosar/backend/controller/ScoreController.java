package com.adosar.backend.controller;

import com.adosar.backend.business.ScoreManager;
import com.adosar.backend.business.request.score.GetAllScoresRequest;
import com.adosar.backend.business.request.score.GetScoreByIdRequest;
import com.adosar.backend.business.request.score.GetScoresByMapIdRequest;
import com.adosar.backend.business.response.score.GetAllScoresResponse;
import com.adosar.backend.business.response.score.GetScoreByIdResponse;
import com.adosar.backend.business.response.score.GetScoresByMapIdResponse;
import com.adosar.backend.domain.Score;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"https://dev.adosar.io:5173", "https://adosar.io", "https://localhost:5137"})
@RequestMapping(path = "/score")
@AllArgsConstructor
@Builder
public class ScoreController {
	private final ScoreManager scoreManager;

	@GetMapping(path = "/all/{page}")
	public @ResponseBody ResponseEntity<Iterable<Score>> getAllScores(@PathVariable Integer page) {
		GetAllScoresRequest request = new GetAllScoresRequest(page);
		GetAllScoresResponse response = scoreManager.getAllScores(request);
		return new ResponseEntity<>(response.getScores(), response.getHttpStatus());
	}

	@GetMapping(path = "/{id}")
	public @ResponseBody ResponseEntity<Score> getScoreById(@PathVariable Integer id) {
		GetScoreByIdRequest request = new GetScoreByIdRequest(id);
		GetScoreByIdResponse response = scoreManager.getScoreById(request);
		return new ResponseEntity<>(response.getScore(), response.getHttpStatus());
	}

	@GetMapping(path = "/map/{id}/{page}")
	public @ResponseBody ResponseEntity<Iterable<Score>> getScoresByMapId(@PathVariable Integer id, @PathVariable Integer page) {
		GetScoresByMapIdRequest request = new GetScoresByMapIdRequest(id, page);
		GetScoresByMapIdResponse response = scoreManager.getScoresByMapId(request);
		return new ResponseEntity<>(response.getScores(), response.getHttpStatus());
	}
}