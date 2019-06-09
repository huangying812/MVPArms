package sl.recharge.call.app.utils;

import android.content.Context;
import android.text.TextUtils;

import com.plain.utils.LogUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashSet;

import androidx.annotation.NonNull;


public class StringUtils {

    private static final float BASE_UNIT = 45f / 4;

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    /**
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!TextUtils.isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * encoded in utf-8
     * <p/>
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * 正北	    （45/4*31）°≤正北＜360° 0°≤正北≤（45/4）°
     * 东北偏北	（45/4）°＜东北偏北＜（45/4*3）°
     * 东北	    （45/4*3）°≤东北≤（45/4*5）°
     * 东北偏东	（45/4*5）°＜东北偏东＜（45/4*7）°
     * 正东	    （45/4*7）°≤正东≤（45/4*9）°
     * 东南偏东	（45/4*9）°＜东南偏东＜（45/4*11）°
     * 东南	    （45/4*11）°≤东南≤（45/4*13）°
     * 东南偏南	（45/4*13）°＜东南偏南＜（45/4*15）°
     * 正南	    （45/4*15）°≤正南≤（45/4*17）°
     * 西南偏南	（45/4*17）°＜西南偏南＜（45/4*19）°
     * 西南	    （45/4*19）°≤西南≤（45/4*21）°
     * 西南偏西	（45/4*21）°＜西南偏西＜（45/4*23）°
     * 正西	    （45/4*23）°≤正西≤（45/4*25）°
     * 西北偏西	（45/4*25）°＜西北偏西＜（45/4*27）°
     * 西北	    （45/4*27）°≤西北≤（45/4*29）°
     * 西北偏北	（45/4*29）°＜西北偏北＜（45/4*31）°
     */
    public static String getDirectionStr(float azimuth) {

        String direction;

        if (azimuth < 0 || azimuth > 360) {
            direction = "--";
        } else if (azimuth <= BASE_UNIT) {
            direction = "正北";
        } else if (azimuth <= BASE_UNIT * 3) {
            direction = "东北偏北";
        } else if (azimuth <= BASE_UNIT * 5) {
            direction = "东北";
        } else if (azimuth <= BASE_UNIT * 7) {
            direction = "东北偏东";
        } else if (azimuth <= BASE_UNIT * 9) {
            direction = "正东";
        } else if (azimuth <= BASE_UNIT * 11) {
            direction = "东南偏东";
        } else if (azimuth <= BASE_UNIT * 13) {
            direction = "东南";
        } else if (azimuth <= BASE_UNIT * 15) {
            direction = "东南偏南";
        } else if (azimuth <= BASE_UNIT * 19) {
            direction = "正南";
        } else if (azimuth <= BASE_UNIT * 21) {
            direction = "西南偏南";
        } else if (azimuth <= BASE_UNIT * 23) {
            direction = "西南";
        } else if (azimuth <= BASE_UNIT * 25) {
            direction = "西南偏西";
        } else if (azimuth <= BASE_UNIT * 27) {
            direction = "正西";
        } else if (azimuth <= BASE_UNIT * 29) {
            direction = "西北偏西";
        } else if (azimuth <= BASE_UNIT * 31) {
            direction = "西北";
        } else if (azimuth <= BASE_UNIT * 29) {
            direction = "西北偏北";
        } else {
            direction = "正北";
        }
        return direction;
    }

