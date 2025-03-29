package com.mareen.taskservice.dto;

import lombok.Data;

@Data
public class UpdateTaskDto {
    private String name;

    private String description;

    private String assignedTo;

    private String deadline;
}
