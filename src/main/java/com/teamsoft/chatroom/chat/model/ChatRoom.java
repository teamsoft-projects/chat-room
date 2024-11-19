package com.teamsoft.chatroom.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 聊天室实体类
 */
@Data
public class ChatRoom {
	// 聊天室编号
	private Integer roomNo;
	// 聊天室名称
	private String roomName;
	// 聊天室用户列表
	private List<User> users = new ArrayList<>();
	// 是否有密码
	private Integer hasPassword;
	// 密码
	@JsonIgnore
	private String passwd;
	// 创建时间
	private LocalDateTime createTime;
	// 聊天室用户数量
	private Integer userCount = 0;
}