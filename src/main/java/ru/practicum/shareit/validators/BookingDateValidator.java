package ru.practicum.shareit.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingDtoInput;

import java.time.LocalDateTime;

public class BookingDateValidator implements ConstraintValidator<BookingDate, BookingDtoInput> {
    @Override
    public void initialize(BookingDate constraintAnnotation) {
        // Инициализация, если требуется
    }

    @Override
    public boolean isValid(BookingDtoInput bookingDtoInput, ConstraintValidatorContext constraintValidatorContext) {
        if (bookingDtoInput.getStart() == null || bookingDtoInput.getEnd() == null) {
            return false;
        }

        boolean endNotInPast = bookingDtoInput.getStart().isAfter(LocalDateTime.now())
                || bookingDtoInput.getEnd().equals(LocalDateTime.now());

        boolean startNotInPast = bookingDtoInput.getStart().isAfter(LocalDateTime.now())
                || bookingDtoInput.getStart().equals(LocalDateTime.now());

        boolean endNotEqualStart = !bookingDtoInput.getStart().equals(bookingDtoInput.getEnd());
        boolean endAfterStart = bookingDtoInput.getEnd().isAfter(bookingDtoInput.getStart());

        return endNotInPast && startNotInPast && endAfterStart && endNotEqualStart;
    }

}