package com.opencv.face.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassNmae: OpenCVService
 * @Author : zzf
 * @CreateDate:2019/3/15$ 13:18$
 */
public interface OpenCVService {

    void FaceDetector(HttpServletResponse response, MultipartFile file) throws IOException;

}
