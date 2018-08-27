package com.mrping.face.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author caoqc
 * @Description: 图片处理工具
 * @date 2018/7/26 16:34
 */
public class ImageUtil {

    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     */
    public static String generateImage(String imgStr, String path) {
        if (StrUtil.isBlank(imgStr) && StrUtil.isBlank(path)) {
            return "";
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return path;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @return
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /**
     * @param img
     * @return Image图像的Shape图形表示
     * @throws InterruptedException
     * @author Hexen
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
     * 判断图片是否大于目标尺寸
     *
     * @param srcPath
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static boolean isBigImage(String srcPath, int maxWidth, int maxHeight) {
        BufferedImage bufferedImage = null;
        try {
            File of = new File(srcPath);
            if (of.canRead()) {
                bufferedImage = ImageIO.read(of);
            }
            if (bufferedImage != null) {
                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();
                if (width > maxWidth && height > maxHeight) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (null != bufferedImage) {
                bufferedImage.flush();
            }
        }
        return false;
    }

    public static void zoomInImage(String srcPath, int maxWidth, int maxHeight) throws IOException {

        Image image = null;
        OutputStream outputStream = null;
        try {
            image = ImageIO.read(new File(srcPath));
            image = zoomInImage((BufferedImage) image, maxWidth, maxHeight);
            String formatName = srcPath.substring(srcPath.lastIndexOf(".") + 1);
            outputStream = new FileOutputStream(srcPath);
            ImageIO.write((RenderedImage) image, formatName, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != image) {
                image.flush();
            }
            if (null != outputStream) {
                outputStream.flush();
            }
        }
    }

    /**
     * 对图片进行放大或缩小
     * @param originalImage 原始图片
     * @param maxWidth      目标宽度
     * @param maxHeight     目标高度
     * @return
     */
    private static BufferedImage zoomInImage(BufferedImage originalImage, int maxWidth, int maxHeight) {
        double times = 1;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        double sw = (maxWidth * 1.0) / (width * 1.0);
        double sh = (maxHeight * 1.0) / (height * 1.0);
        if (width > maxWidth && height > maxHeight) {
            if (width > height) {
                times = sw;
            } else {
                times = sh;
            }
        } else if (width < maxWidth && height < maxHeight) {
            if (sw > sh) {
                times = sw;
            } else {
                times = sh;
            }
        } else if (width < maxWidth && height > maxHeight) {
            times = sw;
        } else {
            times = sh;
        }
        int lastW = (int) (times * width);
        int lastH = (int) (times * height);
        BufferedImage newImage = new BufferedImage(lastW, lastH, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, lastW, lastH, null);
        g.dispose();
        newImage.flush();
        return newImage;
    }

    /**
     * 裁剪图片
     * @param sourceImgPath
     * @param outImgPath
     * @param maxWidth
     * @param maxHeight
     */
    public static void cropPngImage(String sourceImgPath, String outImgPath, int x, int y, int maxWidth, int maxHeight) {
        Image image = null;
        BufferedImage targerImage = null;
        ImageInputStream imageStream = null;
        InputStream inputStream = null;
        try {
            targerImage = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_RGB);
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("png");
            ImageReader reader = readers.next();
            if (sourceImgPath.contains("http")) {
                URL url = new URL(sourceImgPath);
                inputStream = url.openStream();
            } else {
                inputStream = new FileInputStream(new File(sourceImgPath));
            }
            imageStream = ImageIO.createImageInputStream(inputStream);
            reader.setInput(imageStream, true);
            ImageReadParam param = reader.getDefaultReadParam();
            Rectangle rect = new Rectangle(x, y, maxWidth, maxHeight);
            param.setSourceRegion(rect);
            targerImage = reader.read(0, param);
            writeImage(outImgPath, targerImage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != image) {
                image.flush();
            }
            if (null != targerImage) {
                targerImage.flush();
            }
        }
    }

    /**
     * @param imagePath
     * @param shapeImagePath
     * @param outPath
     * @param color
     * @param f
     */
    public static void shear(String imagePath, String shapeImagePath, String outPath, Color color, float f) throws IOException {
        Image shapeImage = null;
        Image image = null;
        BufferedImage bImage = null;
        OutputStream outputStream = null;
        try {
            shapeImage = readImage(shapeImagePath);
            int width = shapeImage.getWidth(null);
            int height = shapeImage.getHeight(null);
            Shape shape = getImageShape(shapeImage);
            image = readImage(imagePath);
            int bw = image.getWidth(null);
            bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = bImage.createGraphics();
            bImage = g2d.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            g2d.dispose();
            g2d = bImage.createGraphics();
            g2d.setClip(shape);
            g2d.drawImage(image, 0, 0, null);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setStroke(new BasicStroke(f));
            g2d.setColor(color);
            g2d.setComposite(AlphaComposite.SrcAtop);
            g2d.draw(shape);
            g2d.dispose();
            outputStream = new FileOutputStream(outPath);
            ImageIO.write(bImage, "png", outputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != image) {
                image.flush();
            }
            if (null != shapeImage) {
                shapeImage.flush();
            }
            if (null != bImage) {
                bImage.flush();
            }
            if (null != outputStream) {
                outputStream.flush();
            }
        }
    }

    /**
     * 生成渐变图
     *
     * @param sourceImagPath  底板宽、高
     * @param targetImagePath 生成图片的地址
     * @param r               rgb r
     * @param g               rgb g
     * @param b               rgb b
     * @param x1              开始位置 x
     * @param y1              开始位置 y
     * @param x2              结束位置 x
     * @param y2              结束位置 y
     * @param a1              alpha 开始值
     * @param a2              alpha 结束值
     * @param direction       渐变图方向 “x”：x方向  “y”：y方向
     */
    public static void createShade(String sourceImagPath, String targetImagePath, int r, int g, int b, int x1, int y1, int x2, int y2, int a1, int a2, String direction) {
        Image sourceImage = null;
        BufferedImage targerImage = null;
        try {

            if (StrUtil.isNotBlank(sourceImagPath)) {

                sourceImage = readImage(sourceImagPath);
                int w = sourceImage.getWidth(null) + 1;
                int h = sourceImage.getHeight(null) + 1;

                if (x1 > w) {
                    x1 = w;
                }

                if (x2 > w) {
                    x2 = w;
                }

                if (y1 > h) {
                    y1 = h;
                }

                if (y2 > h) {
                    y2 = h;
                }

                targerImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

                Graphics2D graphics2D = targerImage.createGraphics();

                /**使得背景透明**/
                targerImage = graphics2D.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
                graphics2D.dispose();
                graphics2D = targerImage.createGraphics();

                Color color = null;
                if ("x".equals(direction)) {
                    for (int i = x1; i < x2; i++) {
                        int ii = 0;
                        for (int j = y1; j < y2; j++) {
                            color = new Color(r, g, b, getAlpha(i, j, x1, y1, x2, y2, a1, a2, direction));
                            graphics2D.setColor(color);
                            graphics2D.drawRect(i, j, 1, 1);
                        }
                    }
                } else {
                    for (int j = y1; j < y2; j++) {
                        for (int i = x1; i < x2; i++) {
                            color = new Color(r, g, b, getAlpha(i, j, x1, y1, x2, y2, a1, a2, direction));
                            graphics2D.drawRect(i, j, 1, 1);
                            graphics2D.setColor(color);
                        }
                    }
                }
                writeImage(targetImagePath, targerImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != sourceImage) {
                sourceImage.flush();
            }
            if (null != targerImage) {
                targerImage.flush();
            }
        }
    }

    /**
     * 获取图片流
     * @param imgPath
     * @return
     * @throws IOException
     */
    private static Image readImage(String imgPath) throws IOException {
        Image image = null;
        InputStream inputStream = null;
        try {
            if (imgPath.contains("http")) {
                URL url = new URL(imgPath);
                image = ImageIO.read(url.openStream());
            } else {
                image = ImageIO.read(new File(imgPath));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != image) {
                image.flush();
            }
            if (null != inputStream) {
                inputStream.close();
            }
        }
        return image;
    }

    /**
     * 生成图片
     *
     * @param targerImgPath
     * @param targerImage
     * @throws IOException
     */
    private static void writeImage(String targerImgPath, BufferedImage targerImage) throws IOException {

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(targerImgPath);
            String formatName = targerImgPath.substring(targerImgPath.lastIndexOf(".") + 1);
            ImageIO.write(targerImage, formatName, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != targerImage) {
                targerImage.flush();
            }
        }
    }

    /**
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param a1
     * @param a2
     * @param direction
     * @return
     */
    private static int getAlpha(int x, int y, int x1, int y1, int x2, int y2, int a1, int a2, String direction) {
        if ("x".equals(direction)) {
            float ratio = (a2 - a1) * 1.0F / (y2 - y1) * 1.0F;
            float result = ratio * ((y - y1) * 1.0F);
            return (int) result;
        } else {
            float ratio = (a2 - a1) / (x2 - x1);
            float result = ratio * (x - x1);
            return (int) result;
        }
    }


    public static void main(String[] args) throws IOException {
        String sourceImagePath = "C:/Users/user/Pictures/123.jpg";
        String targetImagePath = "C:/Users/user/Pictures/123_01.png";

        //String sourceImagPath, String targetImagePath, int r, int g, int b, int x, int y, int x2, int y2, int a, int a2, String direction
        createShade(sourceImagePath, targetImagePath, 0, 0, 0, 0, 226, 600, 553, 0, 60, "x");

        System.out.println("end end end");
        System.out.println("end end end");
        System.out.println("end end end");

    }
}