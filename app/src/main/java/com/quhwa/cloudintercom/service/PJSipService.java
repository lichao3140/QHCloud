package com.quhwa.cloudintercom.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.activity.CallActivity;
import com.quhwa.cloudintercom.activity.MonitorActivity;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.TextMsg;
import com.quhwa.cloudintercom.utils.Constants;
import com.quhwa.cloudintercom.utils.JsonParserManager;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.utils.PhoneManager;
import com.quhwa.cloudintercom.utils.PowerAndScreenManager;
import com.quhwa.cloudintercom.utils.ProgressUtil;
import com.quhwa.cloudintercom.utils.ReceiverAction;
import com.quhwa.cloudintercom.utils.SipMsgType;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.AuthCredInfoVector;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnInstantMessageStatusParam;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.app.MSG_TYPE;
import org.pjsip.pjsua2.app.MyAccount;
import org.pjsip.pjsua2.app.MyApp;
import org.pjsip.pjsua2.app.MyAppObserver;
import org.pjsip.pjsua2.app.MyBuddy;
import org.pjsip.pjsua2.app.MyCall;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_status_code;

import java.io.UnsupportedEncodingException;

import cloudintercom.quhwa.com.qhcloud.IServiceAidl;

/**
 * 负责pjsip的初始化，监听来电状态，消息收发
 *
 * @author lxz
 * @date 2017年4月15日
 */
public class PJSipService extends Service implements MyAppObserver, Handler.Callback {
    private static String Tag = "PJSipService";
    public static MyApp app = null;
    public static MyCall currentCall = null;
    public static MyAccount account = null;
    public static AccountConfig accCfg = null;
    public static CallInfo callInfo = null;
    public static PJSipService instance;
    private static AccountInfo accountInfo;
    private final Handler handler = new Handler(this);
    /**
     * 当前消息类型：表示用户给室内机发送各种消息如绑定，开锁等，每种消息返回要处理的操作不同，因为接受消息在同一个回调方法，故用这个类型加以区别
     */
    public static int currentMsgType = 0;
//	/**当前呼叫类型，默认为监控*/
//	public static int CURRENT_CALL_TYPE = 0;
    /**
     * 呼叫类型：监控
     */
    public static int CALL_TYPE_MONITOR = 0;
    /**
     * 呼叫类型：户户通
     */
    public static int CALL_TYPE_DOOR_TO_DOOR = 1;


    private PJSipServiceConnection pjsipServiceCon;
    private PJSipServiceBinder pjsipServiceBinder;

    @Override
    public IBinder onBind(Intent intent) {
        return pjsipServiceBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("------------------onStartCommand");
        registerToSipServerAccordingToUserIsLoginOrNot(MySharedPreferenceManager.queryBoolean(this, Table.TAB_STATUS, Table.TAB_LOGIN_STATUS_KEY));
        //启动远程进程
        ProgressUtil.startForeground(this, 0);
        return START_STICKY;//保证服务可以在后台活着
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (pjsipServiceBinder != null) {
            pjsipServiceBinder = new PJSipServiceBinder();
        }
        pjsipServiceCon = new PJSipServiceConnection();
        System.out.println("------------------onCreate");
        instance = this;
        initPJSip();

    }

    /**
     * 如果处于登陆状态再注册到sip服务器，否则不注册到sip服务器
     *
     * @param islogin true为登陆状态，false为未登陆状态
     */
    public void registerToSipServerAccordingToUserIsLoginOrNot(boolean islogin) {
        if (islogin) {
            //登陆状态
            registerToSipServer(MySharedPreferenceManager.queryString(this, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_ID),
                    MySharedPreferenceManager.queryString(this, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_PASSWORD));
            MyLog.print(Tag, "处于登陆状态时注册到sip服务器", MyLog.PRINT_RED);
        } else {
            //未登陆
            MyLog.print(Tag, "账号未登陆，未注册到sip服务器", MyLog.PRINT_RED);
        }
    }

