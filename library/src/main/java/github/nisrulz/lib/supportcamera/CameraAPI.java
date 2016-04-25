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
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.widget.FrameLayout;
import android.widget.Toast;

public class CameraAPI {

    private Camera camera;
    private PictureCapturedListener pictureCapturedListener;
    private Activity activity;
    private Context context;
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
                int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
                Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);

                if (bm == null) {
                    Toast.makeText(context, "Captured image is empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // Notice that width and height are reversed
                    Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
                    int w = scaled.getWidth();
                    int h = scaled.getHeight();
                    // Setting post rotate to 90
                    Matrix mtx = new Matrix();
                    mtx.postRotate(90);
                    // Rotating Bitmap
                    bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
                } else {// LANDSCAPE MODE
                    //No need to reverse width and height
                    Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
                    bm = scaled;
                }


                pictureCapturedListener.onPictureCaptured(bm);
            }
            camera.startPreview();
        }
    };

    public CameraAPI(Activity activity) {
        this.activity = activity;
        context = activity.getApplicationContext();
    }

    public void init(FrameLayout previewLayout, PictureCapturedListener pictureCapturedListener) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.pictureCapturedListener = pictureCapturedListener;
        camera = checkDeviceCamera();

        ImageSurfaceView imageSurfaceView = new ImageSurfaceView(activity, context, camera);
        previewLayout.addView(imageSurfaceView);

    }

    private Camera checkDeviceCamera() {
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mCamera;
    }

    public void takePicture() {
        camera.takePicture(null, null, pictureCallback);
    }
}
