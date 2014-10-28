package com.mindpin;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;

import java.io.File;

/**
 * Created by dd on 14-10-24.
 */
public class Image4ye {
    private static final String FORMAT_URL_CROP = "%s@%dw_%dh_1e_1c.png";
    private static final String FORMAT_URL = "%s@%dw_%dh.png";
    public String url;

    public Image4ye(String url) {
        this.url = url;
    }

    public String url(int width, int height, boolean crop) {
        if (crop)
            return String.format(FORMAT_URL_CROP, url, width, height);
        else
            return String.format(FORMAT_URL, url, width, height);
    }

    public static void upload(File image_file, Image4yeUploadListener listener) {
        listener.start();
        Image4yeUploadParam param = new Image4yeUploadParam(image_file, listener);
        new UploadTask().execute(param);
    }

    public void download(int width, int height, boolean crop, Image4yeDownloadListener listener) {
        if (TextUtils.isEmpty(url)) {
            System.out.println("not image_path cancel upload");
            // todo raise
            return;
        }
        listener.start();
        String url = url(width, height, crop);
        Image4yeDownloadParam param = new Image4yeDownloadParam(url, listener);
        new DownloadTask().execute(param);
    }

    public static interface Image4yeUploadListener {
        public void start();

        public void end(Image4ye u);
    }

    public static interface Image4yeDownloadListener {
        public void start();

        public void end(File download_image_file);
    }

    private static class Image4yeUploadParam {
        public final File image_file;
        public final Image4yeUploadListener listener;

        public Image4yeUploadParam(File image_file, Image4yeUploadListener listener) {
            this.image_file = image_file;
            this.listener = listener;
        }
    }

    private static class Image4yeDownloadParam {
        public final String url;
        public final Image4yeDownloadListener listener;

        public Image4yeDownloadParam(String url, Image4yeDownloadListener listener) {
            this.url = url;
            this.listener = listener;
        }
    }

    private static class DownloadTask extends AsyncTask<Image4yeDownloadParam, Void, File> {
        Image4yeDownloadParam param;

        @Override
        protected File doInBackground(Image4yeDownloadParam... params) {
            param = params[0];
            File file = HttpApi.download(param.url);
            return file;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            param.listener.end(file);
        }
    }

    private static class UploadTask extends AsyncTask<Image4yeUploadParam, Void, Image4ye> {
        Image4yeUploadParam param;

        @Override
        protected Image4ye doInBackground(Image4yeUploadParam... params) {
            param = params[0];
            Image4ye image4ye = HttpApi.upload(param.image_file);
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
        private static final String TMP_PATH_DIR = "/image4ye/cache";

        public static Image4ye upload(File image_file) {
            HttpRequest request = HttpRequest.post(URL_UPLOAD);
            request.part("file", image_file.getPath(), image_file);
            if (request.ok()) {
                String body = request.body();
                return new Gson().fromJson(body, Image4ye.class);
            } else
                return null;
        }

        public static File download(String url) {
            try {
                File output = new File(get_tmp_path());
                HttpRequest.get(url).receive(output);
                return output;
            } catch (Exception ex) {
                return null;
            }
        }

        private static String get_tmp_path() {
            String tmp_dir_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    TMP_PATH_DIR;
            File file_dir = new File(tmp_dir_path);
            if (file_dir.exists()) {
                return tmp_dir_path + "/" + String.valueOf(System.currentTimeMillis());
            } else {
                try {
                    boolean result = file_dir.mkdirs();
                    if (result) {
                        return tmp_dir_path + "/" + String.valueOf(System.currentTimeMillis());
                    } else {
                        Log.i("get_tmp_path", "目录创建失败");
                    }
                } catch (SecurityException se) {
                    Log.i("get_tmp_path目录创建失败 ", se.toString());
                }
            }
            return null;
        }
    }
}
