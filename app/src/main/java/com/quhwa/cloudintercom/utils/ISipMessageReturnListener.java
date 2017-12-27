package com.quhwa.cloudintercom.utils;

public interface ISipMessageReturnListener {
	/**sip消息返回成功*/
	void sipMessageReturnSuccess();
	/**sip消息返回失败*/
	void sipMessageReturnFail();
}
