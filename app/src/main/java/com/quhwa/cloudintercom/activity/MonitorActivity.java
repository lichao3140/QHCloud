package com.quhwa.cloudintercom.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.MonitorDeviceListAdapter;
import com.quhwa.cloudintercom.bean.BoundResult;
import com.quhwa.cloudintercom.presenter.MonitorPresenter;
import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.MyAnimation;
import com.quhwa.cloudintercom.utils.MyLog;
import com.quhwa.cloudintercom.utils.PhoneManager;
import com.quhwa.cloudintercom.view.IMonitorView;
import com.quhwa.cloudintercom.widget.AlertDialogChooseStatusListener;
import com.quhwa.cloudintercom.widget.MyDialog;
import com.quhwa.cloudintercom.widget.MyToast;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.VideoWindowHandle;
import org.pjsip.pjsua2.app.MSG_TYPE;
import org.pjsip.pjsua2.app.MyApp;
import org.pjsip.pjsua2.pjmedia_orient;
import org.pjsip.pjsua2.pjsip_inv_state;

import java.util.ArrayList;
import java.util.List;

public class MonitorActivity extends BaseActivity implements OnClickListener,SurfaceHolder.Callback,Handler.Callback,IMonitorView,OnItemClickListener,AlertDialogChooseStatusListener,OnRefreshListener{

