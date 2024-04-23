package com.miglani.pdfgenerator.services;

import com.miglani.pdfgenerator.model.Item;
import com.miglani.pdfgenerator.model.PdfInvoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PdfGeneratorServiceTest {
    Item item;
    List<Item> itemList;
    PdfInvoice pdfInvoice;
    @Mock
    CacheService cacheService;
    @InjectMocks
    PdfGeneratorService pdfGeneratorService;

    @BeforeEach
    public void init() {
        item = new Item("item", "1 nos", 123.00,134.00);
        itemList = new ArrayList<>();
        itemList.add(item);
        pdfInvoice = new PdfInvoice("seller", "123456",
                "address","buyer","34567", "address", itemList);
    }

    @Test
    public void testGeneratePdfWithCacheHit() {
        when(cacheService.get("6f80038e")).thenReturn("pdfs/invoice_6f80038e.pdf");
        Map<String, FileInputStream> map = pdfGeneratorService.generatePdf(pdfInvoice);
        Map.Entry<String, FileInputStream> entry = map.entrySet().iterator().next();
        assertEquals(map.size(), 1);
        assertEquals(entry.getKey(), "6f80038e");
        assertNotNull(entry.getValue());

        //Entry will not be added in cache
        Mockito.verify(cacheService, Mockito.times(0)).put(anyString(), anyString());
    }

    @Test
    public void testGeneratePdfWithCacheMiss() {
        when(cacheService.get(anyString())).thenReturn(null);
        Map<String,FileInputStream> map = pdfGeneratorService.generatePdf(pdfInvoice);
        Map.Entry<String, FileInputStream> entry = map.entrySet().iterator().next();
        assertEquals(map.size(), 1);
        assertEquals(entry.getKey(), "6f80038e");
        assertNotNull(entry.getValue());

        //Entry will be added in cache
        Mockito.verify(cacheService,Mockito.times(1)).put(anyString(),anyString());
    }


    @Test
    public void retrievePdfReturnsFileInputStream() throws Exception {
        when(cacheService.get("6f80038e")).thenReturn("pdfs/invoice_6f80038e.pdf");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        FileInputStream fileInputStream = pdfGeneratorService.retrievePdf("6f80038e");
        assertNotNull(fileInputStream);
    }

    @Test
    public void retrievePdfWhenPathIsNull() throws Exception {
        when(cacheService.get(anyString())).thenReturn(null);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        pdfGeneratorService.retrievePdf("12345");
        assertEquals("PDF path not found in cache for identifier: 12345\n", outContent.toString());
    }

    @Test
    public void retrievePdfWhenFileNotExisting() throws Exception {
        when(cacheService.get(anyString())).thenReturn("path");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));
        pdfGeneratorService.retrievePdf("12345");
        assertEquals("File does not exist at given path\n", outContent.toString());
    }
}
