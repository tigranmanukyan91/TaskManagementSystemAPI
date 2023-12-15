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
public class UserConverter implements Converter<UserDTO, User>{
    private final TaskConverter taskConverter;
    private final CommentConverter commentConverter;

    @Autowired
    public UserConverter(@Lazy TaskConverter taskConverter, CommentConverter commentConverter) {
        this.taskConverter = taskConverter;
        this.commentConverter = commentConverter;
    }

    @Override
    public User convertToEntity(UserDTO model, User entity) {
        entity.setEmail(model.getEmail());
        entity.setPassword(model.getPassword());
        Set<TaskDTO> authoredTasksDTO = model.getAuthoredTasks();
        Set<Task> authoredTasks = new HashSet<>();
        for (TaskDTO taskDTO:authoredTasksDTO){
            Task task = taskConverter.convertToEntity(taskDTO, new Task());
            authoredTasks.add(task);
        }
        entity.setAuthoredTasks(authoredTasks);
        Set<TaskDTO> assignedTasksDTO = model.getAssignedTasks();
        Set<Task> assignedTasks = new HashSet<>();
        for (TaskDTO taskDTO:assignedTasksDTO){
            Task task = taskConverter.convertToEntity(taskDTO, new Task());
            assignedTasks.add(task);
        }
        List<CommentDTO> commentDTOList = model.getCommentDTOList();
        List<Comment> commentList = new ArrayList<>();
        for (CommentDTO commentDTO:commentDTOList){
            Comment comment = commentConverter.convertToEntity(commentDTO,new Comment());
            commentList.add(comment);
        }
        entity.setCommentList(commentList);
        entity.setAssignedTasks(assignedTasks);
        return entity;
    }

    @Override
    public UserDTO convertToModel(User entity, UserDTO model) {
        model.setId(entity.getId());
        model.setEmail(entity.getEmail());
        model.setPassword(entity.getPassword());
        Set<Task> assignedTasks = entity.getAssignedTasks();
        Set<TaskDTO> assignedTasksDTO = new HashSet<>();
        for (Task assignedTask : assignedTasks){
            TaskDTO taskDTO = taskConverter.convertToModel(assignedTask, new TaskDTO());
            assignedTasksDTO.add(taskDTO);
        }
        model.setAssignedTasks(assignedTasksDTO);

        Set<Task> authoredTasks = entity.getAuthoredTasks();
        Set<TaskDTO> authoredTasksDTO = new HashSet<>();
        for (Task authoredTask : authoredTasks){
            TaskDTO taskDTO = taskConverter.convertToModel(authoredTask, new TaskDTO());
            authoredTasksDTO.add(taskDTO);
        }

        List<Comment> commentList = entity.getCommentList();
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment:commentList){
            CommentDTO commentDTO = commentConverter.convertToModel(comment, new CommentDTO());
            commentDTOList.add(commentDTO);
        }
        model.setCommentDTOList(commentDTOList);
        model.setAuthoredTasks(authoredTasksDTO);
        return model;
    }
}
