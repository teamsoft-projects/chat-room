package com.teamsoft.chatroom.common.core;

import com.teamsoft.chatroom.chat.model.ChatRoom;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Constants {
	public interface System {
		// 成功标识符
		Integer SUCCESS_FLAG = 100101;
		// 失败标识符
		Integer FAILURE_FLAG = 100102;
		// 无登陆权限标识符
		Integer PERMISSION_DENIED_FLAG = 100103;
		// 提示信息标识符
		Integer BUSSINESS_NOTICE_FLAG = 100303;
		// 提示信息+重定向标识符
		Integer NOTICE_AND_REDIRECT_FLAG = 100305;
		// 提示信息, 抛出到前台, 由回调函数自行处理
		Integer SELF_NOTICE_FLAG = 100306;
		// 重定向标识符
		Integer DO_REDIRECT_FLAG = 100405;
		// Json视图标识符
		Integer JSON_VIEW_FLAG = 100600;
		// Json业务消息标识符
		Integer JSON_NOTICE_FLAG = 100601;
		// 参数错误消息标识符
		Integer PARAMETER_ERROR_FLAG = 100701;
		// 是否-是
		Integer YES_FLAG_YES = 1;

		// 请求成功消息
		String SUCCESS_MESSAGE = "success";
		//请求失败消息
		String FAILURE_MESSAGE = "fail";
		// 无访问权限消息
		String PERMISSIONDENIED_MESSAGE = "permission denied";
		// 参数错误提示
		String PARAM_ERROR = "param error";

		DateFormat FMT_NORMAL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 聊天相关常量
	 */
	public interface Chat {
		List<ChatRoom> CHAT_ROOM_LIST = Collections.synchronizedList(new ArrayList<>());
		Integer MAX_ROOM_NO = 5_000;
	}
}