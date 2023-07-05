package com.tericcabrel.bmi.controllers;

import com.tericcabrel.bmi.dtos.ResultDto;
import com.tericcabrel.bmi.dtos.UserInfoDto;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

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
    public String shareFile() throws IOException {
        SmbFileInputStream is = null;
        FileOutputStream os = null;
        //
        String souFileUrl = "smb://root:123456@192.168.181.1/share-test/test.txt";

        SmbFile souSmbFile = new SmbFile(souFileUrl);
        is = new SmbFileInputStream(souSmbFile);
        File tempOutFile = new java.io.File("/usr/local/test.txt");
        os = new FileOutputStream(tempOutFile);
        byte[] bytes = new byte[1024];
        int c;
        while ((c = is.read(bytes)) != -1) {
            os.write(bytes, 0, c);
        }

//        SmbFile file = new SmbFile("smb://ADMIN/share-test");

        return "index";
    }

    @GetMapping("/smb2")
    public String shareFile2() throws Exception {

//        CIFSContext base = SingletonContext.getInstance();
//        CIFSContext authed1 = base.withCredentials(new NtlmPasswordAuthentication(base, "domainName",
//                "userName", "password"));

//        String souFileUrl = "smb://root:123456@192.168.181.1/share-test/test.txt";
        SmbFile f = new SmbFile("smb:\\\\192.168.181.1\\share-test");

        if (f.exists()) {
            for (SmbFile file : f.listFiles()) {
                System.out.println(file.getName());
            }
        } else {
            System.out.println("============== ERROR ==========");
        }


        return "index";
    }
}