package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {
    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUpMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private final ItemDto itemDto = new ItemDto();

    @Test
    void getAll() throws Exception {
        when(itemService.getAll(Mockito.anyLong())).thenReturn(List.of());
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createItem() throws Exception {
        when(itemService.createItem(Mockito.any(ItemDto.class), Mockito.anyLong())).thenReturn(itemDto);
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(Mockito.any(ItemDto.class), Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemDto);
        mockMvc.perform(patch("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void findByContext() throws Exception {
        when(itemService.getByContext(Mockito.anyString(), Mockito.anyLong())).thenReturn(List.of());
        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "text"))
                .andExpect(status().isOk());
    }
}
