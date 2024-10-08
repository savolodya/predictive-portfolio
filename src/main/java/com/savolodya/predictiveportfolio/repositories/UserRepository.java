package com.savolodya.predictiveportfolio.repositories;

import com.savolodya.predictiveportfolio.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
