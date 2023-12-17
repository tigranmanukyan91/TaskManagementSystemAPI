package com.example.taskmanagementsystem.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private long id;
    @Size(min = 0)
    private String comment;
    @Size(min = 0)
    private TaskDTO taskDTO;

    public CommentDTO(String comment, TaskDTO taskDTO) {
        this.comment = comment;
        this.taskDTO = taskDTO;
    }
}
