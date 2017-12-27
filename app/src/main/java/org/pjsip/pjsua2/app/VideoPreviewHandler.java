package org.pjsip.pjsua2.app;

import org.pjsip.pjsua2.VideoPreviewOpParam;
import org.pjsip.pjsua2.VideoWindowHandle;

import android.view.SurfaceHolder;

import com.quhwa.cloudintercom.service.PJSipService;
import com.quhwa.cloudintercom.utils.MyLog;

public class VideoPreviewHandler implements SurfaceHolder.Callback{
	private String Tag = "VideoPreviewHandler";
	public void updateVideoPreview(SurfaceHolder holder) {
		VideoWindowHandle vidWH = new VideoWindowHandle();
		vidWH.getHandle().setWindow(holder.getSurface());
		VideoPreviewOpParam vidPrevParam = new VideoPreviewOpParam();
		vidPrevParam.setWindow(vidWH);
		try {
			if(PJSipService.currentCall != null){
				PJSipService.currentCall.vidPrev.start(vidPrevParam);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		MyLog.print(Tag, "surfaceCreated", MyLog.PRINT_RED);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		updateVideoPreview(holder);
		MyLog.print(Tag, "surfaceChanged", MyLog.PRINT_RED);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			if(PJSipService.currentCall != null){
				PJSipService.currentCall.vidPrev.stop();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		MyLog.print(Tag, "surfaceDestroyed", MyLog.PRINT_RED);
	}

}
