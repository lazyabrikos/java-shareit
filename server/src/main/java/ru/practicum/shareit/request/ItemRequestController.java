package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Get POST request /requests with body = {}", itemRequestDto);
        ItemRequestDto response = itemRequestService.createItemRequest(userId, itemRequestDto);
        log.info("Send response with body = {}", response);
        return response;
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get GET request /requests to find user with id = {} requests", userId);
        List<ItemRequestDto> response = itemRequestService.getUserRequests(userId);
        log.info("Send response with body = {}", response);
        return response;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOtherUsersRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get GET requests /requests/all to find other users request excluding id = {}", userId);
        List<ItemRequestDto> response = itemRequestService.getOtherUsersRequests(userId);
        log.info("Send response with body = {}", response);
        return response;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long requestId) {
        log.info("Get GET request /request/{} to get info about request", requestId);
        ItemRequestDto response = itemRequestService.getItemRequest(userId, requestId);
        log.info("Send response with body = {}", response);
        return response;
    }
}
