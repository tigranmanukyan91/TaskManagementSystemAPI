package com.example.taskmanagementsystem.Converter;

import com.example.taskmanagementsystem.entity.Comment;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.model.CommentDTO;
import com.example.taskmanagementsystem.model.TaskDTO;
import com.example.taskmanagementsystem.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TaskConverter implements Converter<TaskDTO, Task>{
    private final UserConverter userConverter;
    private final CommentConverter commentConverter;

    @Autowired
    public TaskConverter(@Lazy UserConverter userConverter, CommentConverter commentConverter) {
        this.userConverter = userConverter;
        this.commentConverter = commentConverter;
    }

    //Converting TaskDto to Task entity
    @Override
    public Task convertToEntity(TaskDTO model, Task entity) {
        entity.setTitle(model.getTitle());
        entity.setDescription(model.getDescription());
        entity.setAuthor(userConverter.convertToEntity(model.getAuthor(),new User()));
        Set<UserDTO> assigneesDTO = model.getAssignees();
        Set<User> assignees = entity.getAssignees();
        for (UserDTO assigneeDTO:assigneesDTO){
            User user = userConverter.convertToEntity(assigneeDTO, new User());
            assignees.add(user);
        }
        entity.setAssignees(assignees);
        entity.setTaskStatus(model.getTaskStatus());
        entity.setTaskPriority(model.getTaskPriority());
        List<CommentDTO> commentDTOList = model.getCommentDTO();
        List<Comment> commentList = new ArrayList<>();
        for (CommentDTO commentDTO:commentDTOList){
            commentConverter.convertToEntity(commentDTO,new Comment());
        }
        return entity;
    }

    //Converting Task entity to TaskDTO
    @Override
    public TaskDTO convertToModel(Task entity, TaskDTO model) {
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());
        model.setAuthor(userConverter.convertToModel(entity.getAuthor(),new UserDTO()));
        Set<User> assignees = entity.getAssignees();
        Set<UserDTO> assigneesDTO = new HashSet<>();
        for (User assignee:assignees){
            UserDTO userDTO = userConverter.convertToModel(assignee, new UserDTO());
            assigneesDTO.add(userDTO);
        }
        model.setAssignees(assigneesDTO);
        model.setTaskStatus(entity.getTaskStatus());
        model.setTaskPriority(entity.getTaskPriority());
        return model;
    }
}
