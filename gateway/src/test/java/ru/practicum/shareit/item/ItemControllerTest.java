package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.controller.ItemClient;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @Mock
    private ItemClient itemClient;

    @InjectMocks
    private ItemController itemController;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private ItemDto itemDto;

    @BeforeEach
    void setUpMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(itemController).build();
        itemDto = new ItemDto();
    }

    @Test
    void createItem() throws Exception {
        itemDto.setAvailable(true);
        itemDto.setName("Item");
        itemDto.setDescription("Description");
        when(itemClient.createItem(Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(ResponseEntity.ok().body(itemDto));
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getAllItems() throws Exception {
        when(itemClient.getAllItems(Mockito.anyLong())).thenReturn(ResponseEntity.ok().body(List.of()));
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem() throws Exception {
        itemDto.setAvailable(true);
        itemDto.setName("Item");
        itemDto.setDescription("Description");
        when(itemClient.updateItem(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(ItemDto.class)))
                .thenReturn(ResponseEntity.ok().body(itemDto));
        mockMvc.perform(patch("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getItem() throws Exception {
        when(itemClient.getItem(Mockito.anyLong(), Mockito.anyLong())).thenReturn(ResponseEntity.ok().body(itemDto));
        mockMvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void createComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Teeext");
        when(itemClient.createComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(CommentDto.class)))
                .thenReturn(ResponseEntity.ok().body(commentDto));
        mockMvc.perform(post("/items/{itemId}/comment", 1)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk());
    }

    @Test
    void searchItems() throws Exception {
        when(itemClient.search(Mockito.anyLong(), Mockito.anyString()))
                .thenReturn(ResponseEntity.ok().body(List.of()));
        mockMvc.perform(get("/items/search", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("text", "test"))
                .andExpect(status().isOk());

    }
}
