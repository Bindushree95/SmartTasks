package com.smarttasks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private Boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
