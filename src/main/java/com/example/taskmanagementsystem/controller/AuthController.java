package com.example.taskmanagementsystem.controller;

import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.exception.UserAlreadyExistsException;
import com.example.taskmanagementsystem.model.AuthenticationDTO;
import com.example.taskmanagementsystem.model.LoginJson;
import com.example.taskmanagementsystem.model.ResponseJson;
import com.example.taskmanagementsystem.model.UserDTO;
import com.example.taskmanagementsystem.security.JWTUtil;
import com.example.taskmanagementsystem.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller class handling authentication-related operations.
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    /**
     * Endpoint for user registration.
     *
     * @param userDTO UserDTO object containing user registration data.
     * @return ResponseEntity with a JSON response indicating the registration status.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> register(@Valid @RequestBody UserDTO userDTO){
        if (userServiceImpl.userExists(userDTO.getEmail())){
            log.warn("User registration failed. User with email {} already exists", userDTO.getEmail());
            throw new UserAlreadyExistsException("User with the email already exists");
        }
        userServiceImpl.saveUser(userDTO, passwordEncoder.encode(userDTO.getPassword()),User.Role.ROLE_USER);
        log.info("User registered successfully: {}", userDTO.getEmail());
        String token = jwtUtil.generateToken(userDTO.getEmail());
        return Map.of("jwt-token", token);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String,String> performLogin(@RequestBody AuthenticationDTO authenticationDTO){
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(),authenticationDTO.getPassword());
        try {
            authenticationManager.authenticate(authInputToken);
            log.info("User {} logged in", authenticationDTO.getEmail());
        } catch (AuthenticationException e) {
            log.error("Invalid credentials for login attempt",e);
            throw new RuntimeException(e);
        }

        String token = jwtUtil.generateToken(authenticationDTO.getEmail());
        return Map.of("jwt-token",token);
    }

    /**
     * Endpoint for user logout.
     *
     * @param request HttpServletRequest for accessing the session.
     * @return ResponseEntity with a JSON response indicating the logout status.
     */
    @RequestMapping(value = "/logout",method = RequestMethod.POST)
    public ResponseEntity<?> logout(HttpServletRequest request){
        SecurityContextHolder.getContext().setAuthentication(null);
        HttpSession session = request.getSession(false);
        if (session != null){
            session.invalidate();
        }
        log.info("User logged out");
        return new ResponseEntity<>(new ResponseJson("User logged out","Success"),HttpStatus.OK);
    }

    /**
     * Exception handler for handling validation errors in the request payload.
     *
     * @param exception MethodArgumentNotValidException instance.
     * @return Map containing field names and corresponding error messages.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String,String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) ->{
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        // Log validation errors for debugging purposes
        log.debug("Validation errors: {}", errors);
        return errors;
    }
}
