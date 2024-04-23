package com.miglani.pdfgenerator.controller;

import com.miglani.pdfgenerator.model.PdfInvoice;
import com.miglani.pdfgenerator.services.PdfGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.util.Map;

@RestController
public class PDFGeneratorController {
    @Autowired
    PdfGeneratorService pdfGeneratorService;

    /**
     * Generates and downloads the pdf for the invoice
     */
    @PostMapping("/generator")
    public ResponseEntity generatePdf(@RequestBody PdfInvoice pdfInvoice) throws Exception {
        if(pdfInvoice == null || pdfInvoice.getItems() == null)
            throw new Exception("Invalid request");

        Map<String, FileInputStream> identifierToPdfMap = pdfGeneratorService.generatePdf(pdfInvoice);

        HttpHeaders headers = new HttpHeaders();

        Map.Entry<String, FileInputStream> entry = identifierToPdfMap.entrySet().iterator().next();
        FileInputStream pdfInputStream = entry.getValue();
        String identifier = entry.getKey();

        InputStreamResource resource = new InputStreamResource(pdfInputStream);
        ContentDisposition pdfContentDisposition =
                ContentDisposition.builder("attachment").filename("invoice_" + identifier + ".pdf").build();

        headers.setContentDisposition(pdfContentDisposition);
        headers.setContentType(MediaType.APPLICATION_PDF);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    /**
     * If the pdf identifier is known, this endpoint can be used for re-downloading any pdf
     */
    @GetMapping("/download")
    public ResponseEntity downLoadPdf(@RequestParam("dataId") String dataId) throws Exception {

            FileInputStream pdfFileInputStream =  pdfGeneratorService.retrievePdf(dataId);

            if(pdfFileInputStream == null){
                throw new Exception("Invalid data id");
            }

            InputStreamResource resource = new InputStreamResource(pdfFileInputStream);

            HttpHeaders headers = new HttpHeaders();
            ContentDisposition pdfContentDisposition =
                    ContentDisposition.builder("attachment").filename("invoice" + dataId + ".pdf").build();

            headers.setContentDisposition(pdfContentDisposition);
            headers.setContentType(MediaType.APPLICATION_PDF);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
    }
}
