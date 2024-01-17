package com.adosar.backend.business.impl;

import com.adosar.backend.business.converter.ScoreConverter;
import com.adosar.backend.business.request.score.GetAllScoresRequest;
import com.adosar.backend.business.request.score.GetScoreByIdRequest;
import com.adosar.backend.business.request.score.GetScoresByMapIdRequest;
import com.adosar.backend.business.response.score.GetAllScoresResponse;
import com.adosar.backend.business.response.score.GetScoreByIdResponse;
import com.adosar.backend.business.response.score.GetScoresByMapIdResponse;
import com.adosar.backend.domain.Score;
import com.adosar.backend.persistence.ReplayRepository;
import com.adosar.backend.persistence.ScoreRepository;
import com.adosar.backend.persistence.entity.MapEntity;
import com.adosar.backend.persistence.entity.ReplayEntity;
import com.adosar.backend.persistence.entity.ScoreEntity;
import com.adosar.backend.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("UnusedShould")
class ScoreManagerImplTest {
	// Should return an empty list with status OK when getAllScores is called with a page number greater than the number of pages
	@Test
	void test_getAllScores_returns_empty_list_with_OK_when_page_number_greater_than_number_of_pages() {
		// Arrange
		ScoreRepository scoreRepository = mock(ScoreRepository.class);
		ReplayRepository replayRepository = mock(ReplayRepository.class);
		ScoreManagerImpl scoreManager = new ScoreManagerImpl(scoreRepository, replayRepository);
		GetAllScoresRequest request = GetAllScoresRequest.builder()
				.page(5)
				.build();
		List<ScoreEntity> scoreEntities = new ArrayList<>();
		when(scoreRepository.findAll(PageRequest.of(request.getPage(), 10))).thenReturn(new PageImpl<>(scoreEntities));

		// Act
		GetAllScoresResponse response = scoreManager.getAllScores(request);

		// Assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getHttpStatus());
		assertNotNull(response.getScores());
		assertTrue(StreamSupport.stream(response.getScores().spliterator(), false).toList().isEmpty());
	}

	// Should return all scores with status OK when getAllScores is called with a valid page number
	@Test
	void test_should_return_all_scores_with_status_ok_when_get_all_scores_called_with_valid_page_number() {
		// Arrange
		ScoreRepository scoreRepository = mock(ScoreRepository.class);
		ReplayRepository replayRepository = mock(ReplayRepository.class);
		ScoreManagerImpl scoreManager = new ScoreManagerImpl(scoreRepository, replayRepository);
		GetAllScoresRequest request = GetAllScoresRequest.builder()
				.page(0)
				.build();
		List<ScoreEntity> scoreEntities = new ArrayList<>();
		scoreEntities.add(ScoreEntity.builder()
				.scoreId(1)
				.map(MapEntity.builder().build())
				.user(UserEntity.builder().build())
				.speed(1.0f)
				.timeSet(new Date())
				.replay(ReplayEntity.builder().build())
				.points(100)
				.build());
		scoreEntities.add(ScoreEntity.builder()
				.scoreId(2)
				.map(MapEntity.builder().build())
				.user(UserEntity.builder().build())
				.speed(2.0f)
				.timeSet(new Date())
				.replay(ReplayEntity.builder().build())
				.points(200)
				.build());
		List<Score> expectedScores = scoreEntities.stream()
				.map(ScoreConverter::convert)
				.collect(Collectors.toList());
		when(scoreRepository.findAll(PageRequest.of(request.getPage(), 10))).thenReturn(new PageImpl<>(scoreEntities));

		// Act
		GetAllScoresResponse response = scoreManager.getAllScores(request);

		// Assert
		assertEquals(expectedScores, response.getScores());
		assertEquals(HttpStatus.OK, response.getHttpStatus());
	}

	// Should return an empty list with status OK when getScoresByMapId is called with a page number greater than the number of pages
	@Test
	void test_getScoresByMapId_returns_empty_list_with_status_OK_when_page_number_greater_than_number_of_pages() {
		// Arrange
		ScoreRepository scoreRepository = mock(ScoreRepository.class);
		ReplayRepository replayRepository = mock(ReplayRepository.class);
		ScoreManagerImpl scoreManager = new ScoreManagerImpl(scoreRepository, replayRepository);
		GetScoresByMapIdRequest request = GetScoresByMapIdRequest.builder()
				.id(1)
				.page(2)
				.build();
		List<ScoreEntity> scoreEntities = new ArrayList<>();
		when(scoreRepository.getScoreEntitiesByMap_MapId(request.getId(), PageRequest.of(request.getPage(), 10))).thenReturn(Optional.of(scoreEntities));

		// Act
		GetScoresByMapIdResponse response = scoreManager.getScoresByMapId(request);

		// Assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getHttpStatus());
		assertNotNull(response.getScores());
		assertTrue(StreamSupport.stream(response.getScores().spliterator(), false).toList().isEmpty());
	}

