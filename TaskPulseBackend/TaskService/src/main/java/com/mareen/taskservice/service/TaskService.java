package com.mareen.taskservice.service;

import com.mareen.taskservice.dto.CreateTaskDto;
import com.mareen.taskservice.dto.UpdateTaskDto;
import com.mareen.taskservice.exception.TaskNotFoundException;
import com.mareen.taskservice.model.Task;
import com.mareen.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(CreateTaskDto createTaskDto) {
        String assignedBy = "managerName"; //It will come from user service and temporally hardcoded.
        String assignedTo = "employeeName"; //It will come from user service and temporally hardcoded.
        Task task = new Task();
        task.setName(createTaskDto.getName());
        task.setDescription(createTaskDto.getDescription());
        task.setAssignedBy(assignedBy);
        task.setAssignedTo(assignedTo);
        task.setCreatedAt(LocalDateTime.now());
        task.setDeadline(LocalDateTime.parse(createTaskDto.getDeadline()));
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found with this ID: " + id));
    }

    public Task update(Long id, UpdateTaskDto updateTaskDto) {
        Task existingTask = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("Task not found with this ID: " + id));
        existingTask.setName(updateTaskDto.getName());
        existingTask.setDescription(updateTaskDto.getDescription());
        existingTask.setAssignedTo(updateTaskDto.getAssignedTo());
        existingTask.setDeadline(LocalDateTime.parse(updateTaskDto.getDeadline()));
        return taskRepository.save(existingTask);
    }

    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with this ID: " + id);
        }
        taskRepository.deleteById(id);
    }
}
