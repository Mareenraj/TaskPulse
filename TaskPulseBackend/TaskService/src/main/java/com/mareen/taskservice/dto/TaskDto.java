package com.mareen.taskservice.dto;

import lombok.Data;

@Data
public class TaskDto {

    private String name;

    private String description;

    private String assignedBy;

    private String assignedTo;

    private String createdAt;

    private String deadline;
}
