package com.rnd.scheduler.repository;

import com.rnd.scheduler.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    boolean existsByUsername(String username);
    @Query(value="SELECT s.id FROM User s where s.username=:user")
    Long findUserId(@Param("user") String user);
    @Query(value="SELECT s.groupId FROM User s where s.username=:user")
    Long findGroupId(@Param("user") String user);
    @Query(value="SELECT s FROM User s where s.groupId=:id")
    List<User> findUsersByGroupId(@Param("id") Long id);
    @Query(value="UPDATE User s SET s.groupId=:groupId where s.username=:user")
    void updateUser(@Param("user") String user, @Param("groupId") Long groupId);
}