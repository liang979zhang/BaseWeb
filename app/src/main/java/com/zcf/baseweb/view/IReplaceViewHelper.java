package com.zcf.baseweb.view;

import android.content.Context;
import android.view.View;

public interface  IReplaceViewHelper {

    public abstract View getCurrentLayout();

    public abstract void dismissView();

    public abstract void showLayout(View view);

    public abstract void showLayout(int layoutId);

    public abstract View inflate(int layoutId);

    public abstract Context getContext();

    public abstract View getView();


}
