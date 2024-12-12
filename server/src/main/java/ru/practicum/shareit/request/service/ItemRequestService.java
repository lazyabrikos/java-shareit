package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getUserRequests(Long userId);

    List<ItemRequestDto> getOtherUsersRequests(Long userId);

    ItemRequestDto getItemRequest(Long userId, Long requestId);
}
