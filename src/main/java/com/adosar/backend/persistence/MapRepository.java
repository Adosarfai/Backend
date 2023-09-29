package com.adosar.backend.persistence;

import com.adosar.backend.persistence.entity.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface MapRepository extends JpaRepository<MapEntity, Integer> {

    Collection<MapEntity> getMapByHash(String hash);

}