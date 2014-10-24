package com.mindpin;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 14-10-24.
 */
public class Image4ye {
    private static final String FORMAT_URL_CROP = "%s@%dw_%dh_1e_1c.png";
    private static final String FORMAT_URL = "%s@%dw_%dh.png";
    public String url;

    public String url(int width, int height, boolean crop) {
        if(crop)
            return String.format(FORMAT_URL_CROP, url, width, height);
        else
            return String.format(FORMAT_URL,url, width, height);
    }

    public static void upload(String image_file_path, Image4yeUploadListener listener) {
        listener.start();
        Image4yeUploadParam param = new Image4yeUploadParam(image_file_path, listener);
        if (TextUtils.isEmpty(image_file_path)) {
            System.out.println("not image_path cancel upload");
            // todo raise
            return;
        }
        new DownloadFilesTask().execute(param);
    }

    public static interface Image4yeUploadListener {
        public void start();

        public void end(Image4ye u);
    }

    private static class Image4yeUploadParam {

        public final String image_file_path;
        public final Image4yeUploadListener listener;

        public Image4yeUploadParam(String image_file_path, Image4yeUploadListener listener) {
            this.image_file_path = image_file_path;
            this.listener = listener;
        }
    }

    private static class DownloadFilesTask extends AsyncTask<Image4yeUploadParam, Void, Image4ye> {
        Image4yeUploadParam param;

        @Override
        protected Image4ye doInBackground(Image4yeUploadParam... params) {
            param = params[0];
            Image4ye image4ye = HttpApi.upload(param.image_file_path);
            return image4ye;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Image4ye image4ye) {
            super.onPostExecute(image4ye);
            param.listener.end(image4ye);
        }
    }

    private static class HttpApi {
        public static final String URL_UPLOAD = "http://img.4ye.me/api/upload";

        public static Image4ye upload(String image_path) {
            HttpRequest request = HttpRequest.post(URL_UPLOAD);
            request.part("file", image_path, new File(image_path));
            if (request.ok()) {
                String body = request.body();
                return new Gson().fromJson(body, Image4ye.class);
            } else
                return null;
        }
    }
}
