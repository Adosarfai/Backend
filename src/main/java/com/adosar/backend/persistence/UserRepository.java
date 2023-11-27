package com.adosar.backend.persistence;

import com.adosar.backend.domain.Privilege;
import com.adosar.backend.persistence.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	Optional<UserEntity> getUserEntityByEmail(String email);

	Optional<UserEntity> getUserEntityByUserId(Integer id);

	@Query("select u from UserEntity as u order by u.creationDate desc limit 1")
	Optional<UserEntity> getUserEntityByCreationDateLast();

	@Modifying
	@Transactional
	@Query("update UserEntity u set u.privilege = :newPrivilege where u.userId = :id")
	void updatePrivilegeByUserId(@Param("id") Integer id, @Param("newPrivilege") Privilege newPrivilege);

	@Query("SELECT u.userId from UserEntity u order by u.userId desc limit 1")
	Integer getUserEntityCount();

	Optional<List<UserEntity>> getUserEntitiesByUsernameContainsAndCreationDateBeforeAndCreationDateAfter(String username, Date before, Date after, Pageable pageable);
}