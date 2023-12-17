package controller;

import com.example.taskmanagementsystem.Converter.TaskConverter;
import com.example.taskmanagementsystem.Converter.UserConverter;
import com.example.taskmanagementsystem.controller.TaskController;
import com.example.taskmanagementsystem.model.TaskDTO;
import com.example.taskmanagementsystem.model.UserDTO;
import com.example.taskmanagementsystem.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    @Mock
    private TaskServiceImpl taskService;

    @Mock
    private TaskConverter taskConverter;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    void testCreateTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(taskDTO);

        mockMvc.perform(post("/task/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist()); // adjust the expected JSON structure based on your implementation

        verify(taskService, times(1)).createTask(any(TaskDTO.class));
    }

    @Test
    void testUpdateTask() throws Exception {
        when(taskService.updateTask(any(TaskDTO.class))).thenReturn("Success");

        mockMvc.perform(put("/task/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));

        verify(taskService, times(1)).updateTask(any(TaskDTO.class));
    }

    @Test
    void testGetTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        when(taskService.getTask(anyLong())).thenReturn(taskDTO);

        mockMvc.perform(get("/task/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist()); // adjust the expected JSON structure based on your implementation

        verify(taskService, times(1)).getTask(anyLong());
    }

    @Test
    void testDeleteTask() throws Exception {
        when(taskService.deleteTask(anyLong())).thenReturn("Success");

        mockMvc.perform(delete("/task/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));

        verify(taskService, times(1)).deleteTask(anyLong());
    }

    @Test
    void testGetTasksByAuthor() throws Exception {
        Set<TaskDTO> taskDTOSet = new HashSet<>();
        when(taskService.getTasksByAuthor(any(UserDTO.class))).thenReturn(taskDTOSet);

        mockMvc.perform(get("/task/getTasksByAuthor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(taskService, times(1)).getTasksByAuthor(any(UserDTO.class));
    }

    @Test
    void testGetTasksByAssignee() throws Exception {
        Set<TaskDTO> taskDTOSet = new HashSet<>();
        when(taskService.getTasksByAssignee(any(UserDTO.class))).thenReturn(taskDTOSet);

        mockMvc.perform(get("/task/getTasksByAssignee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(taskService, times(1)).getTasksByAssignee(any(UserDTO.class));
    }

    @Test
    void testGetAuthorByTask() throws Exception {
        UserDTO userDTO = new UserDTO();
        when(taskService.getAuthorByTask(any(TaskDTO.class))).thenReturn(userDTO);

        mockMvc.perform(get("/task/getAuthorByTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").doesNotExist()); // adjust the expected JSON structure based on your implementation

        verify(taskService, times(1)).getAuthorByTask(any(TaskDTO.class));
    }

    @Test
    void testGetAssigneesByTask() throws Exception {
        Set<UserDTO> userDTOSet = new HashSet<>();
        when(taskService.getAssigneesByTask(any(TaskDTO.class))).thenReturn(userDTOSet);

        mockMvc.perform(get("/task/getAssigneesByTask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

        verify(taskService, times(1)).getAssigneesByTask(any(TaskDTO.class));
    }
}

