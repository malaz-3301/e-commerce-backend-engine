package com.saea.bookecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    private String description;
    @NotNull
    @PositiveOrZero
    private BigDecimal price;
    @Min(0)
    private int stockQuantity;
    @NotNull
    private Long categoryId;
}
