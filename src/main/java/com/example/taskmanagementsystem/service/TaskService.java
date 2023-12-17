package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.model.TaskDTO;
import com.example.taskmanagementsystem.model.UserDTO;

import java.util.Set;

public interface TaskService {
    TaskDTO createTask(TaskDTO taskDTO);

    String updateTask(TaskDTO taskDTO);

    TaskDTO getTask(long id);

    String deleteTask(long id);

    Set<TaskDTO> getTasksByAuthor(UserDTO author);

    Set<TaskDTO> getTasksByAssignee(UserDTO assignee);

    UserDTO getAuthorByTask(TaskDTO taskDTO);

    Set<UserDTO> getAssigneesByTask(TaskDTO taskDTO);





}
