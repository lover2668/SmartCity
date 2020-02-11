package com.frame.library.core.manager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tourcool.library.frame.demo.R;

import java.security.MessageDigest;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @Author: JenkinsZhou on 2018/7/23 10:53
 * @E-Mail: 971613168@qq.com
 * Function: Glide 工具类支持加载常规、圆角、圆形图片
 * Description:
 */
public class GlideManager {

    private static int sCommonPlaceholder = -1;
    private static int sCirclePlaceholder = -1;
    private static int sRoundPlaceholder = -1;

    private static Drawable sCommonPlaceholderDrawable;
    private static Drawable sCirclePlaceholderDrawable;
    private static Drawable sRoundPlaceholderDrawable;
    @ColorInt
    private static int mPlaceholderColor = Color.LTGRAY;
    private static float mPlaceholderRoundRadius = 4f;

    private static void setDrawable(GradientDrawable gd, float radius) {
        gd.setColor(mPlaceholderColor);
        gd.setCornerRadius(radius);
    }

    /**
     * 设置默认颜色
     *
     * @param placeholderColor
     */
    public static void setPlaceholderColor(@ColorInt int placeholderColor) {
        mPlaceholderColor = placeholderColor;
        sCommonPlaceholderDrawable = new GradientDrawable();
        sCirclePlaceholderDrawable = new GradientDrawable();
        sRoundPlaceholderDrawable = new GradientDrawable();
        setDrawable((GradientDrawable) sCommonPlaceholderDrawable, 0);
        setDrawable((GradientDrawable) sCirclePlaceholderDrawable, 10000);
        setDrawable((GradientDrawable) sRoundPlaceholderDrawable, mPlaceholderRoundRadius);
    }

    /**
     * 设置圆角图片占位背景图圆角幅度
     *
     * @param placeholderRoundRadius
     */
    public static void setPlaceholderRoundRadius(float placeholderRoundRadius) {
        mPlaceholderRoundRadius = placeholderRoundRadius;
        setPlaceholderColor(mPlaceholderColor);
    }

    /**
     * 设置圆形图片的占位图
     *
     * @param circlePlaceholder
     */
    public static void setCirclePlaceholder(int circlePlaceholder) {
        sCirclePlaceholder = circlePlaceholder;
    }

    public static void setCirclePlaceholder(Drawable circlePlaceholder) {
        sCirclePlaceholderDrawable = circlePlaceholder;
    }

    /**
     * 设置正常图片的占位符
     *
     * @param commonPlaceholder
     */
    public static void setCommonPlaceholder(int commonPlaceholder) {
        sCommonPlaceholder = commonPlaceholder;
    }

    public static void setCommonPlaceholder(Drawable commonPlaceholder) {
        sCommonPlaceholderDrawable = commonPlaceholder;
    }

    /**
     * 设置圆角图片的占位符
     *
     * @param roundPlaceholder
     */
    public static void setRoundPlaceholder(int roundPlaceholder) {
        sRoundPlaceholder = roundPlaceholder;
    }

    public static void setRoundPlaceholder(Drawable roundPlaceholder) {
        sRoundPlaceholderDrawable = roundPlaceholder;
    }

