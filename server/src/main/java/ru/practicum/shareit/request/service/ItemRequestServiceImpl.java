package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public ItemRequestDto createItemRequest(Long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        ItemRequest itemRequest = ItemRequestMapper.itemRequestDtoToItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequester(user);
        ItemRequestDto response = ItemRequestMapper.itemRequestToItemRequestDto(
                itemRequestRepository.save(itemRequest)
        );
        response.setRequester(UserMapper.mapToUserDto(user));
        return response;
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Long userId) {
        return itemRequestRepository.findAllByRequester(userId)
                .stream()
                .map(itemRequest -> {
                    ItemRequestDto itemRequestDto = ItemRequestMapper.itemRequestToItemRequestDto(itemRequest);
                    List<ItemDto> items = itemRepository.findAllByRequestId(itemRequest.getId())
                            .stream()
                            .map(item -> ItemMapper.mapToItemDto(item, null,
                                    null, null))
                            .toList();
                    itemRequestDto.setItems(items);
                    itemRequestDto.setRequester(UserMapper.mapToUserDto(itemRequest.getRequester()));
                    return itemRequestDto;
                })
                .toList();
    }

    @Override
    public List<ItemRequestDto> getOtherUsersRequests(Long userId) {
        return itemRequestRepository.findOtherUsersRequests(userId)
                .stream()
                .map(itemRequest -> {
                    ItemRequestDto itemRequestDto = ItemRequestMapper.itemRequestToItemRequestDto(itemRequest);
                    List<ItemDto> items = itemRepository.findAllByRequestId(itemRequest.getId())
                            .stream()
                            .map(item -> ItemMapper.mapToItemDto(item, null,
                                    null, null))
                            .toList();
                    itemRequestDto.setItems(items);
                    itemRequestDto.setRequester(UserMapper.mapToUserDto(itemRequest.getRequester()));
                    return itemRequestDto;
                })
                .toList();

    }

    @Override
    public ItemRequestDto getItemRequest(Long userId, Long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Item request not found with id = " + requestId));

        ItemRequestDto itemRequestDto = ItemRequestMapper.itemRequestToItemRequestDto(itemRequest);
        List<ItemDto> items = itemRepository.findAllByRequestId(itemRequest.getId())
                .stream()
                .map(item -> ItemMapper.mapToItemDto(item, null,
                        null, null))
                .toList();
        itemRequestDto.setItems(items);
        itemRequestDto.setRequester(UserMapper.mapToUserDto(itemRequest.getRequester()));
        return itemRequestDto;
    }
}
