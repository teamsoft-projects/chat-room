package com.teamsoft.chatroom.chat.model;

import lombok.*;

import java.util.Date;

/**
 * 消息实体类
 * @author alex
 * @version 2020/6/19
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Message {
	@NonNull
	private String from;
	@NonNull
	private String msg;
	private Date time;

	public static Message create(String msg) {
		return new Message("system", msg);
	}

	public static Message create(String from, String msg) {
		return new Message(from, msg);
	}
}