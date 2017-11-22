# 图片服务器

支持跨域上传、图片自定义分辨率下载

有两个项目，image-server是图片服务器，image-web是前台测试项目，使用EasyUI的FileBox和Baidu的WebUploader上传插件进行说明。

## image-server

### 配置

找到 /image-server/src/main/resources/url.properties 中的url.upload，配置的是图片的上传保存的绝对路径。

找到 /image-server/src/main/java/com/jthinking/image/web/interceptor/UploadInterceptor.java，将其中的“*”通配符改为你的前台系统域名。

## image-web

### 配置

找到 /image-web/src/main/webapp/filebox.jsp，其中有两个js变量

        var UPLOAD_URL = 'http://image.example.com/uploadImage?mediaType=Picture&module=Test';
        var DOWNLOAD_URL = 'http://image.example.com/imageThumbnail?relUrl=';

UPLOAD_URL表示上传地址；DOWNLOAD_URL表示图片展示地址。

上传的时候，将域名改为你的域名，参数mediaType代表上传的文件类型，这里是图片，module代表模块，指该图片所属的具体业务模块。上传到服务器后图片会以url.upload/mediaType/module/yyyy/MM/dd/当前时间戳字符串.jpg存储。

展示的时候，将域名改为你的域名，参数relUrl表示图片的相对url.upload的路径，也就是mediaType/module/yyyy/MM/dd/当前时间戳字符串.jpg。
