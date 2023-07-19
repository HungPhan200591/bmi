package com.tericcabrel.bmi.controllers;

import com.tericcabrel.bmi.dtos.ResultDto;
import com.tericcabrel.bmi.dtos.UserInfoDto;
import jcifs.CIFSContext;
import jcifs.Credentials;
import jcifs.context.SingletonContext;

import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
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

    @GetMapping("/smb2")
    public ResponseEntity<String> shareFile2() throws IOException {

        String filePath = "/usr/local/src/test-smb.txt";
//        String filePath = "E:/IT/Ubuntu/user_login.txt";
        String url = "smb://192.168.181.1/test-share/test-smb.txt";

        SingletonContext base = SingletonContext.getInstance();
        Credentials auth = new NtlmPasswordAuthenticator(null,"admin", "186947873");
        CIFSContext cifsContext = base.withCredentials(auth);

        try (SmbFile smbFile = new SmbFile(url, cifsContext)) {
            smbFile.createNewFile();
            SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(smbFile);
            smbFileOutputStream.write(Files.readAllBytes(Paths.get(filePath)));
            System.out.println("Success");
        } catch (Exception e) {
            System.out.println("exception");
        }

        return ResponseEntity.ok("Ok");
    }

}