    /**
     * 普通加载图片
     *
     * @param obj
     * @param iv
     * @param placeholder
     */
    public static void loadImgCenterCrop(Object obj, ImageView iv, Drawable placeholder) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptionsCenterCrop()
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()).into(iv);
    }

    public static void loadImgCenterInside(Object obj, ImageView iv, Drawable placeholder) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptionsCenterInside()
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()).into(iv);
    }


    public static void loadImgAuto(Object obj, ImageView iv, Drawable placeholder) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptionsAuto()
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()).into(iv);
    }
    public static void loadImgAuto(Object obj, ImageView iv, Drawable placeholder,int width,int height) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptionsAuto()
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .override(width,height)
                .dontAnimate()).into(iv);
    }
    public static void loadImg(Object obj, ImageView iv, int placeholderResource) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadImgCenterCrop(obj, iv, drawable != null ? drawable : sCommonPlaceholderDrawable);
    }



    public static void loadImgCenterInside(Object obj, ImageView iv, int placeholderResource) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadImgCenterInside(obj, iv, drawable != null ? drawable : sCommonPlaceholderDrawable);
    }

    public static void loadImgAuto(Object obj, ImageView iv, int placeholderResource) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadImgAuto(obj, iv, drawable != null ? drawable : sCommonPlaceholderDrawable);
    }

    public static void loadImgBySize(Object obj, ImageView iv, int placeholderResource,int width,int height) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadImgAuto(obj, iv, drawable != null ? drawable : sCommonPlaceholderDrawable,width,height);
    }
    public static void loadImg(Object obj, ImageView iv) {
        loadImg(obj, iv, sCommonPlaceholder);
    }

    public static void loadImgCenterInside(Object obj, ImageView iv) {
        loadImgCenterInside(obj, iv, sCommonPlaceholder);
    }

    public static void loadImgAuto(Object obj, ImageView iv) {
        loadImgAuto(obj, iv, sCommonPlaceholder);
    }

    public static void loadImgBySize(Object obj, ImageView iv,int width,int height) {
        loadImgBySize(obj, iv, sCommonPlaceholder,width,height);
    }
    /**
     * 加载圆形图片
     *
     * @param obj
     * @param iv
     * @param placeholder 占位图
     */
    public static void loadCircleImg(Object obj, ImageView iv, Drawable placeholder) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptionsCenterCrop()
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()
                .transform(new CircleCrop())).into(iv);
    }

    public static void loadCircleImg(Object obj, ImageView iv, int placeholderResource) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadCircleImg(obj, iv, drawable != null ? drawable : sCirclePlaceholderDrawable);
    }

    public static void loadCircleImg(Object obj, ImageView iv) {
        loadCircleImg(obj, iv, sCirclePlaceholder);
    }

    /**
     * 加载圆角图片
     *
     * @param obj                 加载的图片资源
     * @param iv
     * @param dp                  圆角尺寸-dp
     * @param placeholder         -占位图
     * @param isOfficial-是否官方模式圆角
     */
    public static void loadRoundImg(Object obj, ImageView iv, float dp, Drawable placeholder, boolean isOfficial) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptionsCenterCrop()
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()
                .transform(isOfficial ? new RoundedCorners(dp2px(dp)) : new GlideRoundTransform(dp2px(dp)))).into(iv);
    }

    public static void loadRoundImgByListener(Object obj, ImageView iv, float dp, Drawable placeholder, boolean isOfficial, RequestListener<Drawable> listener) {
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(iv.getContext())
                .load(obj)
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()
                .transform(isOfficial ? new RoundedCorners(dp2px(dp)) : new GlideRoundTransform(dp2px(dp)))
                .listener(listener).into(iv);
        //获取图片真正的宽高
      /*  Glide.with(iv.getContext())
                .load(obj)
                .asBitmap()//强制Glide返回一个Bitmap对象
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        Log.d(TAG, "width " + width); //200px
                        Log.d(TAG, "height " + height); //200px
                    }
                });*/


    }

    public static void loadRoundImg(Object obj, ImageView iv, float dp, int placeholderResource, boolean isOfficial) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadRoundImg(obj, iv, dp, drawable != null ? drawable : sRoundPlaceholderDrawable, isOfficial);
    }

    public static void loadRoundImg(Object obj, ImageView iv, float dp, boolean isOfficial) {
        loadRoundImg(obj, iv, dp, sRoundPlaceholder, isOfficial);
    }

    public static void loadRoundImg(Object obj, ImageView iv, float dp) {
        loadRoundImg(obj, iv, dp, true);
    }

    public static void loadRoundImg(Object obj, ImageView iv, boolean isOfficial) {
        loadRoundImg(obj, iv, mPlaceholderRoundRadius, isOfficial);
    }

    public static void loadRoundImg(Object obj, ImageView iv) {
        loadRoundImg(obj, iv, true);
    }

    private static RequestOptions getRequestOptionsCenterCrop() {
        RequestOptions requestOptions = new RequestOptions()
                //优先级
                .priority(Priority.HIGH)
                .centerCrop()
                //缓存策略
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        return requestOptions;
    }

    private static RequestOptions getRequestOptionsCenterInside() {
        RequestOptions requestOptions = new RequestOptions()
                //优先级
                .priority(Priority.HIGH)
                .centerInside()
                //缓存策略
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        return requestOptions;
    }

    private static RequestOptions getRequestOptionsAuto() {
        RequestOptions requestOptions = new RequestOptions()
                //优先级
                .priority(Priority.HIGH)
                //缓存策略
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        return requestOptions;
    }
    private static int dp2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private static Drawable getDrawable(Context context, @DrawableRes int res) {
        Drawable drawable = null;
        try {
            drawable = context.getResources().getDrawable(res);
        } catch (Exception e) {

        }
        return drawable;
    }

    private static class GlideRoundTransform extends BitmapTransformation {
        int radius;

        public GlideRoundTransform(int dp) {
            super();
            this.radius = dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }
}