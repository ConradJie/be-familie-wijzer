package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    boolean existsByGivenNamesAndSurnameAndSex(String givenNames,String surname,String sex);
    List<Person> findAllByGivenNamesIsContainingIgnoreCaseOrSurnameContainingIgnoreCase(String givenNames, String surname);
    List<Person> findAllByGivenNamesIsContainingIgnoreCaseAndSurnameIsContainingIgnoreCaseOrderByGivenNames(String givenNames,String surname);
    List<Person> findAllByGivenNamesIsContainingIgnoreCaseOrderByGivenNames(String givenNames);
    List<Person> findAllBySurnameContainingIgnoreCaseOrderBySurname(String surname);

    @Query(value = """
            SELECT
              id,
              given_names,
              surname,
              sex
            FROM persons
            EXCEPT
            (SELECT p.*
            FROM persons p
            JOIN children ON children.child_id = p.id

            UNION

            SELECT p.*
            FROM persons p
            JOIN relations r ON r.person_id = p.id
                             OR r.spouse_id = p.id
            )
            """
            , nativeQuery = true)
    List<Person> findAllPersonswithoutRelationsOrChildOf();
}
