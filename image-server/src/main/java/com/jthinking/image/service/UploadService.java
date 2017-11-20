package com.jthinking.image.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件上传工具类
 * @author JiaBochao
 * @version 2017-11-17 15:33:48
 */
@Component
@PropertySource("classpath:url.properties")
public class UploadService {
    @Autowired
    private Environment env;

    /**
     * 注入项目根路径的绝对路径，需要在web.xml中配置
     */
    @Value("#{systemProperties['webapp.absUrl']}")
    private String webAppAbsUrl;

    /**
     * 文件上传
     * @param file Spring的MultipartFile类
     * @param mediaType 文件类型。e.g. picture, video..
     * @param module 模块。e.g. employee, user, department..
     * @return 图片上传位置的相对路径。相对于url.properties配置文件的url.upload值
     * @throws java.io.IOException
     */
    public String upload(MultipartFile file, String mediaType, String module) throws IOException {
        //设置文件存放位置
        String uploadUrl = env.getProperty("url.upload");
        if (uploadUrl == null || uploadUrl.equals("")) {
            uploadUrl = webAppAbsUrl + "Upload/";
        }

        if (file == null || file.isEmpty()) {
            return null;
        }
        byte[] bytes = file.getBytes();
        String[] nameArr = file.getOriginalFilename().split("\\.");
        String fileType = nameArr[nameArr.length-1];
        SimpleDateFormat dateFormat = new SimpleDateFormat("/yyyy/MM/dd/");
        String dir = dateFormat.format(new Date());
        String fileName = System.currentTimeMillis() + "." + fileType;
        // 文件的相对路径名
        String relUrl = mediaType + "/" + module + dir + fileName;
        // 文件的绝对路径名
        String absUrl = uploadUrl + "/" + relUrl;

        File path = new File(uploadUrl + "/" + mediaType + "/" + module + dir);
        if (!path.exists()) {
            path.mkdirs();
        }
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(absUrl));
        out.write(bytes);
        out.flush();
        out.close();

        return relUrl;
    }
}
