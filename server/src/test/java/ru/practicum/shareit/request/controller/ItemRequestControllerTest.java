package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {
    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();
    private final UserDto userDto = new UserDto(1L, "name", "tmp@mail.ru");
    private final ItemRequestDto itemRequestDto = new ItemRequestDto();

    @BeforeEach
    void setUpMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
    }

    @Test
    void createItemRequest() throws Exception {
        itemRequestDto.setDescription("Description");
        when(itemRequestService.createItemRequest(Mockito.anyLong(), Mockito.any(ItemRequestDto.class)))
                .thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                .header("X-Sharer-User-Id", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(itemRequestDto))).andExpect(status().isOk());
    }

    @Test
    void getItemRequest() throws Exception {
        when(itemRequestService.getUserRequests(Mockito.anyLong())).thenReturn(List.of());
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getOtherUsersRequests() throws Exception {
        when(itemRequestService.getOtherUsersRequests(Mockito.anyLong())).thenReturn(List.of());
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getItemRequests() throws Exception {
        when(itemRequestService.getItemRequest(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemRequestDto);
        mockMvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }
}
