package ru.practicum.shareit.exceptions;

public class NotAvailableForBooking extends RuntimeException {
    public NotAvailableForBooking(String message) {
        super(message);
    }
}
