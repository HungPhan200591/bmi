package com.tericcabrel.bmi.video_stream;

import jcifs.CIFSContext;
import jcifs.Credentials;
import jcifs.context.SingletonContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbFile;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HUNGPC1
 */

@RestController
public class StreamController {

    @GetMapping(value = "/video/stream", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> getVideo() throws IOException {

        String url = "smb://192.168.1.131/sambashare/videotest.mp4";

        SingletonContext base = SingletonContext.getInstance();
        Credentials auth = new NtlmPasswordAuthenticator(null,"phanhung", "123456");
        CIFSContext cifsContext = base.withCredentials(auth);

        InputStream inputStream = null;
        byte[] targetArray = new byte[0];

        try (SmbFile smbFile = new SmbFile(url, cifsContext)) {

            inputStream = smbFile.getInputStream();
            targetArray = IOUtils.toByteArray(inputStream);

        } catch (Exception e) {
            System.out.println("exception");
        }


        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(targetArray));
    }




}
