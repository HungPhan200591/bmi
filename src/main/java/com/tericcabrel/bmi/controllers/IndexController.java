package com.tericcabrel.bmi.controllers;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import com.tericcabrel.bmi.dtos.ResultDto;
import com.tericcabrel.bmi.dtos.UserInfoDto;
import jcifs.CIFSContext;
import jcifs.Credentials;
import jcifs.context.SingletonContext;

import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class IndexController {
    @GetMapping("/")
    public String indexPage(Model model) {
        UserInfoDto userInfoDto = new UserInfoDto();

        userInfoDto.setHeight(40d).setWeight(2d);

        model.addAttribute("userInfo", userInfoDto);

        return "index";
    }

    @PostMapping("/")
    public String calculateBMI(
            @ModelAttribute("userInfo") @Valid UserInfoDto userInfoDto,
            BindingResult inputValidationResult,
            Model model
    ) {
        if (inputValidationResult.hasErrors()) {
            return "index";
        }

        double bmi = calculateBodyMassIndex(userInfoDto.getComputedHeight(), userInfoDto.getWeight());
        ResultDto resultDto = new ResultDto(bmi);

        model.addAttribute("result", resultDto);

        return "index";
    }

    private double calculateBodyMassIndex(double height, double weight) {
        double bmi = weight / Math.pow(height, 2);
        double bmiRounded = Math.round(bmi * 10);

        return  bmiRounded / 10;
    }


    @GetMapping("/smb")
    public ResponseEntity<List<String>> shareFile() {

        String url = "smb://192.168.181.1/test-share";

        SingletonContext base = SingletonContext.getInstance();
        Credentials auth = new NtlmPasswordAuthenticator(null,"admin", "186947873");
        CIFSContext cifsContext = base.withCredentials(auth);

        List<String> listFile = new ArrayList<>();

        try (SmbFile smbFile = new SmbFile(url, cifsContext)) {

            for (SmbFile file : smbFile.listFiles()) {
                listFile.add(file.getName());
            }

        } catch (Exception e) {
            System.out.println("exception");
        }

        return ResponseEntity.ok(listFile);
    }

    @GetMapping("/pdf-box")
    public ResponseEntity<String> createPDF() throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        float margin = 50;
        float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
        float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
        float bottomMargin = 70;

        BaseTable table = new BaseTable(yStartNewPage, yStartNewPage,
                bottomMargin, tableWidth, margin, document, page, true, true);

        Row<PDPage> headerRow = table.createRow(15f);
        Cell<PDPage> cell = headerRow.createCell(100, "Header");
        cell.setFontSize(12);
        cell.setFillColor(Color.LIGHT_GRAY);
        table.addHeaderRow(headerRow);

        Row<PDPage> row = table.createRow(10f);
        cell = row.createCell(20f, "1");
        cell.setFontSize(12);
        cell.setFillColor(Color.WHITE);
        cell.setTextColor(Color.BLACK);

        cell = row.createCell(80f, "This is a sample text");
        cell.setFontSize(12);
        cell.setFillColor(Color.WHITE);
        cell.setTextColor(Color.BLACK);

        table.draw();

        document.save("table.pdf");

        return ResponseEntity.ok("Ok");

    }

}