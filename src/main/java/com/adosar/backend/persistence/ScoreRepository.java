package com.adosar.backend.persistence;

import com.adosar.backend.persistence.entity.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<ScoreEntity, Integer> {
}
