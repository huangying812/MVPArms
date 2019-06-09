package sl.recharge.call.app.extensions

/**
 * 作者：Yu on 2018/9/18 10:32
 * Version v1.0
 * 描述：
 */

sealed class BooleanExt<out T>

object Otherwise : BooleanExt<Nothing>()
class WithData<T>(val data: T) : BooleanExt<T>()

inline fun <T> Boolean.yes(block: () -> T) =
        when {
            this -> {
                WithData(block())
            }
            else -> {
                Otherwise
            }
        }

inline fun <T> Boolean.no(block: () -> T) = when {
    this -> Otherwise
    else -> {
        WithData(block())
    }
}

inline fun <T> BooleanExt<T>.otherwise(block: () -> T): T =
        when (this) {
            is Otherwise -> block()
            is WithData -> data
        }

