package com.plain.permission;

import kotlin.Pair;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/XXPermissions
 * time   : 2018/06/15
 * desc   : 权限请求实体类
 */
public interface Permission {

    String REQUEST_INSTALL_PACKAGES = "android.permission.REQUEST_INSTALL_PACKAGES"; // 8.0及以上应用安装权限

    String SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW"; // 6.0及以上悬浮窗权限

    String READ_CALENDAR = "android.permission.READ_CALENDAR"; // 读取日程提醒
    String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR"; // 写入日程提醒

    String CAMERA = "android.permission.CAMERA"; // 拍照权限

    String READ_CONTACTS = "android.permission.READ_CONTACTS"; // 读取联系人
    String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS"; // 写入联系人
    String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS"; // 访问账户列表

    String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION"; // 获取精确位置
    String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION"; // 获取粗略位置

    String RECORD_AUDIO = "android.permission.RECORD_AUDIO"; // 录音权限

    String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE"; // 读取电话状态
    String CALL_PHONE = "android.permission.CALL_PHONE"; // 拨打电话
    String READ_CALL_LOG = "android.permission.READ_CALL_LOG"; // 读取通话记录
    String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG"; // 写入通话记录
    String ADD_VOICEMAIL = "com.android.voicemail.permission.ADD_VOICEMAIL"; // 添加语音邮件
    String USE_SIP = "android.permission.USE_SIP"; // 使用SIP视频
    String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS"; // 处理拨出电话
    String ANSWER_PHONE_CALLS = "android.permission.ANSWER_PHONE_CALLS";// 8.0危险权限：允许您的应用通过编程方式接听呼入电话。要在您的应用中处理呼入电话，您可以使用 acceptRingingCall() 函数
    String READ_PHONE_NUMBERS = "android.permission.READ_PHONE_NUMBERS";// 8.0危险权限：权限允许您的应用读取设备中存储的电话号码

    String BODY_SENSORS = "android.permission.BODY_SENSORS"; // 传感器

    String SEND_SMS = "android.permission.SEND_SMS"; // 发送短信
    String RECEIVE_SMS = "android.permission.RECEIVE_SMS"; // 接收短信
    String READ_SMS = "android.permission.READ_SMS"; // 读取短信
    String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH"; // 接收WAP PUSH信息
    String RECEIVE_MMS = "android.permission.RECEIVE_MMS"; // 接收彩信

    String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"; // 读取外部存储
    String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"; // 写入外部存储

    Pair<String, String> NAME_PAIR_READ_EXTERNAL_STORAGE = new Pair<>(READ_EXTERNAL_STORAGE, "读取外部存储权限");
    Pair<String, String> NAME_PAIR_REQUEST_INSTALL_PACKAGES = new Pair<>(REQUEST_INSTALL_PACKAGES, "应用安装权限");
    Pair<String, String> NAME_PAIR_ACCESS_COARSE_LOCATION = new Pair<>(ACCESS_COARSE_LOCATION, "获取粗略位置");
    Pair<String, String> NAME_PAIR_ACCESS_FINE_LOCATION = new Pair<>(ACCESS_FINE_LOCATION, "获取位置权限");
    Pair<String, String> NAME_PAIR_ADD_VOICEMAIL = new Pair<>(ADD_VOICEMAIL, "添加语音邮件权限");
    Pair<String, String> NAME_PAIR_ANSWER_PHONE_CALLS = new Pair<>(ANSWER_PHONE_CALLS, "接听电话权限");
    Pair<String, String> NAME_PAIR_BODY_SENSORS = new Pair<>(BODY_SENSORS, "传感器权限");
    Pair<String, String> NAME_PAIR_CALL_PHONE = new Pair<>(CALL_PHONE, "拨打电话权限");
    Pair<String, String> NAME_PAIR_CAMERA = new Pair<>(CAMERA, "拍照权限");
    Pair<String, String> NAME_PAIR_GET_ACCOUNTS = new Pair<>(GET_ACCOUNTS, "访问账户列表权限");
    Pair<String, String> NAME_PAIR_PROCESS_OUTGOING_CALLS = new Pair<>(PROCESS_OUTGOING_CALLS, "处理拨出电话权限");
    Pair<String, String> NAME_PAIR_RECEIVE_WAP_PUSH = new Pair<>(RECEIVE_WAP_PUSH, "接收WAP PUSH信息权限");
    Pair<String, String> NAME_PAIR_WRITE_CALL_LOG = new Pair<>(WRITE_CALL_LOG, "写入通话记录权限");
    Pair<String, String> NAME_PAIR_READ_SMS = new Pair<>(READ_SMS, "读取短信权限");
    Pair<String, String> NAME_PAIR_RECEIVE_MMS = new Pair<>(RECEIVE_MMS, "接收彩信权限");
    Pair<String, String> NAME_PAIR_READ_PHONE_NUMBERS = new Pair<>(READ_PHONE_NUMBERS, "读取电话号码权限");
    Pair<String, String> NAME_PAIR_READ_CONTACTS = new Pair<>(READ_CONTACTS, "读取联系人权限");
    Pair<String, String> NAME_PAIR_WRITE_CONTACTS = new Pair<>(WRITE_CONTACTS, "修改联系人权限");
    Pair<String, String> NAME_PAIR_SEND_SMS = new Pair<>(SEND_SMS, "发送短信权限");
    Pair<String, String> NAME_PAIR_RECORD_AUDIO = new Pair<>(RECORD_AUDIO, "录音权限");
    Pair<String, String> NAME_PAIR_WRITE_CALENDAR = new Pair<>(WRITE_CALENDAR, "写入日程提醒权限");
    Pair<String, String> NAME_PAIR_RECEIVE_SMS = new Pair<>(RECEIVE_SMS, "接收短信权限");
    Pair<String, String> NAME_PAIR_USE_SIP = new Pair<>(USE_SIP, "使用SIP视频权限");
    Pair<String, String> NAME_PAIR_READ_CALENDAR = new Pair<>(READ_CALENDAR, "读取日程提醒权限");
    Pair<String, String> NAME_PAIR_SYSTEM_ALERT_WINDOW = new Pair<>(SYSTEM_ALERT_WINDOW, "悬浮窗权限");
    Pair<String, String> NAME_PAIR_READ_CALL_LOG = new Pair<>(READ_CALL_LOG, "读取通话记录权限");
    Pair<String, String> NAME_PAIR_WRITE_EXTERNAL_STORAGE = new Pair<>(WRITE_EXTERNAL_STORAGE, "写入外部存储权限");
    Pair<String, String> NAME_PAIR_READ_PHONE_STATE = new Pair<>(READ_PHONE_STATE, "读取电话状态权限");

    interface Group {

        // 日历
        String[] CALENDAR = new String[]{
                Permission.READ_CALENDAR,
                Permission.WRITE_CALENDAR};

        // 联系人
        String[] CONTACTS = new String[]{
                Permission.READ_CONTACTS,
                Permission.WRITE_CONTACTS,
                Permission.GET_ACCOUNTS};

        // 位置
        String[] LOCATION = new String[]{
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION};

        // 存储
        String[] STORAGE = new String[]{
                Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE};
    }
}