	// Should return a score with status OK when getScoreById is called with a valid score ID
	@Test
	void test_should_return_score_with_status_OK_when_getScoreById_called_with_valid_score_ID() {
		// Arrange
		ScoreRepository scoreRepository = mock(ScoreRepository.class);
		ReplayRepository replayRepository = mock(ReplayRepository.class);
		ScoreManagerImpl scoreManager = new ScoreManagerImpl(scoreRepository, replayRepository);
		GetScoreByIdRequest request = GetScoreByIdRequest.builder().id(1).build();
		ScoreEntity scoreEntity = ScoreEntity.builder()
				.scoreId(1)
				.map(MapEntity.builder().build())
				.user(UserEntity.builder().build())
				.speed(1.0f)
				.timeSet(new Date())
				.replay(ReplayEntity.builder().build())
				.points(100)
				.build();
		Score score = ScoreConverter.convert(scoreEntity);
		when(scoreRepository.findById(1)).thenReturn(Optional.of(scoreEntity));

		// Act
		GetScoreByIdResponse response = scoreManager.getScoreById(request);

		// Assert
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getHttpStatus());
		assertNotNull(response.getScore());
		assertEquals(score.getScoreId(), response.getScore().getScoreId());
		assertEquals(score.getMap(), response.getScore().getMap());
		assertEquals(score.getUser(), response.getScore().getUser());
		assertEquals(score.getSpeed(), response.getScore().getSpeed());
		assertEquals(score.getTimeSet(), response.getScore().getTimeSet());
		assertEquals(score.getReplay(), response.getScore().getReplay());
		assertEquals(score.getPoints(), response.getScore().getPoints());
	}

	// Should return a not found status when getScoresByMapId is called with a non-existent map ID
	@Test
	void test_getScoresByMapId_returns_not_found_when_map_id_not_found() {
		// Arrange
		ScoreRepository scoreRepository = mock(ScoreRepository.class);
		ReplayRepository replayRepository = mock(ReplayRepository.class);
		ScoreManagerImpl scoreManager = new ScoreManagerImpl(scoreRepository, replayRepository);
		GetScoresByMapIdRequest request = GetScoresByMapIdRequest.builder()
				.id(1)
				.page(0)
				.build();

		// Mock the behavior of scoreRepository.getScoreEntitiesByMap_MapId() to return an empty Optional
		when(scoreRepository.getScoreEntitiesByMap_MapId(request.getId(), PageRequest.of(request.getPage(), 10))).thenReturn(Optional.empty());

		// Act
		GetScoresByMapIdResponse response = scoreManager.getScoresByMapId(request);

		// Assert
		assertNull(response.getScores());
		assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
	}

	// Should return a bad request status when getScoreById is called with a score ID less than 1
	@Test
	void test_should_return_bad_request_when_get_score_by_id_called_with_score_id_less_than_1() {
		// Arrange
		ScoreRepository scoreRepository = mock(ScoreRepository.class);
		ReplayRepository replayRepository = mock(ReplayRepository.class);
		ScoreManagerImpl scoreManager = new ScoreManagerImpl(scoreRepository, replayRepository);
		GetScoreByIdRequest request = GetScoreByIdRequest.builder()
				.id(0)
				.build();

		// Act
		GetScoreByIdResponse response = scoreManager.getScoreById(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
		assertNull(response.getScore());
	}

	// Should return a bad request status when getScoresByMapId is called with a map ID less than 1
	@Test
	void test_getScoresByMapId_returns_bad_request_when_map_id_less_than_1() {
		// Arrange
		ScoreRepository scoreRepository = mock(ScoreRepository.class);
		ReplayRepository replayRepository = mock(ReplayRepository.class);
		ScoreManagerImpl scoreManager = new ScoreManagerImpl(scoreRepository, replayRepository);
		GetScoresByMapIdRequest request = GetScoresByMapIdRequest.builder()
				.id(0)
				.page(0)
				.build();

		// Act
		GetScoresByMapIdResponse response = scoreManager.getScoresByMapId(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
		assertNull(response.getScores());
	}

	// Should return all scores for a map with status OK when getScoresByMapId is called with a valid map ID and page number
	@Test
	void test_get_scores_by_map_id_returns_all_scores_with_status_OK() {
		// Arrange
		ScoreRepository scoreRepository = mock(ScoreRepository.class);
		ReplayRepository replayRepository = mock(ReplayRepository.class);
		ScoreManagerImpl scoreManager = new ScoreManagerImpl(scoreRepository, replayRepository);
		GetScoresByMapIdRequest request = GetScoresByMapIdRequest.builder()
				.id(1)
				.page(0)
				.build();
		List<ScoreEntity> scoreEntities = new ArrayList<>();
		scoreEntities.add(ScoreEntity.builder()
				.scoreId(1)
				.map(MapEntity.builder()
						.mapId(1)
						.build())
				.user(UserEntity.builder()
						.userId(1)
						.build())
				.speed(1.0f)
				.timeSet(new Date())
				.replay(ReplayEntity.builder()
						.replayId(1)
						.build())
				.points(100)
				.build());
		scoreEntities.add(ScoreEntity.builder()
				.scoreId(2)
				.map(MapEntity.builder()
						.mapId(1)
						.build())
				.user(UserEntity.builder()
						.userId(2)
						.build())
				.speed(2.0f)
				.timeSet(new Date())
				.replay(ReplayEntity.builder()
						.replayId(2)
						.build())
				.points(200)
				.build());
		when(scoreRepository.getScoreEntitiesByMap_MapId(1, PageRequest.of(0, 10))).thenReturn(Optional.of(scoreEntities));
		List<Score> expectedScores = scoreEntities.stream()
				.map(ScoreConverter::convert)
				.collect(Collectors.toList());
		HttpStatus expectedHttpStatus = HttpStatus.OK;

		// Act
		GetScoresByMapIdResponse response = scoreManager.getScoresByMapId(request);

		// Assert
		assertEquals(expectedScores, response.getScores());
		assertEquals(expectedHttpStatus, response.getHttpStatus());
	}

	// Should return a not found status when getScoreById is called with a non-existent score ID
	@Test
	void test_getScoreById_returns_not_found_when_score_with_ID_is_not_found() {
		// Arrange
		ScoreRepository scoreRepository = mock(ScoreRepository.class);
		ReplayRepository replayRepository = mock(ReplayRepository.class);
		ScoreManagerImpl scoreManager = new ScoreManagerImpl(scoreRepository, replayRepository);
		GetScoreByIdRequest request = GetScoreByIdRequest.builder().id(1).build();

		// Mock the behavior of scoreRepository.findById() to return an empty Optional
		when(scoreRepository.findById(request.getId())).thenReturn(Optional.empty());

		// Act
		GetScoreByIdResponse response = scoreManager.getScoreById(request);

		// Assert
		assertNull(response.getScore());
		assertEquals(HttpStatus.NOT_FOUND, response.getHttpStatus());
	}

	// Should return a bad request status when getScoreById is called with an ID less than 1
	@Test
	void test_getScoreById_returns_bad_request_when_ID_is_less_than_1() {
		// Arrange
		ScoreRepository scoreRepository = mock(ScoreRepository.class);
		ReplayRepository replayRepository = mock(ReplayRepository.class);
		ScoreManagerImpl scoreManager = new ScoreManagerImpl(scoreRepository, replayRepository);
		GetScoreByIdRequest request = GetScoreByIdRequest.builder().id(0).build();

		// Act
		GetScoreByIdResponse response = scoreManager.getScoreById(request);

		// Assert
		assertNull(response.getScore());
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
	}

	// Should return an internal server error status when an exception occurs
	@Test
	void test_getScoreById_returns_internal_server_error_when_exception_occurs() {
		// Arrange
		ScoreRepository scoreRepository = mock(ScoreRepository.class);
		ReplayRepository replayRepository = mock(ReplayRepository.class);
		ScoreManagerImpl scoreManager = new ScoreManagerImpl(scoreRepository, replayRepository);
		GetScoreByIdRequest request = GetScoreByIdRequest.builder().id(1).build();

		// Mock the behavior of scoreRepository.findById() to throw a RuntimeException
		when(scoreRepository.findById(request.getId())).thenThrow(new RuntimeException("Internal Server Error"));

		// Act
		GetScoreByIdResponse response = scoreManager.getScoreById(request);

		// Assert
		assertNull(response.getScore());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
	}

	// Should return a bad request status when getAllScores is called with a negative page number
	@Test
	void test_getAllScores_returns_BAD_REQUEST_when_page_is_negative() {
		// Arrange
		ScoreRepository scoreRepository = mock(ScoreRepository.class);
		ReplayRepository replayRepository = mock(ReplayRepository.class);
		ScoreManagerImpl scoreManager = new ScoreManagerImpl(scoreRepository, replayRepository);
		GetAllScoresRequest request = GetAllScoresRequest.builder()
				.page(-1)
				.build();

		// Act
		GetAllScoresResponse response = scoreManager.getAllScores(request);

		// Assert
		assertEquals(HttpStatus.BAD_REQUEST, response.getHttpStatus());
		assertNull(response.getScores());
	}
}