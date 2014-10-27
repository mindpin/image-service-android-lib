package com.mindpin.samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.mindpin.Image4ye;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by dd on 14-10-24.
 */
public class DownloadActivity extends Activity implements View.OnClickListener {
    private ImageView iv_image;
    private EditText et_url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);
        init();
    }

    private void init() {
        findViewById(R.id.btn_download).setOnClickListener(this);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        et_url = (EditText) findViewById(R.id.et_url);
        ImageLoader.getInstance().displayImage(et_url.getText().toString(), iv_image);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_download) {
            download();
        }
    }

    private void download() {
        String url = et_url.getText().toString();
        Image4ye u = new Image4ye(url);

        int width = 100;
        int height = 100;
        boolean crop = true;
        // 下载
        u.download(width, height, crop, new Image4ye.Image4yeDownloadListener() {
            public void start() {
                // 方法运行在 UI线程，做一些UI操作
            }

            public void end(File download_image_file) {
                // 方法运行在 UI线程，做一些UI操作
            }
        });
    }
}