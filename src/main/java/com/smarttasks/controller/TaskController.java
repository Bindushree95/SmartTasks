package com.smarttasks.controller;

import com.smarttasks.dto.ApiResponse;
import com.smarttasks.dto.TaskRequest;
import com.smarttasks.dto.TaskResponse;
import com.smarttasks.entity.User;
import com.smarttasks.repository.UserRepository;
import com.smarttasks.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {
        User user = getUserFromAuth(authentication);
        TaskResponse response = taskService.createTask(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @RequestParam(required = false) Boolean completed,
            Authentication authentication) {
        User user = getUserFromAuth(authentication);
        List<TaskResponse> tasks = taskService.getAllTasks(user, completed);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id,
            Authentication authentication) {
        User user = getUserFromAuth(authentication);
        TaskResponse response = taskService.getTaskById(id, user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            Authentication authentication) {
        User user = getUserFromAuth(authentication);
        TaskResponse response = taskService.updateTask(id, request, user);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TaskResponse> toggleTaskCompletion(
            @PathVariable Long id,
            Authentication authentication) {
        User user = getUserFromAuth(authentication);
        TaskResponse response = taskService.toggleTaskCompletion(id, user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {
        User user = getUserFromAuth(authentication);
        taskService.deleteTask(id, user);
        return ResponseEntity.ok(new ApiResponse(true, "Task deleted successfully"));
    }

    private User getUserFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
