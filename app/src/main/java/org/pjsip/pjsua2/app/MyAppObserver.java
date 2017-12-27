package org.pjsip.pjsua2.app;

import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnInstantMessageStatusParam;
import org.pjsip.pjsua2.pjsip_status_code;

public interface MyAppObserver{
	/**
	 *  通知注册状态
	 * @param code
	 * @param reason
	 * @param expiration
	 */
	abstract void notifyRegState(pjsip_status_code code, String reason,int expiration);
	/**
	 *  通知来电
	 * @param call
	 */
	abstract void notifyIncomingCall(MyCall call);
	/**
	 *  通知来电状态
	 * @param call
	 */
	abstract void notifyCallState(MyCall call);
	/**
	 *  通知来电音视频状态
	 * @param call
	 */
	abstract void notifyCallMediaState(MyCall call);
	/**
	 * 
	 * @param buddy
	 */
	abstract void notifyBuddyState(MyBuddy buddy);
	/**
	 *  通知收到远程消息
	 * @param prm
	 */
	abstract void notifyInstantMessage(OnInstantMessageParam prm);
	/**
	 *  通知收到远程消息
	 * @param prm
	 */
	abstract void notifyInstantMessageStatus(OnInstantMessageStatusParam prm);
}
