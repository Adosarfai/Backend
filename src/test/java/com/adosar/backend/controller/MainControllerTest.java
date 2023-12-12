package com.adosar.backend.controller;

import com.adosar.backend.business.GetStatisticsUseCase;
import com.adosar.backend.business.response.misc.GetStatisticsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.view.RedirectView;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("UnusedShould")
class MainControllerTest {
	// GET request to / should redirect to https://youtu.be/dQw4w9WgXcQ
	@Test
	void test_get_request_to_root_should_redirect_to_youtube() {
		// Arrange
		MainController mainController = new MainController(mock(GetStatisticsUseCase.class));

		// Act
		RedirectView result = mainController.home();

		// Assert
		assertEquals("https://youtu.be/dQw4w9WgXcQ", result.getUrl());
	}

	// GET request to /statistics should return a ResponseEntity with GetStatisticsResponse
	@Test
	void test_get_statistics_returns_response_entity_with_body() {
		// Arrange
		GetStatisticsResponse expectedResponse = GetStatisticsResponse.builder()
				.mapCount(10)
				.leaderboardCount(5)
				.scoreCount(100)
				.httpStatus(HttpStatus.OK)
				.build();
		GetStatisticsUseCase getStatisticsUseCase = mock(GetStatisticsUseCase.class);
		when(getStatisticsUseCase.getStatistics()).thenReturn(expectedResponse);

		MainController mainController = new MainController(getStatisticsUseCase);

		// Act
		ResponseEntity<GetStatisticsResponse> response = mainController.getStatistics();

		// Assert
		assertNotNull(response);
		assertEquals(expectedResponse, response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	// GET request to /statistics should return a ResponseEntity when INTERNAL_SERVER_ERROR
	@Test
	void test_get_statistics_returns_response_entity_with_null_body_and_http_status_when_get_statistics_response_has_internal_server_error_http_status() {
		// Arrange
		GetStatisticsResponse expectedResponse = GetStatisticsResponse.builder()
				.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
				.build();
		GetStatisticsUseCase getStatisticsUseCase = mock(GetStatisticsUseCase.class);
		when(getStatisticsUseCase.getStatistics()).thenReturn(expectedResponse);

		MainController mainController = new MainController(getStatisticsUseCase);

		// Act
		ResponseEntity<GetStatisticsResponse> response = mainController.getStatistics();

		// Assert
		assertNotNull(response);
		assertNull(response.getBody());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	// GET request to /statistics should return with 0 as property when count is 0
	@Test
	void test_getStatistics_returns_response_with_zero_count_when_count_is_zero() {
		// Arrange
		GetStatisticsResponse response = GetStatisticsResponse.builder()
				.mapCount(0)
				.leaderboardCount(0)
				.scoreCount(0)
				.httpStatus(HttpStatus.OK)
				.build();
		GetStatisticsUseCase getStatisticsUseCase = mock(GetStatisticsUseCase.class);
		when(getStatisticsUseCase.getStatistics()).thenReturn(response);
		MainController mainController = new MainController(getStatisticsUseCase);

		// Act
		ResponseEntity<GetStatisticsResponse> result = mainController.getStatistics();

		// Assert
		assertNotNull(result);
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertNotNull(result.getBody());
		assertEquals(0, result.getBody().getMapCount().intValue());
		assertEquals(0, result.getBody().getLeaderboardCount().intValue());
		assertEquals(0, result.getBody().getScoreCount().intValue());
	}
}