package com.example.taskmanagementsystem.model;

import com.example.taskmanagementsystem.entity.TaskPriority;
import com.example.taskmanagementsystem.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    @Size(min = 1)
    private long id;

    @NotNull(message = "The title field should not be emty")
    @Size(min = 1,max = 255)
    private String title;

    @NotNull(message = "The description field should not be empty")
    @Size(min = 1, max = 1000)
    private String description;

    @NotNull(message = "Task status can not be empty")
    private TaskStatus taskStatus;

    @NotNull(message = "Task priority can not be empty")
    private TaskPriority taskPriority;

    @NotNull(message = "The task should have a user attached")
    @Size(min = 1, max = 255)
    private UserDTO author;

    @Size(min = 0)
    private Set<UserDTO> assignees;

    @Size(min = 0)
    private List<CommentDTO> commentDTO;



}
