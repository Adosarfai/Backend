package com.adosar.backend.business.impl;

import com.adosar.backend.business.ScoreManager;
import com.adosar.backend.business.converter.ScoreConverter;
import com.adosar.backend.business.exception.NotFoundException;
import com.adosar.backend.business.request.score.GetAllScoresRequest;
import com.adosar.backend.business.request.score.GetScoreByIdRequest;
import com.adosar.backend.business.request.score.GetScoresByMapIdRequest;
import com.adosar.backend.business.response.score.GetAllScoresResponse;
import com.adosar.backend.business.response.score.GetScoreByIdResponse;
import com.adosar.backend.business.response.score.GetScoresByMapIdResponse;
import com.adosar.backend.domain.Score;
import com.adosar.backend.persistence.ScoreRepository;
import com.adosar.backend.persistence.entity.ScoreEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.javapoet.ClassName;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class ScoreManagerImpl implements ScoreManager {
	private static final Logger LOGGER = Logger.getLogger(ClassName.class.getName());
	private final ScoreRepository scoreRepository;

	@Override
	public GetAllScoresResponse getAllScores(GetAllScoresRequest request) {
		try {
			assert request.getPage() >= 0 : "'page' must be at least 0";

			List<ScoreEntity> result = scoreRepository.findAll(PageRequest.of(request.getPage(), 10)).toList();
			List<Score> scores = result.stream().map(ScoreConverter::convert).toList();

			return new GetAllScoresResponse(scores, HttpStatus.OK);
		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new GetAllScoresResponse(null, HttpStatus.BAD_REQUEST);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetAllScoresResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public GetScoreByIdResponse getScoreById(GetScoreByIdRequest request) {
		try {
			assert request.getId() >= 1 : "'id' must be at least 1";

			ScoreEntity result = scoreRepository.findById(request.getId()).orElseThrow(() ->
					new NotFoundException(String.format("Score with ID %s was not found", request.getId()))
			);
			Score score = ScoreConverter.convert(result);

			return new GetScoreByIdResponse(score, HttpStatus.OK);
		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new GetScoreByIdResponse(null, HttpStatus.BAD_REQUEST);
		} catch (NotFoundException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new GetScoreByIdResponse(null, HttpStatus.NOT_FOUND);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetScoreByIdResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public GetScoresByMapIdResponse getScoresByMapId(GetScoresByMapIdRequest request) {
		try {
			assert request.getId() >= 1 : "'id' must be at least 1";
			assert request.getPage() >= 0 : "'page' must be at least 0";

			List<ScoreEntity> result = scoreRepository.getScoreEntitiesByMap_MapId(request.getId(), PageRequest.of(request.getPage(), 10)).orElseThrow(() ->
					new NotFoundException(String.format("Map with ID %s was not found", request.getId()))
			);
			List<Score> scores = result.stream().map(ScoreConverter::convert).toList();

			return new GetScoresByMapIdResponse(scores, HttpStatus.OK);
		} catch (AssertionError exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new GetScoresByMapIdResponse(null, HttpStatus.BAD_REQUEST);
		} catch (NotFoundException exception) {
			LOGGER.log(Level.FINE, exception.toString(), exception);
			return new GetScoresByMapIdResponse(null, HttpStatus.NOT_FOUND);
		} catch (Exception exception) {
			LOGGER.log(Level.SEVERE, exception.toString(), exception);
			return new GetScoresByMapIdResponse(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
