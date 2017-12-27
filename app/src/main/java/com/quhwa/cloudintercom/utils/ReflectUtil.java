package com.quhwa.cloudintercom.utils;

import java.lang.reflect.Method;

public class ReflectUtil {
	public static void invoke(Class<?> clazz, String methodName) {
		try {
			Method m = clazz.getDeclaredMethod(methodName);
			m.invoke(clazz.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
