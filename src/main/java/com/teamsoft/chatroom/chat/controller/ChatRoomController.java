package com.teamsoft.chatroom.chat.controller;

import com.teamsoft.chatroom.chat.model.ChatRoom;
import com.teamsoft.chatroom.common.core.ChatServer;
import com.teamsoft.chatroom.common.core.ResultInfo;
import com.teamsoft.chatroom.common.exception.BusinessException;
import com.teamsoft.chatroom.common.util.ChatUtil;
import com.teamsoft.chatroom.common.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.teamsoft.chatroom.common.core.Constants.Chat.CHAT_ROOM_LIST;
import static com.teamsoft.chatroom.common.core.Constants.System.YES_FLAG_YES;

/**
 * 聊天室控制类
 */
@Slf4j
@RestController
@RequestMapping("chatroom")
public class ChatRoomController {
	@PostMapping("addChatRoom")
	public ResultInfo addChatRoom(String roomName, String passwd) {
		boolean isRoomNameExists = CHAT_ROOM_LIST.stream().anyMatch(i -> roomName.equals(i.getRoomName()));
		if (isRoomNameExists) {
			return ResultInfo.getFailureInfo("聊天室名称已存在");
		}
		Integer roomNo = ChatUtil.generateRoomNo();
		ChatRoom room = new ChatRoom();
		room.setRoomNo(roomNo);
		room.setRoomName(roomName);
		if (StringUtils.hasLength(passwd)) {
			room.setHasPassword(YES_FLAG_YES);
			room.setPasswd(passwd);
		}
		room.setCreateTime(LocalDateTime.now());
		CHAT_ROOM_LIST.add(room);
		return ResultInfo.getSuccessInfo(roomNo);
	}

	@RequestMapping("getChatRoomList")
	public ResultInfo getChatRoomList() {
		List<ChatRoom> rooms = CHAT_ROOM_LIST
				.stream().peek(i -> i.setUserCount(i.getUsers().size()))
				.collect(Collectors.toList());
		return ResultInfo.getSuccessInfo(rooms);
	}

	@PostMapping("joinChatRoom")
	public ResultInfo joinChatRoom(Integer roomNo, String userName) {
		ChatRoom room = CHAT_ROOM_LIST.stream()
				.filter(i -> roomNo.equals(i.getRoomNo()))
				.findFirst()
				.orElseThrow(() -> new BusinessException("聊天室不存在"));
		ChatServer.joinChatRoom(roomNo, userName);
		return ResultInfo.getSuccessInfo(room);
	}
}