package com.quhwa.cloudintercom.utils;

import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.MediaFormatVideo;
import org.pjsip.pjsua2.VidCodecParam;

public class PJSipSetting {
	private static String Tag = "PJSipSetting";
	/**
	 * 设置分辨率
	 * @param width 宽
	 * @param height 高
	 * @param ep
	 */
	public static void setResolution(long width,long height,Endpoint ep){
		try {
//			CodecInfoVector videoCodecEnum = MyApp.ep.videoCodecEnum();
//			for (int i = 0; i < videoCodecEnum.size(); i++) {
//				String codecId = videoCodecEnum.get(i).getCodecId();
				//默认分辨率：352*288
				String codecId = "H264/97";
				MyLog.print(Tag , "codecId:"+codecId, MyLog.PRINT_RED);//codecId = "H264/97";
				VidCodecParam param = ep.getVideoCodecParam(codecId);
				MediaFormatVideo encFmt = param.getEncFmt();
				MediaFormatVideo decFmt = param.getDecFmt();
				encFmt.setFpsNum(18);
				decFmt.setFpsNum(18);
//				mfv.setFpsDenum(1);
//				mfv.setAvgBps(256000);
//				mfv.setMaxBps(512000);  
				encFmt.setWidth(width);
				encFmt.setHeight(height);
				decFmt.setWidth(width);
				decFmt.setHeight(height);
				param.setDecFmt(encFmt);
				param.setEncFmt(decFmt);
				ep.setVideoCodecParam(codecId, param);
				MyLog.print(Tag, "Width:"+param.getDecFmt().getWidth(), MyLog.PRINT_RED);//codecId = "H264/97";
				MyLog.print(Tag, "Height:"+param.getDecFmt().getHeight(), MyLog.PRINT_RED);//codecId = "H264/97";
				MyLog.print(Tag, "getPacking:"+param.getPacking(), MyLog.PRINT_RED);
				MyLog.print(Tag, "getIgnoreFmtp:"+param.getIgnoreFmtp(), MyLog.PRINT_RED);
				MyLog.print(Tag, "getFpsNum:"+param.getEncFmt().getFpsNum(), MyLog.PRINT_RED);
				MyLog.print(Tag, "getAvgBps:"+param.getEncFmt().getAvgBps(), MyLog.PRINT_RED);
				MyLog.print(Tag, "getFpsDenum:"+param.getEncFmt().getFpsDenum(), MyLog.PRINT_RED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
