package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    boolean existsByGivenNamesAndSurnameAndSex(String givenNames,String surname,String sex);
}
