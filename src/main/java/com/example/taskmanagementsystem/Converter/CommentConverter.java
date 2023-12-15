package com.example.taskmanagementsystem.Converter;

import com.example.taskmanagementsystem.entity.Comment;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.model.CommentDTO;
import com.example.taskmanagementsystem.model.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter implements Converter<CommentDTO, Comment>{

    private final TaskConverter taskConverter;

    @Autowired
    public CommentConverter(@Lazy TaskConverter taskConverter) {
        this.taskConverter = taskConverter;
    }

    @Override
    public Comment convertToEntity(CommentDTO model, Comment entity) {
        entity.setComment(model.getComment());
        entity.setTask(taskConverter.convertToEntity(model.getTaskDTO(),new Task()));
        return entity;
    }

    @Override
    public CommentDTO convertToModel(Comment entity, CommentDTO model) {
        model.setId(entity.getId());
        model.setComment(entity.getComment());
        model.setTaskDTO(taskConverter.convertToModel(entity.getTask(), new TaskDTO()));
        return model;
    }
}
