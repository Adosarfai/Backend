package com.adosar.backend.persistence;

import com.adosar.backend.persistence.entity.MapEntity;
import com.adosar.backend.persistence.entity.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ScoreRepository extends JpaRepository<ScoreEntity, Integer> {

    @Query("SELECT m FROM ScoreEntity s JOIN s.map m WHERE DATE(s.timeSet) = :dateCurrent GROUP BY m ORDER BY COUNT(*) DESC LIMIT 1")
    MapEntity getMostPopularMap(@Param("dateCurrent") LocalDate date);

    @Query("SELECT s FROM ScoreEntity s order BY s.timeSet DESC LIMIT 1")
    ScoreEntity getLastSetScore();

    @Query("SELECT s.scoreId from ScoreEntity s order by s.scoreId desc limit 1")
    Integer getScoreEntityCount();
}
