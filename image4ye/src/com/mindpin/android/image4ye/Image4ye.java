package com.mindpin.android.image4ye;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;

/**
 * Created by dd on 14-10-24.
 */
public class Image4ye {
    com.mindpin.java.image4ye.Image4ye image4ye;
    public String url;

    public Image4ye(String url) {
        image4ye = new com.mindpin.java.image4ye.Image4ye(url);
        this.url = image4ye.url;
    }

    public String url(int width, int height, boolean crop) {
        return image4ye.url(width, height, crop);
    }

    public static void upload(File image_file, Image4yeUploadListener listener) {
        if (image_file == null) {
            System.out.println("not image_file cancel upload");
            throw new NullPointerException("image_file is null");
        }
        listener.start();
        Image4yeUploadParam param = new Image4yeUploadParam(image_file, listener);
        new UploadTask().execute(param);
    }

    public void download(int width, int height, boolean crop, Image4yeDownloadListener listener) {
        listener.start();
        Image4yeDownloadParam param = new Image4yeDownloadParam(image4ye, width, height, crop, listener);
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
        public final com.mindpin.java.image4ye.Image4ye image4ye;
        public final Image4yeDownloadListener listener;
        public final int width, height;
        public final boolean crop;

        public Image4yeDownloadParam(com.mindpin.java.image4ye.Image4ye image4ye, int width, int height, boolean crop, Image4yeDownloadListener listener) {
            this.image4ye = image4ye;
            this.width = width;
            this.height = height;
            this.crop = crop;
            this.listener = listener;
        }
    }

    private class DownloadTask extends AsyncTask<Image4yeDownloadParam, Void, File> {
        Image4yeDownloadParam param;

        @Override
        protected File doInBackground(Image4yeDownloadParam... params) {
            param = params[0];
            try {
                File download = param.image4ye.download(param.width, param.height, param.crop);
                url = param.image4ye.url;
                return download;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
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
            com.mindpin.java.image4ye.Image4ye upload = com.mindpin.java.image4ye.Image4ye.upload(param.image_file);
            return new Image4ye(upload.url);
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
}
