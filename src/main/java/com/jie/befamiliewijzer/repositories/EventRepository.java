package com.jie.befamiliewijzer.repositories;

import com.jie.befamiliewijzer.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
}
