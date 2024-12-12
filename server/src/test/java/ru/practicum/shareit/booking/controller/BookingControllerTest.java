package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoInput;
import ru.practicum.shareit.booking.dto.BookingDtoOutput;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingServiceImpl bookingService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUpMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    private final ObjectMapper mapper = new ObjectMapper();
    private final BookingDtoInput bookingDtoInput = new BookingDtoInput();
    private final BookingDtoOutput bookingDtoOutput = new BookingDtoOutput();

    @Test
    void createBookingTest() throws Exception {
        when(bookingService.create(Mockito.any(BookingDtoInput.class), Mockito.anyLong())).thenReturn(bookingDtoOutput);
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDtoInput)))
                .andExpect(status().isOk());
    }

    @Test
    void updateBookingTest() throws Exception {
        when(bookingService.updateStatus(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(bookingDtoOutput);
        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBooking(Mockito.anyLong(), Mockito.anyLong())).thenReturn(bookingDtoOutput);
        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookingsForBooker() throws Exception {
        when(bookingService.getBookingsForBooker(Mockito.anyLong(), Mockito.any(BookingState.class)))
                .thenReturn(List.of());
        mockMvc.perform(get("/bookings", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllBookingsForOwner() throws Exception {
        when(bookingService.getBookingsForOwner(Mockito.anyLong(), Mockito.any(BookingState.class)))
                .thenReturn(List.of());
        mockMvc.perform(get("/bookings/owner", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }
}
