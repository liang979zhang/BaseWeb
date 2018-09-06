package com.zcf.baseweb;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }



    @Test
    public void aa() {
        try {
            JSONObject jo =   new  JSONObject("{a:null,b:\"null\"}");
            Object o = jo.get("a");
            Log.e("tag", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}