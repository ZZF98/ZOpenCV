package com.opencv.face.service.impl;

import com.opencv.face.service.OpenCVService;
import com.opencv.face.util.FileUtil;
import com.opencv.face.util.ImageUtil;
import org.apache.commons.io.FileUtils;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @ClassNmae: OpenCVServiceImpl
 * @Author : zzf
 * @CreateDate:2019/3/15$ 13:19$
 */
@Service
public class OpenCVServiceImpl implements OpenCVService {

    @Value("classpath:haarcascade_frontalface_alt.xml")
    private Resource xml;

    @Value("${file.path}")
    private String path;

    @Value("${file.save.path}")
    private String savePath;

    private final static Logger logger = LoggerFactory.getLogger(OpenCVServiceImpl.class);


    @Override
    public void FaceDetector(HttpServletResponse response, MultipartFile file) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        logger.debug("人脸检测开始……");

        // 创建临时文件，因为boot打包后无法读取文件内的内容
        File targetXmlFile = new File(path + xml.getFilename() + "");
        FileUtils.copyInputStreamToFile(xml.getInputStream(), targetXmlFile);
        CascadeClassifier faceDetector = new CascadeClassifier(targetXmlFile.toString());
        if (faceDetector.empty()) {
            logger.debug("请引入文件……");
            return ;
        }
        // 创建图片tempFile
        File tempFile = new File(path + file.getOriginalFilename() + "");
        FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);

        // 读取创建的图片tempFile
        Mat image = Imgcodecs.imread(tempFile.toString());
        MatOfRect faceDetections = new MatOfRect();
        // 进行人脸检测
        faceDetector.detectMultiScale(image, faceDetections);
        logger.debug(String.format("检测到人脸： %s", faceDetections.toArray().length));
        Integer i = FileUtil.fileNum(savePath)+1;
        // 制图将图填充到image中
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0), 3);
            ImageUtil.imageCut(tempFile.toString(), savePath+""+i+".jpg", rect.x, rect.y, rect.width, rect.height);// 进行图片裁剪
            i++;
        }
        // 下面部分是返回给页面
        String filename = file.getOriginalFilename();
        Imgcodecs.imwrite(filename, image);
        File imgFile = new File(filename);
        if (imgFile.exists()) {
            response.getOutputStream().write(ImageUtil.toByteArray(imgFile));
            response.getOutputStream().close();
        }

        // 删除临时文件
        if (targetXmlFile.exists() && targetXmlFile.isFile()) {
            if (targetXmlFile.delete()) {
                System.out.println("删除临时文件" + targetXmlFile + "成功！");
            }
        }
        if (imgFile.exists() && imgFile.isFile()) {
            if (imgFile.delete()) {
                System.out.println("删除临时文件" + imgFile + "成功！");
            }
        }
        if (tempFile.exists() && tempFile.isFile()) {
            if (tempFile.delete()) {
                System.out.println("删除临时文件" + tempFile + "成功！");
            }
        }
    }
}
