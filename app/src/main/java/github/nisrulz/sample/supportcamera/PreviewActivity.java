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

import github.nisrulz.lib.supportcamera.SupportCameraManager;
import github.nisrulz.lib.supportcamera.PictureCapturedListener;

public class PreviewActivity extends AppCompatActivity {

    private FrameLayout cameraPreviewLayout;
    SupportCameraManager supportCameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        cameraPreviewLayout = (FrameLayout) findViewById(R.id.camera_preview);

        supportCameraManager = new SupportCameraManager(this);
        supportCameraManager.init(new PictureCapturedListener() {
            @Override
            public void onPictureCaptured(Bitmap bitmap) {
                String imagePath = supportCameraManager.storeImage(bitmap);
                Intent intent = new Intent(PreviewActivity.this, MainActivity.class);
                if (imagePath != null) {
                    intent.putExtra("bmp", imagePath);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PreviewActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        }, cameraPreviewLayout);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_take_pic);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportCameraManager.takePicture();
            }
        });
    }
}
