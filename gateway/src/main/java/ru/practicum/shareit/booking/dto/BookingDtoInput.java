package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validators.BookingDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@BookingDate
public class BookingDtoInput {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}
