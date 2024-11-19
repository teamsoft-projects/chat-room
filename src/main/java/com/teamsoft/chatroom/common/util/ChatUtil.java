package com.teamsoft.chatroom.common.util;

import com.teamsoft.chatroom.chat.model.ChatRoom;
import com.teamsoft.chatroom.common.exception.BusinessException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.teamsoft.chatroom.common.core.Constants.Chat.CHAT_ROOM_LIST;
import static com.teamsoft.chatroom.common.core.Constants.Chat.MAX_ROOM_NO;

/**
 * 聊天相关工具类
 */
public class ChatUtil {
	public synchronized static Integer generateRoomNo() {
		List<Integer> existsRoomNos = CHAT_ROOM_LIST.stream()
				.map(ChatRoom::getRoomNo)
				.collect(Collectors.toList());
		if (existsRoomNos.size() >= MAX_ROOM_NO) {
			throw new BusinessException("已超出最大聊天室数量");
		}
		int[] availableNos = IntStream.rangeClosed(1000, MAX_ROOM_NO)
				.filter(i -> !existsRoomNos.contains(i))
				.toArray();
		if (availableNos.length == 0) {
			throw new BusinessException("已超出最大聊天室数量");
		}
		return availableNos[(int) (Math.random() * availableNos.length)];
	}
}