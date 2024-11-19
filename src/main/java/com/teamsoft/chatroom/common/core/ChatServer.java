package com.teamsoft.chatroom.common.core;

import com.alibaba.fastjson.JSONObject;
import com.teamsoft.chatroom.chat.model.Message;
import com.teamsoft.chatroom.chat.model.User;
import com.teamsoft.chatroom.common.exception.BusinessException;
import com.teamsoft.chatroom.common.util.EncryptUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.teamsoft.chatroom.common.core.Constants.System.FMT_NORMAL;

/**
 * @author alex
 * @version 2020/6/18
 */
@Slf4j
@Component
@ServerEndpoint("/chat/{userName}")
public class ChatServer {
	/**
	 * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	 */
	private static int onlineCount = 0;
	/**
	 * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
	 */
	private static final ConcurrentHashMap<String, ChatServer> ONLINE_USER_MAP = new ConcurrentHashMap<>();
	/**
	 * 与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	private Session session;
	/**
	 * 接收userName
	 */
	@Setter
	@Getter
	private String userName = "";
	/**
	 * 上线时间
	 */
	@Setter
	@Getter
	private String onlineTime;
	/**
	 * 历史消息缓存
	 */
	private static final List<Message> MESSAGE_LIST = Collections.synchronizedList(new ArrayList<>());
	/**
	 * 房间号
	 */
	private Integer roomNo;

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session, @PathParam("userName") String userName) {
		this.session = session;
		this.onlineTime = FMT_NORMAL.format(new Date());
		this.userName = userName;
		if (ONLINE_USER_MAP.containsKey(userName)) {
			ONLINE_USER_MAP.remove(userName);
			ONLINE_USER_MAP.put(userName, this);
			// 加入set中
		} else {
			ONLINE_USER_MAP.put(userName, this);
			// 加入set中
			addOnlineCount();
			// 在线数加1
		}
		log.info("用户上线:{},当前在线人数为:{}", userName, getOnlineCount());
		Message message = Message.create("用户 " + userName + " 已连接, 在线: " + getOnlineCount());
		String msg = JSONObject.toJSONString(message);
		sendToAll(EncryptUtil.encrypt(msg));
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		if (ONLINE_USER_MAP.containsKey(userName)) {
			ONLINE_USER_MAP.remove(userName);
			//从set中删除
			subOnlineCount();
		}
		log.info("用户退出:{}, 当前在线人数为:{}", userName, getOnlineCount());
		Message message = Message.create("用户 " + userName + " 已退出, 在线: " + getOnlineCount());
		String msg = JSONObject.toJSONString(message);
		sendToAll(EncryptUtil.encrypt(msg));
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message) {
		String plainText = EncryptUtil.decrypt(message);
		Message msg = JSONObject.parseObject(plainText, Message.class);
		assert msg != null;
		msg.setTime(new Date());
		MESSAGE_LIST.add(msg);
		sendToAll(message);
	}

	@OnError
	public void onError(Session session, Throwable error) {
		try {
			if (session.isOpen()) {
				session.close();
			}
		} catch (IOException ignored) {
		}
		log.error("用户错误:{},原因:{}", userName, error.getMessage());
	}

	/**
	 * 发送消息到所有人
	 * @param message 消息
	 */
	public static void sendToAll(String message) {
		ONLINE_USER_MAP.values().forEach(t -> {
			try {
				t.session.getBasicRemote().sendText(message);
			} catch (IOException ignored) {
			}
		});
	}

	public static void sendToRoom(Integer roomNo, String message) {
		ONLINE_USER_MAP.values().stream()
				.filter(t -> t.roomNo.equals(roomNo))
				.forEach(t -> {
					try {
						t.session.getBasicRemote().sendText(message);
					} catch (IOException ignored) {
					}
				});
	}

	public static void joinChatRoom(Integer roomNo, String userName) {
		ChatServer userServer = ONLINE_USER_MAP.get(userName);
		if (userServer == null) {
			throw new BusinessException("用户不在线");
		}
		userServer.roomNo = roomNo;
		Message message = Message.create("用户 " + userName + " 进入房间, 当前房间人数: " + getRoomOnlineCount(roomNo));
		String msg = JSONObject.toJSONString(message);
		sendToRoom(roomNo, EncryptUtil.encrypt(msg));
	}

	public static boolean isUserExists(String userName) {
		return ONLINE_USER_MAP.keySet().stream().anyMatch(t -> t.equals(userName));
	}

	public static List<User> getOnlineUser() {
		List<ChatServer> chatServers = new ArrayList<>(ONLINE_USER_MAP.values());
		return chatServers.stream().sorted(Comparator.comparing(ChatServer::getOnlineTime)).map(t -> {
			User u = new User();
			u.setName(t.getUserName());
			u.setOnlineTime(t.getOnlineTime());
			return u;
		}).collect(Collectors.toList());
	}

	public static List<Message> getHistoryMessage() {
		return MESSAGE_LIST;
	}

	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized int getRoomOnlineCount(Integer roomNo) {
		return Long.valueOf(ONLINE_USER_MAP.values().stream()
				.filter(i -> i.roomNo.equals(roomNo))
				.count()).intValue();
	}

	public static synchronized void addOnlineCount() {
		ChatServer.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		ChatServer.onlineCount--;
	}

}