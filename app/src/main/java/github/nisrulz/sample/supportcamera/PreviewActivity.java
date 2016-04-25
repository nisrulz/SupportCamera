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

package github.nisrulz.sample.supportcamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import github.nisrulz.lib.supportcamera.PictureCapturedListener;
import github.nisrulz.lib.supportcamera.SupportCameraManager;

public class PreviewActivity extends AppCompatActivity {

    private FrameLayout cameraPreviewLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        cameraPreviewLayout = (FrameLayout) findViewById(R.id.camera_preview);

        SupportCameraManager.getInstance().init(this, cameraPreviewLayout, pictureCapturedListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_take_pic);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SupportCameraManager.getInstance().takePicture();
            }
        });
    }

    PictureCapturedListener pictureCapturedListener = new PictureCapturedListener() {
        @Override
        public void onPictureCaptured(Bitmap bitmap) {
            String imagePath = SupportCameraManager.getInstance().storeImage(PreviewActivity.this, bitmap);
            Intent intent = new Intent(PreviewActivity.this, MainActivity.class);
            if (imagePath != null) {
                intent.putExtra("bmp", imagePath);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(PreviewActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
