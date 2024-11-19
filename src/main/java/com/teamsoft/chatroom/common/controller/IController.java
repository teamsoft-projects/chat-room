package com.teamsoft.chatroom.common.controller;

import com.teamsoft.chatroom.common.core.ResultInfo;
import com.teamsoft.chatroom.common.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共控制类
 * @author alex
 * @version 2020/6/18
 */
@Controller
public class IController {
	@Value("${websocket.host}")
	private String websocketHost;
	@Value("${server.port}")
	private String serverPort;

	@RequestMapping("index")
	public ResultInfo index() {
		String webScocktAddress = "ws://" + websocketHost + ":" + serverPort + "/chat/";
		return ResultInfo.getSuccessInfo(webScocktAddress);
	}

	@RequestMapping("chat")
	public ResultInfo chat() {
		Map<String, Object> retMap = new HashMap<>();
		String webScocktAddress = "ws://" + websocketHost + ":" + serverPort + "/chat/";
		retMap.put("url", webScocktAddress);
		return ResultInfo.getSuccessInfo(retMap);
	}

	@RequestMapping("getServerConfig")
	@ResponseBody
	public ResultInfo getServerConfig() {
		Map<String, Object> ret = new HashMap<>();
		String webScocktAddress = "ws://" + websocketHost + ":" + serverPort + "/chat/";
		ret.put("webScocktAddress", webScocktAddress);
		ret.put("isStopAuth", true);
		return ResultInfo.getSuccessInfo(ret);
	}
}