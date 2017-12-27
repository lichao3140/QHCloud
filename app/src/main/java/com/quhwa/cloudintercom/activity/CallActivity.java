package com.quhwa.cloudintercom.activity;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.VideoWindowHandle;
import org.pjsip.pjsua2.pjmedia_orient;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_role_e;
import org.pjsip.pjsua2.app.MSG_TYPE;
import org.pjsip.pjsua2.app.MyApp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.app.MyApplication;
import com.quhwa.cloudintercom.bean.TextMsg;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.MySharedPreferenceManager;
import com.quhwa.cloudintercom.utils.PhoneManager;
import com.quhwa.cloudintercom.utils.SipMsgManager;
import com.quhwa.cloudintercom.utils.SipMsgType;
import com.quhwa.cloudintercom.db.Table;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;

public class CallActivity extends BaseActivity implements SurfaceHolder.Callback,OnClickListener,Handler.Callback{
	private Button btnVideoHandup,btnIncomingHandup,btnIncomingAccept,btnOutgonigHandup,
	btnVideoMicrophone,btnVideoSpeaker,btnUnlock,btnVideoUnlock,btnSpeaker,btnMute;
	private SurfaceView svIncomingVideo;
//	private SurfaceView svPreviewCapture;
//	private static VideoPreviewHandler previewHandler = new VideoPreviewHandler();
	private TextView tvPeer,tvState,tvIncomingUri,tvOutgoingUri;
	private RelativeLayout rlIncomingUI,rlOutgoingUI;
	private LinearLayout llCallUi;
	public static Handler handler_;
	private final Handler handler = new Handler(this);
	private CallInfo lastCallInfo;
	private String Tag = "CallActivity";
	private RelativeLayout rlMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call);
		setView();
		initListener();
		handler_ = handler;
		if(PJSipService.currentCall != null){
			 try {
				lastCallInfo = PJSipService.currentCall.getInfo();
				updateCallState(lastCallInfo);
			    } catch (Exception e) {
			    	System.out.println(e);
			    }
		} else {//号码不存在
			CallActivity.this.finish();
			MyToast.showToast(this, R.string.phone_num_not_exist);
		}
	}
	@SuppressWarnings("deprecation")
	private void set() {
		Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高 
		 Window window = getWindow(); 
		LayoutParams windowLayoutParams = window.getAttributes(); // 获取对话框当前的参数值 
		windowLayoutParams.width = (int) (display.getWidth()); // 宽度设置为屏幕的0.95  
//		windowLayoutParams.height = (int) (display.getHeight() * 0.1); // 高度设置为屏幕的0.6 
//		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//		int s_height = wm.getDefaultDisplay().getHeight();
//		int s_width = wm.getDefaultDisplay().getWidth();
		LayoutParams params = svIncomingVideo.getLayoutParams();   
		params.height = 5/3*windowLayoutParams.width;
		svIncomingVideo.setLayoutParams(params);
	}
	/**
	 * 初始化监听器
	 */
	private void initListener() {
		btnVideoHandup.setOnClickListener(this);
		btnIncomingHandup.setOnClickListener(this);
		btnIncomingAccept.setOnClickListener(this);
		btnOutgonigHandup.setOnClickListener(this);
		svIncomingVideo.getHolder().addCallback(this);
		btnUnlock.setOnClickListener(this);
		btnVideoUnlock.setOnClickListener(this);
		btnSpeaker.setOnClickListener(this);
		btnMute.setOnClickListener(this);
		btnVideoMicrophone.setOnClickListener(this);
		btnVideoSpeaker.setOnClickListener(this);
		svIncomingVideo.setOnClickListener(this);
	}

	/**
	 * 根据来电状态切换界面
	 * @param callInfo
	 */
	public void updateCallState(CallInfo callInfo) {
		String call_state = "";
		if (callInfo.getRole() == pjsip_role_e.PJSIP_ROLE_UAC) {//客户端机（呼叫）即去电界面
			llCallUi.setVisibility(View.GONE);
	    	rlOutgoingUI.setVisibility(View.VISIBLE);
	    	String remoteUri = callInfo.getRemoteUri();
	    	tvOutgoingUri.setText(remoteUri.substring(remoteUri.indexOf(":")+1, remoteUri.indexOf("@")));
		}

		if (callInfo.getState().swigValue() < pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()){
		    if (callInfo.getRole() == pjsip_role_e.PJSIP_ROLE_UAS) {//服务端机（被呼叫）即来电界面
		    	call_state = "Incoming call...";
		    	llCallUi.setVisibility(View.GONE);
		    	rlIncomingUI.setVisibility(View.VISIBLE);
		    	String remoteUri = callInfo.getRemoteUri();
				//TODO
				String doorName = remoteUri.substring(1,remoteUri.indexOf("_"));
				if(doorName.startsWith("2")){
					doorName = doorName.substring(3,5)+"栋"+doorName.substring(5,7)+"单元"+doorName.substring(doorName.length()-2,doorName.length())+"门口机";
				}else if(doorName.startsWith("3")){
					doorName = doorName.substring(doorName.length()-2)+"别墅门口机";
				}else if(doorName.startsWith("6")){
					doorName = doorName.substring(doorName.length()-2)+"保安机";
				}else if(doorName.startsWith("7")){
					doorName = doorName.substring(doorName.length()-2)+"大门口机";
				}else if(doorName.startsWith("8")){
					doorName = doorName.substring(doorName.length()-2)+"管理中心";
				}

		    	tvIncomingUri.setText(doorName);
		    } else {
		    	call_state = callInfo.getStateText();
		    }
		}else if (callInfo.getState().swigValue() >= pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()){
				PhoneManager.stopPlayRingtone();
				call_state = callInfo.getStateText();
			    if (callInfo.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {//接通界面
			    	MyDialog.dismissDialog();
			    	llCallUi.setVisibility(View.VISIBLE);
			    	rlOutgoingUI.setVisibility(View.GONE);
			    	rlIncomingUI.setVisibility(View.GONE);
			    } else if (callInfo.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED){
			    	String reason = callInfo.getLastReason();
					if(reason.equals("Busy Here")){
						MyToast.showToast(CallActivity.this,R.string.busying);
					}else if(reason.equals("Request Timeout")){
						MyToast.showToast(CallActivity.this,R.string.request_timeout);
						
					}else if(reason.equals("")){
						MyToast.showToast(CallActivity.this,R.string.not_online);
					}else{
						Log.e("---reson---:", reason);
					}
			    	MyDialog.dismissDialog();
			        CallActivity.this.finish();
			    }
		}
		MyLog.print(Tag, "=======call_state======="+call_state, MyLog.PRINT_RED);
//		tvPeer.setText(callInfo.getRemoteUri());
//		tvState.setText(call_state);
	}

	/**
	 * 控件初始化
	 */
	private void setView() {
		btnVideoHandup = (Button) findViewById(R.id.btn_video_handup);
		btnIncomingAccept = (Button) findViewById(R.id.btn_incoming_accept);
		btnIncomingHandup = (Button) findViewById(R.id.btn_incoming_hangup);
		btnOutgonigHandup = (Button) findViewById(R.id.btn_outgoing_hang_up);
		btnUnlock = (Button) findViewById(R.id.btn_incoming_unlock);
		
//		svPreviewCapture = (SurfaceView) findViewById(R.id.sv_preview_capture);
		svIncomingVideo = (SurfaceView) findViewById(R.id.sv_incoming_video);
		
//		svPreviewCapture.getHolder().addCallback(previewHandler);
		tvPeer = (TextView) findViewById(R.id.textViewPeer);
		tvState = (TextView) findViewById(R.id.textViewCallState);
		tvIncomingUri = (TextView) findViewById(R.id.tv_show_incoming_num);
		tvOutgoingUri = (TextView) findViewById(R.id.tv_show_outgoing_num);
		rlIncomingUI = (RelativeLayout) findViewById(R.id.incoming_call_ui);
		rlOutgoingUI = (RelativeLayout) findViewById(R.id.outgoing_call_ui);
		llCallUi = (LinearLayout) findViewById(R.id.ll_call_ui);
		btnVideoUnlock = (Button) findViewById(R.id.btn_video_unlock);
		btnSpeaker = (Button) findViewById(R.id.speaker);
		btnMute = (Button) findViewById(R.id.micro);
		btnVideoMicrophone = (Button) findViewById(R.id.btn_microphone);
		btnVideoSpeaker = (Button) findViewById(R.id.btn_video_speaker);
		rlMenu = (RelativeLayout)findViewById(R.id.rl);
//		set();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		MyLog.print(Tag, "surfaceCreated", MyLog.PRINT_RED);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		updateVideoWindow(true);
		MyLog.print(Tag, "surfaceChanged", MyLog.PRINT_RED);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		updateVideoWindow(false);
		MyLog.print(Tag, "surfaceDestroyed", MyLog.PRINT_RED);
	}
	/**
	 * 更新视频窗口
	 * @param show 是否显示视频
	 */
	private void updateVideoWindow(boolean show){
		if (PJSipService.currentCall != null && PJSipService.currentCall.vidWin != null && PJSipService.currentCall.vidPrev != null){
		    VideoWindowHandle vidWH = new VideoWindowHandle();	    
		    if (show) {
		    	vidWH.getHandle().setWindow(svIncomingVideo.getHolder().getSurface());
		    } else {
		    	vidWH.getHandle().setWindow(null);
		    }
		    try {
		    	PJSipService.currentCall.vidWin.setWindow(vidWH);
		    } catch (Exception e) {
		    	System.out.println(e);
		    }	    
		}
	} 
	private boolean isShow = false;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_incoming_accept:
			accept();
			break;
		case R.id.btn_incoming_hangup:
			hangup();
			PhoneManager.stopPlayRingtone();
			break;
		case R.id.btn_video_handup:
			hangup();
			break;
		case R.id.btn_outgoing_hang_up:
			hangup();
			break;
		case R.id.btn_incoming_unlock:
			unlock();
			break;
		case R.id.btn_video_unlock:
			unlock();
			break;
		case R.id.speaker:
//			speaker();
			break;
		case R.id.micro:
//			mute();
			break;
		case R.id.btn_microphone:
			setMocrophone();
			break;
		case R.id.btn_video_speaker:
			speakerOrEarpiece();
			break;
		case R.id.sv_incoming_video:
			showOrHide();
			break;

		default:
			break;
		}
		
	}
	private boolean isSpeaker = false;
	private void speakerOrEarpiece() {
		if(isSpeaker){
			PhoneManager.setSpeakerphoneOn(true,this);
			MyToast.showToast(this, R.string.speaker_mode);
		}else{
			PhoneManager.setSpeakerphoneOn(false,this);
			MyToast.showToast(this, R.string.earpiece_mode);
		}
		isSpeaker = !isSpeaker;
	}
	private void showOrHide() {
		if(isShow){
			rlMenu.setVisibility(View.VISIBLE);
//			MyAnimation.fromDownToUpAnim(CallActivity.this, rlMenu);
		}else{
			rlMenu.setVisibility(View.GONE);
		}
//		MyAnimation.alphaAnimation(rlMenu);
		isShow = !isShow;
	}
	private boolean close = true;
	/**
	 * 设置麦克风
	 */
	private void setMocrophone() {
		PhoneManager.setMicroPhone(close,this,btnVideoMicrophone);
		close = !close;
		if(close){
			MyToast.showToast(this, R.string.microphone_open);
		}else{
			MyToast.showToast(this, R.string.microphone_forbid);
		}
	}
	/**
	 * 开锁
	 */
	private void unlock() {
		MyDialog.showDialog(this, R.string.unlocking);
		PJSipService.currentMsgType = MSG_TYPE.MSG_CALL_OR_DIRECTOR_UNLOCK;//把当前消息设为开锁消息（即向室内机发送开锁消息）
		String incomingSipId = MySharedPreferenceManager.queryString(MyApplication.instance, Table.TAB_USER, Table.TAB_USER_PASSWORD_INCOMING_SIP_ID_key);
		SipMsgManager.sendInstantMessage(incomingSipId, SipMsgManager.getMsgCommand(new TextMsg(SipMsgType.OPEN_DOOR_TYPE, "", SipMsgType.INCOMING_GATE_CART_ENABLE)),PJSipService.account);
		MyLog.print(Tag, "来电和接通状态发送开锁命令", MyLog.PRINT_RED);
	}
	/**
	 * 接听
	 */
	private void accept() {
		MyDialog.showDialog(this, R.string.accepting);
		rlIncomingUI.setVisibility(View.GONE);
		btnIncomingAccept.setClickable(false);
		PhoneManager.answer();
	}

	/**
	 * 挂断
	 */
	private void hangup() {
		MyDialog.showDialog(this, R.string.hanguping);
		PhoneManager.handup();
//		handler_ = null;
//		finish();
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_TYPE.CALL_STATE:
			 lastCallInfo = (CallInfo) msg.obj;
			 updateCallState(lastCallInfo);
			break;
		case MSG_TYPE.CALL_MEDIA_STATE:
//			  if (PJSipService.currentCall.vidWin != null) {
					/* Set capture orientation according to current
					 * device orientation.
					 */
					onConfigurationChanged(getResources().getConfiguration());
					/* If there's incoming video, display it. */
//					setupVideoSurface();
//				    }
			break;

		default:
			return false;
		}
		return true;
	}
		@Override
	    public void onConfigurationChanged(Configuration newConfig) {
	        super.onConfigurationChanged(newConfig);
	        
	        WindowManager wm;
	        Display display;
	        int rotation;
	        pjmedia_orient orient;

	        wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
	        display = wm.getDefaultDisplay();
	        rotation = display.getRotation();
	        System.out.println("Device orientation changed: " + rotation);
	        switch (rotation) {
	        case Surface.ROTATION_0:   // Portrait
	            orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_270DEG;
	            break;
	        case Surface.ROTATION_90:  // Landscape, home button on the right
	            orient = pjmedia_orient.PJMEDIA_ORIENT_NATURAL;
	            break;
	        case Surface.ROTATION_180:
	            orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_90DEG;
	            break;
	        case Surface.ROTATION_270: // Landscape, home button on the left
	            orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_180DEG;
	            break;
	        default:
	            orient = pjmedia_orient.PJMEDIA_ORIENT_UNKNOWN;
	        }

	        if (MyApp.ep != null && PJSipService.account != null) {
	            try {
	        	AccountConfig cfg = PJSipService.account.cfg;
	        	int cap_dev = cfg.getVideoConfig().getDefaultCaptureDevice();
	        	MyApp.ep.vidDevManager().setCaptureOrient(cap_dev, orient,
	        						  true);
	            } catch (Exception e) {
	        	System.out.println(e);
	            }
	        }
	    }
	 
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler_ = null;
	}
	 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			MyToast.showToast(CallActivity.this, R.string.please_hangup_first);
			return false;
		}
		return false;
	}
	
}


	
