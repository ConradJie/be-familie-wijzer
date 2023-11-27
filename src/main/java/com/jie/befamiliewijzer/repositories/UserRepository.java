package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
}
