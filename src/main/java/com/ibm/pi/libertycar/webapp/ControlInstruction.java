package com.ibm.pi.libertycar.webapp;

public class ControlInstruction {

	@Override
	public String toString() {
		return "ControlInstruction [throttle=" + throttle + ", turning=" + turning + ", id=" + id + ", msggrp=" + msggrp
				+ "]";
	}
	private Long throttle;
	private Long turning;
	private String id;
	private String msggrp;
	
	public Long getThrottle() {
		return throttle;
	}
	public void setThrottle(Long throttle) {
		this.throttle = throttle;
	}
	public Long getTurning() {
		return turning;
	}
	public void setTurning(Long turning) {
		this.turning = turning;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMsggrp() {
		return msggrp;
	}
	public void setMsggrp(String msggrp) {
		this.msggrp = msggrp;
	}
	
}
