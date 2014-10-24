package com.mindpin.samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.mindpin.Image4ye;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by dd on 14-10-24.
 */
public class CropActivity extends Activity implements View.OnClickListener {
    private ImageView iv_image;
    private EditText et_url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop);
        init();
    }

    private void init() {
        findViewById(R.id.btn_crop).setOnClickListener(this);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        et_url = (EditText) findViewById(R.id.et_url);
        ImageLoader.getInstance().displayImage(et_url.getText().toString(), iv_image);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_crop){
            crop();
        }
    }

    private void crop() {
        Image4ye image4ye = new Image4ye(et_url.getText().toString());
        int width = 100;
        int height = 100;
        boolean crop = true;
        // 拿到指定尺寸的 url
        String crop_url = image4ye.url(width, height, crop);
        Toast.makeText(this, crop_url, Toast.LENGTH_LONG).show();
        ImageLoader.getInstance().displayImage(crop_url, iv_image);
    }
}