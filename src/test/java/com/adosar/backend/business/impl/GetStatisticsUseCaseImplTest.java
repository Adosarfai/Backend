package com.adosar.backend.business.impl;

import com.adosar.backend.business.response.misc.GetStatisticsResponse;
import com.adosar.backend.persistence.LeaderboardRepository;
import com.adosar.backend.persistence.MapRepository;
import com.adosar.backend.persistence.ScoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("UnusedShould")
class GetStatisticsUseCaseImplTest {
	// Returns a GetStatisticsResponse object with HttpStatus.OK when all repositories return valid counts.
	@Test
	void test_getStatistics_returns_GetStatisticsResponse_with_HTTPStatus_OK() {
		// Arrange
		Integer mapCount = 10;
		Integer leaderboardCount = 5;
		Integer scoreCount = 20;

		MapRepository mapRepositoryMock = mock(MapRepository.class);
		LeaderboardRepository leaderboardRepositoryMock = mock(LeaderboardRepository.class);
		ScoreRepository scoreRepositoryMock = mock(ScoreRepository.class);

		when(mapRepositoryMock.getMapEntityCount()).thenReturn(mapCount);
		when(leaderboardRepositoryMock.getLeaderboardEntityCount()).thenReturn(leaderboardCount);
		when(scoreRepositoryMock.getScoreEntityCount()).thenReturn(scoreCount);

		GetStatisticsUseCaseImpl getStatisticsUseCase = new GetStatisticsUseCaseImpl(mapRepositoryMock, leaderboardRepositoryMock, scoreRepositoryMock);

		// Act
		GetStatisticsResponse response = getStatisticsUseCase.getStatistics();

		// Assert
		assertNotNull(response);
		assertEquals(mapCount, response.getMapCount());
		assertEquals(leaderboardCount, response.getLeaderboardCount());
		assertEquals(scoreCount, response.getScoreCount());
		assertEquals(HttpStatus.OK, response.getHttpStatus());
	}

	// Returns a GetStatisticsResponse object with HttpStatus.INTERNAL_SERVER_ERROR when an exception is thrown.
	@Test
	void test_getStatistics_returns_GetStatisticsResponse_with_HTTPStatus_INTERNAL_SERVER_ERROR() {
		// Arrange
		MapRepository mapRepositoryMock = mock(MapRepository.class);
		LeaderboardRepository leaderboardRepositoryMock = mock(LeaderboardRepository.class);
		ScoreRepository scoreRepositoryMock = mock(ScoreRepository.class);

		when(mapRepositoryMock.getMapEntityCount()).thenThrow(new RuntimeException());

		GetStatisticsUseCaseImpl getStatisticsUseCase = new GetStatisticsUseCaseImpl(mapRepositoryMock, leaderboardRepositoryMock, scoreRepositoryMock);

		// Act
		GetStatisticsResponse response = getStatisticsUseCase.getStatistics();

		// Assert
		assertNotNull(response);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
	}

	// Returns a GetStatisticsResponse object with correct counts and HttpStatus.INTERNAL_SERVER_ERROR when any repository throws an exception.
	@Test
	void test_getStatistics_logs_exception_and_returns_GetStatisticsResponse_with_INTERNAL_SERVER_ERROR() {
		// Arrange
		MapRepository mapRepositoryMock = mock(MapRepository.class);
		LeaderboardRepository leaderboardRepositoryMock = mock(LeaderboardRepository.class);
		ScoreRepository scoreRepositoryMock = mock(ScoreRepository.class);
		GetStatisticsUseCaseImpl getStatisticsUseCase = new GetStatisticsUseCaseImpl(mapRepositoryMock, leaderboardRepositoryMock, scoreRepositoryMock);

		Integer expectedMapCount = 10;
		Integer expectedLeaderboardCount = 5;
		Integer expectedScoreCount = 20;

		when(mapRepositoryMock.getMapEntityCount()).thenReturn(expectedMapCount);
		when(leaderboardRepositoryMock.getLeaderboardEntityCount()).thenReturn(expectedLeaderboardCount);
		when(scoreRepositoryMock.getScoreEntityCount()).thenReturn(expectedScoreCount);

		// Act
		GetStatisticsResponse response = getStatisticsUseCase.getStatistics();

		// Assert
		assertEquals(expectedMapCount, response.getMapCount());
		assertEquals(expectedLeaderboardCount, response.getLeaderboardCount());
		assertEquals(expectedScoreCount, response.getScoreCount());
		assertEquals(HttpStatus.OK, response.getHttpStatus());

		// Arrange - simulate repository exception
		RuntimeException exception = new RuntimeException("Repository exception");
		when(mapRepositoryMock.getMapEntityCount()).thenThrow(exception);

		// Act
		response = getStatisticsUseCase.getStatistics();

		// Assert
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getHttpStatus());
	}

	// Returns a GetStatisticsResponse object with HttpStatus.OK when all repositories return zero counts.
	@Test
	void test_getStatistics_returns_response_with_OK_status_when_all_repositories_return_zero_counts() {
		// Arrange
		MapRepository mapRepositoryMock = mock(MapRepository.class);
		LeaderboardRepository leaderboardRepositoryMock = mock(LeaderboardRepository.class);
		ScoreRepository scoreRepositoryMock = mock(ScoreRepository.class);

		when(mapRepositoryMock.getMapEntityCount()).thenReturn(0);
		when(leaderboardRepositoryMock.getLeaderboardEntityCount()).thenReturn(0);
		when(scoreRepositoryMock.getScoreEntityCount()).thenReturn(0);

		GetStatisticsUseCaseImpl getStatisticsUseCase = new GetStatisticsUseCaseImpl(mapRepositoryMock, leaderboardRepositoryMock, scoreRepositoryMock);

		// Act
		GetStatisticsResponse response = getStatisticsUseCase.getStatistics();

		// Assert
		assertEquals(HttpStatus.OK, response.getHttpStatus());
	}

}