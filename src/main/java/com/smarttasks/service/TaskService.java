package com.smarttasks.service;

import com.smarttasks.dto.TaskRequest;
import com.smarttasks.dto.TaskResponse;
import com.smarttasks.entity.Task;
import com.smarttasks.entity.User;
import com.smarttasks.exception.ResourceNotFoundException;
import com.smarttasks.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public TaskResponse createTask(TaskRequest request, User user) {
        log.info("Creating new task for user: {}", user.getUsername());
        
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .completed(false)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Task savedTask = taskRepository.save(task);
        log.info("Task created with ID: {}", savedTask.getId());
        
        return mapToResponse(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks(User user, Boolean completed) {
        log.info("Fetching tasks for user: {}, completed filter: {}", user.getUsername(), completed);
        
        List<Task> tasks;
        if (completed != null) {
            tasks = taskRepository.findByUserAndCompleted(user, completed);
        } else {
            tasks = taskRepository.findByUserOrderByCreatedAtDesc(user);
        }

        return tasks.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long id, User user) {
        log.info("Fetching task with ID: {} for user: {}", id, user.getUsername());
        
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        return mapToResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request, User user) {
        log.info("Updating task with ID: {} for user: {}", id, user.getUsername());
        
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);
        log.info("Task updated: {}", updatedTask.getId());
        
        return mapToResponse(updatedTask);
    }

    @Transactional
    public TaskResponse toggleTaskCompletion(Long id, User user) {
        log.info("Toggling completion status for task with ID: {} for user: {}", id, user.getUsername());
        
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setCompleted(!task.getCompleted());
        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);
        log.info("Task completion toggled: {} - completed: {}", updatedTask.getId(), updatedTask.getCompleted());
        
        return mapToResponse(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id, User user) {
        log.info("Deleting task with ID: {} for user: {}", id, user.getUsername());
        
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        taskRepository.delete(task);
        log.info("Task deleted: {}", id);
    }

    @Transactional(readOnly = true)
    public Long getTotalTasksCount(User user) {
        return taskRepository.countByUser(user);
    }

    @Transactional(readOnly = true)
    public Long getCompletedTasksCount(User user) {
        return taskRepository.countByUserAndCompleted(user, true);
    }

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCompleted(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
