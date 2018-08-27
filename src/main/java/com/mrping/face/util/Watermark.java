package com.mrping.face.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * @author Created by Mr.Ping on 2018/7/24.
 */
public class Watermark {

    /**描边宽度**/
    private static int stroke = 10;

    /**文字水印X坐标**/
    private static int positionX = 0;

    /**文字水印Y坐标**/
    private static int positionY = 0;

    /**图片水印Y坐标**/
    private static int iconPositionX = 0;

    /**图片水印Y坐标**/
    private static int iconPositionY = 0;

    /**进度条圆弧**/
    private static int arcSize = 7;

    /**进度条高度**/
    private static int progressBarH = 7;

    /**进度条长度**/
    public static int progressBarW = 200;

    /**颜色**/
    private static Color color = new Color(102, 102, 102);

    /**字体**/
    private static Font font = new Font("微软雅黑", Font.PLAIN, 60);

    /**
     * 加文字水印
     * @param text           添加的文字
     * @param sourceImgPath  原图片地址
     * @param outImgPath     输出地址
     * @throws IOException
     */
    public static void waterMarkByText(String text, String sourceImgPath, String outImgPath) throws IOException {

        Image sourceImg = null;
        BufferedImage bImage = null;
        OutputStream outputStream = null;
        try {
            if (StrUtil.isBlank(sourceImgPath) || StrUtil.isBlank(outImgPath)) {
                return;
            }
            sourceImg = pictureTrans(sourceImgPath);
            int width = sourceImg.getWidth(null);
            int height = sourceImg.getHeight(null);

            bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics2D = bImage.createGraphics();

            bImage = graphics2D.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            graphics2D.dispose();
            graphics2D = bImage.createGraphics();

            graphics2D.setColor(color);

            graphics2D.setFont(font);

            graphics2D.setBackground(Color.white);

            graphics2D.drawImage(sourceImg, 0, 0, null);

            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            /**对显示的文字进行处理**/
            AttributedString ats = new AttributedString(text);
            ats.addAttribute(TextAttribute.FONT, font, 0, text.length());
            AttributedCharacterIterator iterator = ats.getIterator();

            graphics2D.drawString(iterator, positionX, positionY);
            graphics2D.dispose();

            outputStream = new FileOutputStream(outImgPath);
            String formatName = outImgPath.substring(outImgPath.lastIndexOf(".") + 1);
            ImageIO.write(bImage, formatName, outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
            if (null != sourceImg) {
                sourceImg.flush();
            }
            if (null != bImage) {
                bImage.flush();
            }
        }
    }


    /**
     * 加图片水印
     * @param iconImgPath   水印图片地址
     * @param sourceImgPath 源图片地址
     * @param outImgPath    输出地址
     * @throws IOException
     */
    public static void waterMarkByImg(String iconImgPath, String sourceImgPath, String outImgPath) throws IOException {

        Image iconImg = null;
        Image sourceImg = null;
        BufferedImage bImage = null;
        OutputStream outputStream = null;
        try {
            if (StrUtil.isBlank(iconImgPath) ||
                    StrUtil.isBlank(sourceImgPath) ||
                        StrUtil.isBlank(outImgPath)) {
                return;
            }

            sourceImg = pictureTrans(sourceImgPath);
            int width = sourceImg.getWidth(null);
            int height = sourceImg.getHeight(null);

            bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = bImage.createGraphics();

            bImage = graphics2D.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            graphics2D.dispose();
            graphics2D = bImage.createGraphics();

            graphics2D.drawImage(sourceImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

            iconImg = pictureTrans(iconImgPath);

            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            graphics2D.drawImage(iconImg, iconPositionX, iconPositionY, null);
            graphics2D.dispose();

            outputStream = new FileOutputStream(outImgPath);
            String formatName = outImgPath.substring(outImgPath.lastIndexOf(".") + 1);
            ImageIO.write(bImage, formatName, outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (null != outputStream) {
                outputStream.close();
            }
            if (null != iconImg) {
                iconImg.flush();
            }
            if (null != sourceImg) {
                sourceImg.flush();
            }
            if (null != bImage) {
                bImage.flush();
            }
        }
    }

    /**
     * 图片剪切圆形并描边
     * @param imagePath
     * @param outPath
     */
    public static void shearStroke(String imagePath, String outPath, Color color, float line, int zoomWidth) {

        try {
            Image sourceImg = pictureTrans(imagePath);

            sourceImg = zoom(sourceImg, zoomWidth);

            int width = sourceImg.getWidth(null);
            int height = sourceImg.getHeight(null);

            BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bImage.createGraphics();

            bImage = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g2d.dispose();
            g2d = bImage.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, width, height);
            g2d.setClip(shape);

            g2d.drawImage(sourceImg, 0, 0, null);

            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(line));
            g2d.draw(shape);
            g2d.dispose();

            OutputStream outputStream = new FileOutputStream(outPath);
            String formatName = imagePath.substring(imagePath.lastIndexOf(".") + 1);
            ImageIO.write(bImage, formatName, outputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Image zoom(Image image, int zoomWidth) {

        int w = image.getWidth(null);
        int h = image.getHeight(null);
        int width = w * zoomWidth/w;
        int height = h * zoomWidth/w;

        image = image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
        BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bImage.getGraphics().drawImage(image, 0, 0, width, height, null);

        Graphics2D g2d = bImage.createGraphics();
        bImage = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2d.dispose();

        g2d = bImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        bImage.flush();
        return bImage;
    }

    /**
     * 画进度条
     * @param imagePath
     * @param outPath
     * @param ratio
     */
    public static void  paintProgressBar(String imagePath, String outPath, double ratio) {
        try {

            Image image = pictureTrans(imagePath);
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            image = image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
            BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bImage.getGraphics().drawImage(image, 0, 0, width, height, null);

            Graphics2D g2d = bImage.createGraphics();
            bImage = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g2d.dispose();

            g2d = bImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            g2d.drawImage(image, 0, 0, null);

            g2d.setColor(new Color(228,239,250));
            g2d.fillRoundRect(positionX, positionY, progressBarW, progressBarH, arcSize, arcSize);

            g2d.setColor(new Color(4,162,241));
            int barWidth = (int) NumberUtil.round(progressBarW * ratio, 0);
            g2d.fillRoundRect(positionX, positionY, barWidth, progressBarH,  arcSize, arcSize);

            g2d.dispose();

            OutputStream outputStream = new FileOutputStream(outPath);
            ImageIO.write(bImage, "png", outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void waterMarkImgAlign(int x, int y){
        iconPositionX = x;
        iconPositionY = y;
    }

    public static void waterMarkTextAlign(int x, int y, Color colors, Font fonts){
        positionX = x;
        positionY = y;
        font = fonts;
        color = colors;
    }

    /**
     * 夫妻像比率文字水印位置
     * @param ratio   60%
     * @param colors  字体颜色
     * @param fonts   字体
     */
    public static void spouseRatioTextAlign(String ratio, Color colors, Font fonts){
        int a = 2;int b = 3; int c = 4;
        font = fonts;
        color = colors;
        positionY = 1047;
        if(StrUtil.isNotBlank(ratio)){
            if(ratio.length() == a){
                positionX = 471;
            }else if(ratio.length() == b){
                positionX = 461;
            }else if(ratio.length() == c){
                positionX = 451;
            }
        }
    }

    /**
     * 明星相相似度水印位置
     * @param score   60%
     * @param colors  字体颜色
     * @param fonts   字体
     */
    public static void starScoreTextAlign(String score, Color colors, Font fonts){
        int a = 2;int b = 3; int c = 4;
        font = fonts;
        color = colors;
        positionY = 40;
        if(StrUtil.isNotBlank(score)){
            if(score.length() == a){
                positionX = 160;
            }else if(score.length() == b){
                positionX = 165;
            }else if(score.length() == c){
                positionX = 170;
            }
        }
    }

    /**
     * 图片地址转Image类型
     * @param imagePath
     * @return
     */
    public static Image pictureTrans(String imagePath){

        Image sourceImg = null;
        InputStream inputStream;
        try{
            String str = "http";
            if (imagePath.contains(str)) {
                URL url = new URL(imagePath);
                inputStream = url.openStream();
            } else {
                inputStream = new FileInputStream(new File(imagePath));
            }
            sourceImg = ImageIO.read(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceImg;
    }



    public static void main(String[] args) {
//        String imagePath = "C:/Users/Administrator/Desktop/text2.png";
//        String outPath = "C:/Users/Administrator/Desktop/shear.png";
//        shearStroke(imagePath, outPath, new Color(255, 122, 151), stroke, 476);
//
//        String iconImgPath = "C:/Users/Administrator/Desktop/shear.png";
//        String sourceImgPath = "C:/Users/Administrator/Desktop/spouse@3x.png";
//        String outImgPath = "C:/Users/Administrator/Desktop/spouse.png";
//        String outSharePath = "C:/Users/Administrator/Desktop/spouse_share.png";
//        try {
//            Watermark.waterMarkImgAlign(287, 280);
//            waterMarkByImg(iconImgPath, sourceImgPath, outImgPath);
//
//            Watermark.waterMarkImgAlign(703, 280);
//            waterMarkByImg(iconImgPath, outImgPath, outImgPath);
//
//            Watermark.waterMarkTextAlign(913, 937, new Color(255, 122, 151), new Font("微软雅黑", Font.PLAIN, 60));
//            waterMarkByText("77分", outImgPath, outImgPath);
//
//            spouseRatioTextAlign("6%", new Color(102, 102, 102), new Font("微软雅黑", Font.PLAIN, 52));
//            waterMarkByText("超过了全国6%的情侣", outImgPath, outImgPath);
//
//            ImageDealUtil.cropPngImage(outImgPath, outSharePath, 0, 0, 1446, 1227);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        String imagePath = "C:/Users/Administrator/Desktop/star666.png";
        String outPath = "C:/Users/Administrator/Desktop/star_shear.png";
        shearStroke(imagePath, outPath, new Color(37,121,214), 0, 121);

        String iconImgPath = "C:/Users/Administrator/Desktop/star_shear.png";
        String arcImgPath = "C:/Users/Administrator/Desktop/arc.png";
        String sourceImgPath = "C:/Users/Administrator/Desktop/star5.png";
        String outImgPath = "C:/Users/Administrator/Desktop/star.png";
        String outSharePath = "C:/Users/Administrator/Desktop/star_share.png";

        try{

            waterMarkImgAlign(180,70);
            waterMarkByImg(iconImgPath, sourceImgPath, outImgPath);

            waterMarkImgAlign(180,70);
            waterMarkByImg(arcImgPath, outImgPath, outImgPath);


            waterMarkImgAlign(80,70);
            waterMarkByImg(iconImgPath, outImgPath, outImgPath);

            waterMarkImgAlign(80,70);
            waterMarkByImg(arcImgPath, outImgPath, outImgPath);

            starScoreTextAlign("10%", new Color(4,162,241), new Font("微软雅黑", Font.PLAIN, 12));
            waterMarkByText("相似度10%", outImgPath, outImgPath);

            waterMarkTextAlign((375 - progressBarW)/2, 50, null, null);
            paintProgressBar(outImgPath, outImgPath, 0.98);

            waterMarkTextAlign(93, 270, new Color(51,51,51), new Font("微软雅黑", Font.PLAIN, 15));
            waterMarkByText("最像的明星: 曹前超", outImgPath, outImgPath);

            ImageUtil.cropPngImage(outImgPath, outSharePath, 0, 0, 375, 318);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
