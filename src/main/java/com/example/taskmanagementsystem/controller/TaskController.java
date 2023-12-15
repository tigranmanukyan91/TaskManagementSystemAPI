package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.Converter.TaskConverter;
import com.example.taskmanagementsystem.Converter.UserConverter;
import com.example.taskmanagementsystem.model.TaskDTO;
import com.example.taskmanagementsystem.model.UserDTO;
import com.example.taskmanagementsystem.service.TaskServiceImpl;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/task")
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
        return taskService.createTask(taskDTO);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public String updateTask(@RequestBody TaskDTO taskDTO){
        return taskService.updateTask(taskDTO);
    }

    @RequestMapping(value = "/{task_id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public TaskDTO getTask(@PathVariable long task_id){
        return taskService.getTask(task_id);
    }

    @RequestMapping(value = "/{task_id}",method = RequestMethod.DELETE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public String deleteTask(@PathVariable long task_id){
        return taskService.deleteTask(task_id);
    }

    @RequestMapping(value = "/getTasksByAuthor",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public Set<TaskDTO> getTasksByAuthor(@RequestBody UserDTO author){
        return taskService.getTasksByAuthor(author);
    }

    @RequestMapping(value = "/getTasksByAssignee",method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public Set<TaskDTO> getTasksByAssignee(@RequestBody UserDTO assignee){
        return taskService.getTasksByAssignee(assignee);
    }

    @RequestMapping(value = "/getAuthorByTask",method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public UserDTO getAuthorByTask(TaskDTO taskDTO){
        return taskService.getAuthorByTask(taskDTO);
    }

    @RequestMapping(value = "/getAssigneesByTask",method = RequestMethod.GET,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public Set<UserDTO> getAssigneesByTask(TaskDTO taskDTO){
        return taskService.getAssigneesByTask(taskDTO);
    }
}
