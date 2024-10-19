package com.savolodya.predictiveportfolio.repositories;

import com.savolodya.predictiveportfolio.models.user.UserTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, Long> {
}
