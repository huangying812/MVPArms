package sl.recharge.call.mvp.ui.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import com.plain.base.BaseActivity
import com.plain.di.component.AppComponent
import com.plain.mvp.IPresenter
import com.plain.utils.LogUtils
import com.plain.utils.WebViewLifecycleUtils
import kotlinx.android.synthetic.main.activity_web.*
import sl.recharge.call.R

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 浏览器界面
 */
class WebActivity : BaseActivity<IPresenter>() {

    companion object {
        const val EXTR_URL = "EXTR_URL"
        const val EXTR_TITLE = "EXTR_TITLE"
    }

    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_web
    }

    override fun initData(savedInstanceState: Bundle?) {
        initView()
        wv_web_view.webViewClient = MyWebViewClient()
        wv_web_view.webChromeClient = MyWebChromeClient()

        val url = intent?.getStringExtra(EXTR_URL).orEmpty()
        title = intent?.getStringExtra(EXTR_TITLE).orEmpty()
        wv_web_view.loadUrl(url)
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected fun initView() {
        // 不显示滚动条
        wv_web_view.isVerticalScrollBarEnabled = false
        wv_web_view.isHorizontalScrollBarEnabled = false

        val settings = wv_web_view.settings
        // 允许文件访问
        settings.allowFileAccess = true
        // 支持javaScript
        settings.javaScriptEnabled = true
        // 允许网页定位
        settings.setGeolocationEnabled(true)
        // 允许保存密码
        settings.savePassword = true

        // 解决Android 5.0上WebView默认不允许加载Http与Https混合内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //两者都可以
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        // 加快HTML网页加载完成的速度，等页面finish再加载图片
        settings.loadsImagesAutomatically = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && wv_web_view.canGoBack()) {
            // 后退网页并且拦截该事件
            wv_web_view.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        WebViewLifecycleUtils.onResume(wv_web_view)
        super.onResume()
    }

    override fun onPause() {
        WebViewLifecycleUtils.onPause(wv_web_view)
        super.onPause()
    }

    override fun onDestroy() {
        WebViewLifecycleUtils.onDestroy(wv_web_view)
        super.onDestroy()
    }

    private inner class MyWebViewClient : WebViewClient() {

        // 网页加载错误时回调，这个方法会在 onPageFinished 之前调用
        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String?) {
            showError()
        }

        // 开始加载网页
        override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
            pb_web_progress.visibility = View.VISIBLE
        }

        // 完成加载网页
        override fun onPageFinished(view: WebView, url: String) {
            pb_web_progress.visibility = View.GONE
            showComplete()
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            //super.onReceivedSslError(view, handler, error);注意一定要去除这行代码，否则设置无效。
            // handler.cancel();// Android默认的处理方式
            handler.proceed()// 接受所有网站的证书
            // handleMessage(Message msg);// 进行其他处理
        }

        // 跳转到其他链接
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

            var scheme = Uri.parse(url).scheme
            if (scheme != null) {
                scheme = scheme.toLowerCase()
            }
            if ("http".equals(scheme, ignoreCase = true) || "https".equals(scheme, ignoreCase = true)) {
                wv_web_view.loadUrl(url)
            }
            // 已经处理该链接请求
            return true
        }
    }

    private inner class MyWebChromeClient : WebChromeClient() {

        // 收到网页标题
        override fun onReceivedTitle(view: WebView, title: String?) {
            LogUtils.debugInfo("title:${title}")
            if (title != null) {
                setTitle(title)
            }
        }

        // 收到加载进度变化
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            pb_web_progress.progress = newProgress
        }
    }
}