package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child,Integer> {
    Optional<Child> findByPersonId(Integer personId);
    List<Child> findAllByRelationId(Integer relationId);
    boolean existsByRelation_IdAndPersonId(Integer relationId,Integer personId);
}
