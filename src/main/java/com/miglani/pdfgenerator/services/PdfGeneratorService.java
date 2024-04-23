package com.miglani.pdfgenerator.services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.miglani.pdfgenerator.model.Item;
import com.miglani.pdfgenerator.model.PdfInvoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class PdfGeneratorService {

    private final String localPath = "pdfs/invoice_" ;
    private final String extension = ".pdf";

    @Autowired
    CacheService cacheService;

    public Map<String, FileInputStream> generatePdf(PdfInvoice pdfInvoice)  {

        String identifier = generateDataIdentifier(pdfInvoice);
        Map <String, FileInputStream> identifierToPdf = new HashMap<>();
        try {
            //Check if the cache contains our identifier or not
            if (cacheService.get(identifier) == null) {
                String pdfPath = localPath + identifier + extension;

                PdfWriter writer = new PdfWriter(pdfPath);
                PdfDocument pdfDocument = new PdfDocument(writer);
                Document document = new Document(pdfDocument);

                Table table = new Table(4);
                table.setWidth(UnitValue.createPercentValue(100));

                String sellerDetails = "Seller : \n" + pdfInvoice.getSeller()
                        + "\n" + pdfInvoice.getSellerAddress() + "\n" + pdfInvoice.getSellerGstin();

                String buyerDetails = "Buyer : \n" + pdfInvoice.getBuyer()
                        + "\n" + pdfInvoice.getBuyerAddress() + "\n" + pdfInvoice.getBuyerGstin();

                table.addHeaderCell(createCell(sellerDetails, 2, TextAlignment.LEFT).setPadding(20));
                table.addHeaderCell(createCell(buyerDetails, 2, TextAlignment.LEFT).setPadding(20));
                table.addCell(createCell("Item", 1, TextAlignment.CENTER));
                table.addCell(createCell("Quantity", 1, TextAlignment.CENTER));
                table.addCell(createCell("Rate", 1, TextAlignment.CENTER));
                table.addCell(createCell("Amount", 1, TextAlignment.CENTER));

                for (Item item : pdfInvoice.getItems()) {
                    table.addCell(createCell(item.getName(), 1, TextAlignment.CENTER));
                    table.addCell(createCell(item.getQuantity(), 1, TextAlignment.CENTER));
                    table.addCell(createCell(Double.toString(item.getRate()), 1, TextAlignment.CENTER));
                    table.addCell(createCell(Double.toString(item.getAmount()), 1, TextAlignment.CENTER));
                }

                document.add(table);
                // Close the document
                document.close();

                File file = new File(pdfPath);
                FileInputStream fileInputStream = new FileInputStream(file);
                identifierToPdf.put(identifier, fileInputStream);

                //Add the generated PDF to our cache
                cacheService.put(identifier, pdfPath);
            } else {
                //if identifier exits in cache, use it to retrieve the PDF
                FileInputStream fileInputStream = retrievePdf(identifier);
                identifierToPdf = Collections.singletonMap(identifier, fileInputStream);
            }
         } catch (FileNotFoundException e) {
                System.err.println("Error: Unable to generate the file" + e.getMessage());
         }

        return identifierToPdf;
    }

    public FileInputStream retrievePdf (String pdfIdentifier) {
        try {
            String pdfPath = cacheService.get(pdfIdentifier);
            if (pdfPath != null) {
                File file = new File(pdfPath);

                if(file.exists()) {
                    return new FileInputStream(file);
                } else{
                    System.err.println("File does not exist at given path" );
                }
            } else {
                System.err.println("PDF path not found in cache for identifier: " + pdfIdentifier);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find the file" + e.getMessage());
        }

        return null;
    }

    private Cell createCell(String content, int colSpan, TextAlignment textAlignment) {
        Paragraph paragraph = new Paragraph(content);
        Cell cell = new Cell(1,colSpan).add(paragraph);
        cell.setTextAlignment(textAlignment);
        return cell;
    }

    private String generateDataIdentifier(PdfInvoice pdfEntities) {
        // Concatenate relevant fields and generate a hash as the identifier
        String concatenatedData = pdfEntities.getSeller() + pdfEntities.getSellerGstin()
                + pdfEntities.getBuyer() + pdfEntities.getBuyerGstin()
                + pdfEntities.getItems().toString();

        return Integer.toHexString(concatenatedData.hashCode());
    }
}
