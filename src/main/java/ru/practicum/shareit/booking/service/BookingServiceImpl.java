package ru.practicum.shareit.booking.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.DataAccessException;
import ru.practicum.shareit.exceptions.ItemOwnerException;
import ru.practicum.shareit.exceptions.NotAvailableForBooking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDtoOutput create(BookingDtoInput bookingDtoInput, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        Item item = itemRepository.findById(bookingDtoInput.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found with id = " + bookingDtoInput.getItemId()));

        if (item.getOwner().getId().equals(userId)) {
            throw new ItemOwnerException("Owner of item cannot be a booker");
        }

        if (!item.getAvailable()) {
            throw new NotAvailableForBooking("Item is not available for booking");
        }

        Booking booking = BookingMapper.mapBookingDtoInputToBooking(bookingDtoInput);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.mapBookingToBookingDtoOutput(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOutput updateStatus(Long userId, Long bookingId, boolean isApproved) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ItemOwnerException("User not found with id = " + userId));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found with id =" + bookingId));
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ItemOwnerException("Not owner cannot change status of booking");
        }

        if (isApproved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.mapBookingToBookingDtoOutput(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOutput getBooking(Long bookingId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found with id =" + bookingId));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new DataAccessException("Info about booking not available for user with id =" + userId);
        }

        return BookingMapper.mapBookingToBookingDtoOutput(booking);
    }

    @Override
    public List<BookingDtoOutput> getBookingsForBooker(Long userId, BookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));

        return switch (state) {
            case BookingState.ALL -> bookingRepository.findAllByBooker_IdOrderByStartAsc(userId)
                    .stream()
                    .map(BookingMapper::mapBookingToBookingDtoOutput)
                    .collect(Collectors.toList());
            case BookingState.CURRENT -> bookingRepository.findAllBookerCurrentBookings(userId, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::mapBookingToBookingDtoOutput)
                    .collect(Collectors.toList());
            case BookingState.PAST -> bookingRepository.findAllBookerPastBookings(userId, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::mapBookingToBookingDtoOutput)
                    .collect(Collectors.toList());
            case BookingState.FUTURE -> bookingRepository.findAllBookerFutureBookings(userId, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::mapBookingToBookingDtoOutput)
                    .collect(Collectors.toList());
            case BookingState.WAITING -> bookingRepository.findAllByBooker_IdAndStatusOrderByStartAsc(userId,
                            BookingStatus.WAITING)
                    .stream()
                    .map(BookingMapper::mapBookingToBookingDtoOutput)
                    .collect(Collectors.toList());
            case BookingState.REJECTED -> bookingRepository.findAllByBooker_IdAndStatusOrderByStartAsc(userId,
                            BookingStatus.REJECTED)
                    .stream()
                    .map(BookingMapper::mapBookingToBookingDtoOutput)
                    .collect(Collectors.toList());
        };
    }

    @Override
    public List<BookingDtoOutput> getBookingsForOwner(Long userId, BookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id = " + userId));

        List<Long> itemIds = itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(Item::getId)
                .toList();
        return switch (state) {
            case BookingState.ALL -> bookingRepository.findAllByItem_IdInOrderByStartAsc(itemIds)
                    .stream()
                    .map(BookingMapper::mapBookingToBookingDtoOutput)
                    .collect(Collectors.toList());
            case BookingState.CURRENT -> bookingRepository.findAllOwnerCurrentBookings(itemIds, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::mapBookingToBookingDtoOutput)
                    .collect(Collectors.toList());
            case BookingState.PAST -> bookingRepository.findAllOwnerPastBookings(itemIds, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::mapBookingToBookingDtoOutput)
                    .collect(Collectors.toList());
            case BookingState.FUTURE -> bookingRepository.findAllOwnerFutureBookings(itemIds, LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::mapBookingToBookingDtoOutput)
                    .collect(Collectors.toList());
            case BookingState.WAITING -> bookingRepository.findAllByItem_IdInAndStatusOrderByStartAsc(itemIds,
                            BookingStatus.WAITING)
                    .stream()
                    .map(BookingMapper::mapBookingToBookingDtoOutput)
                    .collect(Collectors.toList());
            case BookingState.REJECTED -> bookingRepository.findAllByItem_IdInAndStatusOrderByStartAsc(itemIds,
                            BookingStatus.REJECTED)
                    .stream()
                    .map(BookingMapper::mapBookingToBookingDtoOutput)
                    .collect(Collectors.toList());
        };

    }
}
