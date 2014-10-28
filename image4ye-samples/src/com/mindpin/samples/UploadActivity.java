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

import java.io.File;

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
        switch (v.getId()) {
            case R.id.btn_upload:
                choose_image();
                break;
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
        if (resultCode == RESULT_OK && requestCode == GET_IMAGE_FROM_ALBUM && null != data) {
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
    }

    private void upload_image(final String image_path) {
        if (image_path == null) {
            Toast.makeText(UploadActivity.this, "图片路径为空，取消上传" + image_path, Toast.LENGTH_LONG).show();
            return;
        }

        File image_file;
        try {
            image_file = new File(image_path);
        } catch (Exception ex) {
            Toast.makeText(UploadActivity.this, "打开文件失败:" + image_path, Toast.LENGTH_LONG).show();
            return;
        }

        Image4ye.upload(image_file, new Image4ye.Image4yeUploadListener() {
            @Override
            public void start() {
                // 方法运行在 UI线程，做一些UI操作
                Toast.makeText(UploadActivity.this, "开始上传,image_path:" + image_path, Toast.LENGTH_LONG).show();
            }

            @Override
            public void end(Image4ye u) {
                // 方法运行在 UI线程，做一些UI操作
                // 拿到 url
                String url = u.url;
                int width = 100;
                int height = 100;
                boolean crop = true;
                // 拿到指定尺寸的 url
                String crop_url = u.url(width, height, crop);
                ImageLoader.getInstance().displayImage(crop_url, iv_image);
                Toast.makeText(UploadActivity.this, "上传成功,crop url:" + crop_url, Toast.LENGTH_LONG).show();
            }
        });
    }
}