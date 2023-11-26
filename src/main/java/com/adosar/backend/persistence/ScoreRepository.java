package com.adosar.backend.persistence;

import com.adosar.backend.persistence.entity.MapEntity;
import com.adosar.backend.persistence.entity.ScoreEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<ScoreEntity, Integer> {

	@Query("SELECT m FROM ScoreEntity s JOIN s.map m WHERE DATE(s.timeSet) = :dateCurrent GROUP BY m ORDER BY COUNT(*) DESC LIMIT 1")
	Optional<MapEntity> getMostPopularMap(@Param("dateCurrent") LocalDate date);

	@Query("SELECT s FROM ScoreEntity s ORDER BY s.timeSet DESC LIMIT 1")
	Optional<ScoreEntity> getLastSetScore();

	@Query("SELECT s.scoreId FROM ScoreEntity s ORDER BY s.scoreId DESC LIMIT 1")
	Integer getScoreEntityCount();

	Optional<List<ScoreEntity>> getScoreEntitiesByMap_MapId(Integer id, Pageable pageable);
}
