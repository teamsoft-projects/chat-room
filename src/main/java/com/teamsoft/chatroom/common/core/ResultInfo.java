package com.teamsoft.chatroom.common.core;

import lombok.Data;

/**
 * 返回到接口的数据结构
 * @author zhangcc
 * @version 2017/9/6
 */
@Data
public class ResultInfo {
	// 回传状态位
	private Integer flag;
	// 回传消息
	private String message;
	// 回传数据
	private Object data;

	private ResultInfo(Integer flag, String message, Object data) {
		this.flag = flag;
		this.message = message;
		this.data = data;
	}

	private ResultInfo(Integer flag, String message) {
		this.flag = flag;
		this.message = message;
	}

	public static ResultInfo getSuccessInfo() {
		return new ResultInfo(Constants.System.SUCCESS_FLAG, Constants.System.SUCCESS_MESSAGE, null);
	}

	public static ResultInfo getSuccessInfo(Object data) {
		return new ResultInfo(Constants.System.SUCCESS_FLAG, Constants.System.SUCCESS_MESSAGE, data);
	}

	public static ResultInfo getSuccessInfo(ResultInfo resultInfo) {
		return new ResultInfo(resultInfo.getFlag(), resultInfo.getMessage(), resultInfo.getData());
	}

	public static ResultInfo getSuccessInfo(Integer flag, String message, Object data) {
		return new ResultInfo(flag, message, data);
	}

	public static ResultInfo getFailureInfo() {
		return new ResultInfo(Constants.System.FAILURE_FLAG, Constants.System.FAILURE_MESSAGE);
	}

	public static ResultInfo getFailureInfo(String message) {
		return new ResultInfo(Constants.System.FAILURE_FLAG, message);
	}

	public static ResultInfo getParamErrorInfo() {
		return new ResultInfo(Constants.System.PARAMETER_ERROR_FLAG, Constants.System.PARAM_ERROR);
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}