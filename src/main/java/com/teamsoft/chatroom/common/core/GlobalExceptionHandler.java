package com.teamsoft.chatroom.common.core;

import com.teamsoft.chatroom.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * @author zhangcc
 * @since 2020/7/23
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	/**
	 * 业务异常
	 */
	@ExceptionHandler(BusinessException.class)
	public ResultInfo handleBusinessException(BusinessException e) {
		return ResultInfo.getFailureInfo(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResultInfo handler(Exception ex) {
		log.error("其他异常: ", ex);
		return ResultInfo.getFailureInfo("系统错误");
	}

}