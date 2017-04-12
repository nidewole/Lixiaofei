package com.ctvit.dev.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by jack on 2016/7/11.
 * 基础公用webView
 */
public class H5webView extends WebView{
    public H5webView(Context context) {
        super(context);
    }

    public H5webView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public H5webView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
