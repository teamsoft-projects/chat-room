layui.use(['layer'], function () {
	let layer = layui.layer;
	let form = layui.form;

	let $topContainer = $('#top-container');
	let $loginUser = $('#login-user');
	let $chatRoomCount = $('#chat-room-count');
	let $chatRoomList = $('#chat-room-list');
	let $createRoom = $('#create-room');
	let $joinRoom = $('#join-room');
	let userName;
	let webScocktAddress;

	// 获取聊天室列表
	function refreshChatRoomList() {
		$.ajax({
			url: '/chatroom/getChatRoomList',
			dataType: 'json',
			success: function (_resp) {
				if (_resp['flag'] === 100101) {
					let data = _resp['data'];
					let chatRoomCount = data.length;
					$chatRoomCount.html(chatRoomCount);
					if (data.length > 0) {
						let html = '';
						for (let i = 0; i < data.length; i++) {
							let line = data[i];
							html += '<div class="chat-room-list">\n';
							if (line.hasPassword === 1) {
								html += '<i class="layui-icon layui-icon-password"></i>\n';
							} else {
								html += '<i class="layui-icon layui-icon-none"></i>\n';
							}
							let roomNo = line.roomNo;
							html += '<div class="chat-no">' + roomNo + '</div>\n';
							let roomName = line.roomName;
							html += '<div class="chat-name">' + roomName + '</div>\n';
							let userCount = line.userCount;
							html += '<span class="layui-badge">' + userCount + '</span>\n';
							html += '<i class="layui-icon layui-icon-right chat-right"></i>\n';
							html += '</div>';
						}
						$chatRoomList.html(html);
					}
				} else {
					layer.msg(_resp['message'], {offset: 'b'});
				}
			}
		});
	}

	// 第一次进入时，先刷新一次聊天室列表
	refreshChatRoomList();

	// 每隔一段时间刷新一次聊天室列表
	setInterval(refreshChatRoomList, 15000);

	// 获取服务器配置
	getServerConfig(function (data) {
		// 设置websocket地址
		webScocktAddress = data['webScocktAddress'];
		// 设置名字
		let userName = localStorage.getItem('chat_login_name');
		if (utils.isEmpty(userName)) {
			showPrompt();
		} else {
			verifyName(userName);
		}
	});

	// 名称已设置完成，添加
	utils.addEvent('nameConfirmed', function (name) {
		$loginUser.html(name);
		localStorage.setItem("chat_login_name", name);
		userName = name;
		$topContainer.fadeIn();
		// 建立websocket
		createWebSocket();
	});

	// 点击自己的名字改名
	$loginUser.on('click', function () {
		localStorage.removeItem("chat_login_name");
		location.reload();
	});

	// 创建房间
	$createRoom.on('click', function () {
		layer.open({
			type: 1,
			title: '创建房间',
			shadeClose: false,
			content: $('#createRoomTemplate').html(),
			success: function ($dom, index) {
				form.render();
				form.on('checkbox(hasPassword)', function (data) {
					if (data.value === '1') {
						$dom.find('[name=password-area]').show();
					} else {
						$dom.find('[name=password-area]').hide();
					}
				});
				form.on('submit(create)', function (data) {
					let param = data.field;
					if (utils.isEmpty(param.roomName)) {
						layer.msg('请输入房间名', {offset: 'b'});
						return;
					}
					// 房间已创建事件
					utils.addEvent('chatRoomCreated', function (roomNo) {
						// 刷新聊天室列表
						refreshChatRoomList();
						// 当前用户加入自己创建的房间
						joinChatRoom(roomNo);
					});
					addChatRoom(param, index);
				});
			}
		});
	});

	// 创建聊天室
	function addChatRoom(param, index) {
		$.ajax({
			url: '/chatroom/addChatRoom',
			method: 'post',
			data: param,
			dataType: 'json',
			success: function (_resp) {
				if (_resp['flag'] === 100101) {
					utils.fireEvent('chatRoomCreated', _resp['data']);
					layer.close(index);
				} else {
					layer.msg(_resp['message'], {offset: 'b'});
				}
			}
		});
	}

	// 加入房间
	$joinRoom.on('click', function () {

	});

	function joinChatRoom(roomNo) {

	}

	function createWebSocket() {
		let url = webScocktAddress + userName;
		let socket = new WebSocket(url);
		// 打开事件
		socket.onopen = function () {
		};
		// 获得消息事件
		socket.onmessage = function (msg) {
			// recieved(msg.data);
		};
		// 关闭事件
		socket.onclose = function () {
		};
		// 发生了错误事件
		socket.onerror = function () {
			layer.msg('浏览器不支持websocket', {offset: 'b'});
		}
	}

	function getServerConfig(callback) {
		$.ajax({
			url: '/getServerConfig',
			dataType: 'json',
			success: function (_resp) {
				if (_resp['flag'] === 100101) {
					let data = _resp['data'];
					if (utils.isFunction(callback)) {
						callback(data);
					}
				} else {
					layer.msg('获取系统配置失败，请联系管理员', {offset: 'b'});
				}
			}
		});
	}

	function showPrompt() {
		layer.prompt({
			title: '起一个响亮的名字吧',
			formType: 3,
			btn: '确认',
			closeBtn: 0,
			success: function ($dom) {
				$dom.find('input.layui-layer-input').on('keydown', function (e) {
					if (e.which === 13) {
						verifyName($(this).val());
						layer.closeAll();
					}
				});
			}
		}, function (name, index) {
			if (utils.isEmpty(name)) {
				return;
			}
			layer.close(index);
			verifyName(name);
		});
	}

	function verifyName(name) {
		$.ajax({
			url: '/chat/verifyName',
			data: {name: encp.encrypt(name)},
			dataType: 'json',
			success: function (_resp) {
				if (_resp['flag'] === 100101) {
					utils.fireEvent('nameConfirmed', name);
				} else {
					showPrompt();
					layer.msg(_resp['message'], {offset: 'b'});
				}
			}
		});
	}
});