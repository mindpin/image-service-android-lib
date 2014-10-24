package com.mindpin.samples;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setListAdapter(
                new SimpleAdapter(
                        this, getData(), android.R.layout.simple_list_item_1, new String[]{"title"},
                        new int[]{android.R.id.text1}
                )
        );
        getListView().setScrollbarFadingEnabled(false);
    }

    private void init() {


        DisplayImageOptions options;
        ImageLoaderConfiguration config;


        options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .build();

        config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                // 设置缓存图片的宽度跟高度
                .memoryCacheExtraOptions(480, 800)
                .diskCacheExtraOptions(480, 800, null)

                        // 通过 LruMemoryCache 实现缓存机制
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)

                        // 限制缓存文件数量百分比
                .memoryCacheSizePercentage(13)

                .diskCacheSize(50 * 1024 * 1024)

                        // 硬盘缓存文件数量
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(options)


                .build();

        ImageLoader.getInstance().init(config);

    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, Object> map = (Map<String, Object>) l.getItemAtPosition(position);
        Intent intent = new Intent(this, (Class<? extends Activity>) map.get("activity"));
        startActivity(intent);
    }

    private List<? extends Map<String, ?>> getData() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        addItem(data, "upload", UploadActivity.class);
        addItem(data, "crop", CropActivity.class);
//        addItem(data, "other", OtherActivity.class);

        return data;
    }

    private void addItem(List<Map<String, Object>> data, String title,
                         Class<? extends Activity> activityClass) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", data.size() + ". " + title);
        map.put("activity", activityClass);
        data.add(map);
    }
}