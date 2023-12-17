package controller;

import com.example.taskmanagementsystem.controller.UserController;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.model.ResponseJson;
import com.example.taskmanagementsystem.model.UserDTO;
import com.example.taskmanagementsystem.model.UserResponseJson;
import com.example.taskmanagementsystem.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateUser() {
        UserDTO userDTO = new UserDTO();
        UserDetails principal = userService.getAuthUserPrincipal();
        User user = new User();

        when(userService.getAuthUserPrincipal()).thenReturn(principal);
        when(userService.updateUser(anyString(), any(UserDTO.class))).thenReturn(user);
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));

        ResponseEntity<?> responseEntity = userController.updateUser(userDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User details updated", ((ResponseJson) responseEntity.getBody()).getMessage());
        assertEquals("Success", ((ResponseJson) responseEntity.getBody()).getStatus());

        verify(userService, times(1)).getAuthUserPrincipal();
        verify(userService, times(1)).updateUser(eq(principal.getUsername()), eq(userDTO));
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testGetUser() {
        UserDetails principal = userService.getAuthUserPrincipal();
        UserResponseJson userResponseJson = new UserResponseJson();

        when(userService.getAuthUserPrincipal()).thenReturn(principal);
        when(userService.getUserInfo(anyString())).thenReturn(userResponseJson);

        UserResponseJson result = userController.getUser();

        assertEquals(userResponseJson, result);

        verify(userService, times(1)).getAuthUserPrincipal();
        verify(userService, times(1)).getUserInfo(eq(principal.getUsername()));
    }

    @Test
    void testDeleteUserById() {
        long userId = 1L;
        User user = new User();
        when(userService.getUserById(userId)).thenReturn(user);
        when(userService.deleteUser(user.getEmail())).thenReturn(true);

        ResponseEntity<?> responseEntity = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User deleter", ((ResponseJson) responseEntity.getBody()).getMessage());
        assertEquals("Success", ((ResponseJson) responseEntity.getBody()).getStatus());

        verify(userService, times(1)).getUserById(eq(userId));
        verify(userService, times(1)).deleteUser(eq(user.getEmail()));
    }

    @Test
    void testDeleteUser() {
        UserDetails principal = userService.getAuthUserPrincipal();
        when(userService.getAuthUserPrincipal()).thenReturn(principal);
        when(userService.deleteUser(principal.getUsername())).thenReturn(true);

        ResponseEntity<?> responseEntity = userController.deleteUser();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User deleted", ((ResponseJson) responseEntity.getBody()).getMessage());
        assertEquals("Success", ((ResponseJson) responseEntity.getBody()).getStatus());

        verify(userService, times(1)).getAuthUserPrincipal();
        verify(userService, times(1)).deleteUser(eq(principal.getUsername()));
    }
}

