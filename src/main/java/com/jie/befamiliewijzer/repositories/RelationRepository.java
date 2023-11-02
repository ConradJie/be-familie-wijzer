package com.jie.befamiliewijzer.repositories;


import com.jie.befamiliewijzer.models.Person;
import com.jie.befamiliewijzer.models.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends JpaRepository<Relation,Integer> {
}
