package com.adosar.backend.persistence;

import com.adosar.backend.persistence.entity.LeaderboardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardRepository extends JpaRepository<LeaderboardEntity, Integer> {

    @Query("SELECT l.leaderboardId from LeaderboardEntity l order by l.leaderboardId desc limit 1")
    Integer GetLeaderboardEntityCount();

}