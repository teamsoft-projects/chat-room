package com.teamsoft.chatroom.runner;

import com.teamsoft.chatroom.common.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Spring容器加载完成事件侦听
 * @author zhangcc
 * @version 2017/9/1
 */
@Component
public class InitializedRunner implements ApplicationRunner {
	@Value("${websocket.auth:}")
	private String auth;

	@Override
	public void run(ApplicationArguments args) {
		if (StringUtils.hasLength(auth)) {
			EncryptUtil.updateAuth(auth);
		}
	}
}