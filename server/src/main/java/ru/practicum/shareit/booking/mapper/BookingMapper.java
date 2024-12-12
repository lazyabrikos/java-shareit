package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

public class BookingMapper {

    public static Booking mapBookingDtoInputToBooking(BookingDtoInput bookingDtoInput) {
        return Booking.builder()
                .id(bookingDtoInput.getId())
                .start(bookingDtoInput.getStart())
                .end(bookingDtoInput.getEnd())
                .build();
    }

    public static BookingDtoOutput mapBookingToBookingDtoOutput(Booking booking) {
        return BookingDtoOutput.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.mapToItemDto(booking.getItem(), null, null, null))
                .booker(UserMapper.mapToUserDto(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }
}
