package com.quhwa.cloudintercom.receiver;

import com.quhwa.cloudintercom.service.PJSipService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ProtectReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, PJSipService.class));
	}

}
