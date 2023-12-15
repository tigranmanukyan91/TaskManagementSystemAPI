package com.example.taskmanagementsystem.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginJson {
    @NotEmpty(message = "Field can't be empty")
    @Email
    private String email;

    @NotEmpty(message = "Field can't be empty")
    private String password;
    @NotNull(message = "Every comment should have a task")
    private TaskDTO taskDTO;
}