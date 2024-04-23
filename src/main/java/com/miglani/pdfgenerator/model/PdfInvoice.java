package com.miglani.pdfgenerator.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PdfInvoice {

    @NotBlank(message = "Seller cannot be blank")
    private String seller;

    @NotBlank(message = "Seller Gstin cannot be blank")
    private String sellerGstin;

    @NotBlank(message = "Seller address cannot be blank")
    private String sellerAddress;

    @NotBlank(message = "Buyer cannot be blank")
    private String buyer;

    @NotBlank(message = "Buyer Gstin cannot be blank")
    private String buyerGstin;

    @NotBlank(message = "Buyer Address cannot be blank")
    private String buyerAddress;

    @NotNull(message = "Items list should not be null")
    private List<Item> items;
}
