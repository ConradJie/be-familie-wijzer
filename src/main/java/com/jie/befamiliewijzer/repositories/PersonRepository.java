package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    boolean existsByGivenNamesAndSurnameAndSex(String givenNames,String surname,String sex);
    List<Person> findAllByGivenNamesIsContainingIgnoreCaseOrSurnameContainingIgnoreCase(String givenNames, String surname);
    List<Person> findAllByGivenNamesIsContainingIgnoreCaseAndSurnameIsContainingIgnoreCaseOrderByGivenNames(String givenNames,String surname);
    List<Person> findAllByGivenNamesIsContainingIgnoreCaseOrderByGivenNames(String givenNames);
    List<Person> findAllBySurnameContainingIgnoreCaseOrderBySurname(String surname);
}
