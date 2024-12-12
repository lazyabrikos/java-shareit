package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.controller.BookingClient;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingClient bookingClient;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();
    private BookItemRequestDto bookItemRequestDto;

    @BeforeEach
    void setUpMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        bookItemRequestDto = new BookItemRequestDto();
    }

    @Test
    void bookItem() throws Exception {
        when(bookingClient.bookItem(Mockito.anyLong(), Mockito.any(BookItemRequestDto.class)))
                .thenReturn(ResponseEntity.ok().body(bookItemRequestDto));
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookItemRequestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getBooking() throws Exception {
        when(bookingClient.getBooking(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(ResponseEntity.ok().body(bookItemRequestDto));
        mockMvc.perform(get("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsForBooker() throws Exception {
        when(bookingClient.getBookingsForBooker(Mockito.anyLong(), Mockito.any(BookingState.class)))
                .thenReturn(ResponseEntity.ok().body(bookItemRequestDto));
        mockMvc.perform(get("/bookings", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingsForOwner() throws Exception {
        when(bookingClient.getBookingsForOwner(Mockito.anyLong(), Mockito.any(BookingState.class)))
                .thenReturn(ResponseEntity.ok().body(bookItemRequestDto));
        mockMvc.perform(get("/bookings/owner", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void updateBookingStatus() throws Exception {
        when(bookingClient.updateBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(ResponseEntity.ok().body(bookItemRequestDto));
        mockMvc.perform(patch("/bookings/{bookingId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }
}