    /**
     * 初始化pjsip
     */
    @SuppressWarnings("unused")
    public void initPJSip() {
        if (app == null) {
            app = new MyApp();
            MyLog.print(Tag, "pjsip初始化", MyLog.PRINT_RED);
            app.init(this, getFilesDir().getAbsolutePath());
        }

        if (app.accList.size() == 0) {
            MyLog.print(Tag, "account列表:" + app.accList.size(), MyLog.PRINT_RED);
            accCfg = new AccountConfig();
            accCfg.setIdUri("sip:localhost");
            accCfg.getNatConfig().setIceEnabled(true);
            accCfg.getVideoConfig().setAutoTransmitOutgoing(true);
            accCfg.getVideoConfig().setAutoShowIncoming(true);
            accCfg.getRegConfig().setTimeoutSec(10L);
            accCfg.getRegConfig().setRetryIntervalSec(120L);
            //account = app.addAcc(accCfg);
        } else {
            MyLog.print(Tag, "account列表:" + app.accList.size(), MyLog.PRINT_RED);
            account = app.accList.get(0);
            accCfg = account.cfg;
        }
    }

    /**
     * 注册到服务器
     *
     * @param sipId  sip账号
     * @param sipPSW sip密码
     */
    public static void registerToSipServer(String sipId, String sipPSW) {
        if (sipId != null && sipPSW != null) {
            String serverIP = Constants.SERVER_IP;
            String transport = "udp";
            accCfg = new AccountConfig();
            String acc_id = "sip:" + sipId + "@" + serverIP;
            String registrar = "sip:" + serverIP;
            String proxy = "sip:" + sipId + "@" + serverIP + ";" + "transport=" + transport;
            String username = sipId;
            String password = sipPSW;

            accCfg.setIdUri(acc_id);
            accCfg.getRegConfig().setTimeoutSec(10L);
            accCfg.getRegConfig().setRetryIntervalSec(120L);
            accCfg.getRegConfig().setRegistrarUri(registrar);
            accCfg.getVideoConfig().setAutoTransmitOutgoing(true);
            accCfg.getVideoConfig().setAutoShowIncoming(true);
//			accCfg.getVideoConfig().setDefaultCaptureDevice(1);

            AuthCredInfoVector creds = accCfg.getSipConfig().getAuthCreds();
            creds.clear();
            if (username.length() != 0) {
                creds.add(new AuthCredInfo("Digest", "*", username, 0, password));
            }
            StringVector proxies = accCfg.getSipConfig().getProxies();
            proxies.clear();
            if (proxy.length() != 0) {
                proxies.add(proxy);
            }

			/* Enable ICE */
            accCfg.getNatConfig().setIceEnabled(true);

            if (app.accList.size() > 0 && PJSipService.account != null) {
//				if(PJSipService.currentCall != null){
//					PJSipService.currentCall.delete();
//				}
                PJSipService.account.delete();

            }
            account = app.addAcc(accCfg);

//			account = MyApp.addAcc(accCfg);
            /* Finally */
            try {
                if (account != null) {
                    accountInfo = account.getInfo();
                    MyLog.print(Tag, "开始注册到sip服务器", MyLog.PRINT_RED);
                    account.modify(accCfg);
//				String s = account.getInfo().getRegStatusText();
//				MyLog.print(Tag, "test:"+s, MyLog.PRINT_RED);

                }
            } catch (Exception e) {
                MyLog.print(Tag, e.getMessage(), MyLog.PRINT_RED);
            }
        }
    }

