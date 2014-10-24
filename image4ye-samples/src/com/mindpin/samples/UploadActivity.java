package com.mindpin.samples;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.mindpin.Image4ye;
import com.nostra13.universalimageloader.core.ImageLoader;
import roboguice.util.RoboAsyncTask;

/**
 * Created by dd on 14-7-20.
 */
public class UploadActivity extends Activity implements View.OnClickListener {
    private static final int GET_IMAGE_FROM_ALBUM = 1;
    private ImageView iv_image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);
        init();
    }

    private void init() {
        findViewById(R.id.btn_upload).setOnClickListener(this);
        iv_image = (ImageView) findViewById(R.id.iv_image);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upload:
                choose_image();
                break;
//            case R.id.iv_image:
//                break;
        }
    }

    private void choose_image() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GET_IMAGE_FROM_ALBUM);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GET_IMAGE_FROM_ALBUM:
                    if (null != data) {

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        upload_image(picturePath);
                    }
                    break;
            }
        }
    }

    private void upload_image(final String image_path) {
        if (image_path == null) {
            System.out.println("not image_path cancel upload");
            return;
        }
        Image4ye.upload(image_path, new Image4ye.Image4yeUploadListener() {
            @Override
            public void start() {
                Toast.makeText(UploadActivity.this, "开始上传" + image_path, Toast.LENGTH_LONG).show();
            }

            @Override
            public void end(Image4ye u) {
                // 方法运行在 UI线程，做一些UI操作
                // 拿到 url
                String url = u.url;
                System.out.println("u.url:" + url);

                int width = 100;
                int height = 100;
                boolean crop = true;
                // 拿到指定尺寸的 url
                String crop_url = u.url(width, height, crop);
                Toast.makeText(UploadActivity.this, "crop_url:" + crop_url, Toast.LENGTH_LONG).show();
                System.out.println("crop_url:" + crop_url);
                ImageLoader.getInstance().displayImage(crop_url, iv_image);
            }
        });
    }
}