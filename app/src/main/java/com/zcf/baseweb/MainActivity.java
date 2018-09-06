package com.zcf.baseweb;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jwenfeng.library.pulltorefresh.BaseRefreshListener;
import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.view.CropImageView;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;
import com.zcf.baseweb.bean.PhoneBean;
import com.zcf.baseweb.other.GlideImageLoader;
import com.zcf.baseweb.other.HDBJSObject;
import com.zcf.baseweb.other.OpenFileWebChromeClient;
import com.zcf.baseweb.view.LoadHelpView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import face.com.zdl.cctools.Permission.PermissionResultCallBack;
import face.com.zdl.cctools.Permission.PermissionUtil;
import wendu.dsbridge.DWebView;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.native_web)
    DWebView nativeWeb;
    @BindView(R.id.pulltorefresh)
    PullToRefreshLayout pulltorefresh;
    public static final int IMAGE_PICKER = 300;//单张图片的上传参数
    private static final int IMAGE_PICKERs = 301;//多张图片的上传参数

    private String adUrl = "http://172.16.1.135:8020/together-vue/dist/index.html?__hbt=1535020004606#/";
    private OpenFileWebChromeClient mOpenFileWebChromeClient;
    List<PhoneBean> phoneBeans = new ArrayList<>();
    private HDBJSObject hdbjsObject;
    private LoadingDialog loadingDialog;
    private ImagePicker imagePicker;
    private LoadHelpView loadHelpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
        loadHelpView = new LoadHelpView(nativeWeb);
        pulltorefresh.setCanLoadMore(false);

        pulltorefresh.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                nativeWeb.loadUrl(adUrl);
            }

            @Override
            public void loadMore() {

            }
        });


        webset();

        imagepicSet();
        getPermiss();

        getContactJson();
        hdbjsObject = new HDBJSObject(this, imagePicker);

    }

    public String getContactJson() {
        if (getPermiss()) {
            List<PhoneBean> phoneBeans = getRedBag();
            String ee = new Gson().toJson(phoneBeans);
            return ee;
        } else {

        }

        return "";
    }

    private void imagepicSet() {
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
    }

    private void webset() {

        mOpenFileWebChromeClient = new OpenFileWebChromeClient(this);
        DWebView.setWebContentsDebuggingEnabled(true);
        nativeWeb.addJavascriptObject(hdbjsObject, null);
        //辅助WebView处理图片上传操作
//        nativeWeb.setWebChromeClient(new MyChromeWebClient());
        //注意第二个参数JsTest，这个是JS网页调用Android方法的一个类似ID的东西
        nativeWeb.loadUrl(adUrl);

        nativeWeb.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && nativeWeb.canGoBack()) {
                        if (adUrl.contains("m=Index&a=index")) {
                            return false;
                        } else if (adUrl.contains("m=Help&a=index")) {
                            return false;
                        } else if (adUrl.contains("m=User&a=index")) {
                            return false;
                        } else {
                            nativeWeb.goBack();
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        nativeWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                adUrl = url;
                return false;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                //设置loading时显示的文字
                loadingDialog = new LoadingDialog(MainActivity.this)
                        .setLoadingText("加载中...");
                loadingDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (pulltorefresh != null) {
                    pulltorefresh.finishRefresh();
                }

                if (loadingDialog != null) {
                    loadingDialog.close();

                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("Tag", "");
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e("Tag", "");

                loadHelpView.showError(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("tag", "");
                        nativeWeb.loadUrl(adUrl);
                    }
                });

            }
        });
    }

    boolean isopen = false;

    public boolean getPermiss() {
        PermissionUtil.getInstance().request(this, new String[]{READ_CONTACTS}, new PermissionResultCallBack() {
            @Override
            public void onPerAllAllow() {
                isopen = true;
            }

            @Override
            public void onPerAllowList(String... permissions) {

            }

            @Override
            public void onPerNegativeAndNoRemind(String... permissions) {
                isopen = false;
            }

            @Override
            public void onPerNegativeAndRemind(String... permissions) {
                isopen = false;
            }
        });
        return isopen;
    }

    public List<PhoneBean> getRedBag() {
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME}, null, null, null);

        String[] arr = new String[cursor.getCount()];
        int i = 0;

        if (cursor != null && cursor.moveToFirst()) {
            do {

                String phonestr = null;

                PhoneBean phoneBean = new PhoneBean();

                long id = cursor.getLong(0);

                String name = cursor.getString(1);

                String[] phoneProjection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};

                arr[i] = String.valueOf(id) + " , 姓名：" + name;
                phoneBean.setName(name);
                Cursor phonesCusor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, phoneProjection, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);

                if (phonesCusor != null && phonesCusor.moveToFirst()) {
                    do {
                        String num = phonesCusor.getString(0);
                        arr[i] += " , 电话号码：" + num;
                        phonestr += "$num,";
                    } while (phonesCusor.moveToNext());
                }

                String rr = phonestr.substring(0, phonestr.length() - 1);
                phoneBean.setPhone(rr);
                phoneBeans.add(phoneBean);
                i++;
            } while (cursor.moveToNext());

            return phoneBeans;
        } else {
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

            } else {
                Toast.makeText(MainActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
