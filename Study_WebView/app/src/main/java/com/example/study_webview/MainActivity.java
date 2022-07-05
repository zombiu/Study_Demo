package com.example.study_webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.JsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.study_webview.databinding.ActivityMainBinding;
import com.example.study_webview.databinding.DialogSelectorBinding;
import com.fish.easystoragelib.fileprovider.EasyFileProvider;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    protected ActivityMainBinding binding;
    private DialogSelectorBinding dialogSelectorBinding;

    public static final int FILECHOOSER_RESULTCODE = 5173;
    public static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 5174;
    private ValueCallback<Uri[]> filePathCallback5;

    private File outputImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();
        String filePath = getFilesDir() + File.separator + "yx_demo.apk";
        File file = new File(filePath);
        LogUtils.e("-->>file=" + file);
        // FileProvider 本质上起了一个映射表的作用
        Uri uriForFile = AppFileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);

//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        LogUtils.e("-->>uri=" + uriForFile);

        Intent intent = new Intent();
        EasyFileProvider.fillIntent(this, new File(filePath), intent, true);

//        FileProvider7
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
                openFileInput(filePathCallback, false, true, "image/*");

                // 文件选择
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);*/

                filePathCallback5 = filePathCallback;
//                chooseFile(MainActivity.this, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
                return true;
            }
        });

        String targetUrl = "file:///android_asset/web/test.html";
        binding.webView.loadUrl(targetUrl);

        binding.btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
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
                    e.printStackTrace();
                    LogUtils.e("-->>Activity未找到 " + e.getMessage());
                    // 无法处理也要回调给H5，否则H5页面无法继续响应用户
                    if (filePathCallback5 != null) {
                        filePathCallback5.onReceiveValue(null);
                        filePathCallback5 = null;
                    }
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
            // todo 可能需要把数据返回的整个代码块进行 try catch
            if (resultCode == Activity.RESULT_OK) {
                if (filePathCallback5 != null) {
                    LogUtils.e("-->>" + GsonUtils.toJson(data));
                    // 数据存储在data里面
                    Uri picUri = data.getData();
                    String path = UriUtils.getFileAbsolutePath(getApplicationContext(), picUri);
                    LogUtils.e("-->>" + path);
                    LogUtils.e("-->>" + GsonUtils.toJson(data) + " , " + picUri);

                    if (picUri != null) {
                        Uri[] photoUris = new Uri[]{picUri};
                        filePathCallback5.onReceiveValue(photoUris);
                        filePathCallback5 = null;
                        return;
                    }
                }
            }
            // 用户取消选择，也要回调给前端null 否则页面会阻塞住
            if (filePathCallback5 != null) {
                filePathCallback5.onReceiveValue(null);
                filePathCallback5 = null;
            }
        } else if (RequestCode.OPEN_FILE_PICKER == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                binding.imageView.setImageURI(imageUri);
            }
        } else if (RequestCode.OPEN_CAMERA == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                LogUtils.e("-->>" + GsonUtils.toJson(data));
                if (outputImageFile != null) {
                    Uri uri = Uri.fromFile(outputImageFile);
                    binding.imageView.setImageURI(uri);
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

    /*public void takePhotoNoCompress(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(new Date()) + ".png";
            File file = new File(Environment.getExternalStorageDirectory(), filename);
            mCurrentPhotoPath = file.getAbsolutePath();

            Uri fileUri = FileProvider7.getUriForFile(this, file);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
        }
    }*/


    public class MimeType {
        public static final String JPG = "image/jpeg";
        public static final String BMP = "image/bmp";
        public static final String PNG = "image/png";
        public static final String TXT = "text/plain";
        public static final String PDF = "application/pdf";
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialogSelectorBinding = DialogSelectorBinding.inflate(getLayoutInflater());
//        View view = getLayoutInflater().inflate(R.layout.dialog_selector, null);
        builder.setView(dialogSelectorBinding.getRoot());
        AlertDialog dialog = builder.create();
        dialog.show();

        dialogSelectorBinding.openFileSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openDirChooseFile(MainActivity.this, RequestCode.OPEN_FILE_PICKER, null);
            }
        });

        dialogSelectorBinding.openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                outputImageFile = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
                if (outputImageFile.exists()) {
                    outputImageFile.delete();
                }
                Uri imageUri = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    imageUri = FileProvider.getUriForFile(MainActivity.this, getPackageName() + ".fileprovider", outputImageFile);
                } else {
                    imageUri = Uri.fromFile(outputImageFile);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, RequestCode.OPEN_CAMERA);
            }
        });
    }
}