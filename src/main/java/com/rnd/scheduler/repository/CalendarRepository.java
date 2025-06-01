package com.rnd.scheduler.repository;
import com.rnd.scheduler.model.Calendar;
import com.rnd.scheduler.model.User;
import org.hibernate.mapping.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
    @Query(value="SELECT s FROM Calendar s where s.user=:user")
    List<Calendar> findCalendarByUser(@Param("user") User user);
}
