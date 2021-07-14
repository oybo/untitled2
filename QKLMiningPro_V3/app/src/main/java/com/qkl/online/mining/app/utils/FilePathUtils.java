package com.qkl.online.mining.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.qkl.online.mining.app.AppContext;

import java.io.File;

/**
 * 文件路径工具类
 *
 * @author o
 */
public class FilePathUtils {

    /**
     * 存放HTML压缩包解压路径
     */
    public static final String UNZIP_PATH_NAME = "unZip";
    /**
     * 下载文件路径
     */
    public static final String FILE_PATH_NAME = "file";
    /**
     * 保存图片路径
     */
    public static final String IMAGE_PATH_NAME = "image";
    /**
     * 音频路径
     */
    public static final String RECORD_PATH_NAME = "record";
    /**
     * 视频路径
     */
    public static final String VIDEO_PATH_NAME = "video";
    /**
     * 日志文件路径
     */
    public static final String LOG_PATH_NAME = "log";
    // 插件路径
    public static final String PLUGIN = "plugin";

    private Context mContext;
    private static FilePathUtils mInstance;

    public static FilePathUtils getInstance() {
        if (mInstance == null) {
            synchronized (FilePathUtils.class) {
                if (mInstance == null) {
                    mInstance = new FilePathUtils(AppContext.getInstance());
                }
            }
        }

        return mInstance;
    }

    private FilePathUtils(Context context) {
        mContext = context.getApplicationContext();
    }

    public static boolean isSdCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获得当前应用默认的解压路径
     *
     * @return
     */
    public String getDefaultUnzipFile() {
        return getPath(UNZIP_PATH_NAME);
    }

    public String getDownloadPath() {
        String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download";
        return downloadPath;
    }

    public String getCameraImagePath() {
        String cameraImagePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "image";
        return cameraImagePath;
    }

    public String getBookCachePath(String pathName) {
        return getPath(pathName);
    }

    /**
     * 获取SD卡目录下相对应包名程序下的文件保存的图片的路径
     *
     * @return
     */
    public String getDefaultFilePath() {
        return getPath(FILE_PATH_NAME);
    }

    /**
     * 获取SD卡目录下相对应包名程序下的拍照保存的图片的路径
     *
     * @return
     */
    public String getDefaultImagePath() {
        return getPath(IMAGE_PATH_NAME);
    }

    /**
     * 获取SD卡目录下相对应包名程序下的录音保存的图片的路径
     *
     * @return
     */
    public String getDefaultRecordPath() {
        return getPath(RECORD_PATH_NAME);
    }

    /**
     * 获取SD卡目录下相对应包名程序下的视频保存的图片的路径
     *
     * @return
     */
    public String getDefaultVideoPath() {
        return getPath(VIDEO_PATH_NAME);
    }

    /**
     * 日志保存目录
     *
     * @return
     */
    public String getDefaultLogPath() {
        return getPath(LOG_PATH_NAME);
    }

    /**
     * 插件保存目录
     *
     * @return
     */
    public String getPluginPath() {
        return getPath(PLUGIN);
    }

    /**
     * 创建根缓存目录
     *
     * @return
     */
    public static String createRootPath() {
        String cacheRootPath = "";
        if (isSdCardAvailable()) {
            // /sdcard/Android/data/<application package>/cache
            cacheRootPath = AppContext.getInstance().getExternalCacheDir().getPath();
        } else {
            // /data/data/<application package>/cache
            cacheRootPath = AppContext.getInstance().getCacheDir().getPath();
        }
        return cacheRootPath + "/cache";
    }

    /**
     * 获得SD卡上缓存目录下文件夹路径
     *
     * @param dirName
     * @return
     */
    public String getPath(String dirName) {
        File cacheDir = getDiskCacheDir(dirName);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        return cacheDir.getAbsolutePath();
    }

    /**
     * 根据传入的uniqueName获取硬盘缓存的路径地址。
     */
    @SuppressLint("NewApi")
    private File getDiskCacheDir(String uniqueName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && !Environment.isExternalStorageRemovable()) {
            // 默认第一 SD卡/Android/files目录下
            try {
                cachePath = mContext.getExternalFilesDir(null).getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(cachePath)) {
                // 默认第二 data/data/包名/files目录下
                try {
                    cachePath = mContext.getFilesDir().getAbsolutePath();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (TextUtils.isEmpty(cachePath)) {
                // 默认第三 SD卡/Android/cache目录下
                try {
                    cachePath = mContext.getExternalCacheDir().getAbsolutePath();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (TextUtils.isEmpty(cachePath)) {
                // 默认第四 data/data/包名/cache目录下
                try {
                    cachePath = mContext.getCacheDir().getAbsolutePath();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // 默认第四 data/data/包名/cache目录下
            try {
                cachePath = mContext.getCacheDir().getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new File(cachePath + File.separator + uniqueName);
    }

}
