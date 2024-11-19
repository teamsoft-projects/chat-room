<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>谈天说地 - 聊天室</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="shortcut icon" type="image/x-icon" href="/static/images/favicon.ico"/>
    <link rel="bookmark" href="/static/images/favicon.ico">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/css/reset.css">
    <link rel="stylesheet" href="/static/css/layui.css">
</head>
<body>
<div id="topContainer" class="wrapper" style="display: none;">
    <input type="hidden" id="websocket-url" value="${resultInfo.data}"/>
    <div class="container">
        <div class="right">
            <div class="top">
                <span id="chat-title">群聊: <span class="name" id="chat-name">停下匆匆的脚步</span></span>
            </div>
            <div class="chat" data-chat="person2" id="chat-outer">
                <div class="show-area" id="chat-show">
                    <div style="margin-top: 20px;"></div>
                </div>
            </div>
        </div>
        <div class="right send-area">
            <div class="write">
                <input type="text" id="write">
            </div>
            <div class="send" id="send">
                <a href="javascript:void(0);" class="layui-btn layui-btn-sm">发送</a>
            </div>
        </div>
    </div>
</div>
<script src="../static/js/jquery.min.js"></script>
<script src="../static/common/layer/layer.js"></script>
<script src="../static/common/encrypt/aes.js"></script>
<script src="../static/common/encrypt/pad-nopadding.js"></script>
<script src="../static/common/encrypt/mode-ecb.js"></script>
<script src="../static/js/utils.js"></script>
<script src="../static/js/chat.js?v=220413"></script>
</body>
</html>