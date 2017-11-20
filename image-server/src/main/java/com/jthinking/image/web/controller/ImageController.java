package com.jthinking.image.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.jthinking.image.service.DownloadService;
import com.jthinking.image.service.UploadService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片上传与展示
 *
 * @author JiaBochao
 * @version 2017-11-17 15:33:11
 */
@Controller
public class ImageController {

    @Resource
    private UploadService uploadService;

    @Resource
    private DownloadService downloadService;

    /**
     * 图片缩略图展示
     *
     * @param response
     * @param relUrl   图片的相对路径
     * @param height   图片高度
     * @param width    图片宽度
     * @param force    强制宽高，会裁剪图片。1代表是，0代表否
     */
    @RequestMapping("/imageThumbnail")
    public void imageThumbnail(HttpServletResponse response, String relUrl, Integer height, Integer width, Integer force) {
        try {
            if (relUrl.endsWith(".jpg") || relUrl.endsWith(".jpeg") || relUrl.endsWith(".JPG") || relUrl.endsWith(".JPEG")) {
                response.setContentType("image/jpeg");
            } else if (relUrl.endsWith(".png") || relUrl.endsWith(".PNG")) {
                response.setContentType("image/png");
            } else if (relUrl.endsWith(".gif") || relUrl.endsWith(".GIF")) {
                response.setContentType("image/gif");
            }
            downloadService.imageThumbnail(response.getOutputStream(), relUrl, height, width, force);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跨域上传功能接口
     *
     * @param file      文件表单名
     * @param mediaType 媒体类型。用于分类存储
     * @param module    文件所属业务模块。用于分类存储
     * @return 文件所在位置的相对路径
     */
    @RequestMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(MultipartFile file, String mediaType, String module) {
        String relUrl = null;
        if (mediaType == null || mediaType.trim().equals("")) {
            mediaType = "Picture";
        }
        if (module == null || module.trim().equals("")) {
            module = "Any";
        }
        try {
            relUrl = uploadService.upload(file, mediaType, module);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relUrl;
    }

    /**
     * UEditor自定义图片上传Action
     * 适应各种前端上传插件的原理简单，就是依照插件规定，返回不同格式的返回值
     *
     * @param file 提交的图片表单名称
     * @return UEditor用于回显图片的数据
     */
    @RequestMapping("/ueditorUpload")
    @ResponseBody
    public String upload(MultipartFile file) {
        JSONObject jsonObject = new JSONObject();
        try {
            String relUrl = uploadService.upload(file, "picture", "ueditor");
            //UEditor规定所需字段
            jsonObject.put("state", "SUCCESS");
            jsonObject.put("url", relUrl);
            String[] splits = relUrl.split("/");
            String fileName = splits[splits.length - 1];
            jsonObject.put("title", fileName);
            jsonObject.put("original", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    /**
     * WebUploader插件当使用二进制流的方式上传图片时接收方法
     *
     * @param request
     * @param response
     * @return
     * @throws java.io.IOException
     */
    @RequestMapping(value = "/webUploaderBinary", method = RequestMethod.POST)
    @ResponseBody
    public String webUploaderBinary(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();

        BufferedInputStream in = new BufferedInputStream(inputStream);
        FileOutputStream out = new FileOutputStream(new File("d:/test.jpg"));

        int b = -1;
        byte[] bytes = new byte[1024];

        while ((b = in.read(bytes, 0, bytes.length)) != -1) {
            out.write(b);
        }

        out.close();
        in.close();

        return "success";
    }

}
