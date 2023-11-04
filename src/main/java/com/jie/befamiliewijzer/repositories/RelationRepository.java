package com.jie.befamiliewijzer.repositories;


import com.jie.befamiliewijzer.models.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationRepository extends JpaRepository<Relation,Integer> {
    Optional<Relation> findByPersonIdAndSpouceId(Integer personId, Integer spouceId);
    List<Relation> findAllByPersonIdAndSpouceId(Integer personId, Integer spouceId);
}
