package com.miglani.pdfgenerator.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Item {
    @NotBlank(message = "item name cannot be blank")
    String name;

    @NotNull(message = "quantity can not be blank")
    String quantity;

    @NotNull(message = "quantity value must not be null")
    @Min(value = 0, message = "quantity must be greater than 0")
    double rate;

    @NotNull(message = "quantity value must not be null")
    @Min(value = 0, message = "quantity must be greater than 0")
    double amount;
}
