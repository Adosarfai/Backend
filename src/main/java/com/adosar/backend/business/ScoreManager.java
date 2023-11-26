package com.adosar.backend.business;

import com.adosar.backend.business.request.score.GetAllScoresRequest;
import com.adosar.backend.business.request.score.GetScoreByIdRequest;
import com.adosar.backend.business.request.score.GetScoresByMapIdRequest;
import com.adosar.backend.business.response.score.GetAllScoresResponse;
import com.adosar.backend.business.response.score.GetScoreByIdResponse;
import com.adosar.backend.business.response.score.GetScoresByMapIdResponse;

public interface ScoreManager {
	GetAllScoresResponse getAllScores(GetAllScoresRequest request);

	GetScoreByIdResponse getScoreById(GetScoreByIdRequest request);

	GetScoresByMapIdResponse getScoresByMapId(GetScoresByMapIdRequest request);
}
