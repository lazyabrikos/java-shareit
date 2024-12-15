package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();
    private final UserDto userDto = new UserDto(null, "name", "mail@mail.ru");

    @BeforeEach
    void setUpMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createUser() throws Exception {
        when(userService.createUser(Mockito.any(UserDto.class))).thenReturn(userDto);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

    }

    @Test
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(Mockito.anyLong());
        mockMvc.perform(delete("/users/{userId}", 1)).andExpect(status().isOk());
    }

    @Test
    void getUser() throws Exception {
        when(userService.getUserById(Mockito.anyLong())).thenReturn(userDto);
        mockMvc.perform(get("/users/{userId}", 1)).andExpect(status().isOk());
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(Mockito.any(UserDto.class), Mockito.anyLong())).thenReturn(userDto);
        mockMvc.perform(patch("/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }
}