    /**
     * 拨打音视频电话
     *
     * @param sipId sip账号
     */
    public void makeCall(String sipId, int callType) {// 拨打电话
		/* Only one call at anytime */
//		if (currentCall != null) {
//			Log.e(Tag, "currentCall is not null");
//			return;
//		}
        if (accCfg != null) {
            accCfg.getVideoConfig().setAutoTransmitOutgoing(true);
            accCfg.getVideoConfig().setAutoShowIncoming(true);
        }
//		setResolution();
        MyCall call = new MyCall(account, -1);
        CallOpParam prm = new CallOpParam(true);
        prm.getOpt().setAudioCount(1);
        prm.getOpt().setVideoCount(1);
        try {
            call.makeCall(sipId, prm);
        } catch (Exception e) {
            call.delete();
            return;
        }
        currentCall = call;
        if (callType == CALL_TYPE_DOOR_TO_DOOR) {//户户通
            startToCallActivity(instance);
        } else {//监控
            MyLog.print(Tag, "监控", MyLog.PRINT_RED);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_TYPE.CALL_STATE:
                CallInfo ci = (CallInfo) msg.obj;
                MyLog.print(Tag, "Call状态:" + ci.getStateText(), MyLog.PRINT_RED);
                if (CallActivity.handler_ != null) {
                    Message m2 = Message.obtain(CallActivity.handler_, MSG_TYPE.CALL_STATE, ci);
                    m2.sendToTarget();
                }
                if (MonitorActivity.handler_ != null) {
                    Message m2 = Message.obtain(MonitorActivity.handler_, MSG_TYPE.CALL_STATE, ci);
                    m2.sendToTarget();
                }
                PhoneManager.setMicroPhone(false, MyApplication.instance, null);
                PhoneManager.setSpeakerphoneOn(true, MyApplication.instance);
                break;
            case MSG_TYPE.CALL_MEDIA_STATE:
			/* Forward the message to CallActivity */
                if (CallActivity.handler_ != null) {
                    Message m2 = Message.obtain(CallActivity.handler_, MSG_TYPE.CALL_MEDIA_STATE, null);
                    m2.sendToTarget();
                }
                if (MonitorActivity.handler_ != null) {
                    Message m2 = Message.obtain(MonitorActivity.handler_, MSG_TYPE.CALL_MEDIA_STATE, null);
                    m2.sendToTarget();
                }
                break;
            case MSG_TYPE.REG_STATE:
                String msg_str = (String) msg.obj;
                String lastRegStatus = msg_str;
                break;
            case MSG_TYPE.REG_STATE_FAIL:
//			MyLog.print(Tag, "注册失败再注册", MyLog.PRINT_RED);
//			registerToSipServer(MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_ID), 
//					MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_SIP_PASSWORD));
                break;
            case MSG_TYPE.INCOMING_CALL:
			/* Incoming call */
                final MyCall call = (MyCall) msg.obj;
                CallOpParam prm = new CallOpParam();
                try {
//				String uri = call.getInfo().getRemoteUri();
//				String doorName = "";
//				MySharedPreferenceManager.saveString(MyApplication.instance,Table.TAB_USER,Table.TAB_USER_DOOR_NAME_KEY,doorName);
                    if (call.getInfo().getRemVideoCount() == 1) {
                        MyLog.print(Tag, "视频来电", MyLog.PRINT_RED);
                    } else {
                        MyLog.print(Tag, "音频来电", MyLog.PRINT_RED);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
			/* Only one call at anytime */
                if (currentCall != null) {
				/*
				 * prm.setStatusCode(pjsip_status_code.PJSIP_SC_BUSY_HERE); try
				 * { call.hangup(prm); } catch (Exception e) {}
				 */
                    // TODO: set status code
                    call.delete();
                    return true;
                }

			/* Answer with ringing */
                prm.setStatusCode(pjsip_status_code.PJSIP_SC_RINGING);
                try {
                    call.answer(prm);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                currentCall = call;
                startToCallActivity(instance);
                break;
        }
        return true;
    }

    /**
     * 跳转到CallActivity
     *
     * @param context
     */
    private void startToCallActivity(Context context) {
        Intent intent = new Intent(context, CallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void notifyRegState(pjsip_status_code code, String reason,
                               int expiration) {
        String msg_str = "";
        if (expiration == 0) {
            msg_str += "Unregistration";
        } else {
            msg_str += "Registration";
        }

        if (code.swigValue() / 100 == 2) {
            msg_str += " successful";
//			TimerUtils.stopToSend();
        } else {
            msg_str += " failed: " + reason;
//				TimerUtils.sendSipMsgTimer(null, handler, MSG_TYPE.REG_STATE_FAIL, 5000);
        }
        MyLog.print(Tag, msg_str, MyLog.PRINT_RED);
        Message m = Message.obtain(handler, MSG_TYPE.REG_STATE, msg_str);
        m.sendToTarget();
    }

    @Override
    public void notifyIncomingCall(MyCall call) {
        Message m = Message.obtain(handler, MSG_TYPE.INCOMING_CALL, call);
        m.sendToTarget();
        PhoneManager.startPlayRingtone(instance);
        saveIncomingSip(call);
        PowerAndScreenManager.lightScreen(instance);

    }

    /**
     * 保存来电账号
     *
     * @param call
     */
    private void saveIncomingSip(MyCall call) {
        try {
            MyLog.print(Tag, call.getInfo().getRemoteUri(), MyLog.PRINT_GREEN);
            String remoteUri = call.getInfo().getRemoteUri();
            String incomingSipId = remoteUri.substring(remoteUri.indexOf(":") + 1, remoteUri.indexOf("@"));
            MyLog.print(Tag, incomingSipId, MyLog.PRINT_GREEN);
            MySharedPreferenceManager.saveString(instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_INCOMING_SIP_ID_key, incomingSipId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void notifyCallState(MyCall call) {
        if (currentCall == null || call.getId() != currentCall.getId())
            return;

        CallInfo ci;

        try {
//			MyLog.print(Tag, "---size:"+call.vidWin.getInfo().getSize(), MyLog.PRINT_RED);
            ci = call.getInfo();
        } catch (Exception e) {
            ci = null;
        }

        Message m = Message.obtain(handler, MSG_TYPE.CALL_STATE, ci);
        m.sendToTarget();

        if (ci != null
                && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
            currentCall = null;
        }

    }

    @Override
    public void notifyCallMediaState(MyCall call) {
        Message m = Message.obtain(handler, MSG_TYPE.CALL_MEDIA_STATE, null);
        m.sendToTarget();
    }

    @Override
    public void notifyBuddyState(MyBuddy buddy) {
    }

    @Override
    public void notifyInstantMessageStatus(OnInstantMessageStatusParam prm) {//app发送消息sip自动返回状态码
        System.out.println("======== InstantMessage 消息 ======== ");
        try {
            System.out.println("To       : " + prm.getToUri());
            System.out.println("Reason   : " + prm.getReason());
            System.out.println("Code     : " + prm.getCode());
            System.out.println("Body     : " + prm.getMsgBody());
            System.out.println("Rdata    : " + prm.getRdata());
            System.out.println("UserData : " + prm.getUserData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String msgBody = prm.getMsgBody();
        try {
            TextMsg textMsg = JsonParserManager.parserJson(msgBody);
            String type = textMsg.getType();
            String msg = textMsg.getMsg();
            String status = textMsg.getStatus();
            Intent intent = new Intent();
            MyLog.print(Tag, "app发送返回接受到的消息:" + textMsg.toString(), MyLog.PRINT_RED);
            System.out.println("currentMsgType:" + currentMsgType);
            switch (currentMsgType) {
                case MSG_TYPE.MSG_CALL_OR_DIRECTOR_UNLOCK://发送开锁返回的消息处理
                    if (prm.getCode().equals(pjsip_status_code.PJSIP_SC_OK)) {
//					intent.setAction(ReceiverAction.ACTION_UNLOCK_SUCCESS);
                        sipMsgTimeOutAfterIndoorReturn();
                    } else {
                        intent.setAction(ReceiverAction.ACTION_UNLOCK_FAIL);
                        sendBroadcast(intent);
                    }
                    break;
                case MSG_TYPE.MSG_BIND://发送sipId返回的消息处理
                    if (prm.getCode().equals(pjsip_status_code.PJSIP_SC_OK)) {
                        intent.setAction(ReceiverAction.ACTION_BIND_SUCCESS);
                    } else {
                        intent.setAction(ReceiverAction.ACTION_BIND_FAIL);
                    }
                    sendBroadcast(intent);
                    break;

                case MSG_TYPE.MSG_SEND_UNLOCK_PASSWORD://发送修改开锁密码返回消息处理
                    if (prm.getCode().equals(pjsip_status_code.PJSIP_SC_OK)) {
//					intent.setAction(ReceiverAction.ACTION_UNLOCK_PASSWORD_SUCCESS);
                        sipMsgTimeOutAfterIndoorReturn();
                    } else {
                        intent.setAction(ReceiverAction.ACTION_UNLOCK_PASSWORD_FAIL);
                    }
                    sendBroadcast(intent);
                    break;
                case MSG_TYPE.MSG_SEND_VISITOR_PASSWORD://发送访客密码返回消息处理
                    if (prm.getCode().equals(pjsip_status_code.PJSIP_SC_OK)) {
//					intent.setAction(ReceiverAction.ACTION_VISITOR_PASSWORD_SUCCESS);
                        sipMsgTimeOutAfterIndoorReturn();
                    } else {
                        intent.setAction(ReceiverAction.ACTION_VISITOR_PASSWORD_FAIL);
                    }
                    sendBroadcast(intent);
                    break;
                case MSG_TYPE.MSG_SEND_INCOMING_SHIELD://发送来电屏蔽通知返回消息处理
                    if (prm.getCode().equals(pjsip_status_code.PJSIP_SC_OK)) {
                        intent.setAction(ReceiverAction.ACTION_INCOMING_SHIELD_SUCCESS);
                    } else {
                        intent.setAction(ReceiverAction.ACTION_INCOMING_SHIELD_FAIL);
                    }
                    sendBroadcast(intent);
                    break;
                case MSG_TYPE.MSG_SEND_UNBIND_DEVICE://发送解绑设备通知返回消息处理
                    if (prm.getCode().equals(pjsip_status_code.PJSIP_SC_OK)) {
                        intent.setAction(ReceiverAction.ACTION_UNBIND_DEVICE_FROM_PHONE_SUCCESS);
                    } else {
                        intent.setAction(ReceiverAction.ACTION_UNBIND_DEVICE_FROM_PHONE_FAIL);
                    }
                    sendBroadcast(intent);
                    break;
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private TimeOutRunnable timeOutRunnable;

    /**
     * app发送sip消息室内机接受成功，室内机发sip消息作为反馈，当15秒app还没收到这条消息时，判定为超时
     */
    private void sipMsgTimeOutAfterIndoorReturn() {
        timeOutRunnable = new TimeOutRunnable();
        handler.postDelayed(timeOutRunnable, 15000);
    }

    public class TimeOutRunnable implements Runnable {
        @Override
        public void run() {
            MyToast.showToast(MyApplication.instance, R.string.request_timeout);
            MyDialog.dismissDialog();
        }
    }

    /**
     * 当app收到室内机发sip消息作为反馈时，取消超时处理
     */
    private void cancelTimeOut() {
        if (timeOutRunnable != null) {
            handler.removeCallbacks(timeOutRunnable);
        }
    }


    /**
     * 获取sip注册状态
     *
     * @return 注册状态 true为在线，false为掉线
     */
    public boolean getSipRegisterStatus() {
        if (accountInfo != null) {
            try {
                pjsip_status_code regStatus = accountInfo.getRegStatus();
                MyLog.print(Tag, "regStatus: " + regStatus.toString(), MyLog.PRINT_BLACK);
                if (pjsip_status_code.PJSIP_SC_OK == regStatus) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Service结束发送启动Service广播
        Intent intent = new Intent("action.start.pjsipService");
        sendBroadcast(intent);
        MyLog.print(Tag, "发送启动service广播", MyLog.PRINT_RED);
    }

    @Override
    public void notifyInstantMessage(OnInstantMessageParam prm) {
        try {
            TextMsg textMsg = JsonParserManager.parserJson(prm.getMsgBody());
            MyLog.print(Tag, "室内机发送反馈消息:" + textMsg.toString(), MyLog.PRINT_RED);
            String type = textMsg.getType();
            if (textMsg != null) {
                Intent intent = new Intent();
                if (type.equals(SipMsgType.RECEIVE_UNBIND_DEVICE_TYPE)) {//收到室内机解绑设备反馈消息
                    intent.setAction(ReceiverAction.ACTION_UNBIND_DEVICE_MSG);
                    intent.putExtra("msg", textMsg.getMsg());

                } else if (type.equals(SipMsgType.OPEN_DOOR_TYPE)) {//收到室内机开锁反馈消息
                    if (textMsg.getStatus().equals(SipMsgType.GATE_CART_ENABLE)) {//开门成功
                        intent.setAction(ReceiverAction.ACTION_UNLOCK_SUCCESS);
                    } else {//开门失败
                        intent.setAction(ReceiverAction.ACTION_UNLOCK_FAIL);
                    }
                    cancelTimeOut();
                } else if (type.equals(SipMsgType.SEND_VISITOR_PWD_TYPE)) {
                    if (textMsg.getStatus().equals("1")) {//访客密码设置成功
                        intent.setAction(ReceiverAction.ACTION_VISITOR_PASSWORD_SUCCESS);
                    } else {
                        intent.setAction(ReceiverAction.ACTION_VISITOR_PASSWORD_FAIL);
                    }
                    cancelTimeOut();
                } else if (type.equals(SipMsgType.SEND_UNLOCK_PWD_TYPE)) {
                    if (textMsg.getStatus().equals("1")) {//修改开锁密码成功
                        intent.setAction(ReceiverAction.ACTION_UNLOCK_PASSWORD_SUCCESS);
                    } else {
                        intent.setAction(ReceiverAction.ACTION_UNLOCK_PASSWORD_FAIL);
                    }
                    cancelTimeOut();
                }
                sendBroadcast(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.print(Tag, "解密失败", MyLog.PRINT_RED);
        }
    }


    class PJSipServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            startService(new Intent(getApplicationContext(), RemoteService.class));
            bindService(new Intent(getApplicationContext(), RemoteService.class), pjsipServiceCon, Context.BIND_IMPORTANT);
        }

    }

    class PJSipServiceBinder extends IServiceAidl.Stub {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    }


}
