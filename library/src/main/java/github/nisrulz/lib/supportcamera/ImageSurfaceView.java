/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.nisrulz.lib.supportcamera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;

public class ImageSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

  private Camera camera;
  private SurfaceHolder surfaceHolder;
  Activity activity;

  public ImageSurfaceView(Activity activity, Context context, Camera camera) {
    super(context);
    this.activity = activity;
    this.camera = camera;
    this.surfaceHolder = getHolder();
    this.surfaceHolder.addCallback(this);
  }

  @Override public void surfaceCreated(SurfaceHolder holder) {
    try {

      camera.setPreviewDisplay(holder);
      setCameraDisplayOrientation(activity, Camera.CameraInfo.CAMERA_FACING_BACK, camera);
      camera.startPreview();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

  }

  @Override public void surfaceDestroyed(SurfaceHolder holder) {
    camera.stopPreview();
    camera.release();
  }

  public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
    Camera.CameraInfo info = new Camera.CameraInfo();
    Camera.getCameraInfo(cameraId, info);
    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    int degrees = 0;
    switch (rotation) {
      case Surface.ROTATION_0:
        degrees = 0;
        break;
      case Surface.ROTATION_90:
        degrees = 90;
        break;
      case Surface.ROTATION_180:
        degrees = 180;
        break;
      case Surface.ROTATION_270:
        degrees = 270;
        break;
    }

    int result;
    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      result = (info.orientation + degrees) % 360;
      result = (360 - result) % 360; // compensate the mirror
    } else { // back-facing
      result = (info.orientation - degrees + 360) % 360;
    }
    camera.setDisplayOrientation(result);
  }
}
