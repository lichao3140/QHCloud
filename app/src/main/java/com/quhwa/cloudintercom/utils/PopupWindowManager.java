package com.quhwa.cloudintercom.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import com.quhwa.cloudintercom.R;

/**
 * Created by lxz on 2017/9/21 0021.
 */

public class PopupWindowManager {
    private Activity activity;

    public PopupWindowManager(Activity activity) {
        this.activity = activity;
    }

    private PopupWindow mPopWindow;

    private float alpha = 1f;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    backgroundAlpha((float) msg.obj);
                    break;
            }
        }
    };

    /**
     * 弹出窗口
     *
     * @param context        上下文对象
     * @param layout         窗口布局
     * @param parentLayout   窗口显示在的界面布局
     * @param width          窗口宽度
     * @param height         窗口高度
     * @param layoutCallBack 窗口布局回调对象
     * @param isSetScreenBg  是否设置屏幕背景
     */
    public void showPopupWindow(Context context, int layout, int parentLayout, int width, int height, LayoutCallBackListener layoutCallBack,boolean isSetScreenBg) {
        View contentView = LayoutInflater.from(context).inflate(layout, null);
        mPopWindow = new PopupWindow(contentView, width, height, true);
        mPopWindow.setContentView(contentView);
        mPopWindow.setAnimationStyle(R.style.Popupwindow);
        //点击空白处隐藏窗口
        mPopWindow.setTouchable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());

        layoutCallBack.layoutCallBack(contentView);
        View parentView = LayoutInflater.from(context).inflate(parentLayout, null);
        mPopWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        if(isSetScreenBg){
            backgroundAlpha(1f);
            mPopWindow.setOnDismissListener(new PoponDismissListener());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (alpha > 0.5f) {
                        try {
                            //4是根据弹出动画时间和减少的透明度计算
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        //每次减少0.01，精度越高，变暗的效果越流畅
                        alpha -= 0.01f;
                        msg.obj = alpha;
                        mHandler.sendMessage(msg);
                    }
                }

            }).start();
        }
    }

    /**
     * PopupWindow布局回传监听器(当显示窗口的时候把布局回传到调用者的界面)
     */
    public interface LayoutCallBackListener {
        /**
         * 给调用者回传布局，调用者可拿到布局里面的控件进行下一步操作
         *
         * @param popupWindowLayout 窗口布局
         */
        void layoutCallBack(View popupWindowLayout);
    }

    /**
     * 关闭弹窗
     */
    public void dismissPopupWindow() {
        if (mPopWindow != null) {
            mPopWindow.dismiss();
        }
    }

    /**
     * 菜单是否处于显示状态
     * @return
     */
    public boolean isPopupWindowIsShow(){
        return mPopWindow.isShowing();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * 返回或者点击空白位置的时候将背景透明度改回来
     */
    class PoponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //此处while的条件alpha不能<= 否则会出现黑屏
                    while (alpha < 1f) {
                        try {
                            Thread.sleep(4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        alpha += 0.01f;
                        msg.obj = alpha;
                        mHandler.sendMessage(msg);
                    }
                }

            }).start();
        }

    }

    public void showPopmenu(View view, Context context) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.headmenu, popupMenu.getMenu());
        popupMenu.show();
    }

}

