package com.mvp.demo.app.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.text.SpannedString
import android.text.style.RelativeSizeSpan
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.isDigitsOnly
import androidx.core.text.set
import com.livinglifetechway.k4kotlin.orZero
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern

/**
 * 作者：Yu on 2018/3/19 17:01
 * Version v1.0
 * 描述：
 */

/** 字符串头尾一定长度的原样保留,中间部分使用'*'替换
 *  如果头尾长度加起来大于等于字符串长度,那么全部原样保留
 * @param headKeeps 头部保留的位数,有默认值3
 * @param endKeeps 尾部保留的位数,有默认值3
 *
 * 默认值情况下(headKeeps = 3, endKeeps = 3):
 *  null -> ""
 * "12345" -> "12345"
 * "123456" -> "123456"
 * "1234567" -> "123*567"
 * "12345678901" -> "123*****901"
 * "12345678901234" -> "123********234"
 * "123456789012345678" -> "123************678"
 *
 * */
fun String?.maskPhoneOrIdCardCode(headKeeps: Int = 3, endKeeps: Int = 3): String {
    return maskWithChar(headKeeps, endKeeps)
}

/** 字符串头尾一定长度的原样保留,中间部分使用某个符号替换
 *  如果头尾长度加起来大于等于字符串长度,那么全部原样保留
 * @param headKeeps 头部保留的位数
 * @param endKeeps 尾部保留的位数
 * @param maskChar 中间的部分各个字符需要替换成的字符,有默认值'*'
 * */
fun String?.maskWithChar(headKeeps: Int, endKeeps: Int, maskChar: Char = '*'): String {
    return when {
        this == null -> ""
        length > endKeeps + headKeeps -> {
            val list: List<Char> = mapIndexed { i, c ->
                if (i >= headKeeps && i < (length - endKeeps)) maskChar else c
            }
            String(list.toCharArray())
        }
        else -> this
    }
}

/** 0保密，1男，2女 */
fun Number?.isGenderPrivacy(): Boolean {
    val genderInInt = this?.toInt().orZero()
    return !(genderInInt == 1 || genderInInt == 2)
}

/** 0保密，1男，2女 */
fun Number?.parserGender(): String {
    return when (this?.toInt()) {
        1 -> "男"
        2 -> "女"
        else -> "保密"
    }
}

/** 0保密，1男，2女 */
fun String?.parserGender(): String {
    return this?.toIntOrNull().parserGender()
}

/**把数字转换成人民币货币格式:
 *      小数点前每三位带,分隔,
 *      2位小数,不足的补0
 *      2位小数后5舍6入(暂时不清楚怎么改成4舍5入)
 * 185 -> ¥185.00
 * 185000000 -> ¥185,000,000.00
 * 185.12456 -> ¥185.12
 * 185.12656 -> ¥185.13
 * 185.62456 -> ¥185.62
 *
 * */
fun Number?.toRMBStr1(moneyChar: String = "¥"): String {
    return DecimalFormat("${moneyChar}#,##0.00").format(orZero())
}

/**把数字转换成人民币货币格式:
 *      保留所有非零小数，去除数值是零的小数
 * 185000000.00 -> ¥185000000
 * 185.12456 -> ¥185.12456
 * */
fun Number?.toRMBStrWithoutZeroDecimal1(moneyChar: String = "¥"): String {
    return DecimalFormat("${moneyChar}#.##").format(orZero())
}

/**把数字转换成人民币货币格式:
 *      整数区域每三位用,分隔
 *      保留所有非零小数，去除数值是零的小数
 * 185000000.00 -> ¥185,000,000
 * 185.12456 -> ¥185.12456
 * */
fun Number?.toRMBStrWithoutZeroDecimal2(moneyChar: String = "¥"): String {
    return DecimalFormat("${moneyChar}#,###.##").format(orZero())
}

/**把数字转换成人民币货币格式:
 *      2位小数,不足的补0
 *      2位小数后5舍6入(暂时不清楚怎么改成4舍5入)
 * 185 -> ¥185.00
 * 185000000 -> ¥185000000.00
 * 185.12456 -> ¥185.12
 * 185.12656 -> ¥185.13
 * 185.62456 -> ¥185.62
 *
 * */
fun Number?.toRMBStr2(moneyChar: String = "¥"): String {
    return DecimalFormat("${moneyChar}###0.00").format(orZero())
}

/**把数字转换成人民币货币格式:(4舍5入取整)
 * 185 -> ¥185
 * 185000000 -> ¥185000000
 * 185.12456 -> ¥185
 * 185.12656 -> ¥185
 * 185.62456 -> ¥186
 * */
fun Number?.toRMBStr3(moneyChar: String = "¥"): String {
    return DecimalFormat("${moneyChar}###0").format(orZero())
}

fun Context?.getActivity(): Activity? {
    val context = this ?: return null
    when {
        context is Activity -> return context
        context.javaClass.name.contains("com.android.internal.policy.DecorContext") -> try {
            val field = context.javaClass.getDeclaredField("mPhoneWindow")
            field.isAccessible = true
            val obj = field.get(context)
            val m1 = obj.javaClass.getMethod("getContext")
            return m1.invoke(obj) as Activity
        } catch (e: Exception) {
            e.printStackTrace()
        }
        context is ContextWrapper -> return context.baseContext.getActivity()
    }
    return null
}

fun CharSequence?.parserInt(): Int? {
    return this?.filter { it.isDigit() }?.toString()?.toInt()
}

