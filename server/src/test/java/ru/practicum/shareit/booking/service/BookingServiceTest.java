package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ItemOwnerException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private final Booking booking = new Booking();
    private final BookingDtoInput bookingDtoInput = new BookingDtoInput();
    private final BookingDtoOutput bookingDtoOutput = new BookingDtoOutput();
    private final User user = new User(2L, "name", "email");
    private final Item item = new Item();

    @Test
    void createBooking() {
        item.setAvailable(true);
        item.setOwner(user);
        booking.setItem(item);
        booking.setBooker(user);
        item.setId(1L);
        bookingDtoInput.setItemId(1L);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);
        BookingDtoOutput response = bookingService.create(bookingDtoInput, 1L);
        assertNotNull(response);
    }

    @Test
    void createBookingUserNotFound() {
        item.setAvailable(true);
        item.setOwner(user);
        booking.setItem(item);
        booking.setBooker(user);
        item.setId(1L);
        bookingDtoInput.setItemId(1L);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.create(bookingDtoInput, 1L));
        assertEquals("User not found with id = 1", exception.getMessage());
    }

    @Test
    void createBookingOwner() {
        user.setId(1L);
        item.setAvailable(true);
        item.setOwner(user);
        booking.setItem(item);
        booking.setBooker(user);
        item.setId(1L);
        bookingDtoInput.setItemId(1L);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        ItemOwnerException exception = assertThrows(ItemOwnerException.class,
                () -> bookingService.create(bookingDtoInput, 1L));
        assertEquals("Owner of item cannot be a booker", exception.getMessage());
    }

    @Test
    void updateStatusOwner() {
        item.setAvailable(true);
        item.setOwner(user);
        booking.setItem(item);
        booking.setBooker(user);
        item.setId(1L);
        bookingDtoInput.setItemId(1L);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking));
        ItemOwnerException exception = assertThrows(ItemOwnerException.class,
                () -> bookingService.updateStatus(1L, 1L, true));
        assertEquals("Not owner cannot change status of booking", exception.getMessage());

    }
}
