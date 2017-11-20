package com.jthinking.image.service;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 文件下载工具类
 * @author JiaBochao
 * @version 2017-11-17 15:33:34
 */
@Component
@PropertySource("classpath:url.properties")
public class DownloadService {
    @Autowired
    private Environment env;

    private static final int BUFFER_SIZE = 1024;

    /**
     * 根据文件的相对路径返回文件的字节数组
     * @param relUrl 文件的相对路径。相对于url.upload
     * @return 目标文件的字节数组
     * @throws java.io.IOException
     */
    public byte[] getFileBytes(String relUrl) throws IOException {

        String prefix = env.getProperty("url.upload");

        if (relUrl == null || relUrl.equals("")) {
            return null;
        }

        String filename = prefix + relUrl;

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in = null;

        try {
            in = new BufferedInputStream(new FileInputStream(f));
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, BUFFER_SIZE))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    /**
     * 图片缩略图
     * @param outputStream
     * @param relUrl 图片位置的相对路径。相对于url.properties配置文件的url.upload值
     * @param height 高度（像素）
     * @param width 宽度（像素）
     * @param force 忽略比例，强制宽高。1代表强制，0代表不强制
     * @throws Exception
     */
    public void imageThumbnail(
            ServletOutputStream outputStream,
            String relUrl,
            Integer height,
            Integer width,
            Integer force) throws Exception {

        String prefix = env.getProperty("url.upload");

        if (relUrl == null || relUrl.equals("")) {
            return;
        }
        String filename = prefix + "/" + relUrl;

        //读入图片文件
        File srcImg = new File(filename);
        if (!srcImg.exists()) {
            if (!srcImg.exists()) {
                throw new FileNotFoundException(srcImg.toString());
            }
        }

        Thumbnails.Builder<File> fileBuilder = Thumbnails.of(srcImg).scale(1.0).outputQuality(1.0);
        BufferedImage image = fileBuilder.asBufferedImage();
        Thumbnails.Builder<BufferedImage> builder = null;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        if (height == null) {
            height = imageHeight;
        }

        if (width == null) {
            width = imageWidth;
        }

        if (force == null || force.equals(0)) {
            //保持原始比例
            Thumbnails.of(srcImg).size(height, width).toOutputStream(outputStream);
        } else if (force.equals(1)) {
            //强制宽高，保持原始比例，多余的进行裁剪
            if ((float)height / width != (float)imageWidth / imageHeight) {
                if (imageWidth > imageHeight) {
                    image = Thumbnails.of(srcImg).height(height).asBufferedImage();
                } else {
                    image = Thumbnails.of(srcImg).width(width).asBufferedImage();
                }
                builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, width, height).size(width, height);
            } else {
                builder = Thumbnails.of(image).size(width, height);
            }
            builder.outputFormat("jpg").toOutputStream(outputStream);

        }
    }


}
