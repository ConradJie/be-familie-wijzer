package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultimediaRepository extends JpaRepository<Multimedia, Integer> {
}
