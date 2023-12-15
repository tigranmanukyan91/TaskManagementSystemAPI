package com.example.taskmanagementsystem.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseJson {
    private long id;
    @Email
    private String email;
    @Size(min = 0)
    private Set<TaskDTO> authoredTasks;

    @Size(min = 0)
    private Set<TaskDTO> assignedTasks;}
