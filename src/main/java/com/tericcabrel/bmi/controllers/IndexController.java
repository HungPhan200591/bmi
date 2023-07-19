package com.tericcabrel.bmi.controllers;

import com.tericcabrel.bmi.dtos.ResultDto;
import com.tericcabrel.bmi.dtos.UserInfoDto;
import jcifs.CIFSContext;
import jcifs.Credentials;
import jcifs.context.SingletonContext;

import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
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
//
//    @GetMapping("/smb")
//    public String shareFile() throws IOException {
//        SmbFileInputStream is = null;
//        FileOutputStream os = null;
//        //
//        String souFileUrl = "smb://root:123456@192.168.181.1/test-share/test.txt";
//
//        SmbFile souSmbFile = new SmbFile(souFileUrl);
//        is = new SmbFileInputStream(souSmbFile);
//        File tempOutFile = new java.io.File("/usr/local/test.txt");
//        os = new FileOutputStream(tempOutFile);
//        byte[] bytes = new byte[1024];
//        int c;
//        while ((c = is.read(bytes)) != -1) {
//            os.write(bytes, 0, c);
//        }

//        SmbFile file = new SmbFile("smb://ADMIN/share-test");

//        return "index";
//    }

    @GetMapping("/smb")
    public ResponseEntity<List<String>> shareFile() {

        String url = "smb://192.168.181.1/test-share";

        SingletonContext base = SingletonContext.getInstance();
        Credentials auth = new NtlmPasswordAuthenticator(null,"admin", "186947873");
        CIFSContext cifsContext = base.withCredentials(auth);

        List<String> listFile = new ArrayList<>();

        try (SmbFile f = new SmbFile(url, cifsContext)) {

            for (SmbFile file : f.listFiles()) {
                listFile.add(file.getName());
            }

        } catch (Exception e) {
            System.out.println("exception");
        }

        return ResponseEntity.ok(listFile);
    }

}