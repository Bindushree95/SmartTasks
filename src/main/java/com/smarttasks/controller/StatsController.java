package com.smarttasks.controller;

import com.smarttasks.dto.StatsResponse;
import com.smarttasks.entity.User;
import com.smarttasks.repository.UserRepository;
import com.smarttasks.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class StatsController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<StatsResponse> getStats(Authentication authentication) {
        User user = getUserFromAuth(authentication);
        
        Long totalTasks = taskService.getTotalTasksCount(user);
        Long completedTasks = taskService.getCompletedTasksCount(user);
        Long pendingTasks = totalTasks - completedTasks;

        StatsResponse response = new StatsResponse(totalTasks, completedTasks, pendingTasks);
        return ResponseEntity.ok(response);
    }

    private User getUserFromAuth(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
