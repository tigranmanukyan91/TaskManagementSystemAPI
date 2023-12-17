package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.Converter.TaskConverter;
import com.example.taskmanagementsystem.Converter.UserConverter;
import com.example.taskmanagementsystem.model.TaskDTO;
import com.example.taskmanagementsystem.model.UserDTO;
import com.example.taskmanagementsystem.service.TaskServiceImpl;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.event.WindowFocusListener;
import java.util.Set;

@RestController
@RequestMapping("/task")
@Slf4j
public class TaskController {
    private final TaskServiceImpl taskService;
    private final TaskConverter taskConverter;
    private final UserConverter userConverter;


    @Autowired
    public TaskController(TaskServiceImpl taskService, TaskConverter taskConverter, UserConverter userConverter) {
        this.taskService = taskService;
        this.taskConverter = taskConverter;
        this.userConverter = userConverter;
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public TaskDTO createTask(@RequestBody TaskDTO taskDTO){
        log.info("Creating task: {}",taskDTO);
        return taskService.createTask(taskDTO);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public String updateTask(@RequestBody TaskDTO taskDTO){
        log.info("Updating task: {}",taskDTO);
        return taskService.updateTask(taskDTO);
    }

    @RequestMapping(value = "/{task_id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public TaskDTO getTask(@PathVariable long task_id){
        log.info("Fetching task with ID: {}",task_id);
        return taskService.getTask(task_id);
    }

    @RequestMapping(value = "/{task_id}",method = RequestMethod.DELETE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public String deleteTask(@PathVariable long task_id){
        log.info("Deleting task with ID: {}",task_id);
        return taskService.deleteTask(task_id);
    }

    @RequestMapping(value = "/getTasksByAuthor",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public Set<TaskDTO> getTasksByAuthor(@RequestBody UserDTO author){
        log.info("Fetching tasks by author: {}",author);
        return taskService.getTasksByAuthor(author);
    }

    @RequestMapping(value = "/getTasksByAssignee",method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public Set<TaskDTO> getTasksByAssignee(@RequestBody UserDTO assignee){
        log.info("Fetching tasks by assignee: {}",assignee);
        return taskService.getTasksByAssignee(assignee);
    }

    @RequestMapping(value = "/getAuthorByTask",method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public UserDTO getAuthorByTask(TaskDTO taskDTO){
        log.info("Fetching author by task: {}",taskDTO);
        return taskService.getAuthorByTask(taskDTO);
    }

    @RequestMapping(value = "/getAssigneesByTask",method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public Set<UserDTO> getAssigneesByTask(TaskDTO taskDTO){
        log.info("Fetching assignees by task: {}",taskDTO);
        return taskService.getAssigneesByTask(taskDTO);
    }
}
