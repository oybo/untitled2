package com.qkl.online.mining.app.utils.glide;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.qkl.online.mining.app.R;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by ooo on 2017/8/19.
 */

public class GlideImageLoader {

    // 加载网络资源
    public static void loadImage(Context context, String url, ImageView imageView) {
        if (imageView == null || TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .placeholder(0)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    // 加载网络资源
    public static void loadYanZhengMaImage(Context context, String url, ImageView imageView) {
        if (imageView == null || TextUtils.isEmpty(url)) {
            return;
        }
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.updatelogo)
                .error(R.drawable.yanzhengma)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    // 加载本地资源
    public static void loadWelComeImage(Context context, int resid, ImageView imageView) {
        Glide.with(context)
                .load(resid)
                .placeholder(0)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static boolean isHaveCache(Context context, String imgUrl) {
        File file = null;
        try {
            file = Glide.with(context)
                    .load(imgUrl)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (file != null && file.exists());
    }

}
