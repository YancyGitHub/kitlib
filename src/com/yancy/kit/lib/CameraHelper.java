package com.yancy.kit.lib;

import java.io.IOException;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.util.Log;

@SuppressWarnings("deprecation")
public class CameraHelper {
    
    private static CameraHelper mCameraHelper;
    
    private Camera mCamera;
    
    public static CameraHelper getInstance() {
        if(mCameraHelper == null) {
            mCameraHelper = new CameraHelper();
        }
        
        return mCameraHelper;
    }
    
    private CameraHelper(){
        
    }
    
    public void openCamera(int camId, int w, int h) {
        Parameters parameters = null;
        mCamera = Camera.open(camId);
        if (mCamera != null) {
            mCamera.setDisplayOrientation(90);
            parameters = mCamera.getParameters();
            parameters.setPreviewFormat(ImageFormat.NV21);
            parameters.setPictureSize(w, h);
            parameters.setPreviewSize(w, h);
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            final int size = w * h * ImageFormat.getBitsPerPixel(parameters
                    .getPreviewFormat()) / 8;
            mCamera.addCallbackBuffer(new byte[size]);
            mCamera.setPreviewCallbackWithBuffer(new PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    mCamera.addCallbackBuffer(new byte[size]);
                    Log.d("DEMO", "onPreviewFrame");
                }
            });
            mCamera.setParameters(parameters);
            mCamera.autoFocus(null);
        }
    }
    
    public void startPreview(SurfaceTexture st) {
        if(mCamera == null) throw new IllegalStateException("Camera is null");
        
        try {
            mCamera.setPreviewTexture(st);
        } catch (IOException e) { }
        mCamera.startPreview();
    }
    
    public void stopCamera() {
        if(mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
    }
    
    public int createTextureID()  
    {  
        int[] texture = new int[1];  
  
        GLES20.glGenTextures(1, texture, 0);  
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);  
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);          
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);  
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);  
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);  
  
        return texture[0];  
    }  
}













































