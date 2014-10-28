Android image4ye
============
image-service-android 核心逻辑抽取的Android库

如何引用此组件：
-------------
** 安装 **

```
git clone https://github.com/mindpin/image-service-android-lib
cd image-service-android-lib
mvn clean install
```

** maven引用 **

在maven项目，pom.xml添加以下依赖引用：

```
<dependency>
    <groupId>com.mindpin</groupId>
    <artifactId>image4ye</artifactId>
    <version>0.1.0</version>
    <type>apklib</type>
</dependency>
```

** android权限设置 **
AndroidManifest.xml 添加如下权限
```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

使用说明
---------------------
### 上传一个图片

```java
File image_file = new File("xxx");

Image4ye.upload(image_file , new Image4yeUploadListener(){
  public void start(){
    // 方法运行在 UI线程，做一些UI操作
  }

  public void end(Image4ye u){
    // 方法运行在 UI线程，做一些UI操作
    // 拿到 url
    String url = u.url;

    int width = 100;
    int height = 100;
    boolean crop = true;
    // 拿到指定尺寸的 url
    String crop_url = u.url(width, height, crop);
  }
});
```

### 获取裁切图片

```java
String url = "http://....";
Image4ye u = new Image4ye(url);

int width = 100;
int height = 100;
boolean crop = true;
// 拿到指定尺寸的 url
String crop_url = u.url(width, height, crop);
```

### 下载一个图片

```java
String url = "http://....";
Image4ye u = new Image4ye(url);

int width = 100;
int height = 100;
boolean crop = true;
// 下载
u.download(width, height,crop, new Image4yeDownloadListener(){
  public void start(){
    // 方法运行在 UI线程，做一些UI操作
  }

  public void end(File download_image_file){
   // 方法运行在 UI线程，做一些UI操作
  }
});
```


依赖库
---------------------
* [destinyd/android-archetypes][android-archetypes]


[android-archetypes]: https://github.com/destinyd/android-archetypes
