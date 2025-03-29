package com.mareen.taskservice.mapper;

import com.mareen.taskservice.dto.TaskDto;
import com.mareen.taskservice.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public TaskDto taskEntityToTaskDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setName(task.getName());
        taskDto.setDescription(task.getDescription());
        taskDto.setAssignedBy(task.getAssignedBy());
        taskDto.setAssignedTo(task.getAssignedTo());
        taskDto.setCreatedAt(String.valueOf(task.getCreatedAt()));
        taskDto.setDeadline(String.valueOf(task.getDeadline()));
        return taskDto;
    }
}
