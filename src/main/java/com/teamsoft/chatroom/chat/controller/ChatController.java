package com.teamsoft.chatroom.chat.controller;

import com.alibaba.fastjson.JSONObject;
import com.teamsoft.chatroom.chat.model.Message;
import com.teamsoft.chatroom.common.core.ChatServer;
import com.teamsoft.chatroom.common.core.ResultInfo;
import com.teamsoft.chatroom.common.util.EncryptUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天控制类
 * @author alex
 * @version 2020/6/19
 */
@RestController
@RequestMapping("chat")
public class ChatController {
	@RequestMapping("verifyAuth")
	public ResultInfo verifyAuth(String auth) {
		if (!StringUtils.hasLength(auth)) {
			return ResultInfo.getFailureInfo();
		}
		auth = EncryptUtil.decrypt(auth);
		if (!EncryptUtil.verifyAuth(auth)) {
			return ResultInfo.getFailureInfo();
		}
		return ResultInfo.getSuccessInfo();
	}

	@RequestMapping("verifyName")
	public ResultInfo verifyName(String name) {
		if (!StringUtils.hasLength(name)) {
			return ResultInfo.getFailureInfo();
		}
		name = EncryptUtil.decrypt(name);
		if (ChatServer.isUserExists(name)) {
			return ResultInfo.getFailureInfo("名字已存在，换一个呢");
		}
		assert name != null;
		if (name.length() > 10) {
			return ResultInfo.getFailureInfo("名字太长");
		}
		return ResultInfo.getSuccessInfo();
	}

	@RequestMapping("sendMessage")
	public ResultInfo sendMessage(Integer roomNo, String msg) {
		Message message = Message.create(msg);
		msg = JSONObject.toJSONString(message);
		ChatServer.sendToRoom(roomNo, EncryptUtil.encrypt(msg));
		return ResultInfo.getSuccessInfo();
	}

	@RequestMapping("getHistory")
	public ResultInfo getHistory() {
		List<Message> history = ChatServer.getHistoryMessage();
		String historyStr = JSONObject.toJSONString(history);
		historyStr = EncryptUtil.encrypt(historyStr);
		return ResultInfo.getSuccessInfo(historyStr);
	}

	@RequestMapping("getAuth")
	public ResultInfo getAuth() {
		String auth = EncryptUtil.getAuth();
		auth = EncryptUtil.encrypt(auth);
		return ResultInfo.getSuccessInfo(auth);
	}

	@RequestMapping("changeAuth")
	public ResultInfo changeAuth(String auth) {
		if (!StringUtils.hasLength(auth)) {
			return ResultInfo.getParamErrorInfo();
		}
		EncryptUtil.updateAuth(auth);
		return ResultInfo.getSuccessInfo(auth);
	}

	@RequestMapping("stopAuth")
	public ResultInfo stopAuth() {
		EncryptUtil.stopAuth();
		return ResultInfo.getSuccessInfo();
	}

	@RequestMapping("getOnlineUsers")
	public ResultInfo getOnlineUsers() {
		return ResultInfo.getSuccessInfo(ChatServer.getOnlineUser());
	}
}