package controller;

import com.example.taskmanagementsystem.controller.AuthController;
import com.example.taskmanagementsystem.entity.User;
import com.example.taskmanagementsystem.exception.UserAlreadyExistsException;
import com.example.taskmanagementsystem.model.AuthenticationDTO;
import com.example.taskmanagementsystem.model.UserDTO;
import com.example.taskmanagementsystem.security.JWTUtil;
import com.example.taskmanagementsystem.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        when(request.getSession(true)).thenReturn(session);
    }

    @Test
    void testRegisterSuccess() {
        UserDTO userDTO = new UserDTO("test@example.com", "password");
        when(userServiceImpl.userExists(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtUtil.generateToken(anyString())).thenReturn("jwtToken");

        Map<String, String> result = authController.register(userDTO);

        assertEquals("jwtToken", result.get("jwt-token"));
        verify(userServiceImpl, times(1)).saveUser(eq(userDTO), eq("encodedPassword"), eq(User.Role.ROLE_USER));
    }

    @Test
    void testRegisterUserAlreadyExists() {
        UserDTO userDTO = new UserDTO("existingUser@example.com", "password");
        when(userServiceImpl.userExists(anyString())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authController.register(userDTO));

        verify(userServiceImpl, never()).saveUser(any(UserDTO.class), anyString(), any(User.Role.class));
    }

    @Test
    void testPerformLoginSuccess() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("test@example.com", "password");
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtUtil.generateToken(anyString())).thenReturn("jwtToken");

        Map<String, String> result = authController.performLogin(authenticationDTO);

        assertEquals("jwtToken", result.get("jwt-token"));
    }

    @Test
    void testPerformLoginFailure() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("test@example.com", "wrongPassword");
        when(authenticationManager.authenticate(any())).thenThrow(AuthenticationException.class);

        assertThrows(RuntimeException.class, () -> authController.performLogin(authenticationDTO));
    }

    @Test
    void testLogout() {
        when(session.getAttribute("SPRING_SECURITY_CONTEXT")).thenReturn(mockSecurityContext());
        ResponseEntity<?> result = authController.logout(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(session, times(1)).invalidate();
    }

    @Test
    void testHandleValidationExceptions() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(mock(BindingResult.class));

        Map<String, String> result = authController.handleValidationExceptions(exception);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private SecurityContext mockSecurityContext() {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(mock(Authentication.class));
        return securityContext;
    }
}
