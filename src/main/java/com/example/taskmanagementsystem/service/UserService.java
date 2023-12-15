package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.model.TaskDTO;
import com.example.taskmanagementsystem.model.UserDTO;
import com.example.taskmanagementsystem.model.UserResponseJson;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

public interface UserService {

    User saveUser(UserDTO userDTO,String password, User.Role role);

    User getUserByEmail(String email);

    User getUserById(long userId);

    User updateUser(String email, UserDTO userDTO);

    boolean deleteUser(String email);

    boolean userExists(String email);
    public UserDetails getAuthUserPrincipal();

    List<User> getAllUsers();

    Set<Task> getAssignedTasks(String email);

    Set<Task> getAuthoredTasks(String email);

    UserResponseJson getUserInfo(String email);

    void addComment(TaskDTO taskDTO,String comment);

}
