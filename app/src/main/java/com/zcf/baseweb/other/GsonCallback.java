package com.zcf.baseweb.other;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.callback.AbsCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

public abstract class GsonCallback<T> extends AbsCallback<T> {


    public GsonCallback() {
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        String s = response.body().string();
        Type genericType = this.getClass().getGenericSuperclass();
        ParameterizedType type = (ParameterizedType) genericType;
        Type[] genericTypes = type.getActualTypeArguments();

        Class<T> tClass = (Class<T>) genericTypes[0];
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls(); //重点
        Gson gson = gsonBuilder.create();
        T tClassa = gson.fromJson(s, tClass);
        return tClassa;
    }
}
