package com.quhwa.cloudintercom.utils;

import android.app.Activity;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShareUtil {
	public static void showShare(Activity activity,String content) {
	     OnekeyShare oks = new OnekeyShare();
	     //关闭sso授权
	     oks.disableSSOWhenAuthorize(); 

	    // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
	     //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
	     // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
	     oks.setTitle("龙侨华");
	     // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
	     oks.setTitleUrl("http://www.quhwa.cn/");
	     oks.setImageUrl("");
//	     oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
	     
	     // text是分享文本，所有平台都需要这个字段
	     oks.setText(content);
	     // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//	     oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
	     // url仅在微信（包括好友和朋友圈）中使用
	     oks.setUrl("http://www.quhwa.cn/");
	     // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//	     oks.setComment("我是测试评论文本");
	     // site是分享此内容的网站名称，仅在QQ空间使用
//	     oks.setSite(getString(R.string.app_name));
	     // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//	     oks.setSiteUrl("http://sharesdk.cn");

	    // 启动分享GUI
	    oks.show(activity);
	}
}
