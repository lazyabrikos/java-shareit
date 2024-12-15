package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private final ItemDto itemDto = new ItemDto();
    private final CommentDto commentDto = new CommentDto();
    private final User user = new User(1L, "name", "email");
    private final Item item = new Item();
    private final Comment comment = new Comment();
    private final Booking lastBooking = new Booking();
    private final Booking nextBooking = new Booking();


    @Test
    void createItem() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);
        ItemDto response = itemService.createItem(itemDto, 1L);
        assertNotNull(response);
    }

    @Test
    void updateItem() {
        item.setOwner(user);
        when(itemRepository.findById(Mockito.any())).thenReturn(Optional.of(item));
        when(bookingRepository.findLastBookingForItem(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.any(LocalDateTime.class))).thenReturn(Optional.of(lastBooking));
        when(bookingRepository.findNextBookingForItem(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.any(LocalDateTime.class))).thenReturn(Optional.of(nextBooking));
        when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);
        when(commentRepository.findAllByItemId(Mockito.anyLong())).thenReturn(List.of());
        ItemDto response = itemService.updateItem(itemDto, 1L, 1L);
        assertNotNull(response);
    }

    @Test
    void createComment() {
        comment.setAuthor(user);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.existsByItem_IdAndBooker_IdAndEndBefore(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.any(LocalDateTime.class))).thenReturn(true);
        when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        CommentDto response = itemService.createComment(1L, 1L, commentDto);
        assertNotNull(response);
    }

    @Test
    void findByContext() {
        when(itemRepository.getByContext(Mockito.anyString())).thenReturn(List.of());
        List<ItemDto> response = itemService.getByContext("SS", 1L);
        assertNotNull(response);
    }
}
