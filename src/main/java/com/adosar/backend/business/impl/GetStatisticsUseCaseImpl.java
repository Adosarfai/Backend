package com.adosar.backend.business.impl;

import com.adosar.backend.business.GetStatisticsUseCase;
import com.adosar.backend.business.response.GetStatisticsResponse;
import com.adosar.backend.persistence.LeaderboardRepository;
import com.adosar.backend.persistence.MapRepository;
import com.adosar.backend.persistence.ScoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class GetStatisticsUseCaseImpl implements GetStatisticsUseCase {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private MapRepository mapRepository;
	private LeaderboardRepository leaderboardRepository;
	private ScoreRepository scoreRepository;

	@Override
	public GetStatisticsResponse getStatistics() {
		try {
			long mapCount = mapRepository.getMapEntityCount();
			long leaderboardCount = leaderboardRepository.GetLeaderboardEntityCount();
			long scoreCount = scoreRepository.getScoreEntityCount();

			return GetStatisticsResponse.builder()
					.MapCount(mapCount)
					.LeaderboardCount(leaderboardCount)
					.ScoreCount(scoreCount)
					.httpStatus(HttpStatus.OK)
					.build();

		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return GetStatisticsResponse.builder()
					.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
					.build();
		}
	}
}
