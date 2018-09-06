package com.zcf.baseweb.other;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.lzy.imagepicker.ui.ImageGridActivity;
import com.zcf.baseweb.MainActivity;

import static com.zcf.baseweb.MainActivity.IMAGE_PICKER;


/**
 * Created by Administrator on 2018/4/21.
 */

public class OpenFileWebChromeClient extends WebChromeClient {
    public ValueCallback<Uri> mFilePathCallback;
    public ValueCallback<Uri[]> mFilePathCallbacks;
    public static final int REQUEST_FILE_PICKER = 1;

    private Activity context;

    public OpenFileWebChromeClient(MainActivity mainActivity) {
        this.context = mainActivity;
    }
//

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        mFilePathCallbacks = filePathCallback;
        Intent intent = new Intent(context, ImageGridActivity.class);
        context.startActivityForResult(intent, IMAGE_PICKER);

        return true;
    }





    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);

        Log.e("Tag", "");
    }
}
