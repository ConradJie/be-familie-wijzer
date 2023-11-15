package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, String> {
    Optional<Media> findByFilename(String filename);
}
