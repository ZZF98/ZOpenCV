package com.opencv.face.controller;


import com.opencv.face.service.OpenCVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassNmae: a
 * @Author : zzf
 * @CreateDate:2019/3/15$ 13:15$
 */
@RestController
public class OpenCVController {

    @Autowired
    private OpenCVService openCVService;

    /**
     * 截取
     * @param response
     * @param file
     * @throws IOException
     */
    @PostMapping("/face")
    public void FaceDetector(HttpServletResponse response, MultipartFile file) throws IOException {
        openCVService.FaceDetector(response, file);
    }

}
