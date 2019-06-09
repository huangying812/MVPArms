package sl.recharge.call.app.extensions

import kotlinx.coroutines.*


fun launchUI(start: CoroutineStart = CoroutineStart.DEFAULT, block: suspend CoroutineScope.() -> Unit) = GlobalScope.launch(Dispatchers.Main, start, block)

suspend fun <T> Deferred<T>.awaitOrError(): Result<T> {

    return try {
        Result.of(await())
    } catch (e: Exception) {
        Result.of(e)
    }
}