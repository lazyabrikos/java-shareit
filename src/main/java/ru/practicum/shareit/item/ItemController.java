package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get GET request /items");
        List<ItemDto> response = itemService.getAll(userId);
        log.info("Send response for GET request with body {}", response);
        return response;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Get POST request with body {}", itemDto);
        ItemDto response = itemService.createItem(itemDto, userId);
        log.info("Send response for POST request with body {}", response);
        return response;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemDto itemDto, @PathVariable Long itemId) {
        log.info("Get PATCH request with body {}", itemDto);
        ItemDto response = itemService.updateItem(itemDto, itemId, userId);
        log.info("Send response for PATCH request with body {}", response);
        return response;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("Get GET request /items/{}", itemId);
        ItemDto response = itemService.getItemById(itemId, userId);
        log.info("Send response for GET request with body {}", response);
        return response;
    }

    @GetMapping("/search")
    public List<ItemDto> getByContext(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @RequestParam(name = "text") String text) {
        log.info("Get GET request /items/search wich text = {}", text);
        List<ItemDto> response = itemService.getByContext(text, userId);
        log.info("Send response for GET request with body {}", response);
        return response;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.info("Get POST request /{}/comment with body {} for user {}", itemId, commentDto, userId);
        CommentDto response = itemService.createComment(itemId, userId, commentDto);
        log.info("Send response with body {}", response);
        return response;
    }
}
