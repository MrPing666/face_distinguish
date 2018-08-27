package com.mrping.face.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;

/**
 * @author Created by Mr.Ping on 2018/7/6.
 */
public class WaterMarkUtil {

    /**文字水印透明度**/
    private static float alpha = 0.5f;

    /**文字水印横向位置**/
    private static int positionX = 60;

     /**文字水印纵向位置**/
    private static int positionY = 155;

     /**水印文字字体**/
    private static Font font = new Font("黑体", Font.PLAIN, 48);

     /**水印文字颜色**/
    private static Color color = Color.BLACK;

    private static float iconAlpha = 0.5f;

     /**图片水印横向位置**/
    private static int iconPositionX = 640;

     /**图片水印纵向位置**/
    private static int iconPositionY = 525;

     /**图片水印大小**/
    private static int iconWidth = 200;

     /**图片水印大小**/
    private static int iconHeight = 200;

    /**是否等比缩小或放大**/
    private static boolean isEqualRatio = true;

    /**icon 在 x、y点旋转**/
    private static int rotateX = 0;
    private static int rotateY = 0;


    /**
     * 水印位置
     * @param align
     * @param sourceImg
     * @param iconImg
     */
    private static void waterMarkAlign(String align, Image sourceImg, Image iconImg) {
        if (StrUtil.isNotBlank(align) && null != sourceImg && null != iconImg) {

            int sourceImgWidth = sourceImg.getWidth(null);
            int sourceImgHight = sourceImg.getHeight(null);
            int iconImgWidth = iconImg.getWidth(null);
            int iconImgHight = iconImg.getHeight(null);

            switch (align) {
                case "northwest":
                    iconPositionX = 0;
                    iconPositionY = 0;
                    break;
                case "northeast":
                    iconPositionX = sourceImgWidth - iconImgWidth;
                    iconPositionY = 0;
                    break;
                case "center":
                    iconPositionX = (sourceImgWidth - iconImgHight) / 2;
                    iconPositionY = (sourceImgHight - iconImgHight) / 2;
                    break;
                case "southwest":
                    iconPositionX = 0;
                    iconPositionY = sourceImgHight - iconImgHight;
                    break;
                case "southeast":
                    iconPositionX = sourceImgWidth - iconImgWidth - 5;
                    iconPositionY = sourceImgHight - iconImgHight - 8;
                    break;
                default:
                    break;

            }
        }
    }

    /**
     * 文字水印设置
     * @param alpha     水印透明度
     * @param positionX 水印横向位置
     * @param positionY 水印纵向位置
     * @param font      水印文字字体
     * @param color     水印文字颜色
     */
    public static void waterMarkOptions(float alpha, int positionX, int positionY, Font font, Color color) {
        if (alpha != 0.0f) {
            WaterMarkUtil.alpha = alpha;
        }
        if (positionX != 0) {
            WaterMarkUtil.positionX = positionX;
        }
        if (positionY != 0) {
            WaterMarkUtil.positionY = positionY;
        }
        if (font != null) {
            WaterMarkUtil.font = font;
        }
        if (color != null) {
            WaterMarkUtil.color = color;
        }
    }

    /**
     * 水印icon设置
     * @param iconAlpha     透明度
     * @param iconPositionX icon 位置 x
     * @param iconPositionY icon 位置 y
     * @param iconWidth     icon 宽
     * @param iconHeight    icon 高
     * @param isEqualRatio  是否等比缩小icon
     * @param rotateX       相对旋转点 x
     * @param rotateY       相对旋转点 y
     */
    public static void waterMarkOptionsIcon(float iconAlpha, int iconPositionX, int iconPositionY, int iconWidth, int iconHeight, Boolean isEqualRatio, int rotateX, int rotateY) {
        if (iconAlpha != 0.0f) {
            WaterMarkUtil.iconAlpha = iconAlpha;
        }
        if (iconPositionX >= 0) {
            WaterMarkUtil.iconPositionX = iconPositionX;
        }
        if (iconPositionY  >= 0) {
            WaterMarkUtil.iconPositionY = iconPositionY;
        }
        if (iconWidth != 0) {
            WaterMarkUtil.iconWidth = iconWidth;
        }
        if (iconHeight != 0) {
            WaterMarkUtil.iconHeight = iconHeight;
        }
        if (null != isEqualRatio) {
            WaterMarkUtil.isEqualRatio = isEqualRatio;
        }

        WaterMarkUtil.rotateX = rotateX;
        WaterMarkUtil.rotateY = rotateY;

    }

