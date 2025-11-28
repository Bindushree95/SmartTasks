package com.smarttasks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {
    private Long totalTasks;
    private Long completedTasks;
    private Long pendingTasks;
}
