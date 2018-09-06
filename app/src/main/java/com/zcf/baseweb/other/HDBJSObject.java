package com.zcf.baseweb.other;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.maple.msdialog.ActionSheetDialog;
import com.zcf.baseweb.MainActivity;


import wendu.dsbridge.CompletionHandler;

import static com.zcf.baseweb.MainActivity.IMAGE_PICKER;


/**
 * Created by Administrator on 2017/3/18.
 */

public class HDBJSObject {

    /*
     * 绑定的object对象
     * */
    private MainActivity context;
    private String imgId, userid;


    CompletionHandler<Integer> handler;

    private ImagePicker imagePicker;

    private String DEF_BLUE = "#037BFF";

    public HDBJSObject(MainActivity context, ImagePicker imagePicker) {
        this.context = context;
        this.imagePicker = imagePicker;

    }

    /*
     *获取通讯录
     * */
    @JavascriptInterface
    public void getContacts(Object data, CompletionHandler<String> handler) {
        handler.complete(context.getContactJson());
    }


    /**
     * 单张图片上传
     *
     * @param data    传递的参数
     * @param handler 回掉的函数
     *                mark  图片id
     *                userid   用户id
     */
    @JavascriptInterface
    public void uploadImg(Object data, CompletionHandler<Integer> handler) {
        this.handler = handler;
        Log.e("tag", data.toString());
        try {
            JSONObject jsonObject = JSON.parseObject(data.toString());
            imgId = jsonObject.getString("mark");
            userid = jsonObject.getString("userid");
            imagePicker.setSelectLimit(1);
            Intent intent = new Intent(context, ImageGridActivity.class);
            context.startActivityForResult(intent, IMAGE_PICKER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ActionSheetDialog(context)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .addSheetItem("相册", Color.parseColor(DEF_BLUE), new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int i) {
                        Intent intent = new Intent(context, ImageGridActivity.class);  //打开相册
                        context.startActivityForResult(intent, 300);
                    }
                })
                .addSheetItem("拍照", Color.parseColor(DEF_BLUE), new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int i) {
                        Intent intent = new Intent(context, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        context.startActivityForResult(intent, 300);
                    }
                })
                .setCancelText("取消")
                .show();
    }


    /**
     * 多张图片上传
     *
     * @param data    传递的参数
     * @param handler 回掉的函数
     *                mark  图片id
     *                userid   用户id
     */
    @JavascriptInterface
    public void uploadImgList(Object data, CompletionHandler<Integer> handler) {
        this.handler = handler;
        Log.e("tag", data.toString());
        try {
            JSONObject jsonObject = JSON.parseObject(data.toString());
            userid = jsonObject.getString("userid");
            String count = jsonObject.getString("count");
            imagePicker.setSelectLimit(Integer.valueOf(count));
            Intent intent = new Intent(context, ImageGridActivity.class);
            context.startActivityForResult(intent, IMAGE_PICKER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public CompletionHandler<Integer> getHandeler() {

        return handler;
    }


    public String getImId() {

        return imgId;

    }


    public String getUserid() {

        return userid;

    }


}