    public static void waterMarkByText(String text, String sourceImgPath, String targerImgPath) throws IOException {
        waterMarkByText(text, sourceImgPath, targerImgPath, null);
    }

    /**
     * 水印文字
     *
     * @param text          水印文字
     * @param sourceImgPath 源图片地址
     * @param targerImgPath 水印之后图片地址
     * @param degree        水印文字旋转角度
     */
    public static void waterMarkByText(String text, String sourceImgPath, String targerImgPath, Integer degree) throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Image sourceImg = null;
        BufferedImage targerImage = null;
        try {
            if (StrUtil.isBlank(sourceImgPath) || StrUtil.isBlank(targerImgPath)) {
                return;
            }

            sourceImg = Watermark.pictureTrans(sourceImgPath);

            int width = sourceImg.getWidth(null);
            int height = sourceImg.getHeight(null);

            targerImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics2D = targerImage.createGraphics();

            /**使得背景透明**/
            targerImage = graphics2D.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            graphics2D.dispose();
            graphics2D = targerImage.createGraphics();

            graphics2D.setColor(color);
            graphics2D.setFont(font);

            graphics2D.setBackground(Color.white);

            graphics2D.drawImage(sourceImg, 0, 0, null);

            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (null != degree) {
                graphics2D.rotate(Math.toRadians(degree), (double) targerImage.getWidth() / 2, (double) targerImage.getHeight() / 2);
            }

             /*  对要显示的文字进行处理 */
            AttributedString ats = new AttributedString(text);
            ats.addAttribute(TextAttribute.FONT, font, 0, text.length());
            AttributedCharacterIterator iter = ats.getIterator();

            graphics2D.drawString(iter, positionX, positionY);

            graphics2D.dispose();

            outputStream = new FileOutputStream(targerImgPath);
            String formatName = targerImgPath.substring(targerImgPath.lastIndexOf(".") + 1);
            ImageIO.write(targerImage, formatName, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != outputStream) {
                outputStream.close();
            }
            if (null != sourceImg) {
                sourceImg.flush();
            }
            if (null != targerImage) {
                targerImage.flush();
            }
        }
    }

    /**
     * icon 水印
     * @param iconImgPath
     * @param sourceImgPath
     * @param targerImgPath
     * @param degree          水印自旋转角度
     * @param align           水印位置
     * @param iconSourceRatio 水印与原图比例
     * @throws IOException
     */
    public static void waterMarkByImg(String iconImgPath, String sourceImgPath, String targerImgPath, Integer degree, String align, Float iconSourceRatio) throws IOException {
        InputStream inputStream = null;
        InputStream iconInputStream = null;
        OutputStream outputStream = null;
        Image sourceImg = null;
        Image iconImg = null;
        BufferedImage targerImage = null;

        try {
            if (StrUtil.isBlank(iconImgPath) || StrUtil.isBlank(sourceImgPath) || StrUtil.isBlank(targerImgPath)) {
                return;
            }

            sourceImg = Watermark.pictureTrans(sourceImgPath);
            iconImg = Watermark.pictureTrans(iconImgPath);

            int width = sourceImg.getWidth(null);
            int height = sourceImg.getHeight(null);

            targerImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = targerImage.createGraphics();

            targerImage = graphics2D.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            graphics2D.dispose();
            graphics2D = targerImage.createGraphics();

            graphics2D.drawImage(sourceImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

            if (null != degree) {
                if (0 != rotateX || 0 != rotateY) {
                    graphics2D.rotate(Math.toRadians(degree), (double) rotateX, (double) rotateY);
                } else {
                    iconImg = rotate(iconImg, degree);
                }
            }

            if (null != iconSourceRatio) {
                iconWidth = (int) (width * iconSourceRatio);
            }
            iconImg = zoom(iconImg, isEqualRatio);

            waterMarkAlign(align, sourceImg, iconImg);

            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            graphics2D.drawImage(iconImg, iconPositionX, iconPositionY, null);
            graphics2D.dispose();

            outputStream = new FileOutputStream(targerImgPath);
            String formatName = targerImgPath.substring(targerImgPath.lastIndexOf(".") + 1);
            ImageIO.write(targerImage, formatName, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != iconInputStream) {
                iconInputStream.close();
            }
            if (null != outputStream) {
                outputStream.close();
            }
            if (null != iconImg) {
                iconImg.flush();
            }
            if (null != sourceImg) {
                sourceImg.flush();
            }
            if (null != targerImage) {
                targerImage.flush();
            }
        }
    }

    /**
     * 旋转图片
     * @param image
     * @param angle
     * @return
     */
    public static BufferedImage rotate(Image image, float angle) {
        int w = image.getWidth(null);
        int h = image.getHeight(null);
        BufferedImage bImage = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bImage.createGraphics();
        bImage = g2d.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = bImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.rotate(Math.toRadians(angle), w / 2, h / 2);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return bImage;
    }

    /**
     * 图片剪切
     * @param imagePath
     * @param shapeImagePath
     */
    public static void shear(String imagePath, String shapeImagePath, String outPath) {

        try {
            Image shapeImage = ImageIO.read(new File(shapeImagePath));
            int width = shapeImage.getWidth(null);
            int height = shapeImage.getHeight(null);
            Shape shape = getImageShape(shapeImage);

            Image image = ImageIO.read(new File(imagePath));
            int bw = image.getWidth(null);

            BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bImage.createGraphics();

            bImage = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g2d.dispose();
            g2d = bImage.createGraphics();

            g2d.setClip(shape);

            g2d.drawImage(image, - (bw - width) / 2, 0, null);
            g2d.dispose();

            OutputStream outputStream = new FileOutputStream(outPath);
            ImageIO.write(bImage, "png", outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将Image图像转换为Shape图形
     * @param img
     * @return Image图像的Shape图形表示
     */
    public static Shape getImageShape(Image img) throws InterruptedException {

        ArrayList<Integer> x = new ArrayList<Integer>();
        ArrayList<Integer> y = new ArrayList<Integer>();
        int width = img.getWidth(null);
        int height = img.getHeight(null);

        /**首先获取图像所有的像素信息**/
        PixelGrabber pgr = new PixelGrabber(img, 0, 0, -1, -1, true);
        pgr.grabPixels();
        int pixels[] = (int[]) pgr.getPixels();

        /**循环像素**/
        for (int i = 0; i < pixels.length; i++) {
            /**筛选，将不透明的像素的坐标加入到坐标ArrayList x和y中**/
            int alpha = (pixels[i] >> 24) & 0xff;
            if (alpha == 0) {
                continue;
            } else {
                x.add(i % width > 0 ? i % width - 1 : 0);
                y.add(i % width == 0 ? (i == 0 ? 0 : i / width - 1) : i / width);
            }
        }

        /**建立图像矩阵并初始化(0为透明,1为不透明)**/
        int[][] matrix = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = 0;
            }
        }

        /**导入坐标ArrayList中的不透明坐标信息**/
        for (int c = 0; c < x.size(); c++) {
            matrix[y.get(c)][x.get(c)] = 1;
        }

		/** 逐一水平"扫描"图像矩阵的每一行，将不透明的像素生成为Rectangle，
		 * 再将每一行的Rectangle通过Area类的rec对象进行合并， 最后形成一个完整的Shape图形
		 */
        Area rec = new Area();
        int temp = 0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (matrix[i][j] == 1) {
                    if (temp == 0)
                        temp = j;
                    else if (j == width) {
                        if (temp == 0) {
                            Rectangle rectemp = new Rectangle(j, i, 1, 1);
                            rec.add(new Area(rectemp));
                        } else {
                            Rectangle rectemp = new Rectangle(temp, i, j - temp, 1);
                            rec.add(new Area(rectemp));
                            temp = 0;
                        }
                    }
                } else {
                    if (temp != 0) {
                        Rectangle rectemp = new Rectangle(temp, i, j - temp, 1);
                        rec.add(new Area(rectemp));
                        temp = 0;
                    }
                }
            }
            temp = 0;
        }
        return rec;
    }

        /**
         * icon大小
         * @param image
         * @param isEqualRatio 是否等比缩放
         * @return
         */
    public static Image zoom(Image image, boolean isEqualRatio) {

        if (0 != iconWidth) {
            int w = image.getWidth(null);
            int h = image.getHeight(null);
            if (isEqualRatio) {
                double ratio = iconWidth * 1.0d / w;
                iconHeight = (int) (h * ratio);
            }
            image = image.getScaledInstance(iconWidth, iconHeight, Image.SCALE_AREA_AVERAGING);
            BufferedImage bImage = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_INT_RGB);
            bImage.getGraphics().drawImage(image, 0, 0, iconWidth, iconHeight, null);
            Graphics2D g2d = bImage.createGraphics();
            bImage = g2d.getDeviceConfiguration().createCompatibleImage(iconWidth, iconHeight, Transparency.TRANSLUCENT);
            g2d.dispose();
            g2d = bImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();
            bImage.flush();
            return bImage;
        } else {
            return image;
        }
    }

    /**
     * 缩放至同shape相同大小(正方形状)
     * @param imagePath
     * @param shapeImagePath
     * @param savePath
     */
    public static void zoom(String imagePath, String shapeImagePath, String savePath) {

        File file = new File(imagePath);
        File shapeFile = new File(shapeImagePath);
        try {
            Image image = ImageIO.read(file);
            Image bi = ImageIO.read(shapeFile);

            int height = bi.getHeight(null) + 1;
            image = image.getScaledInstance(height, height, Image.SCALE_AREA_AVERAGING);
            BufferedImage bImage = new BufferedImage(height, height, BufferedImage.TYPE_INT_RGB);
            bImage.getGraphics().drawImage(image, 0, 0, height, height, null);

            Graphics2D g2d = bImage.createGraphics();
            bImage = g2d.getDeviceConfiguration().createCompatibleImage(height, height, Transparency.TRANSLUCENT);
            g2d.dispose();

            g2d = bImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            OutputStream outputStream = new FileOutputStream(savePath);
            ImageIO.write(bImage, "png", outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws IOException {
        /*String textSource = "C:/Users/user/Desktop/watermark@3x.png";
        String textTarger = "C:/Users/user/Desktop/watermarkByText.png";
        String source = "C:/Users/user/Desktop/333.jpg";

        String icon = "C:/Users/user/Desktop/watermarkByText.png";
        String targer = "C:/Users/user/Desktop/watermarkByIcon3.png";
        String text = "9.9分女";
        waterMarkByText(text, textSource, textTarger, 0);
        waterMarkByImg(icon, source, targer, -30, "");
        System.out.println("end end end");*/
//        String url = "D:/web/temp/watericon.png";
//        Image image = ImageIO.read(new FileInputStream(new File(url)));
       /* BufferedImage bImg = rotate(image, -30);*/
//        Image zImg = zoom(image, false);
//        ImageIO.write((RenderedImage) zImg, "png", new File("D:/web/temp/watericon22.png"));




        String savePath = "C:/Users/Administrator/Desktop/test666.png";
        String imagePath = "C:/Users/Administrator/Desktop/888.jpg";
        String imageShapePath = "C:/Users/Administrator/Desktop/position@3x.png";

        String outPath = "C:/Users/Administrator/Desktop/test888.png";

        zoom(imagePath, imageShapePath, savePath);

        shear(savePath, imageShapePath, outPath);
    }


}