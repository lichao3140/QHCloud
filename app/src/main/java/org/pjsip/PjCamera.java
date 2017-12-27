package org.pjsip;

import java.io.IOException;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PjCamera
  implements Camera.PreviewCallback, SurfaceHolder.Callback
{
  private final String TAG = "PjCamera";
  private Camera camera = null;
  private boolean isRunning = false;
  private int camIdx;
  private long userData;
  private Param param = null;
  private SurfaceView surfaceView = null;
  private SurfaceHolder surfaceHolder = null;
  private SurfaceTexture surfaceTexture = null;
  
  public PjCamera(int idx, int w, int h, int fmt, int fps, long userData_, SurfaceView surface)
  {
    this.camIdx = idx;
    this.userData = userData_;
    
    this.param = new Param();
    this.param.width = w;
    this.param.height = h;
    this.param.format = fmt;
    this.param.fps1000 = fps;
    
    SetSurfaceView(surface);
  }
  
  public void SetSurfaceView(SurfaceView surface)
  {
    boolean isCaptureRunning = this.isRunning;
    if (isCaptureRunning) {
      Stop();
    }
    if (surface != null)
    {
      this.surfaceView = surface;
      this.surfaceHolder = this.surfaceView.getHolder();
    }
    else
    {
      this.surfaceHolder = null;
      this.surfaceView = null;
      if (this.surfaceTexture == null) {
        this.surfaceTexture = new SurfaceTexture(10);
      }
    }
    if (isCaptureRunning) {
      Start();
    }
  }
  
  public int SwitchDevice(int idx)
  {
    boolean isCaptureRunning = this.isRunning;
    int oldIdx = this.camIdx;
    if (isCaptureRunning) {
      Stop();
    }
    this.camIdx = idx;
    if (isCaptureRunning)
    {
      int ret = Start();
      if (ret != 0)
      {
        this.camIdx = oldIdx;
        Start();
        return ret;
      }
    }
    return 0;
  }
  
  public int Start()
  {
    try
    {
      this.camera = Camera.open(this.camIdx);
    }
    catch (Exception e)
    {
      Log.d("IOException", e.getMessage());
      return -10;
    }
    try
    {
      if (this.surfaceHolder != null)
      {
        this.camera.setPreviewDisplay(this.surfaceHolder);
        this.surfaceHolder.addCallback(this);
      }
      else
      {
        this.camera.setPreviewTexture(this.surfaceTexture);
      }
    }
    catch (IOException e)
    {
      Log.d("IOException", e.getMessage());
      return -20;
    }
    Camera.Parameters cp = this.camera.getParameters();
    cp.setPreviewSize(this.param.width, this.param.height);
    cp.setPreviewFormat(this.param.format);
    try
    {
      this.camera.setParameters(cp);
    }
    catch (RuntimeException e)
    {
      Log.d("RuntimeException", e.getMessage());
      return -30;
    }
    this.camera.setPreviewCallback(this);
    this.camera.startPreview();
    this.isRunning = true;
    
    return 0;
  }
  
  public void Stop()
  {
    this.isRunning = false;
    if (this.camera == null) {
      return;
    }
    if (this.surfaceHolder != null) {
      this.surfaceHolder.removeCallback(this);
    }
    this.camera.setPreviewCallback(null);
    this.camera.stopPreview();
    this.camera.release();
    this.camera = null;
  }
  
  native void PushFrame(byte[] paramArrayOfByte, int paramInt, long paramLong);
  
  public void onPreviewFrame(byte[] data, Camera camera)
  {
    if (this.isRunning) {
      PushFrame(data, data.length, this.userData);
    }
  }
  
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
  {
    Log.d("PjCamera", "VideoCaptureAndroid::surfaceChanged");
  }
  
  public void surfaceCreated(SurfaceHolder holder)
  {
    Log.d("PjCamera", "VideoCaptureAndroid::surfaceCreated");
    try
    {
      if (this.camera != null) {
        this.camera.setPreviewDisplay(holder);
      }
    }
    catch (IOException e)
    {
      Log.e("PjCamera", "Failed to set preview surface!", e);
    }
  }
  
  public void surfaceDestroyed(SurfaceHolder holder)
  {
    Log.d("PjCamera", "VideoCaptureAndroid::surfaceDestroyed");
    try
    {
      if (this.camera != null) {
        this.camera.setPreviewDisplay(null);
      }
    }
    catch (IOException e)
    {
      Log.e("PjCamera", "Failed to clear preview surface!", e);
    }
    catch (RuntimeException e)
    {
      Log.w("PjCamera", "Clear preview surface useless", e);
    }
  }
  
  public class Param
  {
    public int width;
    public int height;
    public int format;
    public int fps1000;
    
    public Param() {}
  }
}

