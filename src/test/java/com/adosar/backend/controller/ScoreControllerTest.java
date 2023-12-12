package com.adosar.backend.controller;

import com.adosar.backend.business.ScoreManager;
import com.adosar.backend.business.request.score.GetAllScoresRequest;
import com.adosar.backend.business.request.score.GetScoreByIdRequest;
import com.adosar.backend.business.request.score.GetScoresByMapIdRequest;
import com.adosar.backend.business.response.score.GetAllScoresResponse;
import com.adosar.backend.business.response.score.GetScoreByIdResponse;
import com.adosar.backend.business.response.score.GetScoresByMapIdResponse;
import com.adosar.backend.domain.Score;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SuppressWarnings("UnusedShould")
class ScoreControllerTest {
	// GET request to /score/all/{page} returns a response with a list of scores and a success status code
	@Test
	void test_get_all_scores_returns_list_of_scores_and_success_status_code() {
		// Arrange
		ScoreManager scoreManager = mock(ScoreManager.class);
		ScoreController scoreController = new ScoreController(scoreManager);
		GetAllScoresRequest request = GetAllScoresRequest.builder()
				.page(1)
				.build();
		GetAllScoresResponse expectedResponse = GetAllScoresResponse.builder()
				.scores(Collections.singletonList(new Score()))
				.httpStatus(HttpStatus.OK)
				.build();

		when(scoreManager.getAllScores(request)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<Iterable<Score>> response = scoreController.getAllScores(request.getPage());

		// Assert
		assertEquals(expectedResponse.getScores(), response.getBody());
		assertEquals(expectedResponse.getHttpStatus(), response.getStatusCode());
	}

	// GET request to /score/{id} returns a response with a score and a success status code
	@Test
	void test_get_score_by_id_returns_response_with_score_and_success_status_code() {
		// Arrange
		ScoreManager scoreManager = mock(ScoreManager.class);
		ScoreController scoreController = new ScoreController(scoreManager);
		Integer id = 1;
		GetScoreByIdRequest request = new GetScoreByIdRequest(id);
		Score score = new Score();
		HttpStatus httpStatus = HttpStatus.OK;
		GetScoreByIdResponse expectedResponse = new GetScoreByIdResponse(score, httpStatus);
		when(scoreManager.getScoreById(request)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<Score> response = scoreController.getScoreById(id);

		// Assert
		assertEquals(expectedResponse.getHttpStatus(), response.getStatusCode());
		assertEquals(expectedResponse.getScore(), response.getBody());
		verify(scoreManager).getScoreById(request);
	}

	// GET request to /score/map/{id}/{page} returns a response with a list of scores and a success status code
	@Test
	void test_get_scores_by_map_id_returns_list_of_scores_and_success_status_code() {
		// Arrange
		ScoreManager scoreManager = mock(ScoreManager.class);
		ScoreController scoreController = new ScoreController(scoreManager);
		Integer id = 1;
		Integer page = 0;
		GetScoresByMapIdRequest request = GetScoresByMapIdRequest.builder()
				.id(id)
				.page(page)
				.build();
		Iterable<Score> scores = Arrays.asList(
				Score.builder().build(),
				Score.builder().build()
		);
		HttpStatus httpStatus = HttpStatus.OK;
		GetScoresByMapIdResponse response = GetScoresByMapIdResponse.builder()
				.scores(scores)
				.httpStatus(httpStatus)
				.build();
		when(scoreManager.getScoresByMapId(request)).thenReturn(response);

		// Act
		ResponseEntity<Iterable<Score>> result = scoreController.getScoresByMapId(id, page);

		// Assert
		assertEquals(scores, result.getBody());
		assertEquals(httpStatus, result.getStatusCode());
		verify(scoreManager).getScoresByMapId(request);
	}

	// GET request to /score/all/{page} with invalid page number returns a response with an error status code
	@Test
	void test_get_all_scores_with_invalid_page_number_returns_error_status_code() {
		// Arrange
		ScoreManager scoreManager = mock(ScoreManager.class);
		ScoreController scoreController = new ScoreController(scoreManager);
		Integer invalidPage = -1;
		GetAllScoresRequest request = GetAllScoresRequest.builder()
				.page(invalidPage)
				.build();
		GetAllScoresResponse response = GetAllScoresResponse.builder()
				.httpStatus(HttpStatus.BAD_REQUEST)
				.build();
		when(scoreManager.getAllScores(request)).thenReturn(response);

		// Act
		ResponseEntity<Iterable<Score>> result = scoreController.getAllScores(invalidPage);

		// Assert
		assertEquals(response.getScores(), result.getBody());
		assertEquals(response.getHttpStatus(), result.getStatusCode());
		verify(scoreManager).getAllScores(request);
	}

	// GET request to /score/map/{id}/{page} with invalid id or page number returns a response with an error status code
	@Test
	void test_get_scores_by_map_id_with_invalid_id_or_page_number_returns_error_status_code() {
		// Arrange
		ScoreManager scoreManager = mock(ScoreManager.class);
		ScoreController scoreController = new ScoreController(scoreManager);
		Integer invalidId = -1;
		Integer invalidPage = -1;
		GetScoresByMapIdRequest request = GetScoresByMapIdRequest.builder()
				.id(invalidId)
				.page(invalidPage)
				.build();
		GetScoresByMapIdResponse response = GetScoresByMapIdResponse.builder()
				.scores(null)
				.httpStatus(HttpStatus.BAD_REQUEST)
				.build();

		// Mock the behavior of scoreManager.getScoresByMapId() to return the response
		when(scoreManager.getScoresByMapId(request)).thenReturn(response);

		// Act
		ResponseEntity<Iterable<Score>> result = scoreController.getScoresByMapId(invalidId, invalidPage);

		// Assert
		assertEquals(response.getScores(), result.getBody());
		assertEquals(response.getHttpStatus(), result.getStatusCode());
		verify(scoreManager).getScoresByMapId(request);
	}

	// GET request to /score/map/{id}/{page} with non-existent map id returns a response with an empty list of scores and a success status code
	@Test
	void test_get_scores_by_map_id_with_non_existent_map_id_returns_empty_list_of_scores_and_success_status_code() {
		// Arrange
		ScoreManager scoreManager = mock(ScoreManager.class);
		ScoreController scoreController = new ScoreController(scoreManager);
		Integer id = 123;
		Integer page = 1;
		GetScoresByMapIdRequest request = GetScoresByMapIdRequest.builder()
				.id(id)
				.page(page)
				.build();
		GetScoresByMapIdResponse response = GetScoresByMapIdResponse.builder()
				.scores(Collections.emptyList())
				.httpStatus(HttpStatus.OK)
				.build();
		when(scoreManager.getScoresByMapId(request)).thenReturn(response);

		// Act
		ResponseEntity<Iterable<Score>> result = scoreController.getScoresByMapId(id, page);

		// Assert
		assertEquals(response.getScores(), result.getBody());
		assertEquals(response.getHttpStatus(), result.getStatusCode());
		verify(scoreManager).getScoresByMapId(request);
	}

	// GET request to /score/map/{id}/{page} with non-existent page number returns a response with an empty list of scores and a success status code
	@Test
	void test_get_scores_by_map_id_with_non_existent_page_number() {
		// Arrange
		ScoreManager scoreManager = mock(ScoreManager.class);
		ScoreController scoreController = new ScoreController(scoreManager);
		Integer id = 1;
		Integer page = 100;
		GetScoresByMapIdRequest request = GetScoresByMapIdRequest.builder()
				.id(id)
				.page(page)
				.build();
		GetScoresByMapIdResponse expectedResponse = GetScoresByMapIdResponse.builder()
				.scores(Collections.emptyList())
				.httpStatus(HttpStatus.OK)
				.build();

		// Mock the behavior of scoreManager.getScoresByMapId() to return the expected response
		when(scoreManager.getScoresByMapId(request)).thenReturn(expectedResponse);

		// Act
		ResponseEntity<Iterable<Score>> response = scoreController.getScoresByMapId(id, page);

		// Assert
		assertEquals(expectedResponse.getScores(), response.getBody());
		assertEquals(expectedResponse.getHttpStatus(), response.getStatusCode());
	}

	// ScoreController returns a response with a success status code when ScoreManager returns an empty response
	@Test
	void test_score_controller_returns_success_status_code_when_score_manager_returns_empty_response() {
		// Arrange
		ScoreManager scoreManager = mock(ScoreManager.class);
		ScoreController scoreController = new ScoreController(scoreManager);
		GetAllScoresRequest getAllScoresRequest = GetAllScoresRequest.builder()
				.page(1)
				.build();
		GetAllScoresResponse getAllScoresResponse = GetAllScoresResponse.builder()
				.scores(Collections.emptyList())
				.httpStatus(HttpStatus.OK)
				.build();

		// Mock the behavior of scoreManager.getAllScores() to return an empty response
		when(scoreManager.getAllScores(getAllScoresRequest)).thenReturn(getAllScoresResponse);

		// Act
		ResponseEntity<Iterable<Score>> responseEntity = scoreController.getAllScores(getAllScoresRequest.getPage());

		// Assert
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(Collections.emptyList(), responseEntity.getBody());
	}
}