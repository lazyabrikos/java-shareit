package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody @Valid ItemDto itemDto) {
        log.info("Get POST request with body = {}", itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable @Positive Long itemId,
                                                @RequestBody @Valid CommentDto commentDto) {
        log.info("Get POST request for /{}/comment with body = {}", itemId, commentDto);
        return itemClient.createComment(userId, itemId, commentDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get GET request /items");
        return itemClient.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable @Positive Long itemId) {
        log.info("Get GET request /items/{}", itemId);
        return itemClient.getItem(userId, itemId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable @Positive Long itemId,
                                             @RequestBody @Valid ItemDto itemDto) {
        log.info("Get PATCH request /items/{} with body = {}", itemId, itemDto);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(name = "text") String text) {
        log.info("Get GET request /items/search?text={}", text);
        return itemClient.search(userId, text);
    }

}
