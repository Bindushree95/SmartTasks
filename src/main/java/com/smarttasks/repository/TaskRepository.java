package com.smarttasks.repository;

import com.smarttasks.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByUserId(Long userId);
    
    List<Task> findByUserIdAndCompleted(Long userId, Boolean completed);
    
    Optional<Task> findByIdAndUserId(Long id, Long userId);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId AND t.completed = true")
    Long countCompletedByUserId(@Param("userId") Long userId);
}
