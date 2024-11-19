+function () {
	let userName = '';
	let $chatOuter = $('#chat-outer');
	let $chatShow = $('#chat-show');
	let $topContainer = $('#topContainer');
	let $send = $('#send');
	let $write = $('#write');
	let websocketURL = $('#websocket-url').val();
	let notifyIndex;
	// 是否停止滚动
	let isStopScroll = false;

	layer.ready(function () {
		$.ajax({
			url: '/getServerConfig',
			dataType: 'json',
			success: function (_resp) {
				if (_resp['flag'] === 100101) {
					let data = _resp['data'];
					if (data.isStopAuth !== true) {
						verifyAuth(function () {
							checkName();
						});
					} else {
						checkName();
					}
				} else {
					layer.msg('获取系统配置失败，请联系管理员');
				}
			}
		});


	});

	// 添加验证
	function verifyAuth(callback) {
		layer.prompt({
			title: '请输入暗号',
			formType: 3,
			btn: '确定',
			closeBtn: 0
		}, function (text, index) {
			if (utils.isEmpty(text)) {
				return;
			}
			$.ajax({
				url: '/chat/verifyAuth',
				data: {auth: encp.encrypt(text)},
				dataType: 'json',
				success: function (_resp) {
					if (_resp['flag'] === 100101) {
						layer.close(index);
						callback();
					} else {
						layer.msg('验证失败');
					}
				}
			});
		});
	}

	function checkName() {
		let oldName = localStorage.getItem('chat_login_name');
		layer.prompt({
			title: '你的名字',
			formType: 3,
			btn: '确认',
			closeBtn: 0,
			value: oldName,
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
			verifyName(name, index);
		});
	}

	function verifyName(name, index) {
		$.ajax({
			url: '/chat/verifyName',
			data: {name: encp.encrypt(name)},
			dataType: 'json',
			success: function (_resp) {
				if (_resp['flag'] === 100101) {
					userName = name;
					localStorage.setItem("chat_login_name", name);
					initPage();
					layer.close(index);
				} else {
					layer.msg('名字已存在，换一个呢');
				}
			}
		});
	}

	// 初始化页面
	function initPage() {
		$topContainer.show();
		// 滚动到最下
		scrollToBottom();
		// 获取历史消息
		getHistory();
	}

	function getHistory() {
		$.ajax({
			url: '/chat/getHistory',
			dataType: 'json',
			success: function (_resp) {
				let data = _resp['data'];
				if (utils.isEmpty(data)) {
					return;
				}
				data = JSON.parse(encp.decrypt(data));
				let html = '';
				$.each(data, function (key, val) {
					if (val['from'] === userName) {
						html += ' <div class="bubble me">' + val['msg'] + '</div>';
					} else if (val['from'] === 'system') {
						html += '<div class="conversation-start"><span>' + val['msg'] + '</span></div>';
					} else {
						html += '<div class="sender">' + val['from'] + '</div>'
						html += ' <div class="bubble you">' + val['msg'] + '</div>';
					}
				});
				$chatShow.append(html);
				scrollToBottom();
				createWebSocket();
			}
		});
	}

	// 建立websocket
	function createWebSocket() {
		let url = websocketURL + userName;
		let socket = new WebSocket(url);
		// 打开事件
		socket.onopen = function () {
			console.log("websocket已打开");
		};
		// 获得消息事件
		socket.onmessage = function (msg) {
			recieved(msg.data);
		};
		// 关闭事件
		socket.onclose = function () {
			console.log("websocket已关闭");
		};
		// 发生了错误事件
		socket.onerror = function () {
			layer.msg('浏览器不支持websocket');
			console.log("websocket发生了错误");
		}

		$send.click(function () {
			let write = $write.val();
			if (utils.isEmpty(write)) {
				return;
			}
			let msg = {
				from: userName,
				msg: write
			};
			let message = encp.encrypt(JSON.stringify(msg));
			socket.send(message);
			$write.val('');
			scrollToBottom(true);
		});

		$write.keypress(function (e) {
			if (e.keyCode === 13) {
				$send.click();
			}
			clearNotify();
		});

		$write.click(function () {
			clearNotify();
		});

		$write.blur(function () {
			clearNotify();
		});

		$chatOuter.scroll(function () {
			let scrollTop = $(this).scrollTop();
			let windowHeight = $(this).height();
			let scrollHeight = $chatShow.height();
			isStopScroll = scrollTop + windowHeight < scrollHeight + 40;
		});
	}

	let lastRecieved;

	function recieved(msg) {
		let now = new Date().getTime() / 1000;
		let html = '';
		if (lastRecieved === undefined || now - lastRecieved > 5 * 60) {
			let nowTime = new Date();
			let hour = nowTime.getHours();
			if (hour < 10) {
				hour = '0' + hour;
			}
			let minutes = nowTime.getMinutes();
			if (minutes < 10) {
				minutes = '0' + minutes;
			}
			let time = hour + ":" + minutes;
			html += '<div class="conversation-start"><span>' + time + '</span></div>';
		}
		lastRecieved = now;

		let message = encp.decrypt(msg);
		let data = JSON.parse(message);
		let isNewMessage = false;
		if (data['from'] === userName) {
			html += ' <div class="bubble me">' + data['msg'] + '</div>';
		} else if (data['from'] === 'system') {
			html += '<div class="conversation-start"><span>' + data['msg'] + '</span></div>';
		} else {
			html += '<div class="sender">' + data['from'] + '</div>'
			html += ' <div class="bubble you">' + data['msg'] + '</div>';
			isNewMessage = true;
		}
		if (isNewMessage) {
			messageNotify();
		}
		$chatShow.append(html);
		scrollToBottom();
	}

	function scrollToBottom(isForceScroll) {
		if (isStopScroll && !isForceScroll) {
			return;
		}
		$chatOuter.animate({
			scrollTop: $chatOuter[0].scrollHeight
		}, 300);
	}

	let $title = $('title');
	let titleOrigin = '谈天说地 - 聊天室';
	let isNotifying = false;

	function messageNotify() {
		if (isNotifying) {
			return;
		}
		isNotifying = true;
		notifyIndex = setInterval(function () {
			let title = $title.html();
			if (title.substr(0, 13) === '[NEW MESSAGE]') {
				$title.html('[&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;]' + titleOrigin);
			} else {
				$title.html('[新消息]' + titleOrigin);
			}
		}, 500);
	}

	function clearNotify() {
		if (!isNotifying) {
			return;
		}
		clearInterval(notifyIndex);
		$title.html(titleOrigin);
		isNotifying = false;
	}
}();