package com.quhwa.cloudintercom.bean;
/**
 * 发送消息返回结果
 *
 * @author lxz
 * @date 2017年4月12日
 */
public class ACK {
	/**
	 * ack:0为成功，1为失败
	 */
	private String ack;
	

	public String getAck() {
		return ack;
	}

	public void setAck(String ack) {
		this.ack = ack;
	}

	@Override
	public String toString() {
		return "ACK [ack=" + ack + "]";
	}
	
}
