package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.model.ResponseJson;
import com.example.taskmanagementsystem.model.UserDTO;
import com.example.taskmanagementsystem.model.UserResponseJson;
import com.example.taskmanagementsystem.service.UserServiceImpl;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
public class UserController {

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserController(UserServiceImpl userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","ASSIGNEE","AUTHOR","USER"})
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO){
        UserDetails principal = userService.getAuthUserPrincipal();
        User user = userService.updateUser(principal.getUsername(), userDTO);

        Authentication authObject = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authObject);

        return new ResponseEntity<>(new ResponseJson("User details updated","Success"), HttpStatus.OK);
    }

    @RequestMapping(value = "/getUser",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed({"ADMIN","AUTHOR","ASSIGNEE","USER"})
    public UserResponseJson getUser(){
        UserDetails principal = userService.getAuthUserPrincipal();
        return userService.getUserInfo(principal.getUsername());
    }

    @RequestMapping(value = "/{userId}",method = RequestMethod.DELETE)
    @RolesAllowed("ADMIN")
    public ResponseEntity<?> deleteUser(@PathVariable long userId){
        User user = userService.getUserById(userId);
        boolean deleteUser = userService.deleteUser(user.getEmail());
        if (deleteUser){
            return new ResponseEntity<>(new ResponseJson("User deleter","Success"),HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseJson("User not deleted","Failed"),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    @RolesAllowed({"ADMIN","USER","AUTHOR","ASSIGNEE"})
    public ResponseEntity<?> deleteUser(){
        UserDetails principal = userService.getAuthUserPrincipal();
        boolean deleteUser = userService.deleteUser(principal.getUsername());
        if (deleteUser){
            return new ResponseEntity<>(new ResponseJson("User deleted","Success"),HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseJson("User not deleted","Failed"),HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
