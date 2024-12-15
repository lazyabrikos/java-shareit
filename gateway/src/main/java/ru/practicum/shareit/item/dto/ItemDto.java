package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemDto {

    @NotBlank(message = "Item description cannot be empty")
    private String description;

    @NotNull
    private Boolean available;

    @NotBlank(message = "Item name cannot be empty")
    private String name;

    private Long requestId;

}
