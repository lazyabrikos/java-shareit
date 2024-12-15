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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerTest {

    @Mock
    private ItemRequestClient itemRequestClient;

    @InjectMocks
    private ItemRequestController itemRequestController;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUpMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemRequestController).build();
    }

    @Test
    void createItemRequest() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Description");
        when(itemRequestClient.createRequest(Mockito.anyLong(), Mockito.any(ItemRequestDto.class)))
                .thenReturn(ResponseEntity.ok(itemRequestDto));
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getUsersRequests() throws Exception {
        when(itemRequestClient.getUsersRequest(Mockito.anyLong()))
                .thenReturn(ResponseEntity.ok(List.of()));
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

    }

    @Test
    void getOtherUsersRequest() throws Exception {
        when(itemRequestClient.getOtherUsersRequest(Mockito.anyLong()))
                .thenReturn(ResponseEntity.ok(List.of()));
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getItemRequest() throws Exception {
        when(itemRequestClient.getItemRequest(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(ResponseEntity.ok(List.of()));
        mockMvc.perform(get("/requests/{requestId}", 2)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());

    }
}
