package com.quhwa.cloudintercom.activity;

import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.adapter.MainPagerAdapter;
import com.quhwa.cloudintercom.presenter.PagerPresenter;
import com.quhwa.cloudintercom.utils.MyAnimation;
import com.quhwa.cloudintercom.utils.PhoneManager;
import com.quhwa.cloudintercom.view.IPagerView;
import com.quhwa.cloudintercom.widget.ViewPagerScroller;

public class MainActivity extends BaseActivity implements IPagerView{
	public static MainActivity instance;
	private RadioGroup rg_tab_menu;
	private ViewPager vp_content;
	private MainPagerAdapter mainPagerAdapter;
	private RadioButton rbSmartArea,rbLifeCircle,rbSmartFamily,rbMy;
	private String Tag = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		instance = this;
		setView();
		loadPager();
		setListener();
	}
	/**
	 * 加载页面
	 */
	private void loadPager() {
		new PagerPresenter(instance).loadFragments();
	}

	@Override
	public void load(List<Fragment> fragments) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		mainPagerAdapter = new MainPagerAdapter(fragmentManager,fragments);
		vp_content.setOffscreenPageLimit(mainPagerAdapter.getCount()-1);//缓存fragment
		vp_content.setAdapter(mainPagerAdapter);
		new ViewPagerScroller(MainActivity.this).initViewPagerScroll(vp_content);//改变viewPager滑动速度
	}
	/**
	 * 控件初始化
	 */
	private void setView() {
		rg_tab_menu = (RadioGroup) findViewById(R.id.rg_tab_menu);
		vp_content = (ViewPager) findViewById(R.id.vp_container);
		rbSmartArea = (RadioButton) findViewById(R.id.rb_tab_menu_smart_area);
		rbLifeCircle = (RadioButton) findViewById(R.id.rb_tab_menu_life_circle);
		rbMy = (RadioButton) findViewById(R.id.rb_tab_menu_own);
		rbSmartFamily = (RadioButton) findViewById(R.id.rb_tab_menu_smart_family);
	}
	/**
	 * 设置监听器
	 */
	@SuppressWarnings("deprecation")
	private void setListener() {
		//监听Radiobutton
		rg_tab_menu.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			RadioButton currentSelect;
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_tab_menu_smart_area:
					vp_content.setCurrentItem(0);
					currentSelect = rbSmartArea;
					break;
				case R.id.rb_tab_menu_life_circle:
					vp_content.setCurrentItem(1);
					currentSelect = rbLifeCircle;
					break;
				case R.id.rb_tab_menu_smart_family:
					vp_content.setCurrentItem(2);
					currentSelect = rbSmartFamily;
					break;
				case R.id.rb_tab_menu_own:
					vp_content.setCurrentItem(3);
					currentSelect = rbMy;
					break;
				}
				MyAnimation.scaleAnimation(currentSelect);
//				MyAnimation.alphaAnimation(vp_content);
			}
		});
		//监听ViewPager页面发送改变
		vp_content.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					rbSmartArea.setChecked(true);
//					rg_tab_menu.check(R.id.rb_tab_menu_smart_area);
					break;
				case 1:
					rbLifeCircle.setChecked(true);
//					rg_tab_menu.check(R.id.rb_tab_menu_life_circle);
					break;
				case 2:
					rbSmartFamily.setChecked(true);
//					rg_tab_menu.check(R.id.rb_tab_menu_smart_family);
					break;
				case 3:
					rbMy.setChecked(true);
//					rg_tab_menu.check(R.id.rb_tab_menu_own);
					break;
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle(getResources().getString(R.string.system_prompt))
			.setMessage(getResources().getString(R.string.are_you_sure_you_exit_the_application))
			.setPositiveButton(getResources().getString(R.string.ok), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MainActivity.this.finish();
				}
			})
			.setNegativeButton(getResources().getString(R.string.cancel), null)
			.create()
			.show();
			return true;//注意要返回布尔值，结束这个方法
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PhoneManager.stopPlayRingtone();
	}
}
