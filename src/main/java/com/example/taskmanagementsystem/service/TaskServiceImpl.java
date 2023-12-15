package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.Converter.CommentConverter;
import com.example.taskmanagementsystem.Converter.TaskConverter;
import com.example.taskmanagementsystem.Converter.UserConverter;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.exception.TaskNotFoundException;
import com.example.taskmanagementsystem.exception.UserNotFoundException;
import com.example.taskmanagementsystem.model.TaskDTO;
import com.example.taskmanagementsystem.model.UserDTO;
import com.example.taskmanagementsystem.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskServiceImpl implements TaskService{
    private final UserConverter userConverter;
    private final TaskRepository taskRepository;
    private final TaskConverter taskConverter;
    private final CommentConverter commentConverter;

    @Autowired
    public TaskServiceImpl(UserConverter userConverter, TaskRepository taskRepository, TaskConverter taskConverter, CommentConverter commentConverter) {
        this.userConverter = userConverter;
        this.taskRepository = taskRepository;
        this.taskConverter = taskConverter;
        this.commentConverter = commentConverter;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());

        task.setAuthor(userConverter.convertToEntity(taskDTO.getAuthor(),new User()));
        Set<UserDTO> assigneesDTO = taskDTO.getAssignees();
        Set<User> assignees = new HashSet<>();
        for (UserDTO assigneeDTO:assigneesDTO){
            User user = userConverter.convertToEntity(assigneeDTO, new User());
            assignees.add(user);
        }
        task.setAssignees(assignees);
        task.setTaskStatus(taskDTO.getTaskStatus());
        task.setTaskPriority(taskDTO.getTaskPriority());
        taskRepository.save(task);
        return taskConverter.convertToModel(task,new TaskDTO());
    }

    @Override
    public String updateTask(TaskDTO taskDTO) {
        Optional<Task> optionalTask = taskRepository.findById(taskDTO.getId());
        if (optionalTask.isEmpty()){
            throw new TaskNotFoundException("Task not found");
        }
        Task task = optionalTask.get();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setTaskStatus(taskDTO.getTaskStatus());
        task.setTaskPriority(taskDTO.getTaskPriority());
        task.setAuthor(userConverter.convertToEntity(taskDTO.getAuthor(),task.getAuthor()));
        Set<UserDTO> assigneesDTO = taskDTO.getAssignees();
        Set<User> assignees = new HashSet<>();
        for (UserDTO assigneeDTO:assigneesDTO){
            User user = userConverter.convertToEntity(assigneeDTO, new User());
            assignees.add(user);
        }
        task.setAssignees(assignees);
        return "Updated";
    }

    @Override
    public TaskDTO getTask(long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty()){
            throw new TaskNotFoundException("Task with the id is not found");
        }
        return taskConverter.convertToModel(optionalTask.get(), new TaskDTO());
    }

    @Override
    public String deleteTask(long id) {
        if (taskRepository.findById(id).isEmpty()){
            throw new TaskNotFoundException("Task with the id is not found");
        }
        taskRepository.deleteById(id);
        return "The task is deleted successfully";
    }

    @Override
    public Set<TaskDTO> getTasksByAuthor(UserDTO author) {
        if (author.getAuthoredTasks().isEmpty()){
            throw new TaskNotFoundException("There are no tasks available");
        }
        return author.getAuthoredTasks();
    }

    @Override
    public Set<TaskDTO> getTasksByAssignee(UserDTO assignee) {
        if (assignee.getAssignedTasks().isEmpty()){
            throw new TaskNotFoundException("There are no tasks available");
        }
        return assignee.getAssignedTasks();
    }
    @Override
    public UserDTO getAuthorByTask(TaskDTO taskDTO) {
        return taskDTO.getAuthor();
    }

    @Override
    public Set<UserDTO> getAssigneesByTask(TaskDTO taskDTO) {
        if (taskDTO.getAssignees().isEmpty()){
            throw new UserNotFoundException("There are no assignees found");
        }
        return taskDTO.getAssignees();
    }
}
