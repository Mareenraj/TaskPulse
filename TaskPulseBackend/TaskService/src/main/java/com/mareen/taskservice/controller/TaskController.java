package com.mareen.taskservice.controller;

import com.mareen.taskservice.dto.CreateTaskDto;
import com.mareen.taskservice.dto.TaskDto;
import com.mareen.taskservice.dto.UpdateTaskDto;
import com.mareen.taskservice.mapper.TaskMapper;
import com.mareen.taskservice.model.Task;
import com.mareen.taskservice.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody @Valid CreateTaskDto createTaskDto) {
        Task task = taskService.create(createTaskDto);
        TaskDto taskDto = taskMapper.taskEntityToTaskDto(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        TaskDto taskDto = taskMapper.taskEntityToTaskDto(task);
        return ResponseEntity.ok(taskDto);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<Task> taskList = taskService.getAllTasks();
        List<TaskDto> taskDtoList = taskList.stream()
                .map(taskMapper::taskEntityToTaskDto)
                .toList();
        return ResponseEntity.ok(taskDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody @Valid UpdateTaskDto updateTaskDto) {
        Task task = taskService.update(id, updateTaskDto);
        TaskDto taskDto = taskMapper.taskEntityToTaskDto(task);
        return ResponseEntity.ok(taskDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
