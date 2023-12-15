package com.example.taskmanagementsystem.Converter;

import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.model.TaskDTO;
import com.example.taskmanagementsystem.model.UserResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserResponseConverter implements Converter<UserResponseJson, User>{
    private final TaskConverter taskConverter;

    @Autowired
    public UserResponseConverter(TaskConverter taskConverter) {
        this.taskConverter = taskConverter;
    }

    @Override
    public User convertToEntity(UserResponseJson model, User entity) {
        return null;
    }

    @Override
    public UserResponseJson convertToModel(User entity, UserResponseJson model) {
        model.setId(entity.getId());
        model.setEmail(entity.getEmail());
        Set<Task> authoredTasks = entity.getAuthoredTasks();
        Set<TaskDTO> authoredTasksDTO = new HashSet<>();
        for (Task task:authoredTasks){
            TaskDTO taskDTO = taskConverter.convertToModel(task,new TaskDTO());
            authoredTasksDTO.add(taskDTO);
        }
        model.setAuthoredTasks(authoredTasksDTO);
        Set<Task> assignedTasks = entity.getAssignedTasks();
        Set<TaskDTO> assignedTasksDTO = new HashSet<>();
        for (Task task : assignedTasks){
            TaskDTO taskDTO = taskConverter.convertToModel(task, new TaskDTO());
            assignedTasksDTO.add(taskDTO);
        }
        model.setAssignedTasks(assignedTasksDTO);
        return model;
    }
}
