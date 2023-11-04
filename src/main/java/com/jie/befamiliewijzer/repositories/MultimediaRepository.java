package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MultimediaRepository extends JpaRepository<Multimedia, Integer> {
    List<Multimedia> findAllByEventId(Integer eventId);
}
