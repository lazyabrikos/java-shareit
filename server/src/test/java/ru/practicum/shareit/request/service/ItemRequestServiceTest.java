package ru.practicum.shareit.request.service;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private final ItemRequestDto itemRequestDto = new ItemRequestDto();
    private final ItemRequest itemRequest = new ItemRequest();
    private final User user = new User(1L, "name", "email@mail.ru");

    @Test
    void createItemRequest() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(Mockito.any(ItemRequest.class))).thenReturn(itemRequest);
        ItemRequestDto result = itemRequestService.createItemRequest(1L, itemRequestDto);
        assertNotNull(result);
    }

    @Test
    void createItemRequestUserNotFound() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> itemRequestService.createItemRequest(1L, itemRequestDto));
        Assertions.assertEquals("User not found with id = " + 1L, exception.getMessage());
    }

    @Test
    void getItemRequest() {
        itemRequest.setRequester(user);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findAllByRequestId(null)).thenReturn(List.of());
        ItemRequestDto response = itemRequestService.getItemRequest(1L, 2L);
        assertNotNull(response);
    }

}
