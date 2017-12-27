package com.quhwa.cloudintercom.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quhwa.cloudintercom.R;
import com.quhwa.cloudintercom.presenter.DialerPresenter;
import com.quhwa.cloudintercom.view.IDialerView;
import com.quhwa.cloudintercom.widget.MyToast;
/**
 * 拨号界面
 *
 * @author lxz
 * @date 2017年4月5日
 */
public class DialerActivity extends BaseActivity implements OnClickListener,IDialerView{
	private TextView tvBack;
	private Button btnCall,btnOne,btnTwo,btnThree,btnFour,btnFive,btnSix,btnSeven,btnEight,btnNine,btnZero,btnStar,btnJing;
	private EditText etShowNum;
	private ImageButton ibDelete;
	private StringBuffer sbShowNum = new StringBuffer();
	private DialerPresenter dialerPresenter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialer_activity);
		setView();
	}

	@Override
	public void onClick(View v) {
		dialerPresenter.showImageDelete(ibDelete);
		switch (v.getId()) {
		case R.id.back:
			back();
			break;
		case R.id.btn_call:
			call();
			break;
		case R.id.btn_one:
			sbShowNum.append(1);
			break;
		case R.id.btn_two:
			sbShowNum.append(2);
			break;
		case R.id.btn_three:
			sbShowNum.append(3);
			break;
		case R.id.btn_four:
			sbShowNum.append(4);
			break;
		case R.id.btn_five:
			sbShowNum.append(5);
			break;
		case R.id.btn_six:
			sbShowNum.append(6);
			break;
		case R.id.btn_seven:
			sbShowNum.append(7);
			break;
		case R.id.btn_eight:
			sbShowNum.append(8);
			break;
		case R.id.btn_nine:
			sbShowNum.append(9);
			break;
		case R.id.btn_zero:
			sbShowNum.append(0);
			break;
		case R.id.btn_star:
			sbShowNum.append("*");
			break;
		case R.id.btn_jing:
			sbShowNum.append("#");
			break;
		case R.id.ib_delete:
			deleteNum();
			break;
		}
		etShowNum.setText(sbShowNum.toString());
	}
	
	/**
	 * 删除号码
	 */
	private void deleteNum() {
		dialerPresenter.deleteNum(sbShowNum);
	}

	/**
	 * 返回
	 */
	private void back() {
		dialerPresenter.back(ibDelete);
	}

	/**
	 * 拨打电话
	 */
	private void call() {
		dialerPresenter.call(etShowNum, sbShowNum);
	}

	private void setView() {
		RelativeLayout rlTitleView = (RelativeLayout) findViewById(R.id.dialer_title);
		TextView tvLogin =(TextView) rlTitleView.findViewById(R.id.tv_title_text);
		tvLogin.setText(R.string.door_to_door);
		tvBack = (TextView) rlTitleView.findViewById(R.id.back);
		tvBack.setOnClickListener(this);
		btnCall = (Button) findViewById(R.id.btn_call);
		btnOne = (Button) findViewById(R.id.btn_one);
		btnTwo = (Button) findViewById(R.id.btn_two);
		btnThree = (Button) findViewById(R.id.btn_three);
		btnFour = (Button) findViewById(R.id.btn_four);
		btnFive = (Button) findViewById(R.id.btn_five);
		btnSix = (Button) findViewById(R.id.btn_six);
		btnSeven = (Button) findViewById(R.id.btn_seven);
		btnEight = (Button) findViewById(R.id.btn_eight);
		btnNine = (Button) findViewById(R.id.btn_nine);
		btnZero = (Button) findViewById(R.id.btn_zero);
		btnStar = (Button) findViewById(R.id.btn_star);
		btnJing = (Button) findViewById(R.id.btn_jing);
		btnCall.setOnClickListener(this);
		btnOne.setOnClickListener(this);
		btnTwo.setOnClickListener(this);
		btnThree.setOnClickListener(this);
		btnFour.setOnClickListener(this);
		btnFive.setOnClickListener(this);
		btnSix.setOnClickListener(this);
		btnSeven.setOnClickListener(this);
		btnEight.setOnClickListener(this);
		btnNine.setOnClickListener(this);
		btnZero.setOnClickListener(this);
		btnStar.setOnClickListener(this);
		btnJing.setOnClickListener(this);
		etShowNum = (EditText) findViewById(R.id.et_show_num);
		ibDelete = (ImageButton) findViewById(R.id.ib_delete);
		ibDelete.setOnClickListener(this);
		dialerPresenter = new DialerPresenter(this);
	}

	@Override
	public void hideImageDelete() {
		ibDelete.setVisibility(View.GONE);
	}

	@Override
	public void showImageDelete(ImageButton ib) {
		ib.setVisibility(View.VISIBLE);
	}

	@Override
	public void back(ImageButton ib) {
		ibDelete.setVisibility(View.GONE);
		DialerActivity.this.finish();
	}

	@Override
	public void call(EditText etShowNum, StringBuffer sbShowNum) {
	}

	@Override
	public void showToastInputIsNull() {
		MyToast.showToast(this, R.string.input_no_null);
	}

	@Override
	public void showToastLoginFirst() {
		MyToast.showToast(this, R.string.please_login_first);
	}

	@Override
	public void startToLoginActivity() {
		startActivity(new Intent(this,LoginActivity.class));
	}
}
