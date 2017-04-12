package com.ctvit.dev.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

/**
 * Created by jack on 2016/7/13.
 */
public class H5Tools {

    private H5Tools(){}

    /**
     * 将cookie同步到WebView
     * @param url WebView要加载的url
     * @return true 同步cookie成功，false同步cookie失败
     * @Author JPH
     */
    @SuppressLint("NewApi")
	public static boolean syncCookie(Context context ,WebView webView,String url,String cookie) {
        CookieManager cookieManager = CookieManager.getInstance();
       /* if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            cookieManager.setAcceptThirdPartyCookies(webView,true);
        } else {
            cookieManager.setAcceptCookie(true);
        }*/
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, cookie);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        CookieSyncManager.getInstance().sync();
        String newCookie = cookieManager.getCookie(url);
        return TextUtils.isEmpty(newCookie)?false:true;
    }

    

}
