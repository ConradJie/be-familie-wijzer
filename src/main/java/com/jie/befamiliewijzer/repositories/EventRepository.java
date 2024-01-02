package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    Optional<Event> findAllById(Integer id);
    Optional<Event> findByPersonIdAndId(Integer personId, Integer id);

    Optional<Event> findByRelationIdAndId(Integer relationId, Integer id);

    List<Event> findEventsByPersonIdOrderByBeginDate(Integer personId);

    List<Event> findEventsByRelationId(Integer relationId);

    Optional<Event> findEventByRelationIdAndEventType(Integer relationId, String EventType);

    Optional<Event> findByPersonIdAndEventType(Integer personId, String eventType);

    @Query(value = """
            SELECT
              e.id,
              e.event_type,
              e.description,
              e.text,
              e.begin_date,
              e.end_date,
              e.person_id,
              e.relation_id
            FROM events e
            WHERE EXTRACT(MONTH FROM e.begin_date)=?1
              AND EXTRACT(MONTH FROM e.end_date)=?1
              AND EXTRACT(DAY FROM e.begin_date)=?2
              AND EXTRACT(DAY FROM e.end_date)=?2
                          """
            , nativeQuery = true)
    List<Event> findAllEventsOnMonthDay(Integer month, Integer day);

}
