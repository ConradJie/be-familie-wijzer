package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MediaRepository extends JpaRepository<Media, String> {
    Optional<Media> findByFilename(String filename);
    void deleteByFilename(String filename);
}
