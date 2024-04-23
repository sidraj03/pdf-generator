package com.miglani.pdfgenerator.controller;

import com.miglani.pdfgenerator.model.Item;
import com.miglani.pdfgenerator.model.PdfInvoice;
import com.miglani.pdfgenerator.services.PdfGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PDFGeneratorControllerTest {

    Item item;
    List<Item> itemList;
    PdfInvoice pdfInvoice;

    PdfInvoice pdfInvoiceWithEmptyItems;

    @Mock
    private PdfGeneratorService pdfGeneratorService;

    @InjectMocks
    private PDFGeneratorController pdfController;

    @BeforeEach
    public void init() {
        item = new Item("item", "1 nos", 123.00,134.00);
        itemList = new ArrayList<>();
        itemList.add(item);
        pdfInvoice = new PdfInvoice("seller", "123456",
                "address","buyer","34567", "address", itemList);
        pdfInvoiceWithEmptyItems = new PdfInvoice("seller", "123456",
                "address","buyer","34567", "address",  new ArrayList<>());
    }

    @Test
    void generatePdfWithValidInvoice() throws Exception {
        // Arrange
        Map<String, FileInputStream> identifierToPdfMap = Collections.singletonMap("123", mock(FileInputStream.class));
        when(pdfGeneratorService.generatePdf(pdfInvoice)).thenReturn(identifierToPdfMap);

        // Act
        ResponseEntity responseEntity = pdfController.generatePdf(pdfInvoice);

        // Assert
        assertEquals(MediaType.APPLICATION_PDF, responseEntity.getHeaders().getContentType());
        assertEquals("attachment; filename=\"invoice_123.pdf\"", responseEntity.getHeaders().getContentDisposition().toString());
        assertEquals(200, responseEntity.getStatusCodeValue());
        verify(pdfGeneratorService, times(1)).generatePdf(pdfInvoice);
    }


    @Test
    void generatePdfWithEmptyItems() {
        pdfInvoiceWithEmptyItems = new PdfInvoice("seller", "123456",
                "address","buyer","34567", "address",  new ArrayList<>());
        // Act and Assert
        assertThrows(Exception.class, () -> pdfController.generatePdf(pdfInvoiceWithEmptyItems));
    }
}