fun TextView.showOrNotByContent(isGoneWhenEmpty: Boolean = false) {
    visibility = if (!text.isNullOrBlank()) View.VISIBLE else if (isGoneWhenEmpty) View.GONE else View.INVISIBLE
}

fun EditText.setTextWithSelection(content: CharSequence?) {
    setText(content)
    setSelection(content?.length.orZero())
}

/** 证件类型（1-身份证，2-护照，3-港澳通行证） */
fun Number?.toCertTypeStr(): String {
    return when (orZero().toInt()) {
        1 -> "身份证"
        2 -> "护照"
        3 -> "港澳通行证"
        else -> "未知证件"
    }
}

fun String?.orDefault(default: String? = ""): String {
    return if (isNullOrBlank()) (default ?: "") else this!!
}

fun kotlin.Byte?.orZero(): kotlin.Byte = this ?: 0

fun DatePicker.safeSetMinTime(timeMillis: Long) {
    if (System.currentTimeMillis() / 1000 == timeMillis / 1000) {
        //设置的时间必须小于当前时间（单位秒），不然报错IllegalArgumentException: fromDate: Mon Aug 06 17:46:45 GMT+08:00 2018 does not precede toDate: Mon Aug 06 17:46:45 GMT+08:00 2018
        minDate = timeMillis - 1000
    } else {
        minDate = timeMillis
    }
}

/**
 * 改变字符串中字体的大小
 */
fun getSpannedString(text: String, start: Int, end: Int, proportion: Float): SpannedString {
    return buildSpannedString {
        append(text)
        set(start, end, RelativeSizeSpan(proportion))
    }
}

fun <E> Collection<E>?.safeSize() = this?.size.orZero()

fun <E> AbstractSequentialList<E>.safeRemoveLast(): E? {
    return if (safeSize() > 0) {
        removeAt(lastIndex)
    } else {
        null
    }
}

fun Byte.toUnsignedInt() = toHex().toInt(16)
fun Byte.toUnsignedShort() = toHex().toShort(16)
fun Short.toUnsignedInt() = toHex(4).toInt(16)

fun allocateByteBuf(capacity: Int, endType: ByteOrder = ByteOrder.BIG_ENDIAN) = ByteBuffer.allocate(capacity).order(endType)
fun ByteArray.toByteBuf(offset: Int = 0, length: Int = size, endType: ByteOrder = ByteOrder.BIG_ENDIAN) = ByteBuffer.wrap(this, offset, length).order(endType)

fun ByteBuffer.getString(strLengthLength: Number = 1): Pair<Int, String> {
    val strLength = when (strLengthLength.toInt()) {
        1 -> get().toUnsignedInt()
        2 -> short.toUnsignedInt()
        4 -> int
        else -> remaining()
    }
    val trackName = ByteArray(strLength)
    get(trackName, 0, strLength)
    return strLength to String(trackName)
}

fun Number?.isSuccess(i: Int = 0) = this != null && toInt() == i

fun CharSequence?.safeToLong(default: Long = 0): Long {
    return this?.toString()?.toLongOrNull() ?: default
}

fun CharSequence?.safeToInt(): Int {
    val string = this?.toString().orEmpty()
    return if (string.isNotEmpty() && string.isDigitsOnly()) {
        string.toInt()
    } else {
        string.safeToDouble().toInt()
    }
}

fun CharSequence?.safeToDouble(): Double {
    val string = this?.toString().orEmpty()
    val matcher = Pattern.compile("-?(\\d+\\.?\\d*|0.\\d*[1-9]\\d*)").matcher(string)
    val targetStr = if (matcher.find()) matcher.group() else ""
    return targetStr.toDoubleOrNull().orZero()
}

fun CharSequence?.safeToFloat(): Float {
    return safeToDouble().toFloat()
}

fun ViewGroup?.isParentsOf(view: View): Boolean {
    if (this == null) {
        return false
    }
    var temp: View? = view
    while (temp != null) {
        if (temp == this) {
            return true
        }
        temp = temp.parent as? View
    }
    return false
}

fun ByteArray.toHexStr(separator: String = " ") = joinToString(separator) { it.toHex() }.toUpperCase()

fun Number.toHex(bit: Int = 2) = String.format("%0${bit}X", this)

fun Number.toDigits(bit: Int = 2) = String.format("%0${bit}d", this)

/**
 * 毫秒值的时间,转换成计时用的显示格式
 *
 * 超过一小时的显示: xh xx′ xx″
 * 其余的显示: xx′ xx″
 * */
fun Number.toCountHMS(): String {
    val seconds = toLong() / 1000
    val hours = seconds / 3600
    val remain = seconds % 3600
    return if (hours > 0) {
        "${hours}h${(remain / 60).toDigits()}′${(remain % 60).toDigits()}″"
    } else {
        "${(remain / 60).toDigits()}′${(remain % 60).toDigits()}″"
    }
}

/**
 * 毫秒值的时间,转换成计时用的显示格式 (因为某些字体,比如Impact 显示 ′ ″ 间隔有问题,所以提供另一种字符的格式)
 * 超过一小时的显示: xh xx' xx"
 * 其余的显示: xx' xx"
 * */
fun Number.toCountHMS2(): String {
    val seconds = toLong() / 1000
    val hours = seconds / 3600
    val remain = seconds % 3600
    return if (hours > 0) {
        "${hours}h${(remain / 60).toDigits()}'${(remain % 60).toDigits()}\""
    } else {
        "${(remain / 60).toDigits()}'${(remain % 60).toDigits()}\""
    }
}

fun <E> List<E>?.safeGet(index: Int) = if (this == null || isEmpty()) null else get(index % size)