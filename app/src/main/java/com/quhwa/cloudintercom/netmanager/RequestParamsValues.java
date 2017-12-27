package com.quhwa.cloudintercom.netmanager;

import java.util.HashMap;
/**
 * 网络请求参数类
 *
 * @author lxz
 * @date 2017年3月23日
 */
public class RequestParamsValues
{
  private HashMap<String, String> strRequestParams = new HashMap<String, String>();
  
  public HashMap<String, String> addRequestParams(HashMap<String, String> requestParams)
  {
    for (String key : requestParams.keySet())
    {
      String value = String.valueOf(requestParams.get(key));
      this.strRequestParams.put(key, value);
    }
    System.out.println(this.strRequestParams.toString());
    return this.strRequestParams;
  }
}
