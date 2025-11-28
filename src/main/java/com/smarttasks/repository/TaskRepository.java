package com.smarttasks.repository;

import com.smarttasks.entity.Task;
import com.smarttasks.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    List<Task> findByUserOrderByCreatedAtDesc(User user);
    
    List<Task> findByUserAndCompleted(User user, Boolean completed);
    
    Optional<Task> findByIdAndUser(Long id, User user);
    
    Long countByUser(User user);
    
    Long countByUserAndCompleted(User user, Boolean completed);
}
