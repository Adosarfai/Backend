package com.adosar.backend.persistence;

import com.adosar.backend.persistence.entity.ReplayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplayRepository extends JpaRepository<ReplayEntity, Integer> {
}
