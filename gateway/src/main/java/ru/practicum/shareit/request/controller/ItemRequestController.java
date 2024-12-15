package ru.practicum.shareit.request.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Get POST request /request with body = {}", itemRequestDto);
        return itemRequestClient.createRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUsersRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get GET request /request for user with id = {}", userId);
        return itemRequestClient.getUsersRequest(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherUsersRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get GET request /request/all for other users with id != {}", userId);
        return itemRequestClient.getOtherUsersRequest(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long requestId) {
        log.info("Get GET /requests/{} to get info about request", requestId);
        return itemRequestClient.getItemRequest(userId, requestId);
    }

}
