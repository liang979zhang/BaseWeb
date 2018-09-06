package com.zcf.baseweb.view;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zcf.baseweb.R;

public class LoadHelpView {
    private IReplaceViewHelper helper;
    private AnimationDrawable animationDrawable;

    public LoadHelpView(View view) {
        this(new ReplaceViewHelper(view));
    }

    public LoadHelpView(IReplaceViewHelper helper) {
        super();
        this.helper = helper;
    }

    // 数据异常
    public void showError(String errorText, String buttonText, int picResId, View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.load_error);
        // 设置图片
        ImageView mIvShowPic = (ImageView) layout.findViewById(R.id.mIvShowPic);
        mIvShowPic.setBackgroundResource(picResId);
        // 设置提示文字
        TextView mTvTip = (TextView) layout.findViewById(R.id.mTvTip);
        mTvTip.setText(errorText);

        // 设置按钮
        TextView mTvBtn = (TextView) layout.findViewById(R.id.mTvBtn);
        mTvBtn.setText(buttonText);
//        mTvBtn.setOnClickListener(onClickListener);

        LinearLayout ll_err = layout.findViewById(R.id.ll_err);
        ll_err.setOnClickListener(onClickListener);

        helper.showLayout(layout);
    }

    // 数据异常简易版
    public void showError(View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.load_error);
        TextView mTvBtn = (TextView) layout.findViewById(R.id.mTvBtn);
//        mTvBtn.setOnClickListener(onClickListener);
        LinearLayout ll_err = layout.findViewById(R.id.ll_err);
        ll_err.setOnClickListener(onClickListener);

        helper.showLayout(layout);
    }


    // 空数据
    public void showEmpty(String errorText, String buttonText, int picResId, View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.load_empty);

        ImageView mIvShowPic = (ImageView) layout.findViewById(R.id.mIvShowPic);
        mIvShowPic.setBackgroundResource(picResId);

        TextView mTvTip = (TextView) layout.findViewById(R.id.mTvTip);
        mTvTip.setText(errorText);

        TextView mTvBtn = (TextView) layout.findViewById(R.id.mTvBtn);
        mTvBtn.setText(buttonText);
//        mTvBtn.setOnClickListener(onClickListener);
        LinearLayout ll_ll2 = layout.findViewById(R.id.ll_ll2);
        ll_ll2.setOnClickListener(onClickListener);
        helper.showLayout(layout);
    }

    // 空数据简易版
    public void showEmpty(View.OnClickListener onClickListener) {
        View layout = helper.inflate(R.layout.load_empty);
        TextView mTvBtn = (TextView) layout.findViewById(R.id.mTvBtn);
        mTvBtn.setOnClickListener(onClickListener);
        helper.showLayout(layout);
    }

//    // 正在加载
//    public void showLoading(String loadText) {
//        View layout = helper.inflate(R.layout.load_ing);
//        TextView mTvTip = (TextView) layout.findViewById(R.id.mTvTip);
//        ImageView mIvAnim = (ImageView) layout.findViewById(R.id.mIvAnim);
//        mIvAnim.setImageResource(R.drawable.loading_animation);
//        animationDrawable = (AnimationDrawable) mIvAnim.getDrawable();
//        animationDrawable.start(); // 开启帧动画
//        mTvTip.setText(loadText);
//        helper.showLayout(layout);
//    }

    public void dismiss() {
        helper.dismissView();
    }

}
