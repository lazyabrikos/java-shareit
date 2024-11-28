package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

public class BookingMapper {
    public static Booking mapBookingDtoInputToBooking(BookingDtoInput bookingDtoInput) {
        Booking booking = new Booking();
        booking.setId(bookingDtoInput.getId());
        booking.setStart(bookingDtoInput.getStart());
        booking.setEnd(bookingDtoInput.getEnd());
        return booking;
    }

    public static BookingDtoOutput mapBookingToBookingDtoOutput(Booking booking) {
        return new BookingDtoOutput(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.mapToItemDto(booking.getItem(), null, null, null),
                UserMapper.mapToUserDto(booking.getBooker()),
                booking.getStatus()
        );
    }
}
