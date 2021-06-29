package com.netty.msg;

import java.io.Serializable;

public class RedisMsg<K, V> implements Serializable {

	private Object key;

	private String type;

	private Integer millisecond;

	private Object data;

	public RedisMsg() {
	}

	public RedisMsg(String key, String type, Integer millisecond, Object data) {
		this.key = key;
		this.type = type;
		this.millisecond = millisecond;
		this.data = data;
	}

	public Object getKey() {
		return key;
	}

	public void setKey(Object key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getMillisecond() {
		return millisecond;
	}

	public void setMillisecond(Integer millisecond) {
		this.millisecond = millisecond;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
