package com.opencv.face;

import com.opencv.face.awt.JFrameGUI;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static com.opencv.face.util.ImageUtil.conver2Image;

/**
 * @ClassNmae: a
 * @Author : zzf
 * @CreateDate:2019/3/15$ 15:00$
 */
public class OpenCVDemo {
    public static void main(String[] args) throws IOException {
        //加载dll
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //获取resource目录下的haarcascade_frontalface_alt.xml
        URL faceUrl = new URL(Thread.currentThread().getContextClassLoader().getResource("") +
                "haarcascade_frontalface_alt.xml");
        URL earUrl = new URL(Thread.currentThread().getContextClassLoader().getResource("") +
                "haarcascade_eye_tree_eyeglasses.xml");
        CascadeClassifier faceDetector = new CascadeClassifier(new File(faceUrl.getPath()).toString());
        CascadeClassifier eyesDetector = new CascadeClassifier(new File(earUrl.getPath()).toString());
//        CascadeClassifier faceDetector = new CascadeClassifier("G:\\opencvs\\src\\main\\resources\\haarcascade_frontalface_alt.xml");
//        CascadeClassifier eyesDetector = new CascadeClassifier("G:\\opencvs\\src\\main\\resources\\haarcascade_eye_tree_eyeglasses.xml");
        // 打开摄像头或者视频文件
        VideoCapture capture = new VideoCapture(0);
        //打开摄像头
//        capture.open(0);
//        打开视频文件
//        capture.open("http://vali-dns.cp31.ott.cibntv.net/6572374C8FE4471B78B4B48E3/03000807005BDD8A16ACA03559D58B7B66A48F-1057-4595-86BB-969B9825D756.mp4?ccode=0501&duration=300&expire=18000&psid=fbcd2f2387fd47f6fea1cb5ba008cfeb&ups_client_netip=71d7bd4f&ups_ts=1547783136&ups_userid=&utid=HOdFFDduVxMCAXHXvEIAXfix&vid=XMzg5NTEwNDg4OA&vkey=A949c7b96d1a234a3934b238a56045964&s=efbfbdd2805befbfbd57&sp=");
//        capture.open("http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/gear2/prog_index.m3u8");
        //判断是否能加载视频
//        capture.set(1, 200);
        if (!capture.isOpened()) {
            System.out.println("无法加载视频数据……");
            return;
        }
        //获取帧的宽度
        int frameWidth = (int) capture.get(3);
        //获取帧的高度
        int frameHeight = (int) capture.get(4);
        //创建容器
        JFrameGUI gui = new JFrameGUI();
        gui.createWin("嘤嘤嘤的人脸识别器", new Dimension(frameWidth, frameHeight));
        //创建图像容器类
        Mat frame = new Mat();
        while (true) {
            //读取一帧
            boolean have = capture.read(frame);
//          会翻转
            Core.flip(frame, frame, 1);// Win上摄像头
            // 进行人脸检测
            MatOfRect faceDetections = new MatOfRect();
            Mat frameGray = new Mat();
            Imgproc.cvtColor(frame, frameGray, Imgproc.COLOR_BGR2GRAY);
            Imgproc.equalizeHist(frameGray, frameGray);
            faceDetector.detectMultiScale(frame, faceDetections);
            System.out.println(String.format("检测到人脸： %s", faceDetections.toArray().length));
            for (Rect rect : faceDetections.toArray()) {
                Imgproc.rectangle(frame, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                        new Scalar(0, 255, 0), 1);
                //进行人眼检测
                Mat faceROI = frameGray.submat(rect);
                MatOfRect eyes = new MatOfRect();
                eyesDetector.detectMultiScale(faceROI, eyes);
                System.out.println(String.format("检测到眼睛： %s", faceDetections.toArray().length));
                for (Rect eye : eyes.toArray()) {
                    Point eyeCenter = new Point(rect.x + eye.x + eye.width / 2, rect.y + eye.y + eye.height / 2);
                    int radius = (int) Math.round((eye.width + eye.height) * 0.25);
                    Imgproc.circle(frame, eyeCenter, radius, new Scalar(255, 0, 0), 2);
                }
            }
            if (!have) break;
            if (!frame.empty()) {
                //Mat转换BufferedImage并刷新
                gui.imshow(conver2Image(frame));
                gui.repaint();
            }
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
