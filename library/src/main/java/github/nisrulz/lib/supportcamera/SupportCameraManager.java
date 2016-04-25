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
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SupportCameraManager {


    private CameraAPI cameraAPI;
    private Camera2API camera2API;
    private static SupportCameraManager INSTANCE;


    private SupportCameraManager() {

    }

    public static SupportCameraManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SupportCameraManager();
        }
        return INSTANCE;
    }

    public void init(Activity activity, FrameLayout cameraPreviewLayout, PictureCapturedListener pictureCapturedListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            camera2API = new Camera2API(activity);
            camera2API.init(cameraPreviewLayout, pictureCapturedListener);

            Log.d("TAG", "Camera API 2 Loaded !");
        } else {
            cameraAPI = new CameraAPI(activity);
            cameraAPI.init(cameraPreviewLayout, pictureCapturedListener);


            Log.d("TAG", "Camera API 1 Loaded !");
        }

    }

    public void takePicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            camera2API.takePicture();

            Log.d("TAG", "Camera API 2 used to take picture !");
        } else {
            cameraAPI.takePicture();


            Log.d("TAG", "Camera API 1 used to take picture !");
        }
    }

    public String storeImage(Activity activity, Bitmap image) {
        File pictureFile = getOutputMediaFile(activity);
        if (pictureFile == null) {
            System.out.println(
                    "Error creating media file, check storage permissions: ");
            return null;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            return pictureFile.getAbsolutePath();

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error accessing file: " + e.getMessage());
        }
        return null;
    }


    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(Activity activity) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + activity.getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}
