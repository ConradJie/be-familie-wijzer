package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.Descendant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DescendantRepository extends JpaRepository<Descendant, String> {
    @Query(value =
            """
 WITH RECURSIVE tree AS (
   SELECT
     (COALESCE(p.relation_id,0) || '-' || relations.person_id || '-' || COALESCE(relations.spouse_id,0)) AS id,
     p.relation_id AS relation_parent,
     relations.id AS relation_child,
     relations.person_id AS person_id,
     persons.given_names,
     persons.surname,
     relations.spouse_id,
     spouses.given_names AS spouse_given_names,
     spouses.surname AS spouse_surname,
     0 AS level,
     CAST(relations.id AS VARCHAR) AS order_sequence
     FROM relations
     JOIN persons ON persons.id = relations.person_id
     LEFT OUTER JOIN children p ON p.child_id = relations.person_id
     LEFT OUTER JOIN persons spouses ON spouses.id = relations.spouse_id
     WHERE relations.person_id =?1
        OR relations.spouse_id =?1
 UNION ALL
   SELECT
     (relations.id || '-' || children.child_id || '-' || COALESCE(spouses.id,0)) AS id,
     relations.id AS relation_parent,
     cr.id AS relation_child,
     children.child_id AS person_id,
     persons.given_names,
     persons.surname,
     spouses.id AS spouse_id,
     spouses.given_names AS spouse_given_names,
     spouses.surname AS spouse_surname,
     level + 1 AS level,
     CAST(order_sequence || '-' ||
          TO_CHAR(level,'fm00') || '(' ||
          COALESCE(TO_CHAR(pb.begin_date,'YYYYMMDD'),'--------') || ')' ||
          COALESCE(TO_CHAR(rm.begin_date,'YYYYMMDD'),'--------') || '-' ||
          COALESCE(cr.id,0) || '-' ||
          CAST(children.child_id AS VARCHAR)
          AS VARCHAR
         ) AS order_sequence
     FROM relations
     JOIN children ON children.relation_id = relations.id
     JOIN persons ON persons.id = children.child_id
     LEFT OUTER JOIN events pb ON pb.event_type = 'BIRTH'
                              AND pb.person_id = persons.id
     LEFT OUTER JOIN relations cr
       ON cr.person_id = children.child_id
       OR cr.spouse_id = children.child_id
     LEFT OUTER JOIN events rm ON rm.event_type = 'MARRIAGE'
                              AND rm.relation_id = cr.id
     LEFT OUTER JOIN persons spouses ON spouses.id = cr.spouse_id AND children.child_id <> spouses.id
                                     OR spouses.id = cr.person_id AND children.child_id <> spouses.id
     INNER JOIN tree ON (    tree.person_id = relations.person_id
                         AND (tree.spouse_id = relations.spouse_id OR relations.spouse_id IS NULL ))
                        OR
                        (    tree.spouse_id = relations.person_id
                         AND tree.person_id = relations.spouse_id)
                    AND level < 50  --Security infinity loop
 )
 SELECT
   tree.id,
   tree.level,
   COALESCE(tree.relation_parent,0) AS relation_parent,
   relation_child,
   rm.begin_date AS marriage_begin_date,
   rm.end_date AS marriage_end_date,
   rd.begin_date AS divorce_begin_date,
   rd.end_date AS divorce_end_date,
   tree.person_id AS person_id,
   given_names,
   surname,
   pb.begin_date AS birth_begin_date,
   pb.end_date AS birth_end_date,
   pd.begin_date AS death_begin_date,
   pd.end_date AS death_end_date,
   spouse_id,
   spouse_given_names,
   spouse_surname,
   sb.begin_date AS spouse_birth_begin_date,
   sb.end_date AS spouse_birth_end_date,
   sd.begin_date AS spouse_death_begin_date,
   sd.end_date AS spouse_death_end_date
 FROM tree
 LEFT OUTER JOIN events rm ON rm.event_type = 'MARRIAGE'
                  AND rm.relation_id = relation_child
 LEFT OUTER JOIN events rd ON rd.event_type = 'DIVORCE'
                  AND rd.relation_id = relation_child
 LEFT OUTER JOIN events pb ON pb.event_type = 'BIRTH'
                  AND pb.person_id = tree.person_id
 LEFT OUTER JOIN events pd ON pd.event_type = 'DEATH'
                  AND pd.person_id = tree.person_id
 LEFT OUTER JOIN events sb ON sb.event_type = 'BIRTH'
                  AND sb.person_id = spouse_id
 LEFT OUTER JOIN events sd ON sd.event_type = 'DEATH'
                  AND sd.person_id = spouse_id
 ORDER BY order_sequence
            """
            , nativeQuery = true)
    List<Descendant> findDescendantsById(Integer id);

}
