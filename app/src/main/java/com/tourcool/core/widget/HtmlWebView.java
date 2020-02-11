package com.tourcool.core.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.just.agentweb.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月11日9:38
 * @Email: 971613168@qq.com
 */
public class HtmlWebView extends WebView {

    private List<String> listImgSrc = new ArrayList<>();

    public void setHtml(String html){
        loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
    }

    public HtmlWebView(Context context) {
        super(context);
        init(context);
    }

    public HtmlWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HtmlWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void init(Context context) {
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setDefaultTextEncodingName("UTF -8");
        this.setHorizontalScrollBarEnabled(false);//水平不显示
        this.setVerticalScrollBarEnabled(false); //垂直不显示
        this.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setDefaultTextEncodingName("UTF -8");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.getSettings().setMixedContentMode(WebSettings
                    .MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        //载入js
        this.addJavascriptInterface(new MyJavascriptInterface(context), "imageListener");
        //获取 html
        this.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        this.setWebViewClient(new WebViewClient() {
            // 网页跳转
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && !url.isEmpty() && onUrlClickListener != null) {
                    return onUrlClickListener.urlClicked(url);
                } else return true;
            }

            // 网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // web 页面加载完成，添加监听图片的点击 js 函数
                HtmlWebView.this.setImageClickListner();
                //解析 HTML
                HtmlWebView.this.parseHTML(view);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }
        });
        this.setOnLongClickListener(v -> {
            setWebImageLongClickListener(v);
            return false;
        });
    }
    /**
     * 响应长按点击事件
     * @param v
     */
    private void setWebImageLongClickListener(View v) {
        if (v instanceof WebView) {
            HitTestResult result = ((WebView) v).getHitTestResult();
            if (result != null) {
                int type = result.getType();
                if (type == HitTestResult.IMAGE_TYPE || type == HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    if(imageLongClickListener!=null){
                        imageLongClickListener.imageLongClicked(result.getExtra());
                    }
                }
            }
        }
    }
    /**
     * 解析 HTML 该方法在 setWebViewClient 的 onPageFinished 方法中进行调用
     *
     * @param view
     */
    private void parseHTML(WebView view) {
        view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
                + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    }

    /**
     * 注入 js 函数监听，这段 js 函数的功能就是，遍历所有的图片，并添加 onclick 函数，实现点击事件，
     * 函数的功能是在图片点击的时候调用本地java接口并传递 url 过去
     */
    private void setImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        this.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imageListener.startShowImageActivity(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    // js 通信接口，定义供 JavaScript 调用的交互接口
    private class MyJavascriptInterface {
        private Context context;

        public MyJavascriptInterface(Context context) {
            this.context = context;
        }


        @android.webkit.JavascriptInterface
        public void startShowImageActivity(String url) {
            for (int i = 0; i < listImgSrc.size(); i++) {
                if (listImgSrc.get(i).equals(url)){
                    imageClickListener.imageClicked(listImgSrc, i);
                    break;
                }
            }
        }
    }

    /**
     * 图片点击回调
     *
     * @param imageClickListener 回调
     * @return RichTextConfigBuild
     */
    public HtmlWebView imageClick(OnImageClickListener imageClickListener) {
        this.imageClickListener = imageClickListener;
        return this;
    }

    private OnImageClickListener imageClickListener;

    public interface OnImageClickListener {
        /**
         * 图片被点击后的回调方法
         *
         * @param imageUrls 本篇富文本内容里的全部图片
         * @param position  点击处图片在imageUrls中的位置
         */
        void imageClicked(List<String> imageUrls, int position);
    }
    /**
     * 图片长按回调
     *
     * @param imageLongClickListener 回调
     * @return RichTextConfigBuild
     */
    public HtmlWebView imageLongClick(OnImageLongClickListener imageLongClickListener) {
        this.imageLongClickListener = imageLongClickListener;
        return this;
    }

    private OnImageLongClickListener imageLongClickListener;

    public interface OnImageLongClickListener {
        /**
         * 图片被长按后的回调方法
         *
         * @param imageUrl 长按图片地址
         */
        void imageLongClicked(String imageUrl);
    }
    /**
     * 链接点击回调
     *
     * @param onUrlClickListener 回调
     * @return RichTextConfigBuild
     */
    public HtmlWebView urlClick(OnUrlClickListener onUrlClickListener) {
        this.onUrlClickListener = onUrlClickListener;
        return this;
    }

    private OnUrlClickListener onUrlClickListener;

    public interface OnUrlClickListener {

        /**
         * 超链接点击得回调方法
         *
         * @param url 点击得url
         * @return true：已处理，false：未处理（会进行默认处理）
         */
        boolean urlClicked(String url);
    }

    private class InJavaScriptLocalObj {
        /**
         * 获取要解析 WebView 加载对应的 Html 文本
         *
         * @param html WebView 加载对应的 Html 文本
         */
        @android.webkit.JavascriptInterface
        public void showSource(String html) {
            //从 Html 文件中提取页面所有图片对应的地址对象
            listImgSrc = getImgSrc(html);
        }
    }

    private List<String> getImgSrc(String htmlStr) {
        Document document = Jsoup.parse(htmlStr);
        Elements images = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
        List<String> pics = new ArrayList<>();
        for (Element image : images) {
            pics.add(image.attr("src"));
        }
        return pics;
    }

    //重写onScrollChanged 方法
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        scrollTo(0, 0);
    }
}
