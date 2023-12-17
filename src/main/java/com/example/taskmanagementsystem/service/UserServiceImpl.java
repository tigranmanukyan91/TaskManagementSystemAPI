package com.example.taskmanagementsystem.service;

import com.example.taskmanagementsystem.Converter.CommentConverter;
import com.example.taskmanagementsystem.Converter.TaskConverter;
import com.example.taskmanagementsystem.Converter.UserConverter;
import com.example.taskmanagementsystem.Converter.UserResponseConverter;
import com.example.taskmanagementsystem.entity.Comment;
import com.example.taskmanagementsystem.entity.Task;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.exception.UserAlreadyExistsException;
import com.example.taskmanagementsystem.exception.UserNotFoundException;
import com.example.taskmanagementsystem.model.CommentDTO;
import com.example.taskmanagementsystem.model.TaskDTO;
import com.example.taskmanagementsystem.model.UserDTO;
import com.example.taskmanagementsystem.model.UserResponseJson;
import com.example.taskmanagementsystem.repository.CommentRepository;
import com.example.taskmanagementsystem.repository.TaskRepository;
import com.example.taskmanagementsystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final UserResponseConverter userResponseConverter;
    private final TaskServiceImpl taskService;
    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final TaskRepository taskRepository;
    private final TaskConverter taskConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, PasswordEncoder passwordEncoder, UserResponseConverter userResponseConverter, TaskServiceImpl taskService, CommentRepository commentRepository, CommentConverter commentConverter, TaskRepository taskRepository, TaskConverter taskConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
        this.userResponseConverter = userResponseConverter;
        this.taskService = taskService;
        this.commentRepository = commentRepository;
        this.commentConverter = commentConverter;
        this.taskRepository = taskRepository;
        this.taskConverter = taskConverter;
    }

    @Override
    @Transactional
    public User saveUser(UserDTO userDTO,String password, User.Role role ) {
        if (userExists(userDTO.getEmail())){
            throw new UserAlreadyExistsException("A user with the provided email already exists");
        }
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setPassword(password);
        user.setAssignedTasks(new HashSet<>());
        user.setAuthoredTasks(new HashSet<>());
        user.getRoles().add(role);
        log.info("User prepared to save: {}", user);
        userRepository.save(user);
        log.info("User saved successfully: {}", user);
        return user;
    }

    @Override
    @Transactional
    public User getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            log.info("User retrieved by email: {}",email);
            return optionalUser.get();
        }else throw new UserNotFoundException("Could not find a user with the email provided");
    }

    @Override
    @Transactional
    public User getUserById(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()){
            log.info("User retrieved by ID: {}",userId);
            return optionalUser.get();
        }
        throw new UserNotFoundException("Could not find the user with the Id provided");
    }

    @Override
    @Transactional
    public User updateUser(String email, UserDTO userDTO) {
        User user = getUserByEmail(email);
        userConverter.convertToEntity(userDTO,user);
        if (userDTO.getPassword() != null){
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        log.info("User updated successfully: {}",user);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean deleteUser(String email) {
        User user = getUserByEmail(email);
        userRepository.delete(user);
        log.info("User deleted successfully: {}",user);
        return !userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public boolean userExists(String email) {
        if (userRepository.findByEmail(email).isPresent()){
            log.info("User exists with email: {}",email);
            return true;
        }
        throw new UserNotFoundException("Could not find a user with the email provided");
    }

    @Override
    @Transactional
    public UserDetails getAuthUserPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authenticated user principal retrieved");
        return (UserDetails)auth.getPrincipal();
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        log.info("All users retrieved");
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public Set<Task> getAssignedTasks(String email) {
        User userByEmail = getUserByEmail(email);
        if (userByEmail.getAssignedTasks().isEmpty()){
            throw new NoSuchElementException("There are no elements");
        }
        log.info("Assigned tasks retrieved for user with email:{}",email);
        return userByEmail.getAssignedTasks();
    }

    @Override
    @Transactional
    public Set<Task> getAuthoredTasks(String email) {
        User userByEmail = getUserByEmail(email);
        if (userByEmail.getAuthoredTasks().isEmpty()){
            throw new NoSuchElementException("There are no elements");
        }
        log.info("Authored tasks retrieved for user with email:{}",email);
        return userByEmail.getAuthoredTasks();
    }

    @Override
    @Transactional
    public UserResponseJson getUserInfo(String email) {
        if (!userRepository.existsByEmail(email)){
            throw new UserNotFoundException("User with provided email does not exist");
        }
        User user = getUserByEmail(email);
        log.info("User info retrieved for user with email: {}",email);
        return userResponseConverter.convertToModel(user,new UserResponseJson());
    }

    @Override
    public void addComment(TaskDTO taskDTO, String comment) {
        TaskDTO task = taskService.getTask(taskDTO.getId());
        List<CommentDTO> commentDTOList = task.getCommentDTO();
        CommentDTO commentDTO = new CommentDTO(comment,taskDTO);
        commentDTOList.add(commentDTO);
        log.info("Comment added to task: {}",taskDTO.getId());
        Comment comment1 = commentConverter.convertToEntity(commentDTO, new Comment());
        commentRepository.save(comment1);
        Task task1 = taskConverter.convertToEntity(taskDTO, new Task());
        taskRepository.save(task1);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList());
        log.info("User loaded by username: {}",email);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
