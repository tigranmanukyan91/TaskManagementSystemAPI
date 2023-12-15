package com.example.taskmanagementsystem.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserDTO {
    @NotNull
    @Size(min = 0)
    private long id;

    @NotNull(message = "Login field can not be empty")
    @Size(min = 1,max = 255)
    @Email
    private String email;

    @NotNull(message = "Password field can not be empty")
    @Size(min = 8,max = 255)
    private String password;

    @Size(min = 0)
    private Set<TaskDTO> authoredTasks;

    @Size(min = 0)
    private Set<TaskDTO> assignedTasks;

    @Size(min = 0)
    List<CommentDTO> commentDTOList;
}
