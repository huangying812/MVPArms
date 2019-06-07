package com.mvp.demo.app

import com.plain.base.BaseActivity
import com.plain.mvp.IPresenter

/**
 * Author : zhongwenpeng
 * Email : 1340751953@qq.com
 * Time :  2019/1/9
 * Description :
 */
abstract  class MyBaseActivity<P: IPresenter>: BaseActivity<P>()