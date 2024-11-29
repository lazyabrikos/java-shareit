package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.BadRequestException;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOutput createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @Valid @RequestBody BookingDtoInput bookingDtoInput) {
        log.info("Got POST request with body {}", bookingDtoInput);
        BookingDtoOutput response = bookingService.create(bookingDtoInput, userId);
        log.info("Send response with body {}", response);
        return response;
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOutput updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long bookingId,
                                                @RequestParam(name = "approved") boolean isApproved) {
        log.info("Got PATCH request for updating status of booking with id {}", bookingId);
        BookingDtoOutput response = bookingService.updateStatus(userId, bookingId, isApproved);
        log.info("Send response with body {}", response);
        return response;
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOutput getBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        log.info("Got GET request with booking id {}", bookingId);
        BookingDtoOutput response = bookingService.getBooking(bookingId, userId);
        log.info("Send response with body {}", response);
        return response;
    }

    @GetMapping
    public List<BookingDtoOutput> getAllBokingsForBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.info("Got GET request for all bookings for user with id = " + userId);
        BookingState stateEnum;
        try {
            stateEnum = BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Wrong value of parameter state");
        }
        List<BookingDtoOutput> response = bookingService.getBookingsForBooker(userId,
                stateEnum);
        log.info("Send response with body {}", response);
        return response;
    }

    @GetMapping("/owner")
    public List<BookingDtoOutput> getAllBookingsForOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.info("Got GET request for all bookings for user with id = " + userId);
        BookingState stateEnum;
        try {
            stateEnum = BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Wrong value of parameter state");
        }
        List<BookingDtoOutput> response = bookingService.getBookingsForOwner(userId,
                stateEnum);
        log.info("Send response with body {}", response);
        return response;
    }
}
