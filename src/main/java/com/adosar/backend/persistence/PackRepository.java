package com.adosar.backend.persistence;

import com.adosar.backend.persistence.entity.PackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackRepository extends JpaRepository<PackEntity, Integer> {
}
