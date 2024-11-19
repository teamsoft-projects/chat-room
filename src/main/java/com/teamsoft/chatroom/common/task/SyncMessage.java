package com.teamsoft.chatroom.common.task;

import com.teamsoft.chatroom.chat.model.Message;
import com.teamsoft.chatroom.common.core.ChatServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Configuration
@EnableScheduling
public class SyncMessage {
	private static final long SUB_TIME = 24 * 60 * 60 * 1000;

	/**
	 * 定时清楚旧的聊天记录
	 * 每天早上6点开始，清除24小时之前的数据
	 */
	@Scheduled(cron = "0 0 6 * * ?")
	public void syncMessage() {
		List<Message> messageList = ChatServer.getHistoryMessage();
		Iterator<Message> iter = messageList.iterator();
		long now = new Date().getTime();
		while (iter.hasNext()) {
			Message message = iter.next();
			long time = message.getTime().getTime();
			if (now - time >= SUB_TIME) {
				iter.remove();
			} else {
				break;
			}
		}
	}
}