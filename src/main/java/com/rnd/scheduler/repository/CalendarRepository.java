package com.rnd.scheduler.repository;
import com.rnd.scheduler.model.Calendar;
import com.rnd.scheduler.model.User;
import org.hibernate.mapping.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    @Query(value="SELECT s FROM Calendar s where s.user=:user")
    List<Calendar> findCalendarByUser(@Param("user") User user);

}
