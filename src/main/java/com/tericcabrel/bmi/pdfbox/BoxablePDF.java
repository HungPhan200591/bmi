package com.tericcabrel.bmi.pdfbox;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.IOException;

/**
 * @author HUNGPC1
 */

public class BoxablePDF {
    public static void main(String[] args) throws IOException {
        PDPage myPage = new PDPage(PDRectangle.A4);
        PDDocument mainDocument = new PDDocument();

        //Dummy Table
        float margin = 50;
        // starting y position is whole page height subtracted by top and bottom margin
        float yStartNewPage = myPage.getMediaBox().getHeight() - (2 * margin);
        // we want table across whole page width (subtracted by left and right margin ofcourse)
        float tableWidth = myPage.getMediaBox().getWidth() - (2 * margin);

        float bottomMargin = 70;
        // y position is your coordinate of top left corner of the table
        float yPosition = 550;

        BaseTable table = new BaseTable(yPosition, yStartNewPage, bottomMargin, tableWidth, margin, mainDocument, myPage, true, true);


        Row<PDPage> headerRow = table.createRow(15f);
        headerRow.createCell(100, "Header");
        table.addHeaderRow(headerRow);


        Row<PDPage> row = table.createRow(12);
        row.createCell(30, "Data 1");
        row.createCell(70, "Some value");

        table.draw();

        mainDocument.addPage(myPage);
        mainDocument.save("testfile.pdf");
        mainDocument.close();

    }
}
