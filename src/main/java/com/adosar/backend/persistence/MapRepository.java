package com.adosar.backend.persistence;

import com.adosar.backend.persistence.entity.MapEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MapRepository extends JpaRepository<MapEntity, Integer> {

	@Query("SELECT m FROM ScoreEntity s JOIN s.map m WHERE DATE(s.timeSet) = :dateCurrent GROUP BY m ORDER BY COUNT(*) DESC LIMIT 1")
	Optional<MapEntity> getMostPopularMap(@Param("dateCurrent") LocalDate date);
	
	
	
	Collection<MapEntity> getMapEntitiesByUser_UserId(Integer id, PageRequest page);

	@Query("SELECT m.mapId from MapEntity m order by m.mapId desc limit 1")
	Integer getMapEntityCount();

	@Query("select m from MapEntity as m order by m.creationDate desc limit 1")
	Optional<MapEntity> getMapEntityByCreationDateLast();

	@Modifying
	@Transactional
	@Query("update MapEntity u set u.hash = :newHash where u.mapId = :id")
	void updateHashByMapId(@Param("id") Integer id, @Param("newHash") String newHash);

	Optional<List<MapEntity>> getUserEntitiesByTitleContainsAndCreationDateBeforeAndCreationDateAfter(String title, Date before, Date after, Pageable pageable);
}