	private TextView tvBack;
	private SurfaceView svMonitor;
	private AppCompatButton btnStopMonitor,btnVideoMicrophone,btnVideoSpeaker;
	public static Handler handler_;
	private final Handler handler = new Handler(this);
	private CallInfo lastCallInfo;
	private GridView lvMonitor;
	/**是否处于监控状态，默认为false*/
	private boolean isOnMonitorState = false;
	private TextView tvTitle;
	private MonitorPresenter monitorPresenter;
	private List<BoundResult.Device> devices = null;
	private String sipId;
	private String Tag = "MonitorActivity";
	private SmartRefreshLayout refreshLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitor);
		handler_ = handler;
		setView();
		setAdapter();
		initListener();
	}
	private void setAdapter() {
		monitorPresenter = new MonitorPresenter(this,this);
		monitorPresenter.loadRoomNumList();
		
	}
	/**
	 * 初始化监听器
	 */
	private void initListener() {
		tvBack.setOnClickListener(this);
		btnVideoMicrophone.setOnClickListener(this);
		btnStopMonitor.setOnClickListener(this);
		btnVideoSpeaker.setOnClickListener(this);
		svMonitor.getHolder().addCallback(this);
		lvMonitor.setOnItemClickListener(this);
		refreshLayout.setOnRefreshListener(this);
	}
	/**
	 * 控件初始化
	 */
	private void setView() {
		// 标题文字设置
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.monitor_title);
		tvTitle = (TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvTitle.setText(R.string.monitor_list);
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
		svMonitor = (SurfaceView) findViewById(R.id.sv_monitor);
//		btnStartMonitor = (AppCompatButton) findViewById(R.id.btn_start_monitor);
		btnStopMonitor = (AppCompatButton) findViewById(R.id.btn_stop_monitor);
		btnVideoMicrophone = (AppCompatButton) findViewById(R.id.btn_microphone);
		btnVideoSpeaker = (AppCompatButton) findViewById(R.id.btn_video_speaker);
		lvMonitor = (GridView) findViewById(R.id.lv_monitor_list);
		refreshLayout = (SmartRefreshLayout) findViewById(R.id.smart_refresh_device_list);
		refreshLayout.setPrimaryColorsId(R.color.app_base_color,R.color.white);
	}
	
	/**
	 * 根据来电状态切换界面
	 * @param callInfo
	 */
	public void updateCallState(CallInfo callInfo) {
		if (callInfo.getState().swigValue() >= pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED
				.swigValue()) {
			if (callInfo.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {// 接通界面
//				btnStartMonitor.setVisibility(View.GONE);
				btnStopMonitor.setVisibility(View.VISIBLE);
				btnVideoMicrophone.setVisibility(View.VISIBLE);
				btnVideoSpeaker.setVisibility(View.VISIBLE);
				MyAnimation.fromDownToUpAnim(this, btnVideoMicrophone);
				MyAnimation.fromDownToUpAnim(this, btnVideoSpeaker);
				updateVideoWindow(true);
				tvBack.setVisibility(View.GONE);
				isOnMonitorState = true;
				MyDialog.dismissDialog();
				tvTitle.setText(R.string.monitoring);
			} else if (callInfo.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
//				MyToast.showToast(MonitorActivity.this,R.string.user_disconnect);
				MyLog.print(Tag , "---reson---:"+callInfo.getLastReason(), MyLog.PRINT_RED);
				MyDialog.dismissDialog();
				String reason = callInfo.getLastReason();
				if(reason.equals("Busy Here")){
					MyToast.showToast(MonitorActivity.this,R.string.busying);
				}else if(reason.equals("Request Timeout")){
					MyToast.showToast(MonitorActivity.this,R.string.request_timeout);
				}else if(reason.equals("") || reason.equals("Not Found")){
					MyToast.showToast(MonitorActivity.this,R.string.not_online);
				}else if(reason.equals("Not Acceptable") || reason.equals("Not Found")){
//					MyToast.showToast(MonitorActivity.this,R.string.not_online);
					Toast.makeText(MonitorActivity.this, "Not Acceptable", Toast.LENGTH_SHORT).show();
				}else{
					Log.e("---reason---:", reason);
				}
				
				MonitorActivity.this.finish();
				
			}else if(callInfo.getState() == pjsip_inv_state.PJSIP_INV_STATE_NULL){
				MyToast.showToast(MonitorActivity.this,R.string.not_online);
				MonitorActivity.this.finish();
				MyDialog.dismissDialog();
			}
		}
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			MonitorActivity.this.finish();
			break;
		case R.id.btn_microphone:
			setMocrophone();
			break;
		case R.id.btn_stop_monitor:
			stopMonitor();
			break;
		case R.id.btn_video_speaker:
			speakerOrEarpiece();
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
	 * 停止监控
	 */
	private void stopMonitor() {
		hangup();
	}
	/**
	 * 挂断
	 */
	private void hangup() {
		MyDialog.showDialog(this, R.string.hanguping);
		PhoneManager.handup();
//		handler_ = null;
		
	}
//	/**
//	 * 启动监控
//	 */
//	private void startMonitor1() {
//		MyDialog.showDialog(MonitorActivity.this, R.string.connecting);
////		svMonitor.getHolder().addCallback(this);
//		String sipId = "sip:"+"1003"+"@"+Constants.SERVER_IP;
//		PJSipService.instance.makeCall(sipId,0);
//	}
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_TYPE.CALL_STATE:
			lastCallInfo = (CallInfo) msg.obj;
			updateCallState(lastCallInfo);
			break;
		case MSG_TYPE.CALL_MEDIA_STATE:
			  if (PJSipService.currentCall.vidWin != null) {
					/* Set capture orientation according to current
					 * device orientation.
					 */
					onConfigurationChanged(getResources().getConfiguration());
				    }
			break;

		default:
			return false;
		}
		return true;
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
//		updateVideoWindow(true);
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		updateVideoWindow(false);
	}
	/**
	 * 更新视频窗口
	 * @param show 是否显示视频
	 */
	private void updateVideoWindow(boolean show){
		if (PJSipService.currentCall != null && PJSipService.currentCall.vidWin != null && PJSipService.currentCall.vidPrev != null){
		    VideoWindowHandle vidWH = new VideoWindowHandle();	    
		    if (show) {
		    	vidWH.getHandle().setWindow(svMonitor.getHolder().getSurface());
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
        	MyApp.ep.vidDevManager().setCaptureOrient(cap_dev, orient,true);
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
			 if(isOnMonitorState){
				 MyToast.showToast(MonitorActivity.this, R.string.please_stop_monitor_first);
				 return false;
			 }else{
				 MonitorActivity.this.finish();
				 return true;
			 }
		 }
		 MonitorActivity.this.finish();
		 return true;
	}
	@Override
	public void loadRoomNumList(ArrayList<BoundResult.Device> devices) {
		this.devices = devices;
		lvMonitor.setAdapter(new MonitorDeviceListAdapter(this, devices));
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(devices != null && devices.size()>0){
//			MyDialog.showAlertDialog(this, R.string.are_you_sure_to_monitor_this_room, R.string.monitor, R.string.ok, R.string.cancel, this);
			MyDialog.showMyDialog(this, R.string.are_you_sure_to_monitor_this_room, R.string.monitor, this,true,null);
			sipId = devices.get(position).getSipid();
		}
	}
	@Override
	public void choosePositiveButton() {
		startMonitor();
	}
	@Override
	public void chooseNegativeButton() {
		MyDialog.dismissMyDialog();
	}
	private void startMonitor() {
		if(sipId != null){
			monitorPresenter.startMonitor(sipId);
		}
	}
	@Override
	public void dimissListView() {
		refreshLayout.setVisibility(View.GONE);
	}
	@Override
	public void showSurfaceView() {
		MyAnimation.fromDownToUpAnim(this, svMonitor);
		MyAnimation.fromDownToUpAnim(this, btnStopMonitor);
		svMonitor.setVisibility(View.VISIBLE);
		btnStopMonitor.setVisibility(View.VISIBLE);
	}
	@Override
	public void showListView() {
		refreshLayout.setVisibility(View.VISIBLE);
	}
	@Override
	public void dimissSurfaceView() {
		svMonitor.setVisibility(View.GONE);
		btnStopMonitor.setVisibility(View.GONE);
	}
	@Override
	public void showConnectingDialog() {
		MyDialog.dismissMyDialog();
		MyDialog.showDialog(MonitorActivity.this, R.string.connecting);
	}
	@Override
	public void dimissConnectingDialog() {
		MyDialog.dismissDialog();
	}

	@Override
	public void showToastNoRoom() {
		MyToast.showToast(this,R.string.no_room);
	}

	@Override
	public void showToastServerBusy() {
		MyToast.showToast(this,R.string.server_is_busy);
	}

	@Override
	public void showToastNoNet() {
		MyToast.showToast(this,R.string.no_internet);
	}

	@Override
	public void finishRefresh() {
		if(refreshLayout.isRefreshing()){
			refreshLayout.finishRefresh();
			MyToast.showToast(this,R.string.refresh_success);
		}
	}

	@Override
	public void finishRefreshFail() {
		if(refreshLayout.isRefreshing()){
			refreshLayout.finishRefresh();
		}
	}

	@Override
	public void choosePositiveButton(String userPassword, String newPassword,
			String confirmPassword,String sipId,String doorList) {
	}
	@Override
	public void choosePositiveButton(String newName) {
	}

	@Override
	public void layoutCallBack(View dialogView) {
	}


	@Override
	public void onRefresh(RefreshLayout refreshlayout) {
		monitorPresenter.loadRoomNumList();
	}
}
