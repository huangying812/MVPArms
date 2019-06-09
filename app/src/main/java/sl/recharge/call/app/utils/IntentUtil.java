package sl.recharge.call.app.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import sl.recharge.call.BuildConfig;
import sl.recharge.call.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import me.jessyan.autosize.utils.LogUtils;

public class IntentUtil {

    public static boolean isIntentSafe(Context context, Intent i) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(i, 0);
        return activities != null && activities.size() > 0;
    }

    /**
     * 用其他浏览器打开网页
     */
    public static void openUriByBrowser(Context context, String webUri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri content_url = Uri.parse(webUri);
        intent.setData(content_url);
        IntentUtil.startActivity(context, intent);
    }

    /**
     * 去应用市场评分
     */
    public static void goMarketToScore(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            IntentUtil.startActivity(context, intent);
        } catch (ActivityNotFoundException e) {
        }
    }

    /**
     * 获得某个应用的启动Intent
     *
     * @param packageName 要启动应用的包名
     */
    public static Intent getLaunchIntent(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }


    /**
     * 获得这个手机是否安装了某些app
     *
     * @param context
     * @param packagenames
     * @return
     */
    public static List<String> getInstallPkgs(Context context, List<String> packagenames) {
        PackageInfo packageInfo;
        List<String> pkgs = new ArrayList<>();
        for (String packagename : packagenames) {
            try {
                packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
            } catch (PackageManager.NameNotFoundException e) {
                packageInfo = null;
                e.printStackTrace();
            }
            if (packageInfo != null) {
                pkgs.add(packagename);
            }
        }
        return pkgs;
    }

    /**
     * 避免有些地方通过Application启动Activity造成的异常
     */
    public static void startActivity(Context context, Intent intent) {
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (isIntentSafe(context, intent)) {//启动前判断intent是否安全，
            context.startActivity(intent);
        } else {//不安全，拒绝本次操作，并土司提示
            Toast.makeText(context, "未知意图：" + intent.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 应用前后台的切换
     *
     * @param taskId
     */
    public static void startActivity(Context context, int taskId) {
        if (context instanceof Activity) {
            //获取ActivityManager
            ActivityManager mAm = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            //并启动task栈顶activity，将taskId切换到前台
            mAm.moveTaskToFront(taskId, 0);
        }
    }

    /**
     * 发送文本到其他应用
     */
    public static void sendText(Context context, String text, ResolveInfo resolveInfo) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setClassName(resolveInfo.activityInfo.packageName,
                resolveInfo.activityInfo.name);
        IntentUtil.startActivity(context, intent);
    }

    /**
     * 获取安装App(支持6.0)的意图
     *
     * @param context
     * @param filePath 文件路径
     * @return intent
     */
    public static Intent getInstallAppIntent(Context context, String filePath) {
        return getInstallAppIntent(context, new File(filePath));
    }

    /**
     * 获取安装App(支持6.0)的意图
     *
     * @param context
     * @param file    文件
     * @return intent
     */
    public static Intent getInstallAppIntent(Context context, File file) {
        if (file == null) {
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT <= 23) {
            String type = "application/vnd.android.package-archive";
            intent.setDataAndType(Uri.fromFile(file), type);
        } else {
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.FILE_AUTHORITY, file);
            intent.setData(uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void sendSMS(Context context, String phone, String smsBody) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + phone));
        intent.putExtra("sms_body", smsBody);
        IntentUtil.startActivity(context, intent);
    }

    /**
     * 通过qq号开始聊天，传入string型的qq号
     */
    public static void contactByQQ(Context context, String qqNumStr) {
        /*if (checkApkExist(mActivity, "com.tencent.mobileqq") && false){
            String qqUrl = StringUtils.format(R.string.placeholder_online_consultation_qq, qqNumStr);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl));
            IntentUtil.startActivity(mActivity, intent);
        }else{
            ToastUtil.showToastInfo("本机未安装QQ应用",false);
            openUriByBrowser(mActivity, qqUrlweb);
        }*/
        String qqUrlweb = StringUtils.format(context, R.string.placeholder_online_consultation_qqweb, qqNumStr);
        String title = StringUtils.format(context, R.string.placeholder_title_customer_qq, qqNumStr);
//        CommonWebviewActivity.launch(context, qqUrlweb, title);
    }

    /**
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     **/
    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(context, R.string.qq_client_inavailable, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static void makePhoneCall(Context context, String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        IntentUtil.startActivity(context, intent);
    }

    private static final HashMap<Class, Map<String, List>> cachedIntentLists = new HashMap<>();

    /**
     * 如果要向Intent里面传List，使用此方法避免数据量太大导致的闪退问题
     *
     * @param activityClass 要传向的Activity的class
     * @param key           列表对应的标识
     * @param listValue     列表数值
     *                      <p>
     *                      例如：
     *                      Intent intent = new Intent();
     *                      intent.setClass(activity, ImagePreviewActivity.class);
     *                      IntentUtil.cacheIntentList(ImagePreviewActivity.class, IMAGE_INFO, imageList);
     *                      activity.startActivity(intent);
     */
    public static <T> void cacheIntentList(@NonNull Class activityClass, @NonNull String key, List<T> listValue) {
        if (listValue != null) {
            Map<String, List> map = cachedIntentLists.get(activityClass);
            if (map == null) {
                map = new HashMap<>();
                cachedIntentLists.put(activityClass, map);
            }
            map.put(key, listValue);
        }
    }

    /**
     * 获取缓存的Intent列表。执行完成之后就会清除缓存，不能再次获取传过来的数据。
     *
     * @param activityClass 要读取数据的界面
     * @param key           数据的key
     *                      <p>
     *                      例如：
     *                      List<FileIdPath> imageInfo = IntentUtil.getCachedIntentList(ImagePreviewActivity.class, IMAGE_INFO);
     */
    @Nullable
    public static <T> List<T> getCachedIntentList(@NonNull Class activityClass, @NonNull String key) {
        Map<String, List> map = cachedIntentLists.get(activityClass);
        if (map != null) {
            List data = map.remove(key);
            if (data != null) {
                List<T> ret = new ArrayList<>(data.size());
                ret.addAll(data);
                return ret;
            }
        }
        return null;
    }

    private static final HashMap<Class, Map<String, Object>> cachedIntentObject = new HashMap<>();

    /**
     * 如果要向Intent里面传List，使用此方法避免数据量太大导致的闪退问题
     *
     * @param activityClass 要传向的Activity的class
     * @param key           列表对应的标识
     * @param value         列表数值
     */
    public static void cacheIntentStr(@NonNull Class activityClass, @NonNull String key, String value) {
        if (value != null) {
            Map<String, Object> map = cachedIntentObject.get(activityClass);
            if (map == null) {
                map = new HashMap<>();
                cachedIntentObject.put(activityClass, map);
            }
            map.put(key, value);
        }
    }

    /**
     * 获取缓存的Intent列表。执行完成之后就会清除缓存，不能再次获取传过来的数据。
     *
     * @param activityClass 要读取数据的界面
     * @param key           数据的key
     */
    @Nullable
    public static String getCachedIntentStr(@NonNull Class activityClass, @NonNull String key, String defaltValue) {
        Map<String, Object> map = cachedIntentObject.get(activityClass);
        if (map != null) {
            Object ret = map.remove(key);
            if (ret != null && ret instanceof String) {
                return (String) ret;
            }
        }
        return defaltValue;
    }

    /**
     * 如果要向Intent里面传List，使用此方法避免数据量太大导致的闪退问题
     *
     * @param activityClass 要传向的Activity的class
     * @param key           列表对应的标识
     * @param value         列表数值
     */
    public static void cacheIntentObject(@NonNull Class activityClass, @NonNull String key, Object value) {
        if (value != null) {
            Map<String, Object> map = cachedIntentObject.get(activityClass);
            if (map == null) {
                map = new HashMap<>();
                cachedIntentObject.put(activityClass, map);
            }
            map.put(key, value);
        }
    }

    /**
     * 获取缓存的Intent列表。执行完成之后就会清除缓存，不能再次获取传过来的数据。
     *
     * @param activityClass 要读取数据的界面
     * @param key           数据的key
     */
    @Nullable
    public static <T> T getCachedIntentObject(@NonNull Class activityClass, @NonNull String key, T defaltValue) {
        try {
            return (T) cachedIntentObject.get(activityClass).remove(key);
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
        }
        return defaltValue;
    }

}