    /**
     * sql语句转义符处理
     */
    public static String sqliteEscape(String keyWord) {
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&", "/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

    public static boolean isNotEmpty(String str) {
        return !TextUtils.isEmpty(str);
    }

    /**
     * 把整数转换成固定长度的String，不足的在前面补0 , 超过的截取后半部分
     * len是10，num是123 => 0000000123
     */
    public static String int2Str(int len, int num) {
        return long2Str(len, num);
    }

    /**
     * 把long型整数转换成固定长度的String，不足的在前面补0 , 超过的截取后半部分
     * len是10，num是123 ==> 0000000123
     * len是11，12345678999123==>45678999123
     */
    public static String long2Str(int len, long num) {
        // 0 代表前面补充0
        // 10代表长度为10
        // d 代表参数为正数型
        String str = String.format("%0" + len + "d", num);
        if (str.length() > len) {
            str = str.substring(str.length() - len);
        }
        LogUtils.debugInfo(num + "==>" + str);
        return str;
    }

    public static String double2Str(int len, double num) {
        // %.2f
        String str = String.format("%." + len + "f", num);
        LogUtils.debugInfo(num + "==>" + str);
        return str;
    }

    /**
     * 使用占位符定义String资源，然后调用填充获取目标 字符串
     * 资源文件中定义：
     * <string name="book">书名 (字符串)%1$s,作者(字符串)%2$s,编号(整数)%3$d,价格(浮点型)：%4$.2f</string>
     * format(R.string.book, "金瓶梅", "西门庆", 2249, 88.3f)的返回值是：
     * 书名 (字符串)金瓶梅,作者(字符串)西门庆,编号(整数)2249,价格(浮点型)：88.30
     */
    public static String format(Context context, int resId, @NonNull Object... args) {
        return format(getString(context, resId), args);
    }

    public static String format(String msg, @NonNull Object... args) {
        String str = null;
        try {
            str = String.format(msg, args);
        } catch (Exception e) {
            e.printStackTrace();
            str = msg;
        }
        return str;
    }

    @NonNull
    public static String getString(Context context, int resId) {
        if (resId > 0) {
            return context.getString(resId);
        } else {
            return "";
        }
    }

    public static String getTimeMS(long time) {
        //return formaterHMS.format(time);  小时出错
        int s = (int) (time / 1000);
        int hh = s / 3600;
        int mm = (s - hh * 3600) / 60;
        int ss = s - hh * 3600 - mm * 60;
        return String.format("%02d", mm) + ":" + String.format("%02d", ss);
    }

    @NonNull
    public static String getNonNullStr(String str) {
        String titl;
        if (!TextUtils.isEmpty(str)) {
            titl = str.trim();
        } else {
            titl = "";
        }
        return titl;
    }

    public static boolean containsEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i + 1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d
                        || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c
                        || hs == 0x2b1b || hs == 0x2b50 || hs == 0x231a) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() - 1) {
                    char ls = source.charAt(i + 1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return isEmoji;
    }

    public static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
        if (isEmpty(source)) {
            return source;
        }
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                buf.append(codePoint);
            }
        }
        if (buf == null) {
            return source;
        } else {
            if (buf.length() == len) {
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }
    }

    public static boolean equals(String a, String b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static boolean changed(String a, String b) {
        boolean aIsEmpty = TextUtils.isEmpty(a);
        boolean bIsEmpty = TextUtils.isEmpty(b);
        if (aIsEmpty && bIsEmpty) {
            return false;
        } else if (aIsEmpty) {
            return !TextUtils.isEmpty(b.trim());
        } else if (bIsEmpty) {
            return !TextUtils.isEmpty(a.trim());
        } else {
            return !a.trim().equals(b.trim());
        }
    }


    /**
     * 判断2个字符串是否相等
     */
    public static boolean isStringEqual(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1 != null) {
            return s1.equals(s2);
        }
        return false;
    }

    /**
     * String 转 long 主要是为了防止转换出错时闪退
     *
     * @param string
     * @return
     */
    public static long stringToLong(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            long newLong = Long.parseLong(string);
            return newLong;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return 0;
    }

    /**
     * String 转 int 主要是为了防止转换出错时闪退
     *
     * @param string
     * @return
     */
    public static int stringToInt(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            int newInteger = Integer.parseInt(string);
            return newInteger;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return 0;
    }

    /**
     * String 转 double 主要是为了防止转换出错时闪退
     *
     * @param string
     * @return
     */
    public static double stringToDouble(String string) {
        if (TextUtils.isEmpty(string)) {
            return 0;
        }
        try {
            double newDouble = Double.parseDouble(string);
            return newDouble;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return 0;
    }

    /**
     * 将一段文字拆分并返回拆分的set集合
     *
     * @param info     需要拆分的文字
     * @param separate 拆分标识
     */
    public static HashSet<String> splitToSet(String info, String separate) {
        HashSet<String> ls = new HashSet<String>();
        if (!TextUtils.isEmpty(info)) {
            String[] ss = info.split(separate);
            for (String s : ss) {
                ls.add(s);
            }
        }
        return ls;
    }

    /**
     * 通过一个分隔符，合并字符串集合
     *
     * @param list     字符串集合
     * @param separate 分隔符
     */
    public static String mergeListToString(Collection<String> list, String separate) {
        if (separate == null) {
            separate = ",";
        }
        StringBuilder sb = new StringBuilder();
        if (list.size() > 0) {
            for (String s : list) {
                sb.append(s + separate);
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
 