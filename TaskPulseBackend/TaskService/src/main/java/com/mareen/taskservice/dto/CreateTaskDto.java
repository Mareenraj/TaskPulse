package com.mareen.taskservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTaskDto {
    @NotBlank(message = "Task name is required!")
    private String name;

    @NotBlank(message = "Description is required!")
    private String description;

    @NotBlank(message = "Employee name is required!")
    private String assignedTo;

    private String deadline;
}
