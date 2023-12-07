package com.jie.befamiliewijzer.repositories;


import com.jie.befamiliewijzer.models.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationRepository extends JpaRepository<Relation,Integer> {
    boolean existsByPersonIdAndSpouseId(Integer personId, Integer spouseId);
    Optional<Relation> findByPersonIdAndSpouseId(Integer personId, Integer spouseId);
    List<Relation> findAllByPersonIdOrSpouseId(Integer personId,Integer spouseId);
    List<Relation> findRelationBySpouseIsNull();

}
