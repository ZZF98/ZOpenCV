package com.opencv.face.awt;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;


/**
 * @ClassNmae: a
 * @Author : zzf
 * @CreateDate:2019/3/15$ 14:14$
 */
public class JFrameGUI extends JComponent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private BufferedImage image;
    public JFrameGUI() {
    }



    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        if(image == null)  {
            g2d.setPaint(Color.BLACK);
            g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
        } else {
            g2d.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
            System.out.println("show frame...");
        }
    }



//    public void createWin(String title) {
//        JFrame ui = new JFrame();
//        ui.setTitle(title);
//        ui.getContentPane().setLayout(new BorderLayout());
//        ui.getContentPane().add(this, BorderLayout.CENTER);
//        ui.setSize(new Dimension(330, 240));
//        ui.setVisible(true);
//    }



    /**
     * 创建基本容器
     * @param title
     * @param size
     */
    public void createWin(String title, Dimension size) {
        JFrame ui = new JFrame();
        ui.setTitle(title);
        ui.getContentPane().setLayout(new BorderLayout());
        ui.getContentPane().add(this, BorderLayout.CENTER);
        ui.setSize(size);
        ui.setVisible(true);
    }



    public void imshow(BufferedImage image) {
        this.image = image;
        this.repaint();
    }

}

