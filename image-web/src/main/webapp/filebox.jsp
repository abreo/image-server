<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>跨域上传图片前端示例 EasyUI FileBox</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.5.3/themes/material/easyui.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.5.3/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.5.3/demo/demo.css">
    <link rel="stylesheet" type="text/css" href="https://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/lightbox/lightbox.min.css">
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/lightbox/lightbox-plus-jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.3/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.3/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.5.3/locale/easyui-lang-zh_CN.js"></script>
</head>
<body>
<h2>跨域上传图片前端示例 <a href="http://www.jeasyui.com/" target="_blank">EasyUI FileBox</a></h2>
<p>打开浏览器控制台查看服务器返回的数据</p>
<form id="ff" method="post">

    <div style="margin-bottom:20px">
        <h3>&bull; 图片</h3>

        <div class="easyui-panel" style="padding:10px;">
            <input name="imageUrls" type="hidden" id="imageUrls" value="">
            <a href="javascript:void(0);" onclick="addImageDlg('imageUrls')" class="easyui-linkbutton"
               data-options="iconCls:'icon-add'" style="width:100px;display:block;">添加图片</a>
        </div>
    </div>

</form>

<div id="image-add"></div>

<script>
    var UPLOAD_URL = 'http://image.example.com/uploadImage?mediaType=Picture&module=Test';
    var DOWNLOAD_URL = 'http://image.example.com/imageThumbnail?relUrl=';

    function addImageDlg(id) {
        $('#image-add').html('<div id="image-add-dlg" style="display: none;">' +
                '<form id="image-add-form" style="margin:0;padding:20px 30px" method="post" enctype="multipart/form-data">' +
                '<input name="file" class="easyui-filebox" data-options="required:true,buttonText:\'点击选择\'" style="width:100%">' +
                '</form>' +
                '</div>');
        jQuery.parser.parse($('#image-add'));
        $('#image-add-dlg').dialog({
            title: '选择图片',
            width: 350,
            height: 150,
            onClose: function () {
                $('#image-add-form').form('clear');
                $('#image-add-dlg').dialog('destroy');
                $('#image-add-dlg').hide();
                $('#image-add-dlg').remove();
            },
            buttons: [{
                text: '增加',
                iconCls: 'icon-ok',
                handler: function () {
                    jQuery.messager.progress({title: '请稍后', msg: '正在上传图片...'});
                    $('#image-add-form').form('submit', {
                        url: UPLOAD_URL,
                        iframe: false,//跨域上传必须设为false，否则收不到返回数据
                        onSubmit: function(){
                            var isValid = $(this).form('validate');
                            if (!isValid){
                                jQuery.messager.progress('close');	// hide progress bar while the form is invalid
                            }
                            return isValid;	// return false will stop the form submission
                        },
                        success: function (data) {
                            console.log(data);
                            $("#" + id).before(
                                    '<div class="imageItem" style="position: relative;">' +
                                    '<div style="float: left;">' +
                                    '<a class="example-image-link" href="' + DOWNLOAD_URL + data + '" data-lightbox="example-2">' +
                                    '<img class="example-image" alt="[上传失败]" src="' + DOWNLOAD_URL + data + '" style="width:100px;height:100px;border:1px solid #5ed5d1;">' +
                                    '</a>' +
                                    '</div>' +
                                    '<div style="float: left;margin-left: 10px;">' +
                                    '<input name="imageDesc" data-img-url="' + data + '" class="image-desc easyui-textbox" data-options="prompt:\'图片描述...\',multiline:true" style="width:300px;height:100px;">' +
                                    '</div>' +
                                    '<i style="float:left;color:red;cursor:pointer;" class="fa fa-times-circle fa-lg" onclick="$(this).parent().remove()"></i><br>' +
                                    '<i style="float:left;color:#2A8FBD;cursor:pointer;" class="fa fa-chevron-circle-up fa-lg" onclick="if ($(this).parent().prev().attr(\'class\') == \'imageItem\') {$(this).parent().insertBefore($(this).parent().prev());}"></i><br>' +
                                    '<i style="float:left;color:#2A8FBD;cursor:pointer;" class="fa fa-chevron-circle-down fa-lg" onclick="if ($(this).parent().next().attr(\'class\') == \'imageItem\') {$(this).parent().insertAfter($(this).parent().next());}"></i><br>' +
                                    '<div style="clear:both;"></div>' +
                                    '</div>');


                            jQuery.parser.parse($(".imageItem"));
                            $('#image-add-dlg').dialog('close');
                            jQuery.messager.progress('close');
                        }
                    });
                }
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#image-add-dlg').dialog('close');
                }
            }]
        });
    }
</script>

</body>
</html>
