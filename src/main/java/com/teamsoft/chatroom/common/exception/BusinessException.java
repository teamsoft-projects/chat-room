package com.teamsoft.chatroom.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常
 * @author zhangcc
 * @since 2020/7/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
	private Integer status;
	private String message;

	public BusinessException() {
		super();
		this.status = 500;
		this.message = "处理失败";
	}

	public BusinessException(String message) {
		super(message);
		this.status = 500;
		this.message = message;
	}

	public BusinessException(Integer status, String message) {
		super(message);
		this.status = status;
		this.message = message;
	}
}