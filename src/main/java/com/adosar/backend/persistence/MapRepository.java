package com.adosar.backend.persistence;

import com.adosar.backend.persistence.entity.MapEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MapRepository extends JpaRepository<MapEntity, Integer> {

    Collection<MapEntity> getMapEntitiesByHash(String hash);

    @Query("SELECT m.mapId from MapEntity m order by m.mapId desc limit 1")
    Integer getMapEntityCount();

    @Query("select m from MapEntity as m order by m.creationDate desc limit 1")
    MapEntity getMapEntityByCreationDateLast();

    MapEntity getMapEntityByMapId(Integer mapId);

    @Modifying
    @Transactional
    @Query("update MapEntity u set u.hash = :newHash where u.mapId = :id")
    void updateHashByMapId(@Param("id") Integer id, @Param("newHash") String newHash);

}