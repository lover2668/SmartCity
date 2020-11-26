package com.tourcool.widget.webview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

import java.util.List;


/**
 * 富文本内部图片点击事件
 * （js调用Android）
 */
public class AndroidInterface {
    private Handler deliver = new Handler(Looper.getMainLooper());
    private Context context;
    private List<String> imgs;

    public AndroidInterface(Context context) {
        this.context = context;
    }


    public AndroidInterface(Context context, List<String> imgs) {
        this.context = context;
        this.imgs = imgs;
    }


    private static final String TAG = "AndroidInterface";

    @JavascriptInterface
    public void openImage(String img, int pos) {
        Log.e(TAG, "openImage: " + new Gson().toJson(imgs));
        deliver.post(new Runnable() {
            @Override
            public void run() {
                //imagesize是作为loading时的图片size
                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(100, 100);
                ImagePagerActivity.startImagePagerActivity(context, imgs, pos, imageSize);
            }
        });

    }
}
