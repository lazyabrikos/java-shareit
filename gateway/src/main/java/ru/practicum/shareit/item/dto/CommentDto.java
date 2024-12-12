package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDto {
    @NotBlank(message = "Text of comment cannot be empty")
    private String text;
}
