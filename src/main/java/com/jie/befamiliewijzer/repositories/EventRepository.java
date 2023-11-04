package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.Event;
import com.jie.befamiliewijzer.models.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    Optional<Event> findByPersonIdAndId(Integer personId, Integer id);
    Optional<Event> findByRelationIdAndId(Integer relationId, Integer id);

    List<Event> findEventsByPersonId(Integer personId);
    List<Event> findEventsByRelationId(Integer relationId);
    Optional<Event> findEventByRelationIdAndEventType(Integer relationId, String EventType);
}
