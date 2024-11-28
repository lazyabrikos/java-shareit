package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;

import java.util.List;

public interface BookingService {
    BookingDtoOutput create(BookingDtoInput bookingDtoInput, Long userId);

    BookingDtoOutput updateStatus(Long userId, Long bookingId, boolean isApproved);

    BookingDtoOutput getBooking(Long bookingId, Long userId);

    List<BookingDtoOutput> getBookingsForBooker(Long userId, String state);

    List<BookingDtoOutput> getBookingsForOwner(Long userId, String state);
}
