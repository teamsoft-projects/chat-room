package com.teamsoft.chatroom.chat.model;

import lombok.Data;

/**
 * 在线用户
 * @author alex
 * @version 2020/6/23
 */
@Data
public class User {
	private String name;
	private String onlineTime;
}