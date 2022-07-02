package com.example.study_webview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.study_webview.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    protected ActivityMainBinding binding;

    public static final int FILECHOOSER_RESULTCODE = 5173;
    public static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 5174;
    private ValueCallback<Uri[]> filePathCallback5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
    }

    private void initView() {
        WebSettings settings = binding.webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        binding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                String[] acceptTypes = fileChooserParams.getAcceptTypes();
                LogUtils.e("-->>" + GsonUtils.toJson(acceptTypes));
                Log.e("-->>", GsonUtils.toJson(fileChooserParams));
//                openFileInput(filePathCallback, false, true, "image/*");

                // 文件选择
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);*/

                filePathCallback5 = filePathCallback;
                chooseFile(MainActivity.this, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
                return true;
            }
        });

        String targetUrl = "file:///android_asset/web/test.html";
        binding.webView.loadUrl(targetUrl);
    }

    protected void openFileInput(ValueCallback<Uri[]> filePathCallback,
                                 final boolean allowMultiple,
                                 final boolean capture,
                                 String acceptType) {
        if (filePathCallback5 != null) {
            filePathCallback5.onReceiveValue(null);
        }
        filePathCallback5 = filePathCallback;

        if (!TextUtils.isEmpty(acceptType) && acceptType.startsWith("image")) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple && Build.VERSION.SDK_INT >= 18);
            if (intent.resolveActivity(this.getPackageManager()) != null) {
                try {
//                    startActivityForResult(intent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
                    startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
                } catch (ActivityNotFoundException e) {
                    filePathCallback5 = null;
                }
            }
        } else {
            Log.w("-->>", "Please contact developers to support other files.");
            // 无法处理也要回调给H5，否则H5页面无法继续响应用户
            if (filePathCallback5 != null) {
                filePathCallback5.onReceiveValue(null);
                filePathCallback5 = null;
            }
        }
    }

    private void startActivityForResultSafe(Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (resultCode == Activity.RESULT_OK) {
                if (filePathCallback5 != null) {
                    LogUtils.e("-->>" + GsonUtils.toJson(data));
                    // 数据存储在data里面
                    Uri picUri = data.getData();
                    String path = UriUtils.getFileAbsolutePath(getApplicationContext(),picUri);
                    LogUtils.e("-->>" + path);
                    LogUtils.e("-->>" + GsonUtils.toJson(data) + " , " + picUri);
                    Uri[] photoUris = null;
                    // TODO 获取所选图片的Uri数组

                    // TODO 是否需要对所有图片作压缩处理？
                    filePathCallback5.onReceiveValue(photoUris);
                    filePathCallback5 = null;
                }
            } else {
                // 用户取消选择，也要回调给前端
                if (filePathCallback5 != null) {
                    filePathCallback5.onReceiveValue(null);
                    filePathCallback5 = null;
                }
            }
        }
    }

    public static void openDirChooseFile(Activity activity, int requestCode, String[] mimeTypes) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        if (mimeTypes != null) {
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        // 您的 Intent 必须指定 MIME 类型，并且必须声明 CATEGORY_OPENABLE 类别。必要时，您可以使用 EXTRA_MIME_TYPES extra 添加一个 MIME 类型数组来指定多个 MIME类型。如果您这样做，必须将 setType() 中的主 MIME 类型设置为 "*/*"。
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//多选
        activity.startActivityForResult(intent, requestCode);
    }

    public static void chooseFile(Activity activity, int requestCode) {
        String[] mimeTypes = {MimeType.JPG, MimeType.BMP, MimeType.PNG, MimeType.TXT, MimeType.PDF};
//        FileUtil.openDirChooseFile(activity, requestCode, mimeTypes);

        openDirChooseFile(activity, requestCode, mimeTypes);
    }

    public class MimeType {
        public static final String JPG = "image/jpeg";
        public static final String BMP = "image/bmp";
        public static final String PNG = "image/png";
        public static final String TXT = "text/plain";
        public static final String PDF = "application/pdf";
    }
}