package org.pjsip;

import java.util.List;

import android.hardware.Camera;
import android.util.Log;

public class PjCameraInfo
{
  public int facing;
  public int orient;
  public int[] supportedSize;
  public int[] supportedFps1000;
  public int[] supportedFormat;
  
  private static int[] IntegerListToIntArray(List<Integer> list)
  {
    int[] li = new int[list.size()];
    int i = 0;
    for (Integer e : list) {
      li[(i++)] = e.intValue();
    }
    return li;
  }
  
  private static int[] IntArrayListToIntArray(List<int[]> list)
  {
    int[] li = new int[list.size() * 2];
    int i = 0;
    for (int[] e : list)
    {
      li[(i++)] = e[0];
      li[(i++)] = e[1];
    }
    return li;
  }
  
  private static int[] CameraSizeListToIntArray(List<Camera.Size> list)
  {
    int[] li = new int[list.size() * 2];
    int i = 0;
    for (Camera.Size e : list)
    {
      li[(i++)] = e.width;
      li[(i++)] = e.height;
    }
    return li;
  }
  
  public static int GetCameraCount()
  {
    return Camera.getNumberOfCameras();
  }
  
  public static PjCameraInfo GetCameraInfo(int idx)
  {
	  Camera cam;
    if ((idx < 0) || (idx >= GetCameraCount())) {
      return null;
    }
    try
    {
    	
      cam = Camera.open(idx);
    }
    catch (Exception e)
    {
     
      Log.d("IOException", e.getMessage());
      return null;
    }
    PjCameraInfo pjci = new PjCameraInfo();
    
    Camera.CameraInfo ci = new Camera.CameraInfo();
    Camera.getCameraInfo(idx, ci);
    
    pjci.facing = ci.facing;
    pjci.orient = ci.orientation;
    
    Camera.Parameters param = cam.getParameters();
    cam.release();
    cam = null;
    
    pjci.supportedFormat = IntegerListToIntArray(
      param.getSupportedPreviewFormats());
    pjci.supportedFps1000 = IntArrayListToIntArray(
      param.getSupportedPreviewFpsRange());
    pjci.supportedSize = CameraSizeListToIntArray(
      param.getSupportedPreviewSizes());
    
    return pjci;
  }
}

