<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>谈天说地 - 聊天室</title>
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="shortcut icon" type="image/x-icon" href="/static/images/favicon.ico"/>
    <link rel="bookmark" href="/static/images/favicon.ico">
    <link rel="stylesheet" href="/static/common/layui/css/layui.css">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" href="/static/css/reset.css">
    <link rel="stylesheet" href="/static/css/index.css">
</head>
<body>
<div id="top-container" class="wrapper hide">
    <div class="container">
        <div class="right">
            <div class="top">
                <span id="chat-title">聊天室列表: <span class="name" id="chat-room-count"></span></span>
                <span class="user"><span class="welcome mr5">欢迎你</span><span id="login-user" class="login-user"></span></span>
            </div>
            <div class="chat" data-chat="person2" id="chat-outer">
                <div class="show-area">
                    <div style="margin-top: 20px;" id="chat-room-list">
                    </div>
                </div>
            </div>
        </div>
        <div>
            <div class="operation">
                <a id="join-room" href="javascript:void(0);" class="layui-btn layui-btn-primary layui-border-black mr5">加入房间</a>
                <a id="create-room" href="javascript:void(0);" class="layui-btn layui-btn-primary layui-border-green">创建房间</a>
            </div>
        </div>
    </div>
</div>
<script src="../static/js/jquery.min.js"></script>
<script src="../static/common/layui/layui.js"></script>
<script src="../static/common/encrypt/aes.js"></script>
<script src="../static/common/encrypt/pad-nopadding.js"></script>
<script src="../static/common/encrypt/mode-ecb.js"></script>
<script src="../static/js/utils.js?v=220413"></script>
<script src="../static/js/index.js?v=22041301"></script>
<script type="text/template" id="createRoomTemplate">
    <form class="layui-form popup-form" action="" lay-filter="example">
        <div class="layui-form-item pr20">
            <label class="layui-form-label">房间名</label>
            <div class="layui-input-block">
                <input type="text" name="roomName" autocomplete="off" placeholder="请输入房间名，8个汉字内" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item pr20">
            <label class="layui-form-label">私密</label>
            <div class="layui-input-block">
                <input type="checkbox" name="hasPassword" lay-filter="hasPassword" lay-skin="primary" value="1">
            </div>
        </div>
        <div class="layui-form-item pr20 hide" name="password-area">
            <label class="layui-form-label">密码</label>
            <div class="layui-input-block">
                <input type="password" name="passwd" autocomplete="off" placeholder="请输入密码" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item pr20">
            <div class="layui-input-block">
                <a name="create" href="javascript:void(0);" class="layui-btn layui-btn-primary layui-border-green layui-btn-sm btn-thin" lay-submit lay-filter="create">创&nbsp;&nbsp;&nbsp;建</a>
            </div>
        </div>
    </form>
</script>
</body>
</html>