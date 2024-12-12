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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserClient userClient;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();
    private UserDto userDto;

    @BeforeEach
    void setUpMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userDto = new UserDto();
    }

    @Test
    void createUser() throws Exception {
        userDto.setName("Vladimir");
        userDto.setEmail("example@mail.com");
        when(userClient.createUser(Mockito.any(UserDto.class))).thenReturn(ResponseEntity.ok().body(userDto));
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

    }

    @Test
    void updateUser() throws Exception {
        when(userClient.updateUser(Mockito.anyLong(), Mockito.any(UserDto.class)))
                .thenReturn(ResponseEntity.ok().body(userDto));
        mockMvc.perform(patch("/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

    }

    @Test
    void getUser() throws Exception {
        when(userClient.getUser(Mockito.anyLong())).thenReturn(ResponseEntity.ok().body(userDto));
        mockMvc.perform(get("/users/{userId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

    